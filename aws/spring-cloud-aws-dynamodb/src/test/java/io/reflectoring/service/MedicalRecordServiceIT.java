package io.reflectoring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import io.reflectoring.dto.MedicalRecordCreationDto;
import io.reflectoring.exception.InvalidMedicalRecordIdException;
import net.bytebuddy.utility.RandomString;

@SpringBootTest
class MedicalRecordServiceIT {
	
	@Autowired
	private MedicalRecordService medicalRecordService;
	
	private static LocalStackContainer localStackContainer;

	static {
		localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:3"))
				.withCopyFileToContainer(MountableFile.forClasspathResource("init-dynamodb.sh", 0744), "/etc/localstack/init/ready.d/init-dynamodb.sh")
				.withServices(Service.DYNAMODB)
				.waitingFor(Wait.forLogMessage(".*Executed init-dynamodb.sh.*", 1));
		localStackContainer.start();
	}

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
		registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);
		registry.add("spring.cloud.aws.dynamodb.region", localStackContainer::getRegion);
		registry.add("spring.cloud.aws.dynamodb.endpoint", localStackContainer::getEndpoint);
	}
	
	@Test
	void validateDynamoDbInteractions() {
		// Prepare medical record creation request
		var patientName = RandomString.make();
		var diagnosis = RandomString.make();
		var treatmentPlan = RandomString.make();
		var creationRequest = new MedicalRecordCreationDto(patientName, diagnosis, treatmentPlan);

		// Create a new medical record
		var medicalRecordId = medicalRecordService.create(creationRequest);

		// Retrieve the newly created medical record and assert values
		var retrievedMedicalRecord = medicalRecordService.retrieve(medicalRecordId);
		assertThat(retrievedMedicalRecord.getId()).isNotBlank();
		assertThat(retrievedMedicalRecord.getPatientName()).isEqualTo(patientName);
		assertThat(retrievedMedicalRecord.getDiagnosis()).isEqualTo(diagnosis);
		assertThat(retrievedMedicalRecord.getTreatmentPlan()).isEqualTo(treatmentPlan);

		// Delete the medical record
		medicalRecordService.delete(medicalRecordId);

		// Attempt to retrieve the deleted medical record and assert that exception is thrown
		assertThrows(InvalidMedicalRecordIdException.class, () -> medicalRecordService.retrieve(medicalRecordId));
	}

}
