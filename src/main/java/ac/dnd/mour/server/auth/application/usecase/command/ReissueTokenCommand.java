package ac.dnd.mour.server.auth.application.usecase.command;

public record ReissueTokenCommand(
        String refreshToken
) {
}
