package com.BankTransactionApp.BankTransactionApp.global.common;

import lombok.Getter;

import java.util.List;

public class Response {

    private static final String OK_MESSAGE = "transaction was successful";

    @Getter
    public static class ItemList<T> {

        private final boolean success;
        private final String message;
        private final int length;
        private final List<T> data;

        public ItemList(List<T> data){
            success = true;
            message = OK_MESSAGE;
            length = data.size();
            this.data = data;
        }
    }


    @Getter
    public static class Item<T> {

        private final boolean success;
        private final String message;
        private final int length;
        private final T data;

        public Item(T data){
            success = true;
            message = OK_MESSAGE;
            length = 1;
            this.data = data;
        }
    }
}
