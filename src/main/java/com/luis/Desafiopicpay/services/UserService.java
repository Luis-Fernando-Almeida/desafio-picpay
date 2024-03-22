package com.luis.Desafiopicpay.services;

import com.luis.Desafiopicpay.domain.user.User;
import com.luis.Desafiopicpay.domain.user.UserType;
import com.luis.Desafiopicpay.exceptions.InsufficientBalanceException;
import com.luis.Desafiopicpay.exceptions.MerchantCannotMakeTransactionsException;
import com.luis.Desafiopicpay.exceptions.UserNotFoundException;
import com.luis.Desafiopicpay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findUserById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }

    public User saveUser(User user) {
        return repository.save(user);
    }

    public boolean validateUser(User user, BigDecimal amount) {
        if (user.getType() == UserType.MERCHANT ) {
            throw new MerchantCannotMakeTransactionsException("Lojista não podem fazer transferências, somente receber");
        }
        if (user.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente");
        }
        return true;
    }
}

