package com.example.alerts.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reading {

    @NotNull
    private UUID readingId;

    @NotBlank
    private String patientId;

    @NotBlank
    private String type;

    private Integer systolic;
    private Integer diastolic;
    private Integer hr;
    private Integer spo2;

    @NotNull
    private Instant capturedAt;
}

