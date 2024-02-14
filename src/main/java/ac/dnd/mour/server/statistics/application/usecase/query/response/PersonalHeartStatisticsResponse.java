package ac.dnd.mour.server.statistics.application.usecase.query.response;

import java.util.List;

public record PersonalHeartStatisticsResponse(
        List<PersonalHeartSummary> give,
        List<PersonalHeartSummary> take
) {
}
