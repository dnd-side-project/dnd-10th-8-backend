package ac.dnd.mour.server.file.domain.model;

import ac.dnd.mour.server.file.exception.FileException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static ac.dnd.mour.server.file.exception.FileExceptionCode.INVALID_FILE_EXTENSION;

@Getter
@RequiredArgsConstructor
public enum FileExtension {
    // 프로필 사진
    JPG(".jpg"),
    JPEG(".jpeg"),
    PNG(".png"),
    ;

    private final String value;

    public static FileExtension getExtensionViaFimeName(final String fileName) {
        return Arrays.stream(values())
                .filter(it -> it.value.equalsIgnoreCase(extractFileExtension(fileName)))
                .findFirst()
                .orElseThrow(() -> new FileException(INVALID_FILE_EXTENSION));
    }

    private static String extractFileExtension(final String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
