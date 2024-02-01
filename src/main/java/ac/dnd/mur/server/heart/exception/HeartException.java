package ac.dnd.mur.server.heart.exception;

import ac.dnd.mur.server.global.base.BaseException;
import ac.dnd.mur.server.global.base.BaseExceptionCode;

public class HeartException extends BaseException {
    private final HeartExceptionCode code;

    public HeartException(final HeartExceptionCode code) {
        super(code);
        this.code = code;
    }

    @Override
    public BaseExceptionCode getCode() {
        return code;
    }
}
