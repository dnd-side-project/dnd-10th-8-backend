package ac.dnd.mur.server.schedule.application.usecase;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.schedule.application.usecase.command.DeleteScheduleCommand;
import ac.dnd.mur.server.schedule.domain.model.Schedule;
import ac.dnd.mur.server.schedule.domain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteScheduleUseCase {
    private final ScheduleRepository scheduleRepository;

    @MurWritableTransactional
    public void invoke(final DeleteScheduleCommand command) {
        final Schedule schedule = scheduleRepository.getMemberSchedule(command.scheduleId(), command.memberId());
        scheduleRepository.deleteMemberSchedule(schedule.getId(), command.memberId());
    }
}
