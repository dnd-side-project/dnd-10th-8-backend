package ac.dnd.mour.server.heart.domain.repository.query.spec;

import ac.dnd.mour.server.member.domain.model.Gender;

public record TrendHeartStatisticsCondition(
        Gender gender,
        TrendRange range
) {
}
