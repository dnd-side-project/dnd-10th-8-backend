package ac.dnd.mur.server.schedule.application.usecase;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import ac.dnd.mur.server.relation.domain.model.Relation;
import ac.dnd.mur.server.relation.domain.repository.RelationRepository;
import ac.dnd.mur.server.schedule.application.usecase.command.CreateScheduleCommand;
import ac.dnd.mur.server.schedule.domain.model.Schedule;
import ac.dnd.mur.server.schedule.domain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateScheduleUseCase {
    private final MemberRepository memberRepository;
    private final RelationRepository relationRepository;
    private final ScheduleRepository scheduleRepository;

    @MurWritableTransactional
    public long invoke(final CreateScheduleCommand command) {
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
