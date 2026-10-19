package com.marketplace.application;

import com.jayway.jsonpath.JsonPath;
import com.marketplace.identity.application.port.out.OtpRequestRateLimitDecision;
import com.marketplace.identity.application.port.out.OtpRequestRateLimiterPort;
import com.marketplace.identity.application.port.out.OtpStoragePort;
import com.marketplace.identity.application.port.out.RefreshSessionPort;
import com.marketplace.identity.application.port.in.CreateUserAccountUseCase;
import com.marketplace.identity.application.command.CreateUserAccountCommand;
import com.marketplace.identity.application.port.out.TokenProviderPort;
import com.marketplace.identity.application.result.AccessToken;
import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserId;
import com.marketplace.identity.domain.model.UserAccount;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.UUID;

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

    @Autowired
    private RefreshSessionPort refreshSessionPort;

    @Autowired
    private CreateUserAccountUseCase createUserAccountUseCase;

    @Autowired
    private TokenProviderPort tokenProviderPort;

    @Test
    void shouldCompleteOtpLoginRefreshAndProtectedRequestFlow() throws Exception {
        String phone = randomPhone().value();
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
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.title").value("Unauthorized"));
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
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn();
        String refreshedAccessToken = JsonPath.read(
                refreshResponse.getResponse().getContentAsString(), "$.accessToken");
        String refreshedRefreshToken = JsonPath.read(
                refreshResponse.getResponse().getContentAsString(), "$.refreshToken");
        assertNotEquals(refreshToken, refreshedRefreshToken);

        MvcResult refreshedSessionResponse = mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + refreshedAccessToken))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(userId, JsonPath.read(
                refreshedSessionResponse.getResponse().getContentAsString(), "$.userId"));

        MvcResult secondRefreshResponse = mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshedRefreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn();
        assertNotNull(JsonPath.read(
                secondRefreshResponse.getResponse().getContentAsString(), "$.refreshToken"));
    }

    @Test
    void shouldRevokeOnlyTheSessionWhoseRefreshTokenWasReused() {
        UserId userId = createTestAccount().id();
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(30L * 24 * 60 * 60);
        String sessionOneToken = "session-one-" + UUID.randomUUID();
        String sessionTwoToken = "session-two-" + UUID.randomUUID();
        refreshSessionPort.start(userId, sessionOneToken, expiresAt, now);
        refreshSessionPort.start(userId, sessionTwoToken, expiresAt, now);

        assertEquals(RefreshSessionPort.RotationResult.ROTATED,
                refreshSessionPort.rotate(
                        userId, sessionOneToken, "session-one-next-" + UUID.randomUUID(), expiresAt, now));
        assertEquals(RefreshSessionPort.RotationResult.REUSED,
                refreshSessionPort.rotate(
                        userId, sessionOneToken, "session-one-replay-" + UUID.randomUUID(), expiresAt, now));
        assertEquals(RefreshSessionPort.RotationResult.ROTATED,
                refreshSessionPort.rotate(
                        userId, sessionTwoToken, "session-two-next-" + UUID.randomUUID(), expiresAt, now));
    }

    @Test
    void shouldNotCreateTwoActiveBranchesForConcurrentRefreshes() throws Exception {
        UserId userId = createTestAccount().id();
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(30L * 24 * 60 * 60);
        String presentedToken = "concurrent-session-" + UUID.randomUUID();
        refreshSessionPort.start(userId, presentedToken, expiresAt, now);

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<RefreshSessionPort.RotationResult> first = executor.submit(() -> {
                ready.countDown();
                start.await();
                return refreshSessionPort.rotate(
                        userId, presentedToken, "concurrent-branch-one-" + UUID.randomUUID(), expiresAt, now);
            });
            Future<RefreshSessionPort.RotationResult> second = executor.submit(() -> {
                ready.countDown();
                start.await();
                return refreshSessionPort.rotate(
                        userId, presentedToken, "concurrent-branch-two-" + UUID.randomUUID(), expiresAt, now);
            });

            ready.await();
            start.countDown();
            List<RefreshSessionPort.RotationResult> results = List.of(
                    first.get(), second.get());
            assertEquals(1, results.stream()
                    .filter(result -> result == RefreshSessionPort.RotationResult.ROTATED)
                    .count());
            assertEquals(1, results.stream()
                    .filter(result -> result == RefreshSessionPort.RotationResult.REUSED)
                    .count());
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void shouldLogoutOnlyTheCurrentRefreshSession() throws Exception {
        UserAccount account = createTestAccount();
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(30L * 24 * 60 * 60);
        String currentDeviceToken = "logout-current-" + UUID.randomUUID();
        String otherDeviceToken = "logout-other-" + UUID.randomUUID();
        refreshSessionPort.start(account.id(), currentDeviceToken, expiresAt, now);
        refreshSessionPort.start(account.id(), otherDeviceToken, expiresAt, now);
        AccessToken accessToken = tokenProviderPort.generateAccessToken(account);

        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("Authorization", "Bearer " + accessToken.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + currentDeviceToken + "\"}"))
                .andExpect(status().isNoContent());

        assertEquals(RefreshSessionPort.RotationResult.INACTIVE,
                refreshSessionPort.rotate(
                        account.id(), currentDeviceToken,
                        "logout-current-next-" + UUID.randomUUID(), expiresAt, now));
        assertEquals(RefreshSessionPort.RotationResult.ROTATED,
                refreshSessionPort.rotate(
                        account.id(), otherDeviceToken,
                        "logout-other-next-" + UUID.randomUUID(), expiresAt, now));
    }

    @Test
    void shouldLogoutAllRefreshSessionsForAuthenticatedUser() throws Exception {
        UserAccount account = createTestAccount();
        UserAccount otherAccount = createTestAccount();
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(30L * 24 * 60 * 60);
        String firstDeviceToken = "logout-all-one-" + UUID.randomUUID();
        String secondDeviceToken = "logout-all-two-" + UUID.randomUUID();
        String otherUserToken = "logout-all-other-user-" + UUID.randomUUID();
        refreshSessionPort.start(account.id(), firstDeviceToken, expiresAt, now);
        refreshSessionPort.start(account.id(), secondDeviceToken, expiresAt, now);
        refreshSessionPort.start(otherAccount.id(), otherUserToken, expiresAt, now);
        AccessToken accessToken = tokenProviderPort.generateAccessToken(account);

        mockMvc.perform(post("/api/v1/auth/logout-all")
                        .header("Authorization", "Bearer " + accessToken.value()))
                .andExpect(status().isNoContent());

        assertEquals(RefreshSessionPort.RotationResult.INACTIVE,
                refreshSessionPort.rotate(
                        account.id(), firstDeviceToken,
                        "logout-all-one-next-" + UUID.randomUUID(), expiresAt, now));
        assertEquals(RefreshSessionPort.RotationResult.INACTIVE,
                refreshSessionPort.rotate(
                        account.id(), secondDeviceToken,
                        "logout-all-two-next-" + UUID.randomUUID(), expiresAt, now));
        assertEquals(RefreshSessionPort.RotationResult.ROTATED,
                refreshSessionPort.rotate(
                        otherAccount.id(), otherUserToken,
                        "logout-all-other-user-next-" + UUID.randomUUID(), expiresAt, now));
    }

    private UserAccount createTestAccount() {
        return createUserAccountUseCase.execute(
                new CreateUserAccountCommand(randomPhone()));
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
