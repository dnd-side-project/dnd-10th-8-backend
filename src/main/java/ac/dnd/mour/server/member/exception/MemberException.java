package ac.dnd.mour.server.member.exception;

import ac.dnd.mour.server.global.base.BaseException;
import ac.dnd.mour.server.global.base.BaseExceptionCode;

public class MemberException extends BaseException {
    private final MemberExceptionCode code;

    public MemberException(final MemberExceptionCode code) {
        super(code);
        this.code = code;
    }

    @Override
    public BaseExceptionCode getCode() {
        return code;
    }
}
