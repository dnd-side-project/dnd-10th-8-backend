package ac.dnd.mur.server.schedule.application.usecase;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.schedule.application.usecase.query.response.SchedulesForAlarmResponse;
import ac.dnd.mur.server.schedule.domain.repository.query.ScheduleQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetSchedulesForAlarmUseCase {
    private final ScheduleQueryRepository scheduleQueryRepository;

    @MurReadOnlyTransactional
    public List<SchedulesForAlarmResponse> invoke(final long memberId) {
        return scheduleQueryRepository.fetchSchedulesForAlarm(memberId)
                .stream()
                .map(SchedulesForAlarmResponse::from)
                .toList();
    }
}
