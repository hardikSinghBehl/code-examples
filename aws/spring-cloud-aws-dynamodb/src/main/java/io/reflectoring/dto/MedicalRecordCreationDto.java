package io.reflectoring.dto;

public record MedicalRecordCreationDto(
    String patientName,
    String medicalHistory,
    String diagnosis,
    String treatmentPlan,
    String allergies,
    String attendingPhysicianName
) {
}