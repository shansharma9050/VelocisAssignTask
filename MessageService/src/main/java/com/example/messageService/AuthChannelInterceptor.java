package com.example.messageService;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.util.JwtUtil;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {

	 @Autowired
	    JwtUtil jwtUtil;
	 
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) { 

        	String token = accessor.getFirstNativeHeader("Authorization");

        	if (token != null && token.startsWith("Bearer ")) {
        	    token = token.substring(7);  
        	}

        	String email = jwtUtil.extractEmail(token);

        	
        	System.out.println("Token===========: " + token);
        	System.out.println("Extracted Email=========: " + email);
        	
            Authentication auth =
                    new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());

            accessor.setUser(auth);   
        }

        return message;
    }
}
