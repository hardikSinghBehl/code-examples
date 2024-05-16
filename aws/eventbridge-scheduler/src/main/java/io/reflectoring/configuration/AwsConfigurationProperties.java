package io.reflectoring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.reflectoring.aws")
public class AwsConfigurationProperties {

	@NotBlank(message = "AWS access key must be configured")
	private String accessKey;

	@NotBlank(message = "AWS secret key must be configured")
	private String secretKey;

	@Valid
	private EventbridgeScheduler eventbridgeScheduler = new EventbridgeScheduler();

	@Valid
	private SQS sqs = new SQS();

	@Getter
	@Setter
	@Validated
	public class EventbridgeScheduler {

		@NotBlank(message = "Eventbridge scheduler region must be configured")
		private String region;

		@Nullable
		private String endpoint;

		@Valid
		private Target target = new Target();

		@Getter
		@Setter
		@Validated
		public class Target {

			@NotBlank(message = "Eventbridge scheduler target ARN must be configured")
			private String arn;

			@NotBlank(message = "Eventbridge scheduler role ARN must be configured")
			private String roleArn;

		}

	}

	@Getter
	@Setter
	@Validated
	public class SQS {

		@NotBlank(message = "SQS queue region must be configured")
		private String region;

		@NotBlank(message = "SQS queue URL must be configured")
		private String queueUrl;

		@Nullable
		private String endpoint;

	}

}