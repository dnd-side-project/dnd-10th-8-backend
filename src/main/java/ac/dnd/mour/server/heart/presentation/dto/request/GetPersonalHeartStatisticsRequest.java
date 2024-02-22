package ac.dnd.mour.server.heart.presentation.dto.request;

import ac.dnd.mour.server.statistics.application.usecase.query.GetPersonalHeartStatistics;
import jakarta.validation.constraints.NotNull;

public record GetPersonalHeartStatisticsRequest(
        @NotNull(message = "Year 정보는 필수입니다.")
        Integer year,

        Integer month
) {
    public GetPersonalHeartStatistics toQuery(final long memberId) {
        return new GetPersonalHeartStatistics(
                memberId,
                year,
                (month == null) ? 0 : month
        );
    }
}
