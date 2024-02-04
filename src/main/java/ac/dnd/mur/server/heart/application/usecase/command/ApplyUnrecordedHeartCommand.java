package ac.dnd.mur.server.heart.application.usecase.command;

import java.util.List;

public record ApplyUnrecordedHeartCommand(
        long memberId,
        long scheduleId,
        long money,
        List<String> tags
) {
}
