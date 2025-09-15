package com.example.alerts.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    private UUID readingId;
    private String patientId;
    private String type;
    private String message;
    private String status; // e.g. OPEN
    private Instant capturedAt;
}
