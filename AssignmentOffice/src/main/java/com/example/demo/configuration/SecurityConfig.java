/*
 * package com.example.demo.configuration;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * EnableWebSecurity; import
 * org.springframework.security.config.http.SessionCreationPolicy; import
 * org.springframework.security.web.SecurityFilterChain; import
 * org.springframework.security.web.authentication.
 * UsernamePasswordAuthenticationFilter;
 * 
 * import com.example.demo.filter.JwtAuthFilter;
 * 
 * @Configuration
 * 
 * @EnableWebSecurity public class SecurityConfig {
 * 
 * private final JwtAuthFilter jwtAuthFilter;
 * 
 * public SecurityConfig(JwtAuthFilter jwtAuthFilter) { this.jwtAuthFilter =
 * jwtAuthFilter; }
 * 
 * @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 * 
 * http .csrf(csrf -> csrf.disable()) .authorizeHttpRequests(auth -> auth
 * .requestMatchers("/login", "/css/**", "/images/**").permitAll()
 * .requestMatchers("/taskassignment/**").authenticated()
 * .anyRequest().authenticated() ) .sessionManagement(session ->
 * session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
 * .formLogin(form -> form.disable()) .httpBasic(basic -> basic.disable())
 * .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
 * 
 * return http.build(); } }
 */