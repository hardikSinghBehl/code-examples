package io.reflectoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

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

}
