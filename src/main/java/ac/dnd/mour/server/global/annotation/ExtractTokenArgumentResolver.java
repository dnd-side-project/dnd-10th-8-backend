package ac.dnd.mour.server.global.annotation;

import ac.dnd.mour.server.auth.domain.model.TokenType;
import ac.dnd.mour.server.auth.domain.service.TokenProvider;
import ac.dnd.mour.server.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static ac.dnd.mour.server.auth.exception.AuthExceptionCode.INVALID_PERMISSION;
import static ac.dnd.mour.server.auth.utils.TokenExtractor.extractAccessToken;
import static ac.dnd.mour.server.auth.utils.TokenExtractor.extractRefreshToken;

@RequiredArgsConstructor
public class ExtractTokenArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ExtractToken.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        final ExtractToken extractToken = parameter.getParameterAnnotation(ExtractToken.class);

        final String token = getToken(request, extractToken.tokenType());
        tokenProvider.validateToken(token);
        return token;
    }

    private String getToken(final HttpServletRequest request, final TokenType type) {
        if (type == TokenType.ACCESS) {
            return extractAccessToken(request)
                    .orElseThrow(() -> new AuthException(INVALID_PERMISSION));
        }
        return extractRefreshToken(request)
                .orElseThrow(() -> new AuthException(INVALID_PERMISSION));
    }
}
