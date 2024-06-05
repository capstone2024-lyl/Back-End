package capstone.capstone2024.global.config;


import capstone.capstone2024.domain.user.application.UserService;
import capstone.capstone2024.global.auth.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Value("${spring.jwt.secret}")
    private String secretkey;

    @Value("${spring.google.token-info-url}")
    private String googleTokenInfoUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .httpBasic((auth) -> auth.disable())
                .csrf((auth) -> auth.disable())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors((auth) -> auth.disable());

        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login/**","/sign-up", "/swagger-ui/**","/v3/**", "/api/v1/user/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtTokenFilter(secretkey, googleTokenInfoUrl), UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }
}
