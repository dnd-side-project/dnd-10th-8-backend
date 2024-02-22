package ac.dnd.mour.server.schedule.presentation.dto.request;

import ac.dnd.mour.server.schedule.application.usecase.query.GetCalendarSchedule;
import jakarta.validation.constraints.NotNull;

public record GetCalendarSchedulesRequest(
        @NotNull(message = "Year 정보는 필수입니다.")
        Integer year,

        @NotNull(message = "Month 정보는 필수입니다.")
        Integer month
) {
    public GetCalendarSchedule toQuery(final long memberId) {
        return new GetCalendarSchedule(
                memberId,
                year,
                month
        );
    }
}
