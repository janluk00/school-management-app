package com.janluk.schoolmanagementapp.security.config;

import com.janluk.schoolmanagementapp.security.service.CustomizedUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    private final CustomizedUserDetailsService userDetailsService;
    private final String API_PREFIX;

    public SecurityConfig(
            CustomizedUserDetailsService userDetailsService,
            @Value("${api.prefix}") String API_PREFIX
    ) {
        this.userDetailsService = userDetailsService;
        this.API_PREFIX = API_PREFIX;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(API_PREFIX + "/auth/**").permitAll()
                        .requestMatchers(API_PREFIX + "/account/**").permitAll()
                        .requestMatchers(API_PREFIX + "/admin/**").hasRole("ADMIN")
                        .requestMatchers(API_PREFIX + "/teachers/**").hasRole("TEACHER")
                        .requestMatchers(API_PREFIX + "/students/**").hasRole("STUDENT")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .userDetailsService(userDetailsService)
                .exceptionHandling((ex) -> ex
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager manager(CustomizedUserDetailsService customizedUserDetailsService) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customizedUserDetailsService);

        return new ProviderManager(authProvider);
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(""); // Roles in database are saved as: `ROLE_x` so there is no need to add `ROLE` prefix.
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
