package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.example.model.CustomUserDetails;
import com.example.util.TockenService;

import capchaFilter.CaptchaFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SpBootProApplicationConfiguration {
	

	@Value("${api.url}")
	String apiUrl;

	@Bean
	String apiUrl() {
		return apiUrl;
	}

	@Bean
	RestTemplate restTemplate() {

		return new RestTemplate();
	}
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	@Autowired
	private CaptchaFilter captchaFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    @Autowired 
	  TockenService tockenService;
    
		

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
        http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
        	    .requestMatchers("/registereduser").hasAuthority("ROLE_ADMIN") 
        	    .requestMatchers("/login","/act","/auth/**","/api/**",
        	                     "/contra/**","/forgot-password/**",
        	                     "/reset-password/**","/chat/**",
        	                     "/send-reset-password","/contra/getDevelopers",
        	                     "/registrationStyle.css",
        	                     "/after-varification-login",
        	                     "/loginStyle.css",
        	                     "/verifyEmail.css",
        	                     "/registeredUser.css",
        	                     "/images/**",
        	                     "/chat/**",
        	                     "/chat",
        	                     "/message/**",
        	                     "/message/chatting",
        	                     "/ws/**",
        	                     "/sockjs/**")
        	    .permitAll()
        	    .anyRequest().authenticated()
        	)
            .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
        	.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")   // IMPORTANT
                .successHandler((request, response, authentication) -> {

                    CustomUserDetails user =
                            (CustomUserDetails) authentication.getPrincipal();

                    String jwt = tockenService.generateToken(
                            user.getId(),
                            user.getUsername(),
                            user.getRole(),
                            user.getEmail()
                    );

                    Cookie cookie = new Cookie("JWT_TOKEN", jwt);
                    cookie.setHttpOnly(true);
                    cookie.setSecure(false); 
                    cookie.setPath("/");
                    cookie.setMaxAge(60 * 60 * 10);

                    response.addCookie(cookie);
                    
                    response.sendRedirect(apiUrl+"/dash/dashboard");
                })
                .failureHandler((request, response, exception) -> {

                    String username = request.getParameter("username");

                    // keep username on page
                    request.getSession().setAttribute("username", username);

                    // error message
                    request.getSession().setAttribute("error",
                            "Invalid Password");

                    response.sendRedirect("/after-varification-login");
                })
                .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .logoutSuccessHandler((request, response, auth) -> {

                        Cookie cookie = new Cookie("JWT_TOKEN", null);
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);

                        response.sendRedirect("/login");
                    })
            );

        return http.build();
    }
}
