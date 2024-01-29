package ac.dnd.mur.server.auth.exception;

import ac.dnd.mur.server.global.base.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode implements BaseExceptionCode {
    AUTH_REQUIRED(UNAUTHORIZED, "AUTH_001", "인증이 필요합니다."),
    INVALID_TOKEN(UNAUTHORIZED, "AUTH_002", "토큰이 유효하지 않습니다."),
    INVALID_PERMISSION(FORBIDDEN, "AUTH_003", "권한이 없습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
