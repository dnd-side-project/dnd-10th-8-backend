package ac.dnd.mour.server.heart.application.usecase.command;

import java.time.LocalDate;
import java.util.List;

public record UpdateHeartCommand(
        long memberId,
        long heartId,
        long money,
        LocalDate day,
        String event,
        String memo,
        List<String> tags
) {
}
