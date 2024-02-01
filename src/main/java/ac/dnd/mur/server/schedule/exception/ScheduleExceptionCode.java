package ac.dnd.mur.server.schedule.exception;

import ac.dnd.mur.server.global.base.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleExceptionCode implements BaseExceptionCode {
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
