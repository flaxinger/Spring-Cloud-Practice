package com.BankTransactionLoadbalancer.Loadbalancer.service;

import com.BankTransactionLoadbalancer.Loadbalancer.controller.BankTransaction.dto.RequestDto;
import com.BankTransactionLoadbalancer.Loadbalancer.global.common.Response;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankTransactionService {

    private final WebClient.Builder loadBalancedWebClientBuilder;
    private final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
            .build();
    private static String baseUrl = "http://bank-transaction";


    public Mono<String> uploadCSVOrig(FilePart file) throws IOException {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file);

        String uri = baseUrl+"/bt/upload";
        log.info("Making request to "+ uri);

        return loadBalancedWebClientBuilder
                .exchangeStrategies(exchangeStrategies)
                .build()
                .post()
                .uri(uri)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
//                .body(BodyInserters.fromPublisher(file.content(), DataBuffer.class))
                .retrieve()
                .bodyToMono(String.class);
    }



    public Mono<String> findTransactionByUser(String userId, RequestDto.RequestByUser requestByUser) {

        String uri = baseUrl+userId+"/transactions"+requestByUser.toString();
        log.info("Making request to "+uri);

        return loadBalancedWebClientBuilder
                .exchangeStrategies(exchangeStrategies)
                .build()
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);

    }


    public Mono<String> findTransactionByBank(RequestDto.RequestByBank requestByBank){

        String uri = baseUrl+"/bt"+requestByBank.toString();
        log.info("Making request to "+uri);

        return loadBalancedWebClientBuilder
                .exchangeStrategies(exchangeStrategies)
                .build()
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);


    }

    public Mono<String> uploadCSV(FilePart file) throws IOException {
//    public Mono<String> uploadCSV(Mono<FilePart> file) throws IOException {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
//        builder.part("file", file.cast(MultipartFile.class).then(data -> data));
        builder.asyncPart("file", file.content(), DataBuffer.class);
//        builder.part("file", file.flatMap(filePart ->
//                filePart.content().map(dataBuffer -> {
//                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
//                    dataBuffer.read(bytes);
//                    DataBufferUtils.release(dataBuffer);
//
//                    return bytes;
//                }));


        String uri = baseUrl+"/bt/upload";
//        String uri = baseUrl+"/bt/upload";

        log.info("Making request to "+ uri);
//        return file
//                .cast(MultipartFile.class)
//                .then(data ->{
//                    loadBalancedWebClientBuilder
//                            .exchangeStrategies(exchangeStrategies)
//                            .build()
//                            .post()
//                            .uri(uri)
//                            .contentType(MediaType.MULTIPART_FORM_DATA)
//                            .body(BodyInserters.fromMultipartData(data))
//                            .retrieve()
//                            .bodyToMono(String.class);
//                });


//            return file
//                .flatMap(filePart -> {
//                    return loadBalancedWebClientBuilder
//                            .exchangeStrategies(exchangeStrategies)
//                            .build()
//                            .post()
//                            .uri(uri)
//                            .body(BodyInserters.fromPublisher(filePart.content(), DataBuffer.class))
//                            .retrieve()
//                            .bodyToMono(String.class);
////                          .exchange()
////                            .flatMap(clientResponse -> {
////                                log.info("got response from "+uri);
////                                return Mono.just(clientResponse);
////                            });
//                });

        return loadBalancedWebClientBuilder
                .exchangeStrategies(exchangeStrategies)
                .build()
                .post()
                .uri(uri)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
//                .body(BodyInserters.fromPublisher(file.content(), DataBuffer.class))
                .retrieve()
                .bodyToMono(String.class);

    }

}
