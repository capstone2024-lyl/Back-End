package capstone.capstone2024.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme bearerAuthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement bearerAuthRequirement = new SecurityRequirement().addList("bearerAuth");


        SecurityScheme googleAuthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("X-Google-Token");

        SecurityRequirement googleAuthRequirement = new SecurityRequirement().addList("X-Google-Token");


        return new OpenAPI()
                .info(new Info()
                        .title("2024-1 capstone API")
                        .description("LYL 팀 2024-1 capstone API 문서")
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", bearerAuthScheme)
                        .addSecuritySchemes("oauth2Scheme", googleAuthScheme))
                .security(Arrays.asList(bearerAuthRequirement, googleAuthRequirement));
    }
}