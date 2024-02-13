package ac.dnd.mour.server.statistics.application.usecase.query;

import ac.dnd.mour.server.member.domain.model.Gender;

public record GetTrendHeartAverageStatistics(
        Gender gender,
        int range
) {
}
