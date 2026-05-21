package com.cbe.banking.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "queue_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tokenNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime calledAt;
    private LocalDateTime completedAt;

    public enum ServiceType {
        CASH_DEPOSIT, CASH_WITHDRAWAL, ACCOUNT_OPENING, LOAN_ENQUIRY, OTHER
    }

    public enum Status {
        WAITING, CALLING, SERVING, COMPLETED, CANCELLED, MISSED
    }
}
