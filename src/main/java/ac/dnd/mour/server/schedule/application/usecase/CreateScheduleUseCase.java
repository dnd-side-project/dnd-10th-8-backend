package ac.dnd.mour.server.schedule.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import ac.dnd.mour.server.relation.domain.model.Relation;
import ac.dnd.mour.server.relation.domain.repository.RelationRepository;
import ac.dnd.mour.server.schedule.application.usecase.command.CreateScheduleCommand;
import ac.dnd.mour.server.schedule.domain.model.Schedule;
import ac.dnd.mour.server.schedule.domain.repository.ScheduleRepository;
import ac.dnd.mour.server.schedule.domain.service.ScheduleValidator;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateScheduleUseCase {
    private final ScheduleValidator scheduleValidator;
    private final MemberRepository memberRepository;
    private final RelationRepository relationRepository;
    private final ScheduleRepository scheduleRepository;

    @MourWritableTransactional
    public long invoke(final CreateScheduleCommand command) {
        scheduleValidator.validateDay(command.day());

        final Member member = memberRepository.getById(command.memberId());
        final Relation relation = relationRepository.getMemberRelation(command.relationId(), member.getId());

        return scheduleRepository.save(new Schedule(
                member,
                relation,
                command.day(),
                command.event(),
                command.repeat(),
                command.alarm(),
                command.time(),
                command.link(),
                command.location(),
                command.memo()
        )).getId();
    }
}
