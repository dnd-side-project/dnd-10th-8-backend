package ac.dnd.bookkeeping.server.global.base;

import org.springframework.http.HttpStatus;

public interface BaseExceptionCode {
    HttpStatus getStatus();

    String getErrorCode();

    String getMessage();
}
