package ac.dnd.bookkeeping.server.member.domain.model;

import ac.dnd.bookkeeping.server.global.base.BaseEntity;
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
    private Email email;

    // TODO 회원가입 + 로그인 플로우에서 추가적인 필드 정의

    public Member(final Email email) {
        this.email = email;
    }
}
