package com.example.alerts.service;

import com.example.alerts.dto.Reading;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ValidatorServiceTest {

    @Autowired
    private ValidatorService validatorService;

    @Test
    void testValidationFailsForMissingFields() {
        Reading reading = new Reading(); // assuming missing required fields
        reading.setReadingId(null);
        reading.setPatientId("");
        reading.setType("");
        assertThrows(ConstraintViolationException.class, () -> validatorService.validate(reading));
    }
}
