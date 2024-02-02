package ac.dnd.mur.server.heart.application.usecase;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.heart.application.usecase.command.CreateHeartCommand;
import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.heart.domain.repository.HeartRepository;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import ac.dnd.mur.server.relation.domain.model.Relation;
import ac.dnd.mur.server.relation.domain.repository.RelationRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateHeartUseCase {
    private final MemberRepository memberRepository;
    private final RelationRepository relationRepository;
    private final HeartRepository heartRepository;

    @MurWritableTransactional
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
