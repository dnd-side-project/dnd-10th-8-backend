package ac.dnd.mur.server.statistics.application.usecase.query.response;

import java.util.Map;

public record TrendHeartAverageStatisticsResponse(
        Map<String, Double> result
) {
}
