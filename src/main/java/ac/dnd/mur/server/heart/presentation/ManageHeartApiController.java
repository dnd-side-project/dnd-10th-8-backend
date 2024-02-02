package ac.dnd.mur.server.heart.presentation;

import ac.dnd.mur.server.auth.domain.model.Authenticated;
import ac.dnd.mur.server.global.annotation.Auth;
import ac.dnd.mur.server.global.dto.ResponseWrapper;
import ac.dnd.mur.server.heart.application.usecase.CreateHeartUseCase;
import ac.dnd.mur.server.heart.application.usecase.DeleteHeartUseCase;
import ac.dnd.mur.server.heart.application.usecase.UpdateHeartUseCase;
import ac.dnd.mur.server.heart.application.usecase.command.CreateHeartCommand;
import ac.dnd.mur.server.heart.application.usecase.command.DeleteHeartCommand;
import ac.dnd.mur.server.heart.application.usecase.command.UpdateHeartCommand;
import ac.dnd.mur.server.heart.presentation.dto.request.CreateHeartRequest;
import ac.dnd.mur.server.heart.presentation.dto.request.UpdateHeartRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "마음 생성/수정/삭제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManageHeartApiController {
    private final CreateHeartUseCase createHeartUseCase;
    private final UpdateHeartUseCase updateHeartUseCase;
    private final DeleteHeartUseCase deleteHeartUseCase;

    @Operation(summary = "마음 생성 Endpoint")
    @PostMapping("/v1/hearts")
    public ResponseEntity<ResponseWrapper<Long>> createHeart(
            @Auth final Authenticated authenticated,
            @RequestBody @Valid final CreateHeartRequest request
    ) {
        final long heartId = createHeartUseCase.invoke(new CreateHeartCommand(
                authenticated.id(),
                request.relationId(),
                request.give(),
                request.money(),
                request.day(),
                request.event(),
                request.memo(),
                request.tags()
        ));
        return ResponseEntity.ok(ResponseWrapper.from(heartId));
    }

    @Operation(summary = "마음 수정 Endpoint")
    @PatchMapping("/v1/hearts/{heartId}")
    public ResponseEntity<Void> updateHeart(
            @Auth final Authenticated authenticated,
            @PathVariable(name = "heartId") final Long heartId,
            @RequestBody @Valid final UpdateHeartRequest request
    ) {
        updateHeartUseCase.invoke(new UpdateHeartCommand(
                authenticated.id(),
                heartId,
                request.money(),
                request.day(),
                request.event(),
                request.memo(),
                request.tags()
        ));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "마음 삭제 Endpoint")
    @DeleteMapping("/v1/hearts/{heartId}")
    public ResponseEntity<Void> deleteHeart(
            @Auth final Authenticated authenticated,
            @PathVariable(name = "heartId") final Long heartId
    ) {
        deleteHeartUseCase.invoke(new DeleteHeartCommand(
                authenticated.id(),
                heartId
        ));
        return ResponseEntity.noContent().build();
    }
}
