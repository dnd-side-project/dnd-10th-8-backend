package ac.dnd.bookkeeping.server.member.application.usecase.command;

import ac.dnd.bookkeeping.server.member.domain.model.Member;

import java.time.LocalDate;

public record CompleteInfoCommand(
        long memberId,
        String name,
        Member.Gender gender,
        LocalDate birth
) {
}
