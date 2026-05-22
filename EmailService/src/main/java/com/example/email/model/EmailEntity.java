package com.example.email.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderEmail;
    private String receiverEmail;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    private boolean readStatus;
    private boolean starred;
    private boolean archived;
    private boolean draft;

    private boolean senderDeleted;
    private boolean receiverDeleted;

    private LocalDateTime createdAt;
}