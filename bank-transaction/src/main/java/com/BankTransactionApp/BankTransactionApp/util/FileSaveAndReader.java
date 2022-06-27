package com.BankTransactionApp.BankTransactionApp.util;

import com.BankTransactionApp.BankTransactionApp.web.Account.dto.AccountDto;
import com.BankTransactionApp.BankTransactionApp.web.bank.dto.BankDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.BankTransactionDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.util.TransactionType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@AllArgsConstructor
public class FileSaveAndReader {

    private MultipartFile file;
    private String path;
    private BufferedReader br;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

    public static FileSaveAndReader setupReader(MultipartFile file, String path) throws IOException {
        log.info("FileSaveAndReader saved file at {}", path);
        FileSaveAndReader fsr = new FileSaveAndReader(file, path, null);
        file.transferTo(new File(path));
        fsr.br = new BufferedReader(new FileReader(path));
        return fsr;
    }

    public BankTransactionDto readNextBankTransactionDto() throws IOException {
        BankTransactionDto result = null;
        if(br == null){
            return null;
        }

        String csvLine;
        try{
            if((csvLine = br.readLine()) == null){
                br.close();
                return null;
            }
            String[] csvRecord = csvLine.split(",");
            if(csvRecord.length != 8){

                return null;
            }
            result = BankTransactionDto.builder()
                    .id(Long.parseLong(csvRecord[0], 10))
                    .date(LocalDate.parse(csvRecord[1]
                            + '-' + csvRecord[2] +
                            '-' + csvRecord[3], formatter))
                    .account(AccountDto.builder()
                            .id(Long.valueOf(csvRecord[4]))
                            .name(csvRecord[4]).build())
                    .bank(BankDto.builder().bankCode(csvRecord[5]).build())
                    .transactionAmount(Long.valueOf(csvRecord[6]))
                    .transactionType(TransactionType.of(csvRecord[7]))
                    .build();
        }catch(Exception e){
            log.info("Exception occurred in FileSaveAndReader: {}", e.getMessage());
        }
        return result;
    }

    public void cleanup(){
        File delFile = new File(path);
        delFile.delete();
    }
}

