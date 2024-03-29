package ac.dnd.mour.server.global.exception;

import ac.dnd.mour.server.global.base.BaseExceptionCode;

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
