package ac.dnd.bookkeeping.server.auth.application.usecase.command.response;

import ac.dnd.bookkeeping.server.auth.domain.model.AuthToken;
import ac.dnd.bookkeeping.server.member.domain.model.Member;

import java.time.LocalDate;

public record LoginResponse(
        boolean isNew,
        MemberInfo info,
        AuthToken token
) {
    public record MemberInfo(
            String name,
            String gender,
            LocalDate birth
    ) {
        public static MemberInfo of(final Member member) {
            return new MemberInfo(
                    member.getName(),
                    convertGender(member.getGender()),
                    member.getBirth()
            );
        }

        private static String convertGender(final Member.Gender gender) {
            if (gender == null) {
                return null;
            }
            return gender.getValue();
        }
    }

    public static LoginResponse of(
            final boolean isNew,
            final Member member,
            final AuthToken token
    ) {
        return new LoginResponse(
                isNew,
                MemberInfo.of(member),
                token
        );
    }
}
