package ac.dnd.mur.server.file.exception;

import ac.dnd.mur.server.global.base.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum FileExceptionCode implements BaseExceptionCode {
    INVALID_FILE_EXTENSION(BAD_REQUEST, "FILE_001", "파일 확장자는 [JPG, JPEG, PNG]만 가능합니다"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
