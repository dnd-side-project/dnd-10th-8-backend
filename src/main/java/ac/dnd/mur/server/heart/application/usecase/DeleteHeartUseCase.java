package ac.dnd.mur.server.heart.application.usecase;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.heart.application.usecase.command.DeleteHeartCommand;
import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.heart.domain.repository.HeartRepository;
import ac.dnd.mur.server.heart.domain.repository.HeartTagRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteHeartUseCase {
    private final HeartRepository heartRepository;
    private final HeartTagRepository heartTagRepository;

    @MurWritableTransactional
    public void invoke(final DeleteHeartCommand command) {
        final Heart heart = heartRepository.getMemberHeart(command.heartId(), command.memberId());
        heartTagRepository.deleteByHeartId(heart.getId());
        heartRepository.deleteMemberHeart(heart.getId(), command.memberId());
    }
}
