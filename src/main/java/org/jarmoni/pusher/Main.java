package org.jarmoni.pusher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// CHECKSTYLE:OFF
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Main {

	public static void main(final String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
// CHECKSTYLE:ON
