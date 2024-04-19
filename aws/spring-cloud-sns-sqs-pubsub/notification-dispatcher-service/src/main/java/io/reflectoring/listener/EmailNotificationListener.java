package io.reflectoring.listener;

import org.springframework.stereotype.Component;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.annotation.SnsNotificationMessage;
import io.reflectoring.dto.UserCreatedEventDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailNotificationListener {

	@SqsListener("${io.reflectoring.aws.sqs.queue-arn}")
	public void listen(@SnsNotificationMessage final UserCreatedEventDto userCreatedEvent) {
		log.info("Dispatching account creation email to {} on {}", userCreatedEvent.getName(), userCreatedEvent.getEmailId());
		// business logic to send email
	}

}
