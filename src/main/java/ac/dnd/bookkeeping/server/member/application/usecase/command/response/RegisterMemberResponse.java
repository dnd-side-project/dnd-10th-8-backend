package ac.dnd.bookkeeping.server.member.application.usecase.command.response;

public record RegisterMemberResponse(
        long id,
        String accessToken,
        String refreshToken
) {
}
