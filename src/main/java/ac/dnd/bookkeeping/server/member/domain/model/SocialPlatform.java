package ac.dnd.bookkeeping.server.member.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static ac.dnd.bookkeeping.server.member.domain.model.SocialPlatform.Type.KAKAO;
import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class SocialPlatform {
    @Enumerated(STRING)
    @Column(name = "social_type", nullable = false, columnDefinition = "VARCHAR(30)")
    private Type type;

    @Column(name = "social_id", nullable = false, unique = true)
    private String socialId;

    @Embedded
    private Email email;

    private SocialPlatform(final Type type, final String socialId, final Email email) {
        this.type = type;
        this.socialId = socialId;
        this.email = email;
    }

    // 카카오 외에 확장될 플랫폼 존재하면 그때 수정
    public static SocialPlatform of(final String socialId, final Email email) {
        return new SocialPlatform(KAKAO, socialId, email);
    }

    public SocialPlatform syncEmail(final Email email) {
        return new SocialPlatform(type, socialId, email);
    }

    public enum Type {
        KAKAO
    }
}
