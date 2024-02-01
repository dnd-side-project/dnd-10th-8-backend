package ac.dnd.mur.server.member.domain.service;

import ac.dnd.mur.server.auth.application.adapter.TokenStore;
import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import ac.dnd.mur.server.heart.domain.repository.HeartRepository;
import ac.dnd.mur.server.heart.domain.repository.HeartTagRepository;
import ac.dnd.mur.server.relation.domain.repository.RelationRepository;
import ac.dnd.mur.server.schedule.domain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteMemberRelatedResourceProcessor {
    private final TokenStore tokenStore;
    private final GroupRepository groupRepository;
    private final RelationRepository relationRepository;
    private final HeartRepository heartRepository;
    private final HeartTagRepository heartTagRepository;
    private final ScheduleRepository scheduleRepository;

    @MurWritableTransactional
    public void invoke(final long memberId) {
        tokenStore.deleteRefreshToken(memberId);
        groupRepository.deleteMemberGroups(memberId);
        relationRepository.deleteMemberRelations(memberId);
        deleteMemberHearts(memberId);
        scheduleRepository.deleteMemberSchedules(memberId);
    }

    private void deleteMemberHearts(final long memberId) {
        final List<Long> heartIds = heartRepository.findIdsByMemberId(memberId);
        if (!CollectionUtils.isEmpty(heartIds)) {
            heartTagRepository.deleteByHeartIds(heartIds);
            heartRepository.deleteMemberHearts(memberId);
        }
    }
}
