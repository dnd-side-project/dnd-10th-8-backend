package ac.dnd.bookkeeping.server.member.domain.model;

import ac.dnd.bookkeeping.server.member.exception.MemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

import static ac.dnd.bookkeeping.server.member.exception.MemberExceptionCode.INVALID_NICKNAME_PATTERN;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Nickname {
    /**
     * 1. 한글 & 알파벳 대소문자 & 숫자 가능
     * <br>
     * 2. 공백 불가능
     * <br>
     * 3. 1자 이상 15자 이하
     */
    private static final Pattern pattern = Pattern.compile("^[a-zA-Z가-힣0-9]{1,15}$");

    @Column(name = "nickname", unique = true)
    private String value;

    private Nickname(final String value) {
        this.value = value;
    }

    public static Nickname from(final String value) {
        validateNicknamePattern(value);
        return new Nickname(value);
    }

    public Nickname update(final String value) {
        validateNicknamePattern(value);
        return new Nickname(value);
    }

    private static void validateNicknamePattern(final String value) {
        if (isInvalidPattern(value)) {
            throw new MemberException(INVALID_NICKNAME_PATTERN);
        }
    }

    private static boolean isInvalidPattern(final String value) {
        return !pattern.matcher(value).matches();
    }
}
