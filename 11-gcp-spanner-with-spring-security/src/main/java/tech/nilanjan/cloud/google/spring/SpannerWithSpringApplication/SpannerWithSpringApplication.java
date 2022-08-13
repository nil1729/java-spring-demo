package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.spanner.SpannerSchemaTools;

@SpringBootApplication
public class SpannerWithSpringApplication {
	private final SpannerSchemaTools spannerSchemaTools;
	@Autowired
	public SpannerWithSpringApplication(SpannerSchemaTools spannerSchemaTools) {
		this.spannerSchemaTools = spannerSchemaTools;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpannerWithSpringApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner() {
		return args -> {
			spannerSchemaTools.createTableIfNotExists();
		};
	}
}
