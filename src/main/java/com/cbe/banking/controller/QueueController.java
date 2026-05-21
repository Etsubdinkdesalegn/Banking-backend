package com.cbe.banking.controller;

import com.cbe.banking.model.QueueToken;
import com.cbe.banking.model.User;
import com.cbe.banking.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/queue")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    @PostMapping("/book")
    public ResponseEntity<QueueToken> bookToken(
            @AuthenticationPrincipal User user,
            @RequestParam QueueToken.ServiceType type
    ) {
        return ResponseEntity.ok(queueService.generateToken(user, type));
    }

    @GetMapping("/status/{tokenId}")
    public ResponseEntity<Long> getStatus(@PathVariable Long tokenId) {
        return ResponseEntity.ok(queueService.getPeopleAhead(tokenId));
    }

    @GetMapping("/waiting")
    public ResponseEntity<List<QueueToken>> getWaiting() {
        return ResponseEntity.ok(queueService.getWaitingTokens());
    }

    @PostMapping("/next")
    public ResponseEntity<Void> callNext() {
        queueService.callNextToken();
        return ResponseEntity.ok().build();
    }
}
