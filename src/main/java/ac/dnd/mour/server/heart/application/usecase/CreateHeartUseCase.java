package ac.dnd.mour.server.heart.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.heart.application.usecase.command.CreateHeartCommand;
import ac.dnd.mour.server.heart.domain.model.Heart;
import ac.dnd.mour.server.heart.domain.repository.HeartRepository;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import ac.dnd.mour.server.relation.domain.model.Relation;
import ac.dnd.mour.server.relation.domain.repository.RelationRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateHeartUseCase {
    private final MemberRepository memberRepository;
    private final RelationRepository relationRepository;
    private final HeartRepository heartRepository;

    @MourWritableTransactional
    public long invoke(final CreateHeartCommand command) {
        final Member member = memberRepository.getById(command.memberId());
        final Relation relation = relationRepository.getById(command.relationId());
        return heartRepository.save(new Heart(
                member,
                relation,
                command.give(),
                command.money(),
                command.day(),
                command.event(),
                command.memo(),
                command.tags()
        )).getId();
    }
}
