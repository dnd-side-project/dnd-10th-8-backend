package ac.dnd.mur.server.heart.exception;

import ac.dnd.mur.server.global.base.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HeartExceptionCode implements BaseExceptionCode {
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
