package ac.dnd.mur.server.group.exception;

import ac.dnd.mur.server.global.base.BaseException;
import ac.dnd.mur.server.global.base.BaseExceptionCode;

public class GroupException extends BaseException {
    private final GroupExceptionCode code;

    public GroupException(final GroupExceptionCode code) {
        super(code);
        this.code = code;
    }

    @Override
    public BaseExceptionCode getCode() {
        return code;
    }
}
