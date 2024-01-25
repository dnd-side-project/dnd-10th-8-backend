package ac.dnd.bookkeeping.server.global.config.etc;

import ac.dnd.bookkeeping.server.global.annotation.Auth;
import ac.dnd.bookkeeping.server.global.annotation.ExtractToken;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
public class SwaggerConfig {
    static {
        SpringDocUtils
                .getConfig()
                .addAnnotationsToIgnore(
                        Auth.class,
                        ExtractToken.class
                );
    }

    @Value("${springdoc.server.url}")
    private String url;

    @Bean
    @Profile("!prod")
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(
                                "JWT Token",
                                new SecurityScheme()
                                        .name(AUTHORIZATION)
                                        .type(SecurityScheme.Type.HTTP)
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ))
                .security(List.of(
                        new SecurityRequirement().addList("JWT Token")
                ))
                .info(new Info()
                        .title("Bookkeeping API 명세서")
                        .description("Bookkeeping API 명세서")
                        .contact(new Contact()
                                .name("DND-10th-8")
                                .email("sjiwon4491@gmail.com")
                                .url("https://github.com/dnd-side-project/dnd-10th-8-backend"))
                        .version("v1"))
                .servers(List.of(
                        new Server().url(url)
                ));
    }
}
