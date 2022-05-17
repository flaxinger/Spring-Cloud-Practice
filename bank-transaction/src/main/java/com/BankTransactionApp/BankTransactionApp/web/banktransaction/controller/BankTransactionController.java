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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Value("${flaxinger.upload-directory}")
    private String uploadDir;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

    @PostMapping("/upload")
    public Response.Item<String> uploadCSV(@RequestParam("file") MultipartFile file) throws IOException {

        String path = request.getServletContext().getRealPath("/");
        log.info("path is {}", path);
        file.transferTo(new File(path));
        Set<AccountDto> accountDtos = new HashSet<>();
        Set<BankTransactionDto> bankTransactionDtos = new HashSet<>();

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

                    if(bankTransactionDtos.size() == batchSize*300){
                        long start = System.currentTimeMillis();
                        bankTransactionService.saveCSVBatch(accountDtos, bankTransactionDtos);
                        log.info("transaction took {}ms", start - System.currentTimeMillis());
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

    @GetMapping("/{userId}/transactions")
    public Response.ItemList<ResponseDto> findTransactionByUser(@PathVariable String userId,
                                                                RequestDto.RequestByUser requestByUser){
        log.info("Got request at findTransactionByUser");
        return new Response.ItemList<>(
                bankTransactionService.findTransactionByUser(
                        Long.valueOf(userId),
                        RequestDto.parseDate(requestByUser.getDate()),
                        TransactionType.of(requestByUser.getType()),
                        requestByUser.getPage()
                )
        );
    }

    @GetMapping
    public Response.ItemList<ResponseDto> findTransactionByBank(RequestDto.RequestByBank requestByBank){

        log.info("Got request at findTransactionByBank");
        return new Response.ItemList<>(
                bankTransactionService.findTransactionByBank(
                        requestByBank.getBank(),
                        RequestDto.parseDate(requestByBank.getDate()),
                        TransactionType.of(requestByBank.getType()),
                        requestByBank.getPage()
                )
        );
    }

    @PostMapping("/upload/loaddata")
    public Response.Item<String> uploadCSVAndLoadData(@RequestParam("file") MultipartFile file) throws IOException {

        String path = request.getServletContext().getRealPath("/");
        file.transferTo(new File(path));
        Set<AccountDto> accountDtos = new HashSet<>();
        log.info("Got request at uploadCSV");

        String csvLine;

        if(file.isEmpty()){
            return new Response.Item<>("The given file is empty");
        }
        else{
            if(! new File(uploadDir).exists()){
                new File(uploadDir).mkdir();
                log.info("{} is created", uploadDir);
            }
            File uploadFile = new File(uploadDir+'\\'+ LocalDateTime.now().hashCode()+".txt");
            System.out.println(uploadFile.getAbsolutePath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(uploadFile.getAbsoluteFile()));
            BufferedReader reader = new BufferedReader(new FileReader(path));

            try(writer){
                try(reader) {
                    while ((csvLine = reader.readLine()) != null) {
                        String[] csvRecord = csvLine.split(",");
                        AccountDto accountDto = AccountDto.builder()
                                .id(Long.valueOf(csvRecord[4]))
                                .name(csvRecord[4]).build();

                        accountDtos.add(accountDto);

                        writer.write(BankTransactionDto.builder()
                                .id(Long.parseLong(csvRecord[0], 10))
                                .date(LocalDate.parse(csvRecord[1]
                                        + '-' + csvRecord[2] +
                                        '-' + csvRecord[3], formatter))
                                .account(accountDto)
                                .bank(BankDto.builder().bankCode(csvRecord[5]).build())
                                .transactionAmount(Long.valueOf(csvRecord[6]))
                                .transactionType(TransactionType.of(csvRecord[7]))
                                .build().toString());
                    }
                }
            }
            catch (IOException e){
                log.error("Error occured in uploadCSV:", e.getMessage());
            }
            bankTransactionService.loadData(accountDtos, uploadFile.getAbsolutePath());
            uploadFile.delete();
            return new Response.Item<>("업로드 성공");
        }
    }


    @PostMapping(name = "/upload/stream", consumes = {"multipart/form-data"})
    public Response.Item<String> uploadCSV(@ModelAttribute Flux<FilePart> file) throws IOException {

        Set<AccountDto> accountDtos = new HashSet<>();
        Set<BankTransactionDto> bankTransactionDtos = new HashSet<>();

        log.info("got file");
        return new Response.Item<>("got file");
    }
}
