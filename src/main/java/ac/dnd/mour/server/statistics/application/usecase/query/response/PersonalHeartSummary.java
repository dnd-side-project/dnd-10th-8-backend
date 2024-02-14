package ac.dnd.mour.server.statistics.application.usecase.query.response;

import java.time.LocalDate;

public record PersonalHeartSummary(
        String event,
        String relationName,
        String groupName,
        long money,
        LocalDate day,
        String memo
) {
}
