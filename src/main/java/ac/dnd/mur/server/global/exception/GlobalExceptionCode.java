package ac.dnd.mur.server.global.exception;

import ac.dnd.mur.server.global.base.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

@Getter
@RequiredArgsConstructor
public enum GlobalExceptionCode implements BaseExceptionCode {
    NOT_SUPPORTED_URI_ERROR(NOT_FOUND, "GLOBAL_001", "잘못된 요청입니다."),
    NOT_SUPPORTED_METHOD_ERROR(METHOD_NOT_ALLOWED, "GLOBAL_002", "잘못된 요청입니다."),
    VALIDATION_ERROR(BAD_REQUEST, "GLOBAL_003", "잘못된 요청입니다."),
    UNSUPPORTED_MEDIA_TYPE_ERROR(UNSUPPORTED_MEDIA_TYPE, "GLOBAL_004", "잘못된 요청입니다."),
    UNEXPECTED_SERVER_ERROR(INTERNAL_SERVER_ERROR, "GLOBAL_005", "내부 서버 오류입니다.\n고객센터에 문의해주세요."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
