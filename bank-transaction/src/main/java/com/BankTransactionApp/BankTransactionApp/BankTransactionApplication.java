package com.BankTransactionApp.BankTransactionApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BankTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankTransactionApplication.class, args);
	}

}
