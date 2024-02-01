package ac.dnd.mur.server.group.exception;

import ac.dnd.mur.server.global.base.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum GroupExceptionCode implements BaseExceptionCode {
    GROUP_NOT_FOUND(NOT_FOUND, "GROUP_001", "그룹 정보가 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
