//package com.BankTransactionLoadbalancer.Loadbalancer.controller.BankTransaction;
//
//import io.github.resilience4j.circuitbreaker.CircuitBreaker;
//import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
//import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
//import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.time.Duration;
//
//@Configuration
//public class BankTransactionConfig {
////
////    @Bean
////    @LoadBalanced
////    WebClient.Builder builder() {
////        return WebClient.builder();
////    }
////
////    @Bean
////    WebClient webClient(WebClient.Builder builder){
////        return builder.build();
////    }
//
////    @Bean // 1
////    public CircuitBreakerRegistry circuitBreakerRegistry() {
////        return CircuitBreakerRegistry.ofDefaults();
////    }
////
////    @Bean
////    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
////        return circuitBreakerRegistry.circuitBreaker("upload");
////    }
////
////    @Bean
////    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
////        return circuitBreakerRegistry.circuitBreaker("select");
////    }
////
////    @Bean
////    public CircuitBreakerConfigCustomizer uploadCSVFBCircuitBreakerConfig(){
////        return CircuitBreakerConfigCustomizer
////                .of("upload", builder ->
////                        builder.minimumNumberOfCalls(10)
////                                .failureRateThreshold(50.0f));
////    }
////
////    @Bean
////    public CircuitBreakerConfigCustomizer findTransactionByUserFBCircuitBreakerConfig(){
////        return CircuitBreakerConfigCustomizer
////                .of("select", builder ->
////                        builder.waitDurationInOpenState(Duration.ofSeconds(30))
////                                .minimumNumberOfCalls(10)
////                                .failureRateThreshold(50.0f));
////    }
//
////    @Bean
////    public CircuitBreakerConfigCustomizer findTransactionByBankFBCircuitBreakerConfig(){
////        return CircuitBreakerConfigCustomizer
////                .of("findTransactionByBankFB", builder ->
////                        builder.waitDurationInOpenState(Duration.ofSeconds(30))
////                                .minimumNumberOfCalls(10)
////                                .failureRateThreshold(50.0f));
////    }
//}
