package ac.dnd.bookkeeping.server.common.fixture;

import ac.dnd.bookkeeping.server.member.domain.model.Email;
import ac.dnd.bookkeeping.server.member.domain.model.Gender;
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
            SocialPlatform.of("ID-MEMBER-1", Email.from("member1@kakao.com")), "https://member-1-url",
            Nickname.from("사용자1"), Gender.MALE, LocalDate.of(2000, 1, 1)
    ),
    MEMBER_2(
            SocialPlatform.of("ID-MEMBER-2", Email.from("member2@kakao.com")), "https://member-2-url",
            Nickname.from("사용자2"), Gender.FEMALE, LocalDate.of(2000, 1, 2)
    ),
    MEMBER_3(
            SocialPlatform.of("ID-MEMBER-3", Email.from("member3@kakao.com")), "https://member-3-url",
            Nickname.from("사용자3"), Gender.MALE, LocalDate.of(2000, 1, 3)
    ),
    MEMBER_4(
            SocialPlatform.of("ID-MEMBER-4", Email.from("member4@kakao.com")), "https://member-4-url",
            Nickname.from("사용자4"), Gender.FEMALE, LocalDate.of(2000, 1, 4)
    ),
    MEMBER_5(
            SocialPlatform.of("ID-MEMBER-5", Email.from("member5@kakao.com")), "https://member-5-url",
            Nickname.from("사용자5"), Gender.MALE, LocalDate.of(2000, 1, 5)
    ),
    MEMBER_6(
            SocialPlatform.of("ID-MEMBER-6", Email.from("member6@kakao.com")), "https://member-6-url",
            Nickname.from("사용자6"), Gender.FEMALE, LocalDate.of(2000, 1, 6)
    ),
    MEMBER_7(
            SocialPlatform.of("ID-MEMBER-7", Email.from("member7@kakao.com")), "https://member-7-url",
            Nickname.from("사용자7"), Gender.MALE, LocalDate.of(2000, 1, 7)
    ),
    MEMBER_8(
            SocialPlatform.of("ID-MEMBER-8", Email.from("member8@kakao.com")), "https://member-8-url",
            Nickname.from("사용자8"), Gender.FEMALE, LocalDate.of(2000, 1, 8)
    ),
    MEMBER_9(
            SocialPlatform.of("ID-MEMBER-9", Email.from("member9@kakao.com")), "https://member-9-url",
            Nickname.from("사용자9"), Gender.MALE, LocalDate.of(2000, 1, 9)
    ),
    MEMBER_10(
            SocialPlatform.of("ID-MEMBER-10", Email.from("member10@kakao.com")), "https://member-10-url",
            Nickname.from("사용자10"), Gender.FEMALE, LocalDate.of(2000, 1, 10)
    ),
    ;

    private final SocialPlatform platform;
    private final String profileImageUrl;
    private final Nickname nickname;
    private final Gender gender;
    private final LocalDate birth;

    public Member toDomain() {
        return Member.create(platform, profileImageUrl, nickname, gender, birth);
    }
}
