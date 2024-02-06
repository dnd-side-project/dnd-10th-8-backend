package ac.dnd.mur.server.heart.domain.repository.query.response;

import com.querydsl.core.annotations.QueryProjection;

public record TrendHeartStatistics(
        String event,
        double average
) {
    @QueryProjection
    public TrendHeartStatistics {
    }
}
