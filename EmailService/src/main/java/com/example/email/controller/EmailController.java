package com.example.email.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.CustomUserPrincipal;
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
    public String getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		CustomUserPrincipal cust = (CustomUserPrincipal) auth.getPrincipal();
		String userName = cust.getEmail();
		return userName;
	}

    @GetMapping("/email")
    public String inbox(Model model, Principal principal) {
        return loadPage(model, principal, "inbox", emailService.inbox(getCurrentUser()), null);
    }

    @GetMapping("/sent")
    public String sent(Model model, Principal principal) {
        return loadPage(model, principal, "sent", emailService.sent(getCurrentUser()), null);
    }

    @GetMapping("/drafts")
    public String drafts(Model model, Principal principal) {
        return loadPage(model, principal, "drafts", emailService.drafts(getCurrentUser()), null);
    }

    @GetMapping("/starred")
    public String starred(Model model, Principal principal) {
        return loadPage(model, principal, "starred", emailService.starred(getCurrentUser()), null);
    }

    @GetMapping("/archived")
    public String archived(Model model, Principal principal) {
        return loadPage(model, principal, "archived", emailService.archived(getCurrentUser()), null);
    }

    @GetMapping("/trash")
    public String trash(Model model, Principal principal) {
        return loadPage(model, principal, "trash", emailService.trash(getCurrentUser()), null);
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model, Principal principal) {
        return loadPage(model, principal, "search", emailService.search(getCurrentUser(), keyword), null);
    }

    @GetMapping("/view/{id}")
    public String viewMail(@PathVariable Long id, Model model, Principal principal) {
        String user = getCurrentUser();
        EmailEntity selectedMail = emailService.openMail(id, user);
        return loadPage(model, principal, "inbox", emailService.inbox(user), selectedMail);
    }

    @PostMapping("/send")
    public String send(@ModelAttribute ComposeMailDTO dto, Principal principal) {
        emailService.sendMail(dto, getCurrentUser());
        return "redirect:"+apiUrl+"/dashboard/sent";
    }

    @PostMapping("/draft")
    public String draft(@ModelAttribute ComposeMailDTO dto, Principal principal) {
        emailService.saveDraft(dto,getCurrentUser());
        return "redirect:"+apiUrl+"/dashboard/drafts";
    }

    @PostMapping("/{id}/star")
    public String star(@PathVariable Long id, Principal principal) {
        emailService.toggleStar(id, getCurrentUser());
        return "redirect:"+apiUrl+"/dashboard/view/" + id;
    }

    @PostMapping("/{id}/archive")
    public String archive(@PathVariable Long id, Principal principal) {
        emailService.archive(id, getCurrentUser());
        return "redirect:"+apiUrl+"/dashboard/email";
    }

    @PostMapping("/{id}/trash")
    public String trash(@PathVariable Long id, Principal principal) {
        emailService.moveToTrash(id, getCurrentUser());
        return "redirect:"+apiUrl+"/dashboard/email";
    }

    private String loadPage(
            Model model,
            Principal principal,
            String activeFolder,
            List<EmailEntity> emails,
            EmailEntity selectedMail
    ) {
        model.addAttribute("username", getCurrentUser());
        model.addAttribute("activeFolder", activeFolder);
        model.addAttribute("emails", emails);
        model.addAttribute("selectedMail", selectedMail == null && !emails.isEmpty() ? emails.get(0) : selectedMail);

        return "email-dashboard";
    }
}