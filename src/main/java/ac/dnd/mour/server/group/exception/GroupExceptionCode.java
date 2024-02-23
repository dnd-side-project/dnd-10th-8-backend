package ac.dnd.mour.server.group.exception;

import ac.dnd.mour.server.global.base.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum GroupExceptionCode implements BaseExceptionCode {
    GROUP_NOT_FOUND(NOT_FOUND, "GROUP_001", "그룹 정보가 존재하지 않습니다."),
    GROUP_ALREADY_EXISTS(CONFLICT, "GROUP_002", "이미 존재하는 그룹입니다."),
    CANNOT_DELETE_FROM_REGISTERED_RELATIONSHIP_EXISTS(CONFLICT, "GROUP_003", "해당 그룹으로 등록된 관계가 존재하기 때문에 삭제할 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
