package ac.dnd.mur.server.member.application.usecase.command;

import ac.dnd.mur.server.member.domain.model.Gender;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.model.Nickname;
import ac.dnd.mur.server.member.domain.model.SocialPlatform;

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
