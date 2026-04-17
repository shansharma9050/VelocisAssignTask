package com.example.messageService.model;

import java.io.Serializable;

public class ChatMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String senderEmail;
	private String receiverEmail;
	private String message;

	public ChatMessage() {
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}