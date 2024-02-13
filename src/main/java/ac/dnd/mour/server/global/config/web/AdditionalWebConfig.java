package ac.dnd.mour.server.global.config.web;

import ac.dnd.mour.server.auth.domain.service.TokenProvider;
import ac.dnd.mour.server.global.annotation.AuthArgumentResolver;
import ac.dnd.mour.server.global.annotation.ExtractTokenArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AdditionalWebConfig implements WebMvcConfigurer {
    private final TokenProvider tokenProvider;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(tokenProvider));
        resolvers.add(new ExtractTokenArgumentResolver(tokenProvider));
    }
}
