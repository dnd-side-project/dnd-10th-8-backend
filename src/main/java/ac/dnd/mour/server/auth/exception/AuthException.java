package ac.dnd.mour.server.auth.exception;

import ac.dnd.mour.server.global.base.BaseException;
import ac.dnd.mour.server.global.base.BaseExceptionCode;

public class AuthException extends BaseException {
    private final AuthExceptionCode code;

    public AuthException(final AuthExceptionCode code) {
        super(code);
        this.code = code;
    }

    @Override
    public BaseExceptionCode getCode() {
        return code;
    }
}
