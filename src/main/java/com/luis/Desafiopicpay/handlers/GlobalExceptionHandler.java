package com.luis.Desafiopicpay.handlers;

import com.luis.Desafiopicpay.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ExceptionResponse> handlerInsufficientBalanceException(
            InsufficientBalanceException exception, HttpServletRequest request) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .title("Saldo insuficiente")
                        .details(exception.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timeStamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MerchantCannotMakeTransactionsException.class)
    public ResponseEntity<ExceptionResponse> handlerMerchantCannotMakeTransactionsException(
            MerchantCannotMakeTransactionsException exception, HttpServletRequest request) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .title("Lojistas não pode fazer transferências, somente receber")
                        .details(exception.getMessage())
                        .status(HttpStatus.FORBIDDEN.value())
                        .timeStamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build(), HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(NotificationServiceDownException.class)
    public ResponseEntity<ExceptionResponse> handlerNotificationServiceDownException(
            NotificationServiceDownException exception, HttpServletRequest request) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .title("Serviço de notificação está fora do ar")
                        .details(exception.getMessage())
                        .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                        .timeStamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build(), HttpStatus.SERVICE_UNAVAILABLE

        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handlerUserNotFoundException(
            UserNotFoundException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .title("Usuário não encontrado")
                        .status(HttpStatus.NOT_FOUND.value())
                        .details(ex.getMessage())
                        .timeStamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build(), HttpStatus.NOT_FOUND
        );
    }

}
