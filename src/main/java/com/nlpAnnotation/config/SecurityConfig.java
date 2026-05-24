package com.nlpAnnotation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.nlpAnnotation.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        System.out.println(">>> DaoAuthenticationProvider créé avec: " 
            + customUserDetailsService.getClass().getName());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	.authenticationProvider(authenticationProvider())
        	.authorizeHttpRequests(auth -> auth
		    .requestMatchers(
		        new AntPathRequestMatcher("/login"),
		        new AntPathRequestMatcher("/css/**"),
		        new AntPathRequestMatcher("/js/**")
		    ).permitAll()
    		    .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAuthority("ADMIN_ROLE")
    		    .requestMatchers(new AntPathRequestMatcher("/annotateur/**")).hasAuthority("ANNOTATEUR_ROLE")
    		    .anyRequest().authenticated()
    		)
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
    
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst()
                    .orElse("");
            if (role.equals("ADMIN_ROLE")) {
                response.sendRedirect(request.getContextPath()+"/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath()+"/annotateur/taches");
            }
        };
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
