package ac.dnd.bookkeeping.server.common.fixture;

import ac.dnd.bookkeeping.server.member.domain.model.Email;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberFixture {
    MEMBER_1(Email.from("member1@kakao.com")),
    MEMBER_2(Email.from("member2@kakao.com")),
    MEMBER_3(Email.from("member3@kakao.com")),
    MEMBER_4(Email.from("member4@kakao.com")),
    MEMBER_5(Email.from("member5@kakao.com")),
    MEMBER_6(Email.from("member6@kakao.com")),
    MEMBER_7(Email.from("member7@kakao.com")),
    MEMBER_8(Email.from("member8@kakao.com")),
    MEMBER_9(Email.from("member9@kakao.com")),
    MEMBER_10(Email.from("member10@kakao.com")),
    ;

    private final Email email;

    public Member toDomain() {
        return new Member(email);
    }
}
