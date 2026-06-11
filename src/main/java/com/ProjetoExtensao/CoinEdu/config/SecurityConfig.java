package com.ProjetoExtensao.CoinEdu.config;

import com.ProjetoExtensao.CoinEdu.security.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {


    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/usuario/cadastrar",
                                "/usuario/confirmar-cadastro",
<<<<<<< HEAD
                                "/usuario/login",
                                "/usuario/editar-fotoPerfil",
                                "/usuario/editar-fotoPerfil/**",
                                "/coin",
                                "/coin/**",
                                "/educativo",
                                "/educativo/**",
                                "/alertas/stream",
                                "/carteira/**"

=======
                                "/usuario/login"
>>>>>>> 0492d1d7fcdab0cf6881f27414dd37333a5ad531
                        ).permitAll().anyRequest().authenticated()
                ).addFilterBefore(jwtFilter , UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

@Bean
    public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

}
