package ac.dnd.mur.server.statistics.application.usecase.query.response;

import java.time.LocalDate;

public record PersonalHeartSummary(
        String relationName,
        String groupName,
        long money,
        LocalDate day,
        String memo
) {
}
