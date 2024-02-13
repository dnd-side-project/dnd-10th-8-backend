package ac.dnd.mour.server.schedule.exception;

import ac.dnd.mour.server.global.base.BaseException;
import ac.dnd.mour.server.global.base.BaseExceptionCode;

public class ScheduleException extends BaseException {
    private final ScheduleExceptionCode code;

    public ScheduleException(final ScheduleExceptionCode code) {
        super(code);
        this.code = code;
    }

    @Override
    public BaseExceptionCode getCode() {
        return code;
    }
}
