package com.vmware.sfdc.demo.accountservice.security;

import java.security.Principal;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

	@GetMapping("/whoami")
	public String whoami(Principal principal) {
		if (principal == null) {
			return "";
		}
		return principal.getName();
	}

	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<String> handleAccessDeniedException(Exception e) {
		return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler({ IllegalArgumentException.class })
	public ResponseEntity<String> handleIllegalArgumentException(Exception e) {
		return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

}
