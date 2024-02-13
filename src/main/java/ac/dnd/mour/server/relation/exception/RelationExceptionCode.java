package ac.dnd.mour.server.relation.exception;

import ac.dnd.mour.server.global.base.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum RelationExceptionCode implements BaseExceptionCode {
     RELATION_NOT_FOUND(NOT_FOUND, "RELATION_001", "관계 정보가 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
