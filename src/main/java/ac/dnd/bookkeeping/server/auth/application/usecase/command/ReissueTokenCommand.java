package ac.dnd.bookkeeping.server.auth.application.usecase.command;

public record ReissueTokenCommand(
        String refreshToken
) {
}
