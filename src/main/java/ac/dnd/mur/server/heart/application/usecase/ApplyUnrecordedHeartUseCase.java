package ac.dnd.mur.server.heart.application.usecase;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.heart.application.usecase.command.ApplyUnrecordedHeartCommand;
import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.heart.domain.repository.HeartRepository;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import ac.dnd.mur.server.relation.domain.model.Relation;
import ac.dnd.mur.server.relation.domain.repository.RelationRepository;
import ac.dnd.mur.server.schedule.domain.model.Schedule;
import ac.dnd.mur.server.schedule.domain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ApplyUnrecordedHeartUseCase {
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final RelationRepository relationRepository;
    private final HeartRepository heartRepository;

    @MurWritableTransactional
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
