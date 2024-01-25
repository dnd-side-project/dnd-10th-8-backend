package ac.dnd.bookkeeping.server.common.fixture;

import ac.dnd.bookkeeping.server.member.domain.model.Email;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.model.SocialPlatform;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberFixture {
    MEMBER_1(SocialPlatform.of("ID-member1", Email.from("member1@kakao.com")), "사용자1"),
    MEMBER_2(SocialPlatform.of("ID-member2", Email.from("member2@kakao.com")), "사용자2"),
    MEMBER_3(SocialPlatform.of("ID-member3", Email.from("member3@kakao.com")), "사용자3"),
    MEMBER_4(SocialPlatform.of("ID-member4", Email.from("member4@kakao.com")), "사용자4"),
    MEMBER_5(SocialPlatform.of("ID-member5", Email.from("member5@kakao.com")), "사용자5"),
    MEMBER_6(SocialPlatform.of("ID-member6", Email.from("member6@kakao.com")), "사용자6"),
    MEMBER_7(SocialPlatform.of("ID-member7", Email.from("member7@kakao.com")), "사용자7"),
    MEMBER_8(SocialPlatform.of("ID-member8", Email.from("member8@kakao.com")), "사용자8"),
    MEMBER_9(SocialPlatform.of("ID-member9", Email.from("member9@kakao.com")), "사용자9"),
    MEMBER_10(SocialPlatform.of("ID-member10", Email.from("member10@kakao.com")), "사용자10"),
    ;

    private final SocialPlatform platform;
    private final String name;

    public Member toDomain() {
        return new Member(platform, name);
    }
}
