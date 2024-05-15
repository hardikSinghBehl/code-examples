package io.reflectoring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
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
	private Eventbridge eventbridge = new Eventbridge();

	@Getter
	@Setter
	@Validated
	public class Eventbridge {

		@NotBlank(message = "AWS eventbridge region must be configured")
		private String region;

	}

}