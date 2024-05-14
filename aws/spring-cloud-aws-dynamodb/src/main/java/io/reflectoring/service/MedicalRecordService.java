package io.reflectoring.service;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;
import org.springframework.stereotype.Service;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import io.reflectoring.dto.MedicalRecordCreationDto;
import io.reflectoring.entity.MedicalRecord;
import io.reflectoring.exception.InvalidMedicalRecordIdException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

	private final DynamoDbTemplate dynamoDbTemplate;
	private final ModelMapper modelMapper = new ModelMapper().registerModule(new RecordModule());

	public String create(@NonNull final MedicalRecordCreationDto medicalRecordCreationRequest) {
		final var medicalRecord = modelMapper.map(medicalRecordCreationRequest, MedicalRecord.class);
		medicalRecord.setId(String.valueOf(UUID.randomUUID()));
		final var savedMedicalRecord = dynamoDbTemplate.save(medicalRecord);
		return savedMedicalRecord.getId();
	}

	public MedicalRecord retrieve(@NonNull final String medicalRecordId) {
		final var key = Key.builder().partitionValue(medicalRecordId).build();
		final var medicalRecord = dynamoDbTemplate.load(key, MedicalRecord.class);
		return Optional.ofNullable(medicalRecord).orElseThrow(InvalidMedicalRecordIdException::new);
	}

	public void delete(@NonNull final String medicalRecordId) {
		final var medicalRecord = retrieve(medicalRecordId);
		dynamoDbTemplate.delete(medicalRecord);
	}

}
