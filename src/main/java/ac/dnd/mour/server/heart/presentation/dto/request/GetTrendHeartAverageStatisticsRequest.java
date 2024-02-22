package ac.dnd.mour.server.heart.presentation.dto.request;

import ac.dnd.mour.server.member.domain.model.Gender;
import ac.dnd.mour.server.statistics.application.usecase.query.GetTrendHeartAverageStatistics;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GetTrendHeartAverageStatisticsRequest(
        @NotBlank(message = "성별 정보는 필수입니다.")
        String gender,

        @NotNull(message = "연령대 정보는 필수입니다.")
        Integer range
) {
    public GetTrendHeartAverageStatistics toQuery() {
        return new GetTrendHeartAverageStatistics(
                Gender.from(gender),
                range
        );
    }
}
