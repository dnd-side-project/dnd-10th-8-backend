package ac.dnd.bookkeeping.server.common.fixture;

import ac.dnd.bookkeeping.server.member.domain.model.Email;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.model.Nickname;
import ac.dnd.bookkeeping.server.member.domain.model.SocialPlatform;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public enum MemberFixture {
    MEMBER_1(
            SocialPlatform.of("ID-member1", Email.from("member1@kakao.com")), "https://member-1-url",
            Nickname.from("사용자1"), Member.Gender.MAIL, LocalDate.of(2000, 1, 1)
    ),
    MEMBER_2(
            SocialPlatform.of("ID-member2", Email.from("member2@kakao.com")), "https://member-2-url",
            Nickname.from("사용자2"), Member.Gender.FEMAIL, LocalDate.of(2000, 1, 2)
    ),
    MEMBER_3(
            SocialPlatform.of("ID-member3", Email.from("member3@kakao.com")), "https://member-3-url",
            Nickname.from("사용자3"), Member.Gender.MAIL, LocalDate.of(2000, 1, 3)
    ),
    MEMBER_4(
            SocialPlatform.of("ID-member4", Email.from("member4@kakao.com")), "https://member-4-url",
            Nickname.from("사용자4"), Member.Gender.FEMAIL, LocalDate.of(2000, 1, 4)
    ),
    MEMBER_5(
            SocialPlatform.of("ID-member5", Email.from("member5@kakao.com")), "https://member-5-url",
            Nickname.from("사용자5"), Member.Gender.MAIL, LocalDate.of(2000, 1, 5)
    ),
    MEMBER_6(
            SocialPlatform.of("ID-member6", Email.from("member6@kakao.com")), "https://member-6-url",
            Nickname.from("사용자6"), Member.Gender.FEMAIL, LocalDate.of(2000, 1, 6)
    ),
    MEMBER_7(
            SocialPlatform.of("ID-member7", Email.from("member7@kakao.com")), "https://member-7-url",
            Nickname.from("사용자7"), Member.Gender.MAIL, LocalDate.of(2000, 1, 7)
    ),
    MEMBER_8(
            SocialPlatform.of("ID-member8", Email.from("member8@kakao.com")), "https://member-8-url",
            Nickname.from("사용자8"), Member.Gender.FEMAIL, LocalDate.of(2000, 1, 8)
    ),
    MEMBER_9(
            SocialPlatform.of("ID-member9", Email.from("member9@kakao.com")), "https://member-9-url",
            Nickname.from("사용자9"), Member.Gender.MAIL, LocalDate.of(2000, 1, 9)
    ),
    MEMBER_10(
            SocialPlatform.of("ID-member10", Email.from("member10@kakao.com")), "https://member-10-url",
            Nickname.from("사용자10"), Member.Gender.FEMAIL, LocalDate.of(2000, 1, 10)
    ),
    ;

    private final SocialPlatform platform;
    private final String profileImageUrl;
    private final Nickname nickname;
    private final Member.Gender gender;
    private final LocalDate birth;

    public Member toDomain() {
        final Member member = Member.create(platform, profileImageUrl);
        member.complete(nickname, gender, birth);
        return member;
    }
}
