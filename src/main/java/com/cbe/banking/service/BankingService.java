package com.cbe.banking.service;

import com.cbe.banking.dto.TransactionRequest;
import com.cbe.banking.model.Account;
import com.cbe.banking.model.Transaction;
import com.cbe.banking.model.User;
import com.cbe.banking.repository.AccountRepository;
import com.cbe.banking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankingService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public List<Account> getUserAccounts(User user) {
        return accountRepository.findAllByUser(user);
    }

    @Transactional
    public Transaction performTransfer(TransactionRequest request) {
        Account sender = accountRepository.findByAccountNumber(request.getSenderAccountNumber())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));
        
        Account receiver = accountRepository.findByAccountNumber(request.getReceiverAccountNumber())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = Transaction.builder()
                .senderAccount(sender)
                .receiverAccount(receiver)
                .amount(request.getAmount())
                .type(Transaction.Type.TRANSFER)
                .status(Transaction.Status.COMPLETED)
                .description(request.getDescription())
                .timestamp(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction performWithdrawal(TransactionRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getSenderAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .senderAccount(account)
                .amount(request.getAmount())
                .type(Transaction.Type.WITHDRAWAL)
                .status(Transaction.Status.COMPLETED)
                .description(request.getDescription())
                .timestamp(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction performDeposit(TransactionRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getReceiverAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .receiverAccount(account)
                .amount(request.getAmount())
                .type(Transaction.Type.DEPOSIT)
                .status(Transaction.Status.COMPLETED)
                .description(request.getDescription())
                .timestamp(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction performCashTransfer(TransactionRequest request) {
        // This is typically from an account to a pickup code, or cash to account
        // If senderAccountNumber is present, it's account to cash.
        // If not, it's cash to account (similar to deposit).
        
        if (request.getSenderAccountNumber() != null && !request.getSenderAccountNumber().isEmpty()) {
            Account account = accountRepository.findByAccountNumber(request.getSenderAccountNumber())
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            if (account.getBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("Insufficient balance");
            }

            account.setBalance(account.getBalance().subtract(request.getAmount()));
            accountRepository.save(account);

            Transaction transaction = Transaction.builder()
                    .senderAccount(account)
                    .amount(request.getAmount())
                    .type(Transaction.Type.CASH_TRANSFER)
                    .status(Transaction.Status.PENDING) 
                    .description("Cash Transfer for Pickup: " + request.getDescription())
                    .timestamp(LocalDateTime.now())
                    .build();

            return transactionRepository.save(transaction);
        } else {
            // Cash to account deposit
            return performDeposit(request);
        }
    }

    public List<Transaction> getAccountHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findAllBySenderAccountOrReceiverAccountOrderByTimestampDesc(account, account);
    }
}
