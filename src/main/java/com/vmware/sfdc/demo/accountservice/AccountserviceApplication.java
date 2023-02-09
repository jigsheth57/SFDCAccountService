package com.vmware.sfdc.demo.accountservice;

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

import com.force.api.ApiSession;

@SpringBootApplication
public class AccountserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountserviceApplication.class, args);
	}

	@Bean
	public ReactiveRedisTemplate<String, ApiSession> reactiveRedisTemplate(
			ReactiveRedisConnectionFactory factory) {
		StringRedisSerializer keySerializer = new StringRedisSerializer();
		Jackson2JsonRedisSerializer<ApiSession> valueSerializer = new Jackson2JsonRedisSerializer<>(ApiSession.class);
		RedisSerializationContext.RedisSerializationContextBuilder<String, ApiSession> builder = RedisSerializationContext
				.newSerializationContext(keySerializer);
		RedisSerializationContext<String, ApiSession> context = builder.value(valueSerializer).build();
		return new ReactiveRedisTemplate<>(factory, context);
	}

	@Bean
	public HttpTraceRepository htttpTraceRepository() {
		return new InMemoryHttpTraceRepository();
	};

}
