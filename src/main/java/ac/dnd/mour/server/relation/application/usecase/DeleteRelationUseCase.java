package ac.dnd.mour.server.relation.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.heart.domain.repository.HeartRepository;
import ac.dnd.mour.server.heart.domain.repository.HeartTagRepository;
import ac.dnd.mour.server.relation.application.usecase.command.DeleteRelationCommand;
import ac.dnd.mour.server.relation.domain.model.Relation;
import ac.dnd.mour.server.relation.domain.repository.RelationRepository;
import ac.dnd.mour.server.schedule.domain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class DeleteRelationUseCase {
    private final RelationRepository relationRepository;
    private final HeartRepository heartRepository;
    private final HeartTagRepository heartTagRepository;
    private final ScheduleRepository scheduleRepository;

    @MourWritableTransactional
    public void invoke(final DeleteRelationCommand command) {
        final Relation relation = relationRepository.getMemberRelation(command.relationid(), command.memberId());
        deleteRelatedHeartAndSchedules(command.memberId(), relation.getId());
        relationRepository.deleteMemberRelation(relation.getId(), command.memberId());
    }

    private void deleteRelatedHeartAndSchedules(final long memberId, final long relationId) {
        final List<Long> heartIds = heartRepository.findIdsByMemberIdAndRelationId(memberId, relationId);
        heartTagRepository.deleteByHeartIds(heartIds);
        heartRepository.deleteByIds(heartIds);
        scheduleRepository.deleteWithMemberRelations(memberId, relationId);
    }
}
