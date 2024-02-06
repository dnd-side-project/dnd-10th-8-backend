package ac.dnd.mur.server.statistics.application.usecase.query.response;

import java.math.BigDecimal;
import java.util.Map;

public record TrendHeartAverageStatisticsResponse(
        Map<String, BigDecimal> result
) {
}
