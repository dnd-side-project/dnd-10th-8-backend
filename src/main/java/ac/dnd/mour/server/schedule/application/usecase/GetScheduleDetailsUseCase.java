package ac.dnd.mour.server.schedule.application.usecase;

import ac.dnd.mour.server.global.annotation.MourReadOnlyTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.schedule.application.usecase.query.GetScheduleDetails;
import ac.dnd.mour.server.schedule.application.usecase.query.response.ScheduleDetailsResponse;
import ac.dnd.mour.server.schedule.domain.repository.query.ScheduleQueryRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetScheduleDetailsUseCase {
    private final ScheduleQueryRepository scheduleQueryRepository;

    @MourReadOnlyTransactional
    public ScheduleDetailsResponse invoke(final GetScheduleDetails query) {
        return ScheduleDetailsResponse.from(scheduleQueryRepository.fetchScheduleDetails(
                query.scheduleId(),
                query.memberId()
        ));
    }
}
