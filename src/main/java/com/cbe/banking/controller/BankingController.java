package com.cbe.banking.controller;

import com.cbe.banking.dto.TransactionRequest;
import com.cbe.banking.model.Account;
import com.cbe.banking.model.Transaction;
import com.cbe.banking.model.User;
import com.cbe.banking.service.BankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banking")
@RequiredArgsConstructor
public class BankingController {

    private final BankingService bankingService;

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getMyAccounts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(bankingService.getUserAccounts(user));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(bankingService.performTransfer(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(bankingService.performWithdrawal(request));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(bankingService.performDeposit(request));
    }

    @PostMapping("/cash-transfer")
    public ResponseEntity<Transaction> cashTransfer(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(bankingService.performCashTransfer(request));
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable String accountNumber) {
        return ResponseEntity.ok(bankingService.getAccountHistory(accountNumber));
    }
}
