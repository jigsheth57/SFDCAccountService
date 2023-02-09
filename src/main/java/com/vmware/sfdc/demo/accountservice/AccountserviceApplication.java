package com.vmware.sfdc.demo.accountservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.reactive.function.client.WebClient;

import com.vmware.sfdc.demo.accountservice.model.Account;
import com.vmware.sfdc.demo.accountservice.model.AccountList;

@SpringBootApplication
public class AccountserviceApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountserviceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AccountserviceApplication.class, args);
	}

	@Autowired
	@Bean
	public WebClient webClient(@Value("${sfdc.authserviceURL}") String authserviceURL) {
		LOGGER.debug("webClient: authserviceURL {}", authserviceURL);
		return WebClient.builder().baseUrl(authserviceURL).build();
	}

	@Bean("accountTemplate")
	public ReactiveRedisTemplate<String, Account> accountTemplate(
			ReactiveRedisConnectionFactory factory) {
		StringRedisSerializer keySerializer = new StringRedisSerializer();
		Jackson2JsonRedisSerializer<Account> valueSerializer = new Jackson2JsonRedisSerializer<>(Account.class);
		RedisSerializationContext.RedisSerializationContextBuilder<String, Account> builder = RedisSerializationContext
				.newSerializationContext(keySerializer);
		RedisSerializationContext<String, Account> context = builder.value(valueSerializer).build();
		return new ReactiveRedisTemplate<>(factory, context);
	}

	@Bean("accountListTemplate")
	public ReactiveRedisTemplate<String, AccountList> accountListTemplate(
			ReactiveRedisConnectionFactory factory) {
		StringRedisSerializer keySerializer = new StringRedisSerializer();
		Jackson2JsonRedisSerializer<AccountList> valueSerializer = new Jackson2JsonRedisSerializer<>(AccountList.class);
		RedisSerializationContext.RedisSerializationContextBuilder<String, AccountList> builder = RedisSerializationContext
				.newSerializationContext(keySerializer);
		RedisSerializationContext<String, AccountList> context = builder.value(valueSerializer).build();
		return new ReactiveRedisTemplate<>(factory, context);
	}

	@Bean
	public HttpTraceRepository htttpTraceRepository() {
		return new InMemoryHttpTraceRepository();
	};

}
