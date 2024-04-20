package io.reflectoring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import io.reflectoring.validation.SqsQueueExists;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.reflectoring.aws.sqs")
public class AwsSqsQueueProperties {

	@SqsQueueExists
	@NotBlank(message = "SQS queue URL must be configured")
	private String queueUrl;

}
