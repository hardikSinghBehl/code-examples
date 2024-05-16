package io.reflectoring.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import io.reflectoring.configuration.UserAccountProperties;
import io.reflectoring.dto.AccountDeactivationRequestDto;
import io.reflectoring.dto.CancelAccountDeactivationRequestDto;
import io.reflectoring.utility.ScheduleExpressionGenerator;
import io.reflectoring.utility.ScheduleNameGenerator;
import io.reflectoring.utility.ScheduleRegistrar;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(UserAccountProperties.class)
public class UserAccountService {

	private final ScheduleRegistrar scheduleRegistrar;
	private final UserAccountProperties userAccountProperties;
	private final ScheduleNameGenerator scheduleNameGenerator;
	private final ScheduleExpressionGenerator scheduleExpressionGenerator;

	public void deactivateAccount(@NonNull final AccountDeactivationRequestDto request) {
		final var scheduleName = scheduleNameGenerator.generate(request.emailId());

		final var deactivationDateTime = LocalDateTime.now(ZoneOffset.UTC).plus(userAccountProperties.getDeactivationDelay());
		final var scheduleExpression = scheduleExpressionGenerator.generate(deactivationDateTime);

		scheduleRegistrar.register(scheduleName, scheduleExpression, request);
	}

	public void cancelAccountDeactivation(@NonNull final CancelAccountDeactivationRequestDto request) {
		final var scheduleName = scheduleNameGenerator.generate(request.emailId());
		scheduleRegistrar.delete(scheduleName);
	}

}
