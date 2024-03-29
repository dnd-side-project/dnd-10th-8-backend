package ac.dnd.mour.server.schedule.exception;

import ac.dnd.mour.server.global.base.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum ScheduleExceptionCode implements BaseExceptionCode {
    SCHEDULE_NOT_FOUND(NOT_FOUND, "SCHEDULE_001", "일정 정보가 존재하지 않습니다."),
    INVALID_REPEAT_TYPE(BAD_REQUEST, "SCHEDULE_002", "제공하는 않는 일정 반복 주기입니다."),
    INVALID_SCHEDULE_DAY(BAD_REQUEST, "SCHEDULE_003", "지나간 일정은 등록할 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
