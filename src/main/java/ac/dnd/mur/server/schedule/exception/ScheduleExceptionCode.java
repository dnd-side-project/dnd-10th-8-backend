package ac.dnd.mur.server.schedule.exception;

import ac.dnd.mur.server.global.base.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum ScheduleExceptionCode implements BaseExceptionCode {
    SCHEDULE_NOT_FOUND(NOT_FOUND, "SCHEDULE_001", "일정 정보가 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
