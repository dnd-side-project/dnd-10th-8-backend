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
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.Arrays;

import static ac.dnd.bookkeeping.server.member.domain.model.Member.Status.ACTIVE;
import static ac.dnd.bookkeeping.server.member.domain.model.Member.Status.INACTIVE;
import static ac.dnd.bookkeeping.server.member.exception.MemberExceptionCode.INVALID_GENDER;
import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "member")
@SQLRestriction("status = 'ACTIVE'")
public class Member extends BaseEntity<Member> {
    @Embedded
    private SocialPlatform platform;

    @Column(name = "profile_image_url", nullable = false)
    private String profileImageUrl;

    @Embedded
    private Nickname nickname;

    @Enumerated(STRING)
    @Column(name = "gender", columnDefinition = "VARCHAR(20)")
    private Gender gender;

    @Column(name = "birth")
    private LocalDate birth;

    @Enumerated(STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(20)")
    private Status status;

    private Member(
            final SocialPlatform platform,
            final String profileImageUrl,
            final Nickname nickname,
            final Gender gender,
            final LocalDate birth,
            final Status status
    ) {
        this.platform = platform;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
        this.status = status;
    }

    public static Member create(final SocialPlatform platform, final String profileImageUrl) {
        return new Member(platform, profileImageUrl, null, null, null, ACTIVE);
    }

    public void syncEmail(final Email email) {
        this.platform = platform.syncEmail(email);
    }

    public void complete(final Nickname nickname, final Gender gender, final LocalDate birth) {
        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
    }

    public void delete() {
        this.platform = null;
        this.profileImageUrl = null;
        this.nickname = null;
        this.gender = null;
        this.birth = null;
        this.status = INACTIVE;
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

    public enum Status {
        ACTIVE, INACTIVE, BAN
    }
}
