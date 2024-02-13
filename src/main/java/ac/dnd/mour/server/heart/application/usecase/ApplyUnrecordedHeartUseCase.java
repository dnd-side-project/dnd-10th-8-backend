package ac.dnd.mour.server.heart.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.heart.application.usecase.command.ApplyUnrecordedHeartCommand;
import ac.dnd.mour.server.heart.domain.model.Heart;
import ac.dnd.mour.server.heart.domain.repository.HeartRepository;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import ac.dnd.mour.server.relation.domain.model.Relation;
import ac.dnd.mour.server.relation.domain.repository.RelationRepository;
import ac.dnd.mour.server.schedule.domain.model.Schedule;
import ac.dnd.mour.server.schedule.domain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ApplyUnrecordedHeartUseCase {
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final RelationRepository relationRepository;
    private final HeartRepository heartRepository;

    @MourWritableTransactional
    public long invoke(final ApplyUnrecordedHeartCommand command) {
        final Member member = memberRepository.getById(command.memberId());
        final Schedule schedule = scheduleRepository.getMemberSchedule(command.scheduleId(), member.getId());
        final Relation relation = relationRepository.getMemberRelation(schedule.getRelationId(), member.getId());

        return heartRepository.save(new Heart(
                member,
                relation,
                true,
                command.money(),
                schedule.getDay(),
                schedule.getEvent(),
                schedule.getMemo(),
                command.tags()
        )).getId();
    }
}
