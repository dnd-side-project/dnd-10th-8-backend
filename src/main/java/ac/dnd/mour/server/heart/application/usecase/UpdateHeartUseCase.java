package ac.dnd.mour.server.heart.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.heart.application.usecase.command.UpdateHeartCommand;
import ac.dnd.mour.server.heart.domain.model.Heart;
import ac.dnd.mour.server.heart.domain.repository.HeartRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdateHeartUseCase {
    private final HeartRepository heartRepository;

    @MourWritableTransactional
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
