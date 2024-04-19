package io.reflectoring.validation;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.ListTopicsResponse;
import software.amazon.awssdk.services.sns.model.Topic;

@RequiredArgsConstructor
public class SnsTopicExistenceValidator implements ConstraintValidator<SnsTopicExists, String> {

	private final SnsClient snsClient;

	@Override
	public boolean isValid(final String topicArn, final ConstraintValidatorContext context) {
		final var snsTopics = getTopics();
		for (var snsTopic : snsTopics) {
			if (snsTopic.topicArn().equals(topicArn)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	private List<Topic> getTopics() {
		final var snsTopics = new ArrayList<Topic>();
		ListTopicsResponse listTopicsResponse = snsClient.listTopics();
		snsTopics.addAll(listTopicsResponse.topics());

		while (listTopicsResponse.nextToken() != null) {
			var nextToken = listTopicsResponse.nextToken();
			listTopicsResponse = snsClient.listTopics(request -> request.nextToken(nextToken));
			snsTopics.addAll(listTopicsResponse.topics());
		}
		return snsTopics;
	}

}