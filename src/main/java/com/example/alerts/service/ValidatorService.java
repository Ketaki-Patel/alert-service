package com.example.alerts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ValidatorService {
    private final Validator validator;

    public ValidatorService(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> String.format("%s: %s", v.getPropertyPath(), v.getMessage()))
                    .collect(Collectors.joining("; "));

            log.warn("Validation failed for {}: {}", object.getClass().getSimpleName(), errorMsg);

            throw new ConstraintViolationException("Validation failed: " + errorMsg, violations);
        }
    }
}
