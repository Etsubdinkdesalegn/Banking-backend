package com.cbe.banking.service;

import com.cbe.banking.model.QueueToken;
import com.cbe.banking.model.User;
import com.cbe.banking.repository.QueueTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueTokenRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    public QueueToken generateToken(User user, QueueToken.ServiceType type) {
        QueueToken token = QueueToken.builder()
                .tokenNumber("CBE-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase())
                .user(user)
                .serviceType(type)
                .status(QueueToken.Status.WAITING)
                .createdAt(LocalDateTime.now())
                .build();
        
        QueueToken saved = repository.save(token);
        notifyQueueUpdate();
        return saved;
    }

    public void callNextToken() {
        repository.findFirstByStatusOrderByCreatedAtAsc(QueueToken.Status.WAITING)
            .ifPresent(token -> {
                token.setStatus(QueueToken.Status.CALLING);
                token.setCalledAt(LocalDateTime.now());
                repository.save(token);
                notifyQueueUpdate();
                // In a real app, send SMS here
            });
    }

    public long getPeopleAhead(Long tokenId) {
        return repository.countPeopleAhead(tokenId);
    }

    public List<QueueToken> getWaitingTokens() {
        return repository.findAllByStatusOrderByCreatedAtAsc(QueueToken.Status.WAITING);
    }

    private void notifyQueueUpdate() {
        messagingTemplate.convertAndSend("/topic/queue", getWaitingTokens());
    }
}
