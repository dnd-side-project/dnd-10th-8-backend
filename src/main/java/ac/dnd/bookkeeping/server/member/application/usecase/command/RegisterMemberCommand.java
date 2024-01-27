package ac.dnd.bookkeeping.server.member.application.usecase.command;

import ac.dnd.bookkeeping.server.member.domain.model.Gender;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.model.Nickname;
import ac.dnd.bookkeeping.server.member.domain.model.SocialPlatform;

import java.time.LocalDate;

public record RegisterMemberCommand(
        SocialPlatform platform,
        String profileImageUrl,
        String name,
        Nickname nickname,
        Gender gender,
        LocalDate birth
) {
    public Member toDomain() {
        return Member.create(platform, profileImageUrl, name, nickname, gender, birth);
    }
}
