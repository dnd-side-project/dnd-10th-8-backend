package ac.dnd.mur.server.heart.exception;

import ac.dnd.mur.server.global.base.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum HeartExceptionCode implements BaseExceptionCode {
    HEART_NOT_FOUND(NOT_FOUND, "HEART_001", "마음 정보가 존재하지 않습니다."),
    INVALID_SORT(BAD_REQUEST, "HEART_002", "제공하지 않는 정렬 타입입니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
