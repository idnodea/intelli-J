package com.shop.exception;

//page298주문기능에서 실제 재고가 주문수량보다 적을 때 발생시키는 exception
public class OutOfStockException extends RuntimeException{

    public OutOfStockException(String message) {
        super(message);
    }

}