package ac.dnd.mour.server.global.exception;

import ac.dnd.mour.server.global.base.BaseException;
import ac.dnd.mour.server.global.base.BaseExceptionCode;

public class GlobalException extends BaseException {
    private final GlobalExceptionCode code;

    public GlobalException(final GlobalExceptionCode code) {
        super(code);
        this.code = code;
    }

    @Override
    public BaseExceptionCode getCode() {
        return code;
    }
}
