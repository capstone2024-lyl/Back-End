package capstone.capstone2024.global.config;


import capstone.capstone2024.domain.user.application.UserService;
import capstone.capstone2024.domain.user.domain.UserRole;
import capstone.capstone2024.global.auth.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Value("${spring.jwt.secret}")
    private String secretkey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .httpBasic((auth) -> auth.disable())
                .csrf((auth) -> auth.disable())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors((auth) -> auth.disable());

        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/signUp", "/swagger-ui/**","/v3/**", "/api/v1/user/**").permitAll()
                        .requestMatchers("/jwt-login/admin").hasAuthority(UserRole.ADMIN.name())
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtTokenFilter(userService, secretkey), UsernamePasswordAuthenticationFilter.class);


//
//        //경로별 인가 작업
//        httpSecurity.authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/login", "/", "/signUp", "/swagger-ui/**","/v3/**").permitAll()
//                        .requestMatchers("/admin").hasRole("ADMIN")
//                        .anyRequest().authenticated());
//


        return httpSecurity.build();
    }

}
