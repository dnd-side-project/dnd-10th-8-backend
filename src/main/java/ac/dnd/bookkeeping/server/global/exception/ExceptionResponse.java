package ac.dnd.bookkeeping.server.global.exception;

import ac.dnd.bookkeeping.server.global.base.BaseExceptionCode;

public record ExceptionResponse(
        String code,
        String message
) {
    public ExceptionResponse(final BaseExceptionCode code) {
        this(code.getCode(), code.getMessage());
    }

    public ExceptionResponse(final BaseExceptionCode code, final String message) {
        this(code.getCode(), message);
    }
}
