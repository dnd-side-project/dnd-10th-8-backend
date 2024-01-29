package ac.dnd.mur.server.member.domain.model;

import ac.dnd.mur.server.member.exception.MemberException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static ac.dnd.mur.server.member.exception.MemberExceptionCode.INVALID_GENDER;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("male"),
    FEMALE("female"),
    ;

    private final String value;

    public static Gender from(final String value) {
        return Arrays.stream(values())
                .filter(it -> it.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new MemberException(INVALID_GENDER));
    }
}
