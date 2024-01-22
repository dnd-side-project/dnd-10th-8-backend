package ac.dnd.bookkeeping.server.global.base;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final BaseExceptionCode code;

    protected BaseException(final BaseExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
