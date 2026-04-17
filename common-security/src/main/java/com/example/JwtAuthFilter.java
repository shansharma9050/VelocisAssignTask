package com.example;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        String path = request.getServletPath();

        //  VERY IMPORTANT: Skip filter for public URLs
        if (path.equals("/login") ||
            path.equals("/auth/send-otp") ||
            path.equals("/auth/verify-otp") ||
            path.equals("/contra/register") ||
            path.endsWith(".css") ||
            path.endsWith(".js") ||
            path.startsWith("/images")||
            path.startsWith("/chat") ||
            path.contains("sockjs") ||
            path.startsWith("/message")) {

            chain.doFilter(request, response);
            return;
        }

        try {

            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {

                    if ("JWT_TOKEN".equals(cookie.getName())) {

                        String token = cookie.getValue();

                        Long userId = jwtUtil.extractUserId(token);
                        String userName = jwtUtil.extractUserName(token);
                        String role = jwtUtil.extractRole(token).toUpperCase();
                        String email = jwtUtil.extractEmail(token);

                        CustomUserPrincipal custUserPrincipal =
                                new CustomUserPrincipal(userName, userId, role,email);

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        custUserPrincipal,
                                        null,
                                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                                );

                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }

        } catch (ExpiredJwtException e) {

            System.out.println("JWT Token Expired");

            //  Delete expired cookie
            Cookie cookie = new Cookie("JWT_TOKEN", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            //  Redirect to login
            response.sendRedirect("/login");
            return;

        } catch (Exception e) {

            System.out.println("Invalid JWT: " + e.getMessage());

            // Optional: clear cookie
            Cookie cookie = new Cookie("JWT_TOKEN", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            response.sendRedirect("/login");
            return;
        }

        chain.doFilter(request, response);
    }
}