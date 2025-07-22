package com.example.finalproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.addAllowedOrigin("http://localhost:4200");
                    config.addAllowedHeader("*");
                    config.addAllowedMethod("*");
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 🟢 Không cần login hoặc role
                        .requestMatchers("/auth/login", "/auth/register").permitAll()

                        // 🟢 Cho phép gọi subscribe ngay cả khi chưa ACTIVE
                        .requestMatchers(HttpMethod.POST, "/api/agent-package/subscribe").permitAll()

                        // 🔒 Các route agent khác cần ACTIVE và role AGENT
                        .requestMatchers("/api/agent-package/**").hasRole("AGENT")

                        // 🟢 Cho phép các GET công khai
                        .requestMatchers(HttpMethod.GET, "/agents/**", "/services/**", "/shops/**",
                                "/products/**", "/hairstylists/**", "/products/by-agent/{id}/grouped").permitAll()

                        // 🔒 Các POST/PUT/DELETE vào hairstylists chỉ AGENT được phép
                        .requestMatchers(HttpMethod.POST, "/hairstylists/**").hasRole("AGENT")
                        .requestMatchers(HttpMethod.PUT, "/hairstylists/**").hasRole("AGENT")
                        .requestMatchers(HttpMethod.DELETE, "/hairstylists/**").hasRole("AGENT")

                        // 🔒 Các route cần quyền AGENT đã được kích hoạt
                        .requestMatchers("/agents/**", "/products/**", "/shops/**",
                                "/services/**", "/categories/**", "/bookings/my", "/bookings/booking-payments/**", "/orders/order-payments/**").hasRole("AGENT")

                        .requestMatchers(HttpMethod.POST, "/reviews").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/reviews/**").permitAll()


                        // 🟢 Các route công khai khác
                        .requestMatchers("/customers/**", "/orders/**", "/payments/**", "/images/**",
                                "/reviews/**", "/preferences/**", "/bookings/**", "/booking-details/**",
                                "/order-details/**", "/style-tags/**", "/styles/**").permitAll()

                        // 🔐 Tất cả còn lại phải đăng nhập
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
