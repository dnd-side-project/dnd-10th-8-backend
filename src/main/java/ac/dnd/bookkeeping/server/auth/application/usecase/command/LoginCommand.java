package ac.dnd.bookkeeping.server.auth.application.usecase.command;

import ac.dnd.bookkeeping.server.member.domain.model.Email;

public record LoginCommand(
        String socialId,
        Email email
) {
}
