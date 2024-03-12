package ac.dnd.mour.server.file.presentation.v1;

import ac.dnd.mour.server.auth.domain.model.Authenticated;
import ac.dnd.mour.server.file.application.usecase.CreatePresignedUrlUseCase;
import ac.dnd.mour.server.file.application.usecase.command.CreatePresignedUrlCommand;
import ac.dnd.mour.server.file.domain.model.PresignedUrlDetails;
import ac.dnd.mour.server.file.presentation.v1.request.GetImagePresignedUrlRequest;
import ac.dnd.mour.server.global.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "파일 관련 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileManagementApiController {
    private final CreatePresignedUrlUseCase createPresignedUrlUseCase;

    @Operation(summary = "이미지 업로드에 대한 Presigned Url 응답 Endpoint")
    @GetMapping("/files/presigned/image")
    public ResponseEntity<PresignedUrlDetails> getImagePresignedUrl(
            @Auth final Authenticated authenticated,
            @ModelAttribute @Valid final GetImagePresignedUrlRequest request
    ) {
        final PresignedUrlDetails presignedUrl = createPresignedUrlUseCase.invoke(new CreatePresignedUrlCommand(request.toFileData()));
        return ResponseEntity.ok(presignedUrl);
    }
}
