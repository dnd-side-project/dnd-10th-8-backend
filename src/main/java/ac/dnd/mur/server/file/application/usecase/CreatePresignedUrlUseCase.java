package ac.dnd.mur.server.file.application.usecase;

import ac.dnd.mur.server.file.application.adapter.FileManager;
import ac.dnd.mur.server.file.application.usecase.command.CreatePresignedUrlCommand;
import ac.dnd.mur.server.file.domain.model.PresignedUrlDetails;
import ac.dnd.mur.server.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreatePresignedUrlUseCase {
    private final FileManager fileManager;

    public PresignedUrlDetails invoke(final CreatePresignedUrlCommand command) {
        return fileManager.createPresignedUrl(command.file());
    }
}
