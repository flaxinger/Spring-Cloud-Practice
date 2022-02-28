package com.BankTransactionLoadbalancer.Loadbalancer.controller.BankTransaction.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestByUser{
        private String date;
        private String type;

        public String toString(){
            String date = (this.date == null)? "" : "date="+this.date;
            String type = (this.type == null)? "" : "type="+this.type;
            return "?"+date+type;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestByBank{
        private String bank;
        private String date;
        private String type;

        public String toString(){
            String bank = (this.bank == null)? "" : "date="+this.bank;
            String date = (this.date == null)? "" : "date="+this.date;
            String type = (this.type == null)? "" : "type="+this.type;
            return "?"+bank+date+type;
        }
    }

    public static LocalDate parseDate(String date){
        if(date == null) return null;
        else return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }
}

