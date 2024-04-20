package io.reflectoring.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;

@RequiredArgsConstructor
public class SnsTopicExistenceValidator implements ConstraintValidator<SqsQueueExists, String> {

	private final SqsAsyncClient sqsAsyncClient;

	@Override
	@SneakyThrows
	public boolean isValid(final String queueUrl, final ConstraintValidatorContext context) {
		try {
			sqsAsyncClient.getQueueAttributes(request -> request.queueUrl(queueUrl)).get();
		} catch (Exception exception) {
			if (exception.getCause() instanceof QueueDoesNotExistException) {
				return Boolean.FALSE;
			}
			throw exception;
		}
		return Boolean.TRUE;
	}

}