package com.BankTransactionLoadbalancer.Loadbalancer.global.common;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

public class Response {

    private static final String OK_MESSAGE = "transaction unsuccessful";
    public static final Response.Item FALLBACK = new Item("There is an error with the server");

    @Getter
    @ToString
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
    @ToString
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

