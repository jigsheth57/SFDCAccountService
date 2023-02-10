package com.vmware.sfdc.demo.accountservice.security;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;

@EnableWebFluxSecurity
@Configuration
@Profile("!cloud & !k8s") // cloud profile is automatically activated on CloudFoundry
public class SecurityConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	ReactiveUserDetailsService reactiveUserDetailsService(PasswordEncoder passwordEncoder) {
		return new MapReactiveUserDetailsService(
				User.withUsername("alice").password(passwordEncoder.encode("test")).roles("USER").build(),
				User.withUsername("bob").password(passwordEncoder.encode("test")).roles("USER").build());
	}

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
		LOGGER.debug("securityWebFilterChain(): default");
		// @formatter:off
		RedirectServerLogoutSuccessHandler logoutHandler = new RedirectServerLogoutSuccessHandler();
		logoutHandler.setLogoutSuccessUrl(URI.create("http://localhost:52885/"));
		return httpSecurity
			.httpBasic().disable()
			.formLogin().authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("http://localhost:52885/")).and()
			.logout()
				.logoutSuccessHandler(logoutHandler)
				.and()
			.csrf().disable()
			.authorizeExchange()
				.pathMatchers("/whoami").authenticated()
				.anyExchange().permitAll()
			.and()
			.build();
		// @formatter:on
	}
}
