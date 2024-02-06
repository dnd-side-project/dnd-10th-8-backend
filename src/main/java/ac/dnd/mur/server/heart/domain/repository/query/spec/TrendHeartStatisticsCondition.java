package ac.dnd.mur.server.heart.domain.repository.query.spec;

import ac.dnd.mur.server.member.domain.model.Gender;

public record TrendHeartStatisticsCondition(
        Gender gender,
        TrendRange range
) {
}
