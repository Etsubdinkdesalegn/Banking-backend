package com.cbe.banking.repository;

import com.cbe.banking.model.QueueToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QueueTokenRepository extends JpaRepository<QueueToken, Long> {
    List<QueueToken> findAllByStatusOrderByCreatedAtAsc(QueueToken.Status status);
    
    @Query("SELECT COUNT(q) FROM QueueToken q WHERE q.status = 'WAITING' AND q.createdAt < (SELECT qt.createdAt FROM QueueToken qt WHERE qt.id = :tokenId)")
    long countPeopleAhead(Long tokenId);

    Optional<QueueToken> findFirstByStatusOrderByCreatedAtAsc(QueueToken.Status status);
}
