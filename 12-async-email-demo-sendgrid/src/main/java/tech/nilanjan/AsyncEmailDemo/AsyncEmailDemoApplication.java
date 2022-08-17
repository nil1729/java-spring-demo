package tech.nilanjan.AsyncEmailDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AsyncEmailDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsyncEmailDemoApplication.class, args);
	}

}
