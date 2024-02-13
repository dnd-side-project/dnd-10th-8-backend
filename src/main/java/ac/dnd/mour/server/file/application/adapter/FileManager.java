package ac.dnd.mour.server.file.application.adapter;

import ac.dnd.mour.server.file.domain.model.PresignedFileData;
import ac.dnd.mour.server.file.domain.model.PresignedUrlDetails;

public interface FileManager {
    PresignedUrlDetails createPresignedUrl(final PresignedFileData file);
}
