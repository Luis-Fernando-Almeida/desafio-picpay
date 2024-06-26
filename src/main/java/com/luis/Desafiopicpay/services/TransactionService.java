package com.luis.Desafiopicpay.services;

import com.luis.Desafiopicpay.domain.transaction.Transaction;
import com.luis.Desafiopicpay.domain.user.User;
import com.luis.Desafiopicpay.dtos.TransactionDTO;
import com.luis.Desafiopicpay.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
        User sender = this.userService.findUserById(transactionDTO.senderId());
        User receiver = this.userService.findUserById(transactionDTO.receiverId());

        validateTransaction(sender, transactionDTO);

        Transaction transaction = createTransactionObject(transactionDTO, sender, receiver);

        updateBalances(sender, receiver, transactionDTO.amount());

        saveTransactionAndUser(transaction, sender, receiver);

        sendNotifications(sender, receiver);

        return transaction;
    }

    private void validateTransaction(User sender, TransactionDTO transactionDTO) throws Exception {
        userService.validateUser(sender, transactionDTO.amount());
        boolean isAuthorized = this.authorizeTransaction(sender, transactionDTO.amount());
        if (!isAuthorized) {
            throw new Exception("Transação não autorizada");
        }
    }

    private Transaction createTransactionObject(TransactionDTO transactionDTO, User sender, User receiver) {

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.amount());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());
        return transaction;
    }

    private void updateBalances(User sender, User receiver, BigDecimal amount) {
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));
    }

    private void saveTransactionAndUser(Transaction transaction, User sender, User receiver) {
        this.repository.save(transaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);
    }

    private void sendNotifications(User sender, User receiver) throws Exception {
        this.notificationService.sendNotification(sender, "Transação realizada com sucesso");
        this.notificationService.sendNotification(receiver, "Transação recebida");
    }

    public boolean authorizeTransaction(User sender, BigDecimal value) {
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);
        if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
            String message = (String) authorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        } else return false;
    }
}
