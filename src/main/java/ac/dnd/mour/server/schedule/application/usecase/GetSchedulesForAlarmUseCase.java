package ac.dnd.mour.server.schedule.application.usecase;

import ac.dnd.mour.server.global.annotation.MourReadOnlyTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.schedule.application.usecase.query.response.SchedulesForAlarmResponse;
import ac.dnd.mour.server.schedule.domain.repository.query.ScheduleQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetSchedulesForAlarmUseCase {
    private final ScheduleQueryRepository scheduleQueryRepository;

    @MourReadOnlyTransactional
    public List<SchedulesForAlarmResponse> invoke(final long memberId) {
        return scheduleQueryRepository.fetchSchedulesForAlarm(memberId)
                .stream()
                .map(SchedulesForAlarmResponse::from)
                .toList();
    }
}
