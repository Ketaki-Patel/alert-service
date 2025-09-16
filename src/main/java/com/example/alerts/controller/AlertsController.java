package com.example.alerts.controller;
import com.example.alerts.dto.Reading;
import com.example.alerts.model.Alert;
import com.example.alerts.repository.AlertRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AlertsController {

    private final AlertRepository repository;

    @PostMapping("/evaluate")
    public Mono<ResponseEntity<Void>> evaluateReading(@Valid @RequestBody Mono<Reading> readingMono) {
        return readingMono.flatMap(reading -> {
            UUID readingId = reading.getReadingId();
            log.info("vital reading forwarded from vital service: " +  reading);

            // Ignore duplicates
            if (repository.findByReadingId(readingId).isPresent()) {
                return Mono.just(ResponseEntity.ok().build());
            }

            boolean alertTriggered = false;
            String message = "";

            switch (reading.getType()) {
                case "BP":
                    if ((reading.getSystolic() != null && reading.getSystolic() >= 140)
                            || (reading.getDiastolic() != null && reading.getDiastolic() >= 90)) {
                        alertTriggered = true;
                        message = "High blood pressure";
                    }
                    break;
                case "HR":
                    if ((reading.getHr() != null && (reading.getHr() < 50 || reading.getHr() > 110))) {
                        alertTriggered = true;
                        message = "Abnormal heart rate";
                    }
                    break;
                case "SPO2":
                    if (reading.getSpo2() != null && reading.getSpo2() < 92) {
                        alertTriggered = true;
                        message = "Low oxygen saturation";
                    }
                    break;
                default:
                    // unknown type, ignore
                    break;
            }

            if (alertTriggered) {
                Alert alert = Alert.builder()
                        .readingId(reading.getReadingId())
                        .patientId(reading.getPatientId())
                        .type(reading.getType())
                        .message(message)
                        .status("OPEN")
                        .capturedAt(reading.getCapturedAt())
                        .build();

                repository.save(alert);
            }else{
                log.info("vital reading with no alert: " +  reading);
            }

            return Mono.just(ResponseEntity.ok().build());
        });
    }

    @GetMapping("/alerts")
    public Flux<Alert> getAlertsByPatient(@RequestParam String patientId) {
        return Flux.fromIterable(repository.findByPatientId(patientId));
    }
}
