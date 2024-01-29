package ac.dnd.mur.server.file.application.adapter;

import ac.dnd.mur.server.file.domain.model.PresignedFileData;
import ac.dnd.mur.server.file.domain.model.PresignedUrlDetails;

public interface FileManager {
    PresignedUrlDetails createPresignedUrl(final PresignedFileData file);
}
