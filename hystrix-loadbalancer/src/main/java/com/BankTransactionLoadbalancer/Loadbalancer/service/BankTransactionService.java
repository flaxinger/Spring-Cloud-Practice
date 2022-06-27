package com.BankTransactionLoadbalancer.Loadbalancer.service;

import com.BankTransactionLoadbalancer.Loadbalancer.controller.BankTransaction.dto.RequestDto;
import com.BankTransactionLoadbalancer.Loadbalancer.global.config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankTransactionService {

    private final WebClient webClient;
    private static String baseUrl = "http://bank-transaction";


    public Mono<String> uploadCSV(FilePart file) throws IOException {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file);

        String uri = baseUrl+"/bt/upload/loaddata";
        log.info("Making request to "+ uri);

        return webClient
                .post()
                .uri(uri)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(String.class);
    }



    public Mono<String> findTransactionByUser(String userId, RequestDto.RequestByUser requestByUser) {

        String uri = baseUrl+"/bt/"+userId+"/transactions"+requestByUser.toString();
        log.info("Making request to "+uri);

        return webClient
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);

    }


    public Mono<String> findTransactionByBank(RequestDto.RequestByBank requestByBank){

        String uri = baseUrl+"/bt"+requestByBank.toString();
        log.info("Making request to "+uri);

        return webClient
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);


    }

}
