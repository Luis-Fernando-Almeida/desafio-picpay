package com.luis.Desafiopicpay.exceptions;

public class NotificationServiceDownException extends Exception{
    public NotificationServiceDownException(String message) {
        super(message);
    }
    public  NotificationServiceDownException(String message, Throwable cause) {
        super(message, cause);
    }
}
