package ac.dnd.mour.server.member.exception;

import ac.dnd.mour.server.global.base.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum MemberExceptionCode implements BaseExceptionCode {
    MEMBER_NOT_FOUND(NOT_FOUND, "MEMBER_001", "사용자 정보가 존재하지 않습니다."),
    INVALID_EMAIL_PATTERN(BAD_REQUEST, "MEMBER_002", "이메일 형식에 맞지 않습니다."),
    INVALID_GENDER(BAD_REQUEST, "MEMBER_003", "잘못된 성별입니다."),
    INVALID_NICKNAME_PATTERN(BAD_REQUEST, "MEMBER_004", "닉네임 형식에 맞지 않습니다."),
    DUPLICATE_NICKNAME(CONFLICT, "MEMBER_005", "이미 사용중인 닉네임입니다."),
    MEMBER_GROUP_NAME_TOO_LONG(BAD_REQUEST, "MEMBER_OO6", "그룹 이름은 최대 8자까지 가능합니다."),
    MEMBER_GROUP_ALREADY_EXISTS(CONFLICT, "MEMBER_007", "동일한 그룹을 중복해서 추가할 수 없습니다."),
    MEMBER_GROUP_NOT_FOUND(NOT_FOUND, "MEMBER_008", "그룹 정보가 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
