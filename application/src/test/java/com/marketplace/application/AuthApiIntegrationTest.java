package com.marketplace.application;

import com.jayway.jsonpath.JsonPath;
import com.marketplace.identity.application.port.out.OtpRequestRateLimitDecision;
import com.marketplace.identity.application.port.out.OtpRequestRateLimiterPort;
import com.marketplace.identity.application.port.out.OtpStoragePort;
import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.PhoneNumber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class AuthApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OtpRequestRateLimiterPort otpRequestRateLimiter;

    @Autowired
    private OtpStoragePort otpStorage;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void shouldCompleteOtpLoginRefreshAndProtectedRequestFlow() throws Exception {
        String phone = "+12025550123";
        MvcResult otpResponse = mockMvc.perform(post("/api/v1/auth/otp/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"" + phone + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expiresInSeconds").value(180))
                .andReturn();
        String otp = JsonPath.read(otpResponse.getResponse().getContentAsString(), "$.debugCode");
        assertNotNull(otp);

        MvcResult loginResponse = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"" + phone + "\",\"code\":\"" + otp + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn();
        String loginJson = loginResponse.getResponse().getContentAsString();
        String accessToken = JsonPath.read(loginJson, "$.accessToken");
        String refreshToken = JsonPath.read(loginJson, "$.refreshToken");

        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());

        MvcResult sessionResponse = mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"))
                .andReturn();
        String userId = JsonPath.read(
                sessionResponse.getResponse().getContentAsString(), "$.userId");

        MvcResult refreshResponse = mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andReturn();
        String refreshedAccessToken = JsonPath.read(
                refreshResponse.getResponse().getContentAsString(), "$.accessToken");

        MvcResult refreshedSessionResponse = mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + refreshedAccessToken))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(userId, JsonPath.read(
                refreshedSessionResponse.getResponse().getContentAsString(), "$.userId"));
    }

    @Test
    void shouldLimitRequestsPerPhone() {
        PhoneNumber phone = randomPhone();
        String ip = "phone-limit-test-" + System.nanoTime();

        assertTrue(otpRequestRateLimiter.tryAcquire(phone, ip).allowed());

        OtpRequestRateLimitDecision decision = otpRequestRateLimiter.tryAcquire(phone, ip);

        assertEquals(false, decision.allowed());
        assertTrue(decision.retryAfterSeconds() > 0);
        assertTrue(decision.retryAfterSeconds() <= 60);
    }

    @Test
    void shouldStoreOtpWithTtlAndAllowItOnlyOnce() {
        PhoneNumber phone = randomPhone();
        Instant expiresAt = Instant.now().plusSeconds(180);
        var keysBeforeSave = redisTemplate.keys("identity:otp:code:*");
        otpStorage.save(phone, OtpCode.create("123456", expiresAt));

        String key = redisTemplate.keys("identity:otp:code:*").stream()
                .filter(candidate -> !keysBeforeSave.contains(candidate))
                .findFirst()
                .orElseThrow();
        long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        assertTrue(ttl > 0 && ttl <= 180);
        assertTrue(otpStorage.verify(phone, "123456", Instant.now()));
        assertFalse(otpStorage.verify(phone, "123456", Instant.now()));
    }

    @Test
    void shouldAllowOnlyOneConcurrentOtpVerification() throws Exception {
        PhoneNumber phone = randomPhone();
        Instant now = Instant.now();
        otpStorage.save(phone, OtpCode.create("123456", now.plusSeconds(180)));
        List<Callable<Boolean>> verifications = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            verifications.add(() -> otpStorage.verify(phone, "123456", now));
        }

        var executor = Executors.newFixedThreadPool(10);
        try {
            long successfulVerifications = executor.invokeAll(verifications).stream()
                    .mapToLong(future -> {
                        try {
                            return future.get() ? 1 : 0;
                        } catch (Exception e) {
                            throw new IllegalStateException("OTP verification failed", e);
                        }
                    })
                    .sum();

            assertEquals(1, successfulVerifications);
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void shouldLockOtpAfterFiveFailedAttempts() {
        PhoneNumber phone = randomPhone();
        Instant now = Instant.now();
        otpStorage.save(phone, OtpCode.create("123456", now.plusSeconds(180)));

        for (int attempt = 0; attempt < OtpCode.MAX_VERIFICATION_ATTEMPTS; attempt++) {
            assertFalse(otpStorage.verify(phone, "000000", now.plusSeconds(attempt)));
        }

        assertFalse(otpStorage.verify(phone, "123456", now.plusSeconds(10)));
    }

    @Test
    void shouldReturnProblemDetailWhenOtpRequestIsRateLimited() throws Exception {
        String phone = randomPhone().value();

        mockMvc.perform(post("/api/v1/auth/otp/request")
                        .with(request -> {
                            request.setRemoteAddr("198.51.100.25");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"" + phone + "\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/otp/request")
                        .with(request -> {
                            request.setRemoteAddr("198.51.100.25");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"" + phone + "\"}"))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.status").value(429))
                .andExpect(jsonPath("$.retryAfterSeconds").isNumber())
                .andExpect(result -> assertNotNull(
                        result.getResponse().getHeader("Retry-After")));
    }

    @Test
    void shouldApplyIpLimitAtomicallyForConcurrentRequests() throws Exception {
        String ip = "concurrent-ip-limit-test-" + System.nanoTime();
        List<Callable<OtpRequestRateLimitDecision>> requests = new ArrayList<>();
        for (int index = 0; index < 25; index++) {
            PhoneNumber phone = randomPhone();
            requests.add(() -> otpRequestRateLimiter.tryAcquire(phone, ip));
        }

        var executor = Executors.newFixedThreadPool(10);
        try {
            long permitted = executor.invokeAll(requests).stream()
                    .mapToLong(future -> {
                        try {
                            return future.get().allowed() ? 1 : 0;
                        } catch (Exception e) {
                            throw new IllegalStateException("Rate-limit request failed", e);
                        }
                    })
                    .sum();

            assertEquals(20, permitted);
        } finally {
            executor.shutdownNow();
        }
    }

    private static PhoneNumber randomPhone() {
        long suffix = ThreadLocalRandom.current().nextLong(1_000_000_000L);
        return new PhoneNumber("+1" + String.format("%010d", suffix));
    }
}
