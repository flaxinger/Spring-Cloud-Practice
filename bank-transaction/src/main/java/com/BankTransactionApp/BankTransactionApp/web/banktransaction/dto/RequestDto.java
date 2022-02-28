package com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RequestDto {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class RequestByUser{
        private String date;
        private String type;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class RequestByBank{
        private String bank;
        private String date;
        private String type;
    }

    public static LocalDate parseDate(String date){
        if(date == null) return null;
        else return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }
}
