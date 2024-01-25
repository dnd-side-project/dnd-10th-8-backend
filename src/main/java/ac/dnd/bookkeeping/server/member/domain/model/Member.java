package ac.dnd.bookkeeping.server.member.domain.model;

import ac.dnd.bookkeeping.server.global.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Member(final SocialPlatform platform, final String name) {
        this.platform = platform;
        this.name = name;
    }

    public void syncEmail(final Email email) {
        platform.syncEmail(email);
    }
}
