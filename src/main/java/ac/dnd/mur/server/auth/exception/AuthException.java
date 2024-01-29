package ac.dnd.mur.server.auth.exception;

import ac.dnd.mur.server.global.base.BaseException;
import ac.dnd.mur.server.global.base.BaseExceptionCode;

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
