package ac.dnd.mour.server.file.presentation.dto.request;

import ac.dnd.mour.server.file.domain.model.PresignedFileData;
import jakarta.validation.constraints.NotBlank;

public record GetImagePresignedUrlRequest(
        @NotBlank(message = "파일명은 필수입니다.")
        String fileName
) {
    public PresignedFileData toFileData() {
        return new PresignedFileData(fileName);
    }
}
