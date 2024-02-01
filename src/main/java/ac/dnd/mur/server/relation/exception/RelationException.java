package ac.dnd.mur.server.relation.exception;

import ac.dnd.mur.server.global.base.BaseException;
import ac.dnd.mur.server.global.base.BaseExceptionCode;

public class RelationException extends BaseException {
    private final RelationExceptionCode code;

    public RelationException(final RelationExceptionCode code) {
        super(code);
        this.code = code;
    }

    @Override
    public BaseExceptionCode getCode() {
        return code;
    }
}
