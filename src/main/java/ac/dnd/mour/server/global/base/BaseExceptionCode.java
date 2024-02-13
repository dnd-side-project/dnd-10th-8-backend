package ac.dnd.mour.server.global.base;

import org.springframework.http.HttpStatus;

public interface BaseExceptionCode {
    HttpStatus getStatus();

    String getCode();

    String getMessage();
}
