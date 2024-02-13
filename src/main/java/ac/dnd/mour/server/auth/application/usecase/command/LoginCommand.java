package ac.dnd.mour.server.auth.application.usecase.command;

import ac.dnd.mour.server.member.domain.model.Email;

public record LoginCommand(
        String socialId,
        Email email
) {
}
