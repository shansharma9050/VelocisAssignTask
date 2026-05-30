package com.example.email.service;



import org.springframework.stereotype.Service;

import com.example.email.dto.ComposeMailDTO;
import com.example.email.model.EmailEntity;
import com.example.email.repository.EmailRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;

    public void sendMail(ComposeMailDTO dto, String senderEmail) {
        EmailEntity email = EmailEntity.builder()
                .senderEmail(senderEmail)
                .receiverEmail(dto.getReceiverEmail())
                .subject(dto.getSubject())
                .body(dto.getBody())
                .createdAt(LocalDateTime.now())
                .readStatus(false)
                .starred(false)
                .archived(false)
                .draft(false)
                .senderDeleted(false)
                .receiverDeleted(false)
                .build();

        emailRepository.save(email);
    }

    public void saveDraft(ComposeMailDTO dto, String senderEmail) {
        EmailEntity email = EmailEntity.builder()
                .senderEmail(senderEmail)
                .receiverEmail(dto.getReceiverEmail())
                .subject(dto.getSubject())
                .body(dto.getBody())
                .createdAt(LocalDateTime.now())
                .draft(true)
                .build();

        emailRepository.save(email);
    }

    public List<EmailEntity> inbox(String userEmail) {
        return emailRepository
                .findByReceiverEmailAndReceiverDeletedFalseAndArchivedFalseAndDraftFalseOrderByCreatedAtDesc(userEmail);
    }

    public List<EmailEntity> sent(String userEmail) {
        return emailRepository
                .findBySenderEmailAndSenderDeletedFalseAndDraftFalseOrderByCreatedAtDesc(userEmail);
    }

    public List<EmailEntity> drafts(String userEmail) {
        return emailRepository.findBySenderEmailAndDraftTrueOrderByCreatedAtDesc(userEmail);
    }

    public List<EmailEntity> starred(String userEmail) {
        return emailRepository.findByReceiverEmailAndStarredTrueAndReceiverDeletedFalseOrderByCreatedAtDesc(userEmail);
    }

    public List<EmailEntity> archived(String userEmail) {
        return emailRepository.findByReceiverEmailAndArchivedTrueAndReceiverDeletedFalseOrderByCreatedAtDesc(userEmail);
    }

    public List<EmailEntity> trash(String userEmail) {
        return emailRepository.findByReceiverEmailAndReceiverDeletedTrueOrderByCreatedAtDesc(userEmail);
    }

    public List<EmailEntity> search(String userEmail, String keyword) {
        return emailRepository
                .findByReceiverEmailAndSubjectContainingIgnoreCaseOrReceiverEmailAndBodyContainingIgnoreCaseOrderByCreatedAtDesc(
                        userEmail,
                        keyword,
                        userEmail,
                        keyword
                );
    }

    public EmailEntity openMail(Long id, String userEmail) {
        EmailEntity email = emailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        boolean allowed = email.getSenderEmail().equals(userEmail)
                || email.getReceiverEmail().equals(userEmail);

        if (!allowed) {
            throw new RuntimeException("Access denied");
        }

        if (email.getReceiverEmail().equals(userEmail)) {
            email.setReadStatus(true);
            emailRepository.save(email);
        }

        return email;
    }

    public void toggleStar(Long id, String userEmail) {
        EmailEntity email = openMail(id, userEmail);
        email.setStarred(!email.isStarred());
        emailRepository.save(email);
    }

    public void archive(Long id, String userEmail) {
        EmailEntity email = openMail(id, userEmail);
        email.setArchived(true);
        emailRepository.save(email);
    }

    public void moveToTrash(Long id, String userEmail) {
        EmailEntity email = openMail(id, userEmail);

        if (email.getReceiverEmail().equals(userEmail)) {
            email.setReceiverDeleted(true);
        }

        if (email.getSenderEmail().equals(userEmail)) {
            email.setSenderDeleted(true);
        }

        emailRepository.save(email);
    }
}