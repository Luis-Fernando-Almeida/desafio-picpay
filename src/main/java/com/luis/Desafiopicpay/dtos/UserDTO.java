package com.luis.Desafiopicpay.dtos;

import com.luis.Desafiopicpay.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(
        Long id,
        String firstName,
        String lastName,
        String document,
        String email,
        BigDecimal balance,
        UserType userType
) {
}
