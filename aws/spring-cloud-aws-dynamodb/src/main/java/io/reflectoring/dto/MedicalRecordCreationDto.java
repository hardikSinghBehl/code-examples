package io.reflectoring.dto;

public record MedicalRecordCreationDto(
    String patientName,
    String diagnosis,
    String treatmentPlan
) {
}