package com.codigo.pedido.orders.infrastructure.resilience;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Component
public class SimpleCircuitBreaker {

    private final int failureThreshold;
    private final Duration openDuration;
    private final Map<String, CircuitState> circuits = new ConcurrentHashMap<>();

    public SimpleCircuitBreaker(
            @Value("${resilience.circuit-breaker.failure-threshold:3}") int failureThreshold,
            @Value("${resilience.circuit-breaker.open-duration-ms:10000}") long openDurationMs) {
        this.failureThreshold = failureThreshold;
        this.openDuration = Duration.ofMillis(openDurationMs);
    }

    public <T> T execute(String integrationName, Supplier<T> action) {
        CircuitState state = circuits.computeIfAbsent(integrationName, key -> new CircuitState());

        if (state.isOpen()) {
            throw new IllegalStateException("Circuit breaker abierto para " + integrationName);
        }

        try {
            T result = action.get();
            state.reset();
            return result;
        } catch (RuntimeException exception) {
            state.registerFailure(failureThreshold, openDuration);
            throw exception;
        }
    }

    private static class CircuitState {
        private int failures;
        private Instant openUntil;

        synchronized boolean isOpen() {
            if (openUntil == null) {
                return false;
            }

            if (Instant.now().isAfter(openUntil)) {
                reset();
                return false;
            }

            return true;
        }

        synchronized void registerFailure(int threshold, Duration duration) {
            failures++;
            if (failures >= threshold) {
                openUntil = Instant.now().plus(duration);
            }
        }

        synchronized void reset() {
            failures = 0;
            openUntil = null;
        }
    }
}
