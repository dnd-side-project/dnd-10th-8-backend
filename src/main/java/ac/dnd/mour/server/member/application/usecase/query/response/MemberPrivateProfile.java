package ac.dnd.mour.server.member.application.usecase.query.response;

import ac.dnd.mour.server.member.domain.model.Member;

import java.time.LocalDate;

public record MemberPrivateProfile(
        long id,
        String email,
        String profileImageUrl,
        String name,
        String nickname,
        String gender,
        LocalDate birth
) {
    public static MemberPrivateProfile of(final Member member) {
        return new MemberPrivateProfile(
                member.getId(),
                member.getPlatform().getEmail().getValue(),
                member.getProfileImageUrl(),
                member.getName(),
                member.getNickname().getValue(),
                member.getGender().getValue(),
                member.getBirth()
        );
    }
}
