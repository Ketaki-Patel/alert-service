package com.example.alerts.repository;

import com.example.alerts.model.Alert;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class AlertRepository {

    private final Map<UUID, Alert> alerts = new ConcurrentHashMap<>();

    public Optional<Alert> findByReadingId(UUID readingId) {
        return Optional.ofNullable(alerts.get(readingId));
    }

    public void save(Alert alert) {
        alerts.put(alert.getReadingId(), alert);
    }

    public List<Alert> findByPatientId(String patientId) {
        return alerts.values().stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .sorted(Comparator.comparing(Alert::getCapturedAt).reversed())
                .collect(Collectors.toList());
    }
}

