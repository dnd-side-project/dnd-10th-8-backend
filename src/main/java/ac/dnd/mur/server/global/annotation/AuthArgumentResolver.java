package ac.dnd.mur.server.global.annotation;

import ac.dnd.mur.server.auth.domain.model.Authenticated;
import ac.dnd.mur.server.auth.domain.service.TokenProvider;
import ac.dnd.mur.server.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static ac.dnd.mur.server.auth.exception.AuthExceptionCode.AUTH_REQUIRED;
import static ac.dnd.mur.server.auth.utils.TokenExtractor.extractAccessToken;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Authenticated resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        final String accessToken = getAccessToken(request);
        return new Authenticated(tokenProvider.getId(accessToken));
    }

    private String getAccessToken(final HttpServletRequest request) {
        final String accessToken = extractAccessToken(request)
                .orElseThrow(() -> new AuthException(AUTH_REQUIRED));
        tokenProvider.validateToken(accessToken);
        return accessToken;
    }
}
