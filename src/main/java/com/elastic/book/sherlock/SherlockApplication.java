package com.elastic.book.sherlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@EnableScheduling
public class SherlockApplication {

	public static void main(String[] args) {
		SpringApplication.run(SherlockApplication.class, args);
	}

	@Bean
	public TaskScheduler taskScheduler(){

		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(1);
		threadPoolTaskScheduler.setThreadNamePrefix("sherlock-scheduler-task");
		return threadPoolTaskScheduler;
	}
}
