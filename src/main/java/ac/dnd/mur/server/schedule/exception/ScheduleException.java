package ac.dnd.mur.server.schedule.exception;

import ac.dnd.mur.server.global.base.BaseException;
import ac.dnd.mur.server.global.base.BaseExceptionCode;

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
