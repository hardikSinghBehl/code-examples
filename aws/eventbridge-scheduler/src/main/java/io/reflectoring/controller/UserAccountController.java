package io.reflectoring.controller;

import io.reflectoring.dto.AccountDeactivationRequestDto;
import io.reflectoring.dto.CancelAccountDeactivationRequestDto;
import io.reflectoring.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserAccountController {

	private final UserAccountService userAccountService;

	@DeleteMapping(value = "/deactivate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deactivateAccount(@Valid @RequestBody AccountDeactivationRequestDto request) {
		userAccountService.deactivateAccount(request);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/deactivate/cancel", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> cancelAccountDeactivation(@Valid @RequestBody CancelAccountDeactivationRequestDto request) {
		userAccountService.cancelAccountDeactivation(request);
		return ResponseEntity.noContent().build();
	}

}