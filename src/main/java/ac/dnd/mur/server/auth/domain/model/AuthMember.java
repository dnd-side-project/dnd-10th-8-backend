package ac.dnd.mur.server.auth.domain.model;

import ac.dnd.mur.server.member.domain.model.Member;

public record AuthMember(
        long id,
        String accessToken,
        String refreshToken
) {
    public static AuthMember of(final Member member, final AuthToken token) {
        return new AuthMember(member.getId(), token.accessToken(), token.refreshToken());
    }
}
