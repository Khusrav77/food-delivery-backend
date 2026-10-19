package com.marketplace.identity.infrastructure.event;

import com.marketplace.identity.domain.event.UserAccountCreated;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class SpringEventPublisherAdapterTest {

    @Test
    void shouldPublishEventToSpringListener() {

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(TestConfig.class)) {

            SpringEventPublisherAdapter publisher =
                    context.getBean(SpringEventPublisherAdapter.class);

            TestEventListener listener =
                    context.getBean(TestEventListener.class);

            UserAccountCreated event = new UserAccountCreated(
                    UserId.generate(),
                    new PhoneNumber("+79991234567"),
                    Instant.parse("2026-01-01T10:00:00Z"));

            publisher.publish(event);

            assertEquals(1, listener.eventCount());
            assertSame(event, listener.receivedEvent());
        }
    }

    @Configuration
    static class TestConfig {

        @Bean
        SpringEventPublisherAdapter springEventPublisherAdapter(
                ApplicationEventPublisher publisher) {

            return new SpringEventPublisherAdapter(publisher);
        }

        @Bean
        TestEventListener testEventListener() {
            return new TestEventListener();
        }
    }

    static class TestEventListener {

        private UserAccountCreated receivedEvent;
        private int eventCount;

        @EventListener
        public void handle(UserAccountCreated event) {
            this.receivedEvent = event;
            this.eventCount++;
        }

        UserAccountCreated receivedEvent() {
            return receivedEvent;
        }

        int eventCount() {
            return eventCount;
        }
    }
}