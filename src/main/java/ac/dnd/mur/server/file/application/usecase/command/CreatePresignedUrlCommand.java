package ac.dnd.mur.server.file.application.usecase.command;

import ac.dnd.mur.server.file.domain.model.PresignedFileData;

public record CreatePresignedUrlCommand(
        PresignedFileData file
) {
}
