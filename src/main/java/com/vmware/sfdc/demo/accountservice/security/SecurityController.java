package com.vmware.sfdc.demo.accountservice.security;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityController.class);

	@GetMapping("/whoami")
	public String whoami(Principal principal, @RequestHeader HttpHeaders headers) {
		if (principal == null) {
			return "";
		}
		headers.forEach((key, value) -> {
			LOGGER.debug((String.format("Header '%s' = %s", key, value)));
		});
		// LOGGER.debug("jwt.getAuthorities().size(): " + jwt.getAuthorities().size());
		// Iterator iter = jwt.getAuthorities().iterator();
		// while (iter.hasNext()) {
		// GrantedAuthority ga = (GrantedAuthority) iter.next();
		// LOGGER.debug(ga.getAuthority());
		// }

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
