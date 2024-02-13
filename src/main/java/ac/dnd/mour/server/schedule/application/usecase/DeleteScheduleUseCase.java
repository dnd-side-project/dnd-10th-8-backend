package ac.dnd.mour.server.schedule.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.schedule.application.usecase.command.DeleteScheduleCommand;
import ac.dnd.mour.server.schedule.domain.model.Schedule;
import ac.dnd.mour.server.schedule.domain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteScheduleUseCase {
    private final ScheduleRepository scheduleRepository;

    @MourWritableTransactional
    public void invoke(final DeleteScheduleCommand command) {
        final Schedule schedule = scheduleRepository.getMemberSchedule(command.scheduleId(), command.memberId());
        scheduleRepository.deleteMemberSchedule(schedule.getId(), command.memberId());
    }
}
