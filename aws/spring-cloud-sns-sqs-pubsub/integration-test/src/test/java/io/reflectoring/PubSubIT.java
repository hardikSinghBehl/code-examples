package io.reflectoring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import lombok.SneakyThrows;
import net.bytebuddy.utility.RandomString;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class PubSubIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	private static final LocalStackContainer localStackContainer;
	
	// names as configured in src/test/resources
	private static final String TOPIC_NAME = "user-account-created";
	private static final String QUEUE_NAME = "dispatch-email-notification";
	
	static {
		localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.3"))
				.withCopyFileToContainer(MountableFile.forClasspathResource("init-sns-topic.sh", 0744), "/etc/localstack/init/ready.d/init-sns-topic.sh")
				.withCopyFileToContainer(MountableFile.forClasspathResource("init-sqs-queue.sh", 0744), "/etc/localstack/init/ready.d/init-sqs-queue.sh")
				.withCopyFileToContainer(MountableFile.forClasspathResource("subscribe-sqs-to-sns.sh", 0744), "/etc/localstack/init/ready.d/subscribe-sqs-to-sns.sh")
				.withServices(Service.SNS, Service.SQS)
				.withEnv("SQS_ENDPOINT_STRATEGY", "off")
				.waitingFor(Wait.forLogMessage(".*Executed subscribe-sqs-to-sns.*", 1));
		localStackContainer.start();
	}
	
	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
		registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);
		
		registry.add("spring.cloud.aws.sns.region", localStackContainer::getRegion);
		registry.add("spring.cloud.aws.sns.endpoint", localStackContainer::getEndpoint);
		registry.add("io.reflectoring.aws.sns.topic-name", () -> TOPIC_NAME);

		registry.add("spring.cloud.aws.sqs.region", localStackContainer::getRegion);
		registry.add("spring.cloud.aws.sqs.endpoint", localStackContainer::getEndpoint);
		registry.add("io.reflectoring.aws.sqs.queue-name", () -> QUEUE_NAME);		
	}
	
	@Test
	@SneakyThrows
	void test(CapturedOutput output) {
		// prepare API request body to create user
		final var name = RandomString.make();
		final var emailId = RandomString.make() + "@domain.it";
		final var password = RandomString.make();
		final var userCreationRequestBody = String.format("""
		{
			"name"   : "%s",
			"emailId"  : "%s",
			"password" : "%s"
		}
		""", name, emailId, password);
				
		// execute API request to create user
		final var userCreationApiPath = "/api/v1/users";
		mockMvc.perform(post(userCreationApiPath)
			.contentType(MediaType.APPLICATION_JSON)
			.content(userCreationRequestBody))
			.andExpect(status().isCreated());
		
		// assert that message has been received by the queue
		final var expectedLog = String.format("Dispatching account creation email to %s on %s", name, emailId);
		Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> output.getAll().contains(expectedLog));
	}
	
}
