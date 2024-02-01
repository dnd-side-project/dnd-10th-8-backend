package ac.dnd.mur.server.member.domain.model;

import ac.dnd.mur.server.global.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

import static ac.dnd.mur.server.member.domain.model.Member.Status.ACTIVE;
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

    @Column(name = "name", nullable = false)
    private String name;

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
            final String name,
            final Nickname nickname,
            final Gender gender,
            final LocalDate birth,
            final Status status
    ) {
        this.platform = platform;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
        this.status = status;
    }

    public static Member create(
            final SocialPlatform platform,
            final String profileImageUrl,
            final String name,
            final Nickname nickname,
            final Gender gender,
            final LocalDate birth
    ) {
        return new Member(platform, profileImageUrl, name, nickname, gender, birth, ACTIVE);
    }

    public void syncEmail(final Email email) {
        this.platform = platform.syncEmail(email);
    }

    public enum Status {
        ACTIVE, INACTIVE, BAN
    }
}
