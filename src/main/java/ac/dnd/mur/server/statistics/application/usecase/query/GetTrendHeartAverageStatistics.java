package ac.dnd.mur.server.statistics.application.usecase.query;

import ac.dnd.mur.server.member.domain.model.Gender;

public record GetTrendHeartAverageStatistics(
        Gender gender,
        int range
) {
}
