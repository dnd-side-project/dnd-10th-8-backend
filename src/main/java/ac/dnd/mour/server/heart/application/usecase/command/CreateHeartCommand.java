package ac.dnd.mour.server.heart.application.usecase.command;

import java.time.LocalDate;
import java.util.List;

public record CreateHeartCommand(
        long memberId,
        long relationId,
        boolean give,
        long money,
        LocalDate day,
        String event,
        String memo,
        List<String> tags
) {
}
