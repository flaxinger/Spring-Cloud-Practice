package com.BankTransactionLoadbalancer.Loadbalancer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@EnableDiscoveryClient
@SpringBootApplication
public class LoadbalancerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoadbalancerApplication.class, args);
	}

}

@Configuration
class Config {

	@Bean
	@LoadBalanced
	WebClient.Builder builder() {
		return WebClient.builder();
	}

	@Bean
	WebClient webClient(WebClient.Builder builder){
		return builder.build();
	}
}
