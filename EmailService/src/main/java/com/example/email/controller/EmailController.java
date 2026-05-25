package com.example.email.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.email.dto.ComposeMailDTO;
import com.example.email.model.EmailEntity;
import com.example.email.service.EmailService;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class EmailController {

	@Autowired
	private String apiUrl;
	
    private final EmailService emailService;

    @GetMapping("/email")
    public String inbox(Model model, Principal principal) {
        return loadPage(model, principal, "inbox", emailService.inbox(principal.getName()), null);
    }

    @GetMapping("/sent")
    public String sent(Model model, Principal principal) {
        return loadPage(model, principal, "sent", emailService.sent(principal.getName()), null);
    }

    @GetMapping("/drafts")
    public String drafts(Model model, Principal principal) {
        return loadPage(model, principal, "drafts", emailService.drafts(principal.getName()), null);
    }

    @GetMapping("/starred")
    public String starred(Model model, Principal principal) {
        return loadPage(model, principal, "starred", emailService.starred(principal.getName()), null);
    }

    @GetMapping("/archived")
    public String archived(Model model, Principal principal) {
        return loadPage(model, principal, "archived", emailService.archived(principal.getName()), null);
    }

    @GetMapping("/trash")
    public String trash(Model model, Principal principal) {
        return loadPage(model, principal, "trash", emailService.trash(principal.getName()), null);
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model, Principal principal) {
        return loadPage(model, principal, "search", emailService.search(principal.getName(), keyword), null);
    }

    @GetMapping("/view/{id}")
    public String viewMail(@PathVariable Long id, Model model, Principal principal) {
        String user = principal.getName();
        EmailEntity selectedMail = emailService.openMail(id, user);
        return loadPage(model, principal, "inbox", emailService.inbox(user), selectedMail);
    }

    @PostMapping("/send")
    public String send(@ModelAttribute ComposeMailDTO dto, Principal principal) {
        emailService.sendMail(dto, principal.getName());
        return "redirect:"+apiUrl+"/dashboard/sent";
    }

    @PostMapping("/draft")
    public String draft(@ModelAttribute ComposeMailDTO dto, Principal principal) {
        emailService.saveDraft(dto, principal.getName());
        return "redirect:"+apiUrl+"/dashboard/drafts";
    }

    @PostMapping("/{id}/star")
    public String star(@PathVariable Long id, Principal principal) {
        emailService.toggleStar(id, principal.getName());
        return "redirect:"+apiUrl+"/dashboard/view/" + id;
    }

    @PostMapping("/{id}/archive")
    public String archive(@PathVariable Long id, Principal principal) {
        emailService.archive(id, principal.getName());
        return "redirect:"+apiUrl+"/dashboard/email";
    }

    @PostMapping("/{id}/trash")
    public String trash(@PathVariable Long id, Principal principal) {
        emailService.moveToTrash(id, principal.getName());
        return "redirect:"+apiUrl+"/dashboard/email";
    }

    private String loadPage(
            Model model,
            Principal principal,
            String activeFolder,
            List<EmailEntity> emails,
            EmailEntity selectedMail
    ) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("activeFolder", activeFolder);
        model.addAttribute("emails", emails);
        model.addAttribute("selectedMail", selectedMail == null && !emails.isEmpty() ? emails.get(0) : selectedMail);

        return "email-dashboard";
    }
}