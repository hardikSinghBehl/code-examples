package io.reflectoring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import io.reflectoring.validation.SnsTopicExists;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.reflectoring.aws.sns")
public class AwsSnsTopicProperties {

	@SnsTopicExists
	@NotBlank(message = "SNS topic ARN must be configured")
	private String topicArn;

}
