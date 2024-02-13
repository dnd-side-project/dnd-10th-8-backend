package ac.dnd.mour.server.file.application.usecase.command;

import ac.dnd.mour.server.file.domain.model.PresignedFileData;

public record CreatePresignedUrlCommand(
        PresignedFileData file
) {
}
