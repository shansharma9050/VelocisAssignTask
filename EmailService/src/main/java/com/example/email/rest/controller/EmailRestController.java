/*
 * package com.example.email.rest.controller;
 * 
 * import java.security.Principal; import java.util.List;
 * 
 * import org.springframework.web.bind.annotation.*;
 * 
 * import com.example.email.dto.ComposeMailDTO; import
 * com.example.email.model.EmailEntity; import
 * com.example.email.service.EmailService;
 * 
 * @RestController
 * 
 * @RequestMapping("/email") public class EmailRestController {
 * 
 * private final EmailService service;
 * 
 * public EmailRestController( EmailService service ) { this.service = service;
 * }
 * 
 * @PostMapping("/send") public EmailEntity sendMail(
 * 
 * @RequestBody ComposeMailDTO dto, Principal principal ) {
 * 
 * return service.sendMail( dto, principal.getName() ); }
 * 
 * @GetMapping("/inbox") public List<EmailEntity> inbox( Principal principal ) {
 * 
 * return service.inbox( principal.getName() ); }
 * 
 * @GetMapping("/sent") public List<EmailEntity> sent( Principal principal ) {
 * 
 * return service.sentMail( principal.getName() ); }
 * 
 * @DeleteMapping("/{id}") public String deleteMail(
 * 
 * @PathVariable Long id ) {
 * 
 * service.deleteMail(id);
 * 
 * return "Deleted"; }
 * 
 * }
 */