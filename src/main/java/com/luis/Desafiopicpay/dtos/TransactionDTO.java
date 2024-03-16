package com.luis.Desafiopicpay.dtos;

import java.math.BigDecimal;

public record TransactionDTO(
        Long senderId,
        Long receiverId,
        BigDecimal amount
) {
}
