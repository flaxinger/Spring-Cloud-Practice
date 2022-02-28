package com.BankTransactionLoadbalancer.Loadbalancer.controller.BankTransaction;

import com.BankTransactionLoadbalancer.Loadbalancer.controller.BankTransaction.dto.RequestDto;
import com.BankTransactionLoadbalancer.Loadbalancer.global.common.Response;
import com.BankTransactionLoadbalancer.Loadbalancer.service.BankTransactionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@RequestMapping("/bt")
@RestController
@RequiredArgsConstructor
public class BankTransactionController {

    private final BankTransactionService bankTransactionService;

    @CircuitBreaker(name = "upload", fallbackMethod = "uploadCSVFB")
    @PostMapping(value = "/upload")
    public Mono<String> uploadCSV(@RequestPart("file") FilePart file) throws IOException {
//    public Mono<String> uploadCSV(@RequestPart("file") MultipartFile file) throws IOException {
//    public Mono<String> uploadCSV(@RequestPart("file") Mono<FilePart> file) throws IOException{

        if(file==null) {
            log.warn("file is empty");
            return Mono.empty();
        }
//        return bankTransactionService.uploadCSV(file);
        return bankTransactionService.uploadCSVOrig(file);
    }

    @GetMapping(value = "/{userId}/transactions")
    @CircuitBreaker(name = "select", fallbackMethod = "findTransactionByUserFB")
    public Mono<String> findTransactionByUser(@PathVariable String userId,
                                              RequestDto.RequestByUser requestByUser) {
        return bankTransactionService.findTransactionByUser(userId, requestByUser);
    }

    @GetMapping
    @CircuitBreaker(name = "select", fallbackMethod = "findTransactionByBankFB")
    public Mono<String> findTransactionByBank(RequestDto.RequestByBank requestByBank){

        return bankTransactionService.findTransactionByBank(requestByBank);
    }

    public Mono<String> uploadCSVFB(FilePart file, Throwable t) throws IOException {
//        log.error("[Fallback Error] "+t.getMessage());
        return Mono.just(Response.FALLBACK);
    }

    public Mono<String> findTransactionByUserFB(String userId, RequestDto.RequestByUser requestByUser, Throwable t) {
//        log.error("[Fallback Error] "+t.getMessage());
        return Mono.just(Response.FALLBACK);
    }

    public Mono<String> findTransactionByBankFB(RequestDto.RequestByBank requestByBank, Throwable t){
//        log.error("[Fallback Error] "+t.getMessage());
        return Mono.just(Response.FALLBACK);
    }

}