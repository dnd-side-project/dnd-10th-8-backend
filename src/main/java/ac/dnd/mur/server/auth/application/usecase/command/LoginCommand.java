package ac.dnd.mur.server.auth.application.usecase.command;

import ac.dnd.mur.server.member.domain.model.Email;

public record LoginCommand(
        String socialId,
        Email email
) {
}
