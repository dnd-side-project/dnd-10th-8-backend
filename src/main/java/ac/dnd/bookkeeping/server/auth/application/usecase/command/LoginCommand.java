package ac.dnd.bookkeeping.server.auth.application.usecase.command;

import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.model.SocialPlatform;

import java.time.LocalDate;

public record LoginCommand(
        SocialPlatform platform,
        String name,
        Member.Gender gender,
        LocalDate birth
) {
    public Member toDomain() {
        return new Member(platform, name, gender, birth);
    }
}
