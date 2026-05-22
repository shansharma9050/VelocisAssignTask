package com.example.email.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComposeMailDTO {
    private String receiverEmail;
    private String subject;
    private String body;
}