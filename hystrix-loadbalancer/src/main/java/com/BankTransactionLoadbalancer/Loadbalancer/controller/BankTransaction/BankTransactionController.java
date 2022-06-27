package com.BankTransactionLoadbalancer.Loadbalancer.controller.BankTransaction;

import com.BankTransactionLoadbalancer.Loadbalancer.controller.BankTransaction.dto.RequestDto;
import com.BankTransactionLoadbalancer.Loadbalancer.global.common.Response;
import com.BankTransactionLoadbalancer.Loadbalancer.service.BankTransactionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@RequestMapping("/bt")
@RestController
@RequiredArgsConstructor
public class BankTransactionController {

    private final BankTransactionService bankTransactionService;


    @CircuitBreaker(name = "upload", fallbackMethod = "uploadCSVFB")
    @PostMapping(value = "/upload", produces = "application/json; charset=UTF8")
    public Mono<String> uploadCSV(@RequestPart("file") FilePart file) throws IOException {

        if(file==null) {
            log.warn("file is empty");
            return Mono.empty();
        }
        return bankTransactionService.uploadCSV(file);
    }

    @GetMapping(value = "/{userId}/transactions", produces = "application/json; charset=UTF8")
    @CircuitBreaker(name = "select", fallbackMethod = "findTransactionByUserFB")
    public Mono<String> findTransactionByUser(@PathVariable String userId,
                                              RequestDto.RequestByUser requestByUser) {
        return bankTransactionService.findTransactionByUser(userId, requestByUser);
    }

    @GetMapping(produces = "application/json; charset=UTF8")
    @CircuitBreaker(name = "select", fallbackMethod = "findTransactionByBankFB")
    public Mono<String> findTransactionByBank(RequestDto.RequestByBank requestByBank){

        return bankTransactionService.findTransactionByBank(requestByBank);
    }

    public Mono<String> uploadCSVFB(FilePart file, Throwable t) throws IOException {
        log.error(t.getMessage());
        return Mono.just(Response.FALLBACK).cast(String.class);
    }

    public Mono<String> findTransactionByUserFB(String userId, RequestDto.RequestByUser requestByUser, Throwable t) {
        log.error(t.getMessage());
        return Mono.just(Response.FALLBACK).cast(String.class);
    }

    public Mono<String> findTransactionByBankFB(RequestDto.RequestByBank requestByBank, Throwable t){
        log.error(t.getMessage());
        return Mono.just(Response.FALLBACK).cast(String.class);
    }

}
