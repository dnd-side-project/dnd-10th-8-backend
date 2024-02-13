package ac.dnd.mour.server.file.application.usecase;

import ac.dnd.mour.server.file.application.adapter.FileManager;
import ac.dnd.mour.server.file.application.usecase.command.CreatePresignedUrlCommand;
import ac.dnd.mour.server.file.domain.model.PresignedUrlDetails;
import ac.dnd.mour.server.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreatePresignedUrlUseCase {
    private final FileManager fileManager;

    public PresignedUrlDetails invoke(final CreatePresignedUrlCommand command) {
        return fileManager.createPresignedUrl(command.file());
    }
}
