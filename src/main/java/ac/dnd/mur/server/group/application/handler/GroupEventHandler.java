package ac.dnd.mur.server.group.application.handler;

import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import ac.dnd.mur.server.member.domain.event.MemberRegisteredEvent;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupEventHandler {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW)
    public void addGroups(final MemberRegisteredEvent event) {
        log.debug("MemberRegisteredEvent memberId={}, eventPublishedAt={}", event.memberId(), event.eventPublishedAt());

        final Member member = memberRepository.getById(event.memberId());
        groupRepository.saveAll(Group.init(member));
    }
}
