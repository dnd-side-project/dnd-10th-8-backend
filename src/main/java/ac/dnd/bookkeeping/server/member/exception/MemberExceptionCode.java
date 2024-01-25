package ac.dnd.bookkeeping.server.member.exception;

import ac.dnd.bookkeeping.server.global.base.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum MemberExceptionCode implements BaseExceptionCode {
    MEMBER_NOT_FOUND(NOT_FOUND, "MEMBER_001", "사용자 정보가 존재하지 않습니다."),
    INVALID_EMAIL_PATTERN(BAD_REQUEST, "MEMBER_002", "이메일 형식에 맞지 않습니다."),
    INVALID_GENDER(BAD_REQUEST, "MEMBER_003", "잘못된 성별입니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
