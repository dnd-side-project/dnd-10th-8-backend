package ac.dnd.bookkeeping.server.member.domain.model;

import ac.dnd.bookkeeping.server.global.base.BaseEntity;
import ac.dnd.bookkeeping.server.member.exception.MemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;

import static ac.dnd.bookkeeping.server.member.exception.MemberExceptionCode.INVALID_GENDER;
import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "member")
public class Member extends BaseEntity<Member> {
    @Embedded
    private SocialPlatform platform;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(STRING)
    @Column(name = "gender", columnDefinition = "VARCHAR(20)")
    private Gender gender;

    @Column(name = "birth")
    private LocalDate birth;

    public Member(
            final SocialPlatform platform,
            final String name,
            final Gender gender,
            final LocalDate birth
    ) {
        this.platform = platform;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
    }

    public void syncEmail(final Email email) {
        this.platform = platform.syncEmail(email);
    }

    public void complete(final String name, final Gender gender, final LocalDate birth) {
        this.name = name;
        this.gender = gender;
        this.birth = birth;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Gender {
        MAIL("male"),
        FEMAIL("female"),
        ;

        private final String value;

        public static Gender from(final String value) {
            return Arrays.stream(values())
                    .filter(it -> it.value.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new MemberException(INVALID_GENDER));
        }
    }
}
