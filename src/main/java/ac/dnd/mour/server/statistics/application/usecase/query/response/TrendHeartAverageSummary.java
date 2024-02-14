package ac.dnd.mour.server.statistics.application.usecase.query.response;

import java.math.BigDecimal;

public record TrendHeartAverageSummary(
        String event,
        BigDecimal amount
) {
}
