package ac.dnd.mour.server.schedule.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.schedule.application.usecase.command.HideScheduleCommand;
import ac.dnd.mour.server.schedule.domain.model.Schedule;
import ac.dnd.mour.server.schedule.domain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class HideScheduleUseCase {
    private final ScheduleRepository scheduleRepository;

    @MourWritableTransactional
    public void invoke(final HideScheduleCommand command) {
        final Schedule schedule = scheduleRepository.getMemberSchedule(command.scheduleId(), command.memberId());
        schedule.hide();
    }
}
