package ac.dnd.mour.server.statistics.application.usecase.query.response;

import java.util.List;
import java.util.Map;

public record PersonalHeartStatisticsResponse(
        List<Map<String, List<PersonalHeartSummary>>> give,
        List<Map<String, List<PersonalHeartSummary>>> take
) {
}
