package ac.dnd.bookkeeping.server.auth.application.usecase.command.response;

import ac.dnd.bookkeeping.server.auth.domain.model.AuthToken;
import ac.dnd.bookkeeping.server.member.domain.model.Member;

public record LoginResponse(
        boolean isNew,
        MemberInfo info,
        AuthToken token
) {
    public record MemberInfo(
            String name
    ) {
        public MemberInfo(final Member member) {
            this(member.getName());
        }
    }

    public static LoginResponse of(
            final boolean isNew,
            final Member member,
            final AuthToken token
    ) {
        return new LoginResponse(
                isNew,
                new MemberInfo(member),
                token
        );
    }
}
