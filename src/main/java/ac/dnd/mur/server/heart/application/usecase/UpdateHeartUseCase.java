package ac.dnd.mur.server.heart.application.usecase;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.heart.application.usecase.command.UpdateHeartCommand;
import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.heart.domain.repository.HeartRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdateHeartUseCase {
    private final HeartRepository heartRepository;

    @MurWritableTransactional
    public void invoke(final UpdateHeartCommand command) {
        final Heart heart = heartRepository.getMemberHeart(command.heartId(), command.memberId());
        heart.update(
                command.money(),
                command.day(),
                command.event(),
                command.memo(),
                command.tags()
        );
    }
}
