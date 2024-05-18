package capstone.capstone2024.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.Scopes;
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

//        SecurityScheme oauth2Scheme = new SecurityScheme()
//                .type(SecurityScheme.Type.OAUTH2)
//                .flows(new OAuthFlows()
//                        .authorizationCode(new OAuthFlow()
//                                .authorizationUrl("https://accounts.google.com/o/oauth2/auth")
//                                .tokenUrl("https://oauth2.googleapis.com/token")
//                                .scopes(new Scopes()
//                                        .addString("profile", "Access your profile")
//                                        .addString("email", "Access your email")
//                                        .addString("https://www.googleapis.com/auth/youtube.readonly", "Access your YouTube subscriptions")
//                                )))
//                .name("X-Google-Token");

//        SecurityRequirement oauth2Requirement = new SecurityRequirement().addList("oauth2Scheme");

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