package ac.dnd.bookkeeping.server.auth.application.usecase.command;

import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.model.SocialPlatform;

public record LoginCommand(
        SocialPlatform platform,
        String profileImageUrl
) {
    public Member toDomain() {
        return Member.create(platform, profileImageUrl);
    }
}
