/*
 * package com.example.email.impl;
 * 
 * import java.util.List;
 * 
 * import org.springframework.stereotype.Service;
 * 
 * import com.example.email.dto.ComposeMailDTO; import
 * com.example.email.model.EmailEntity; import
 * com.example.email.repository.EmailRepository; import
 * com.example.email.service.EmailService;
 * 
 * @Service public class EmailServiceImpl implements EmailService {
 * 
 * private final EmailRepository repository;
 * 
 * public EmailServiceImpl(EmailRepository repository) { this.repository =
 * repository; }
 * 
 * @Override public EmailEntity sendMail(ComposeMailDTO dto, String senderEmail)
 * {
 * 
 * EmailEntity email = new EmailEntity();
 * 
 * email.setSenderEmail(senderEmail);
 * email.setReceiverEmail(dto.getReceiverEmail());
 * 
 * email.setReceiverId(dto.getReceiverId());
 * 
 * email.setSubject(dto.getSubject());
 * 
 * email.setBody(dto.getBody());
 * 
 * return repository.save(email); }
 * 
 * @Override public List<EmailEntity> inbox(String receiverEmail) {
 * 
 * return repository.findByReceiverEmailAndIsDeletedFalse(receiverEmail); }
 * 
 * @Override public List<EmailEntity> sentMail(String senderEmail) {
 * 
 * return repository.findBySenderEmail(senderEmail); }
 * 
 * @Override public void deleteMail(Long id) {
 * 
 * EmailEntity email = repository.findById(id).orElseThrow();
 * 
 * email.setIsDeleted(true);
 * 
 * repository.save(email); } }
 */