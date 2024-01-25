package ac.dnd.bookkeeping.server.member.application.usecase.command;

import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.model.Nickname;

import java.time.LocalDate;

public record CompleteInfoCommand(
        long memberId,
        Nickname nickname,
        Member.Gender gender,
        LocalDate birth
) {
}
