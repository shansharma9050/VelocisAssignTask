package com.example.email.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.email.model.EmailEntity;

import java.util.List;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {

    List<EmailEntity> findByReceiverEmailAndReceiverDeletedFalseAndArchivedFalseAndDraftFalseOrderByCreatedAtDesc(
            String receiverEmail
    );

    List<EmailEntity> findBySenderEmailAndSenderDeletedFalseAndDraftFalseOrderByCreatedAtDesc(
            String senderEmail
    );

    List<EmailEntity> findBySenderEmailAndDraftTrueOrderByCreatedAtDesc(
            String senderEmail
    );

    List<EmailEntity> findByReceiverEmailAndStarredTrueAndReceiverDeletedFalseOrderByCreatedAtDesc(
            String receiverEmail
    );

    List<EmailEntity> findByReceiverEmailAndArchivedTrueAndReceiverDeletedFalseOrderByCreatedAtDesc(
            String receiverEmail
    );

    List<EmailEntity> findByReceiverEmailAndReceiverDeletedTrueOrderByCreatedAtDesc(
            String receiverEmail
    );

    List<EmailEntity> findByReceiverEmailAndSubjectContainingIgnoreCaseOrReceiverEmailAndBodyContainingIgnoreCaseOrderByCreatedAtDesc(
            String receiverEmail1,
            String subject,
            String receiverEmail2,
            String body
    );
}