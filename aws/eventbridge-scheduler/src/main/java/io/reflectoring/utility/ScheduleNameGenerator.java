package io.reflectoring.utility;

import org.springframework.stereotype.Component;

import lombok.NonNull;

@Component
public class ScheduleNameGenerator {

	private static final String DEACTIVATION_SCHEDULE_NAME_FORMAT = "user_deactivation_schedule_%s";

	public String generateDeactivationScheduleName(@NonNull final String userIdentifier) {
		return String.format(DEACTIVATION_SCHEDULE_NAME_FORMAT, sanitize(userIdentifier));
	}

	private String sanitize(@NonNull final String userIdentifier) {
		return userIdentifier.replaceAll("[^0-9a-zA-Z-_.]", "_");
	}

}