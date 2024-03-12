package ac.dnd.mour.server.file.application.adapter;

import ac.dnd.mour.server.file.domain.model.PresignedFileData;
import ac.dnd.mour.server.file.domain.model.PresignedUrlDetails;
import ac.dnd.mour.server.file.domain.model.RawFileData;

public interface FileManager {
    PresignedUrlDetails createPresignedUrl(final PresignedFileData file);

    String upload(RawFileData file);
}
