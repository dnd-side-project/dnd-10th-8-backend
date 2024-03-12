package ac.dnd.mour.server.schedule.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.schedule.application.usecase.command.UpdateScheduleCommand;
import ac.dnd.mour.server.schedule.domain.model.Schedule;
import ac.dnd.mour.server.schedule.domain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdateScheduleUseCase {
    private final ScheduleRepository scheduleRepository;

    @MourWritableTransactional
    public void invoke(final UpdateScheduleCommand command) {
        final Schedule schedule = scheduleRepository.getMemberSchedule(command.scheduleId(), command.memberId());
        schedule.update(
                command.day(),
                command.event(),
                command.repeat(),
                command.alarm(),
                command.time(),
                command.link(),
                command.location(),
                command.memo()
        );
    }
}
