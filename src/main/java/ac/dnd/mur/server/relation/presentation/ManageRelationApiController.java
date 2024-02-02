package ac.dnd.mur.server.relation.presentation;

import ac.dnd.mur.server.auth.domain.model.Authenticated;
import ac.dnd.mur.server.global.annotation.Auth;
import ac.dnd.mur.server.global.dto.ResponseWrapper;
import ac.dnd.mur.server.relation.application.usecase.CreateRelationUseCase;
import ac.dnd.mur.server.relation.application.usecase.DeleteRelationUseCase;
import ac.dnd.mur.server.relation.application.usecase.UpdateRelationUseCase;
import ac.dnd.mur.server.relation.application.usecase.command.CreateRelationCommand;
import ac.dnd.mur.server.relation.application.usecase.command.DeleteRelationCommand;
import ac.dnd.mur.server.relation.application.usecase.command.UpdateRelationCommand;
import ac.dnd.mur.server.relation.presentation.dto.request.CreateRelationRequest;
import ac.dnd.mur.server.relation.presentation.dto.request.UpdateRelationRequest;
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

@Tag(name = "관계 생성/수정/삭제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManageRelationApiController {
    private final CreateRelationUseCase createRelationUseCase;
    private final UpdateRelationUseCase updateRelationUseCase;
    private final DeleteRelationUseCase deleteRelationUseCase;

    @Operation(summary = "관계 생성 Endpoint")
    @PostMapping("/v1/relations")
    public ResponseEntity<ResponseWrapper<Long>> createRelation(
            @Auth final Authenticated authenticated,
            @RequestBody @Valid final CreateRelationRequest request
    ) {
        final long relationId = createRelationUseCase.invoke(new CreateRelationCommand(
                authenticated.id(),
                request.groupId(),
                request.name(),
                request.imageUrl(),
                request.memo()
        ));
        return ResponseEntity.ok(ResponseWrapper.from(relationId));
    }

    @Operation(summary = "관계 수정 Endpoint")
    @PatchMapping("/v1/relations/{relationId}")
    public ResponseEntity<Void> updateRelation(
            @Auth final Authenticated authenticated,
            @PathVariable(name = "relationId") final Long relationId,
            @RequestBody @Valid final UpdateRelationRequest request
    ) {
        updateRelationUseCase.invoke(new UpdateRelationCommand(
                authenticated.id(),
                relationId,
                request.groupId(),
                request.name(),
                request.imageUrl(),
                request.memo()
        ));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "관계 삭제 Endpoint")
    @DeleteMapping("/v1/relations/{relationId}")
    public ResponseEntity<Void> deleteRelation(
            @Auth final Authenticated authenticated,
            @PathVariable(name = "relationId") final Long relationId
    ) {
        deleteRelationUseCase.invoke(new DeleteRelationCommand(
                authenticated.id(),
                relationId
        ));
        return ResponseEntity.noContent().build();
    }
}
