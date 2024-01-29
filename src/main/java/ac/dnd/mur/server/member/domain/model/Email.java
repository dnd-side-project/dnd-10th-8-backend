package ac.dnd.mur.server.member.domain.model;

import ac.dnd.mur.server.member.exception.MemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

import static ac.dnd.mur.server.member.exception.MemberExceptionCode.INVALID_EMAIL_PATTERN;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Email {
    private static final Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    @Column(name = "email", nullable = false, unique = true)
    private String value;

    private Email(final String value) {
        this.value = value;
    }

    public static Email from(final String value) {
        validateEmailPattern(value);
        return new Email(value);
    }

    private static void validateEmailPattern(final String value) {
        if (isNotValidPattern(value)) {
            throw new MemberException(INVALID_EMAIL_PATTERN);
        }
    }

    private static boolean isNotValidPattern(final String value) {
        return !pattern.matcher(value).matches();
    }
}
