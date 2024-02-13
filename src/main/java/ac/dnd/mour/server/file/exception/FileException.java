package ac.dnd.mour.server.file.exception;

import ac.dnd.mour.server.global.base.BaseException;
import ac.dnd.mour.server.global.base.BaseExceptionCode;

public class FileException extends BaseException {
    private final FileExceptionCode code;

    public FileException(final FileExceptionCode code) {
        super(code);
        this.code = code;
    }

    @Override
    public BaseExceptionCode getCode() {
        return code;
    }
}
