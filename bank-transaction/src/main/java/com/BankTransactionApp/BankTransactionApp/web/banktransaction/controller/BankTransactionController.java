package com.BankTransactionApp.BankTransactionApp.web.banktransaction.controller;

import com.BankTransactionApp.BankTransactionApp.global.common.Response;
import com.BankTransactionApp.BankTransactionApp.web.Account.dto.AccountDto;
import com.BankTransactionApp.BankTransactionApp.web.bank.dto.BankDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.BankTransactionDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.RequestDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.ResponseDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.service.BankTransactionService;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.util.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/bt")
@RequiredArgsConstructor
public class BankTransactionController {

    private final BankTransactionService bankTransactionService;
    @Autowired
    private HttpServletRequest request;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private Integer batchSize;

    @PostMapping("/upload")
    public Response.Item<String> uploadCSV(@RequestParam("file") MultipartFile file) throws IOException {

        String path = request.getServletContext().getRealPath("/");
        log.info("path is {}", path);
        file.transferTo(new File(path));
        Set<AccountDto> accountDtos = new HashSet<>();
        Set<BankTransactionDto> bankTransactionDtos = new HashSet<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

        log.info("Got request at uploadCSV");


        String csvLine;

        if(file.isEmpty()){
            return new Response.Item<>("The given file is empty");
        }
        else{
//            Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

            BufferedReader reader = new BufferedReader(new FileReader(path));

            try(reader){
                while((csvLine = reader.readLine())!=null){
                    String[] csvRecord = csvLine.split(",");
                    AccountDto accountDto = AccountDto.builder()
                            .id(Long.valueOf(csvRecord[4]))
                            .name(csvRecord[4]).build();

                    accountDtos.add(accountDto);

                    bankTransactionDtos.add(BankTransactionDto.builder()
                            .id(Long.parseLong(csvRecord[0], 10))
                            .date(LocalDate.parse(csvRecord[1]
                                    +'-'+csvRecord[2]+
                                    '-'+csvRecord[3], formatter))
                            .account(accountDto)
                            .bank(BankDto.builder().bankCode(csvRecord[5]).build())
                            .transactionAmount(Long.valueOf(csvRecord[6]))
                            .transactionType(TransactionType.of(csvRecord[7]))
                            .build());

                    if(bankTransactionDtos.size() == batchSize*3){
                        bankTransactionService.saveCSVBatch(accountDtos, bankTransactionDtos);
                        accountDtos.clear();
                        bankTransactionDtos.clear();
                    }
                }
            }
            catch (IOException e){
                log.error("Error occured in uploadCSV:", e.getMessage());
            }
            return new Response.Item<>("All transactions were saved properly");
        }
    }

    @GetMapping(value = "/{userId}/transactions")
    @ResponseBody
    public Response.ItemList<ResponseDto> findTransactionByUser(@PathVariable String userId,
                                                                RequestDto.RequestByUser requestByUser){
        log.info("Got request at findTransactionByUser");
        return new Response.ItemList<>(bankTransactionService.findTransactionByUser(Long.valueOf(userId),
                RequestDto.parseDate(requestByUser.getDate()),
                TransactionType.of(requestByUser.getType())));
    }

    @GetMapping
    @ResponseBody
    public Response.ItemList<ResponseDto> findTransactionByBank(RequestDto.RequestByBank requestByBank){

        log.info("Got request at findTransactionByBank");
        return new Response.ItemList<>(bankTransactionService.findTransactionByBank(requestByBank.getBank(),
                RequestDto.parseDate(requestByBank.getDate()),
                TransactionType.of(requestByBank.getType())));
    }

    @PostMapping(name = "/upload/stream", consumes = {"multipart/form-data"})
//    public Response.Item<String> uploadCSV(@RequestBody Flux<FilePart> file) throws IOException {
    public Response.Item<String> uploadCSV(@ModelAttribute Flux<FilePart> file) throws IOException {

        Set<AccountDto> accountDtos = new HashSet<>();
        Set<BankTransactionDto> bankTransactionDtos = new HashSet<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

        log.info("got file");
        return new Response.Item<>("got file");
//        if(file.isEmpty()){
//            return new Response.Item<>("The given file is empty");
//        }
//        else{
//            Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
//
//            try(reader){
//
//                CSVParser csvParser = CSVFormat.DEFAULT.parse(reader);
//                csvParser.getRecords()
//                        .forEach(csvRecord->{
//
//                            AccountDto accountDto = AccountDto.builder()
//                                    .id(Long.valueOf(csvRecord.get(4)))
//                                    .name(csvRecord.get(4)).build();
//
//                            accountDtos.add(accountDto);
//
//                            bankTransactionDtos.add(BankTransactionDto.builder()
////                                    .id(csvRecord.get(0) instanceof String ? Long.parseLong(String.valueOf(csvRecord.get(0)), 10) : Long.valueOf(csvRecord.get(0)))
//                                    .id(Long.parseLong(csvRecord.get(0).toString(), 10))
//                                    .date(LocalDate.parse(csvRecord.get(1).toString()
//                                            +'-'+csvRecord.get(2).toString()+
//                                            '-'+csvRecord.get(3).toString(), formatter))
//                                    .account(accountDto)
//                                    .bank(BankDto.builder().bankCode(csvRecord.get(5)).build())
//                                    .transactionAmount(Long.valueOf(csvRecord.get(6)))
//                                    .transactionType(TransactionType.of(csvRecord.get(7)))
//                                    .build());
//
//                        });
//            }
//            catch (IOException e){
//                log.error("Error occured in uploadCSV:", e.getMessage());
//            }
//            return new Response.Item<>(bankTransactionService.saveCSVBatch(accountDtos, bankTransactionDtos));
//        }
    }
}
