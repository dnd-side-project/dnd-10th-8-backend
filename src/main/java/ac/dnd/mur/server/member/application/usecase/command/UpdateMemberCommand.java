package ac.dnd.mur.server.member.application.usecase.command;

import ac.dnd.mur.server.member.domain.model.Gender;
import ac.dnd.mur.server.member.domain.model.Nickname;

import java.time.LocalDate;

public record UpdateMemberCommand(
        long id,
        String profileImageUrl,
        Nickname nickname,
        Gender gender,
        LocalDate birth
) {
}
