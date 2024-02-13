package ac.dnd.mour.server.schedule.application.usecase;

import ac.dnd.mour.server.global.annotation.MourReadOnlyTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.schedule.application.usecase.query.GetCalendarSchedule;
import ac.dnd.mour.server.schedule.application.usecase.query.response.CalendarScheduleResponse;
import ac.dnd.mour.server.schedule.domain.repository.query.ScheduleQueryRepository;
import ac.dnd.mour.server.schedule.domain.repository.query.spec.SearchCalendarScheduleCondition;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetCalendarScheduleUseCase {
    private final ScheduleQueryRepository scheduleQueryRepository;

    @MourReadOnlyTransactional
    public List<CalendarScheduleResponse> invoke(final GetCalendarSchedule query) {
        final SearchCalendarScheduleCondition condition = new SearchCalendarScheduleCondition(query.memberId(), query.year(), query.month());
        return scheduleQueryRepository.fetchCalendarSchedules(condition)
                .stream()
                .map(CalendarScheduleResponse::from)
                .toList();
    }
}
