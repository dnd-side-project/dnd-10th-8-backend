package ac.dnd.mur.server.auth.application.usecase.command;

public record ReissueTokenCommand(
        String refreshToken
) {
}
