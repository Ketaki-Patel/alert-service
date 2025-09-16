
### For more details on both vitals-service and alert service refer
- @see README.MD(from vitals-service) https://github.com/Ketaki-Patel/vitals-service/blob/main/README.md
- @see Note.md(from vitals-service) https://github.com/Ketaki-Patel/vitals-service/blob/main/NOTES.md

### Alerts Service (Port 8082)
Evaluates readings against threshold rules, stores alerts in memory, and exposes them via an API.

- swagger api(http://localhost:8081/swagger-ui/index.html)
- validation supported
  - Used @RestControllerAdvice(@see GlobalErrorController.java in alert-service project)
  - Provided support for Jakarta Bean Validation (e.g., @Valid, @NotNull) using Hibernate Validator through dependency spring-boot-starter-validation
- In Memory persistence (ConcurrentHashMap)
  note: database is not supported right now but can be easily supported as I implemented H2 databasee support for vitals-service, so we provide similar solution.

### Alert Generation Rules
**POST** `/evaluate` → returns `Mono<Void>` or `Mono<ResponseEntity<...>>`
- Apply thresholds:
    - BP: `systolic ≥ 140` or `diastolic ≥ 90`
    - HR: `< 50` or `> 110`
    - SPO2: `< 92`
- If exceeded, create `OPEN` alert in memory.
- Ignore duplicates by `readingId`.

**GET** `/alerts?patientId=...` → returns `Flux<Alert>`
- Return alerts for that patient, newest first.

