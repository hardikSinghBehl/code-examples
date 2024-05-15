package io.reflectoring.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import io.reflectoring.dto.AccountDeactivationRequestDto;
import io.reflectoring.dto.CancelAccountDeactivationRequestDto;
import io.reflectoring.utility.ScheduleExpressionGenerator;
import io.reflectoring.utility.ScheduleNameGenerator;
import io.reflectoring.utility.ScheduleRegistrar;

@Service
@RequiredArgsConstructor
public class UserAccountService {

	private final ScheduleRegistrar scheduleRegistrar;
	private final ScheduleNameGenerator scheduleNameGenerator;
	private final ScheduleExpressionGenerator scheduleExpressionGenerator;

	public void deactivateAccount(@NonNull final AccountDeactivationRequestDto request) {
		final var scheduleName = scheduleNameGenerator.generateDeactivationScheduleName(request.emailId());

		final var deactivationDate = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(2);
		final var scheduleExpression = scheduleExpressionGenerator.generate(deactivationDate);

		scheduleRegistrar.register(scheduleName, scheduleExpression, request);
	}

	public void cancelAccountDeactivation(@NonNull final CancelAccountDeactivationRequestDto request) {
		final var scheduleName = scheduleNameGenerator.generateDeactivationScheduleName(request.emailId());
		scheduleRegistrar.delete(scheduleName);
	}

}
