package ac.dnd.mur.server.global.base;

import org.springframework.http.HttpStatus;

public interface BaseExceptionCode {
    HttpStatus getStatus();

    String getCode();

    String getMessage();
}
