package ac.dnd.mour.server.file.utils;

import ac.dnd.mour.server.file.domain.model.FileExtension;
import ac.dnd.mour.server.file.domain.model.RawFileData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileConverter {
    public static RawFileData convertFile(final MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        final String fileName = file.getOriginalFilename();
        try {
            return new RawFileData(
                    fileName,
                    file.getContentType(),
                    FileExtension.getExtensionViaFimeName(fileName),
                    file.getInputStream()
            );
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
