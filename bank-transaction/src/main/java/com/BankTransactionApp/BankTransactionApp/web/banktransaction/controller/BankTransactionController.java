package com.BankTransactionApp.BankTransactionApp.web.banktransaction.controller;

import com.BankTransactionApp.BankTransactionApp.global.common.Response;
import com.BankTransactionApp.BankTransactionApp.web.Account.dto.AccountDto;
import com.BankTransactionApp.BankTransactionApp.web.bank.dto.BankDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.BankTransactionDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.RequestDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.ResponseDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.service.BankTransactionService;
import com.BankTransactionApp.BankTransactionApp.util.FileSaveAndReader;
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

    @PostMapping("/upload")
    public Response.Item<String> uploadCSV(@RequestParam("file") MultipartFile file) throws IOException {

        FileSaveAndReader fsr = FileSaveAndReader.setupReader(file, request.getServletContext().getRealPath("/"));
        Set<AccountDto> accountDtos = new HashSet<>();
        Set<BankTransactionDto> bankTransactionDtos = new HashSet<>();

        log.info("Got request at uploadCSV");

        if(file.isEmpty()){
            return new Response.Item<>("The given file is empty");
        }
        else{
            BankTransactionDto bankTransactionDto;
            while((bankTransactionDto = fsr.readNextBankTransactionDto())!=null){
                accountDtos.add(bankTransactionDto.getAccount());
                bankTransactionDtos.add(bankTransactionDto);
                if(bankTransactionDtos.size() == batchSize*300){
                    long start = System.currentTimeMillis();
                    bankTransactionService.saveCSVBatch(accountDtos, bankTransactionDtos);
                    log.info("transaction took {}ms", System.currentTimeMillis()-start);
                    accountDtos.clear();
                    bankTransactionDtos.clear();
                }
            }
            fsr.cleanup();
            bankTransactionService.saveCSVBatch(accountDtos, bankTransactionDtos);
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

        FileSaveAndReader fsr = FileSaveAndReader.setupReader(file, request.getServletContext().getRealPath("/"));
        Set<AccountDto> accountDtos = new HashSet<>();
        log.info("Got request at uploadCSV");

        if(file.isEmpty()){
            return new Response.Item<>("The given file is empty");
        }
        else{
            if(! new File(uploadDir).exists()){
                new File(uploadDir).mkdir();
                log.info("{} is created", uploadDir);
            }
            File uploadFile = new File(uploadDir+File.separator+"bt"+LocalDateTime.now().hashCode()+".txt");
            log.info("created new file at {}", uploadFile.getAbsolutePath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(uploadFile.getAbsoluteFile()));
            BankTransactionDto bankTransactionDto;
            int bankTransactionRowCount=0;
            try(writer){
                while ((bankTransactionDto = fsr.readNextBankTransactionDto()) != null) {
                    bankTransactionRowCount++;
                    accountDtos.add(bankTransactionDto.getAccount());
                    writer.write(bankTransactionDto.toString());
                }
            }
            catch (IOException e){
                log.error("Error occured in uploadCSV:", e.getMessage());
            }
            long start = System.currentTimeMillis();
            try {
                bankTransactionService.loadData(accountDtos, bankTransactionRowCount, uploadFile.getAbsolutePath());
            }catch (Exception e){
                return new Response.Item<>(e.getMessage());
            }
            log.info("transaction took {}ms", System.currentTimeMillis()-start);
            fsr.cleanup();
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
