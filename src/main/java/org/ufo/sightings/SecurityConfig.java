package org.ufo.sightings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/ufo-sightings/**")
                        .hasRole("AGENT"))
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
        User.UserBuilder users = User.builder();
        UserDetails mulder = users
                .username("mulder")
                .password(passwordEncoder().encode("abc123"))
                .roles("AGENT")
                .build();
        UserDetails scully = users
                .username("scully")
                .password(passwordEncoder.encode("xyz456"))
                .roles("AGENT")
                .build();
        UserDetails norm = users
                .username("norm")
                .password(passwordEncoder.encode("789fed"))
                .roles("NON-BELIEVER")
                .build();
        return new InMemoryUserDetailsManager(mulder, scully, norm);
    }
}
