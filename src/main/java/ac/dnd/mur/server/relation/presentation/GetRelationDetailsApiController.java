package ac.dnd.mur.server.relation.presentation;

import ac.dnd.mur.server.auth.domain.model.Authenticated;
import ac.dnd.mur.server.global.annotation.Auth;
import ac.dnd.mur.server.relation.application.usecase.GetRelationDetailsUseCase;
import ac.dnd.mur.server.relation.application.usecase.query.GetSingleRelationDetails;
import ac.dnd.mur.server.relation.application.usecase.query.response.SingleRelationDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "등록한 관계 관련 정보 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GetRelationDetailsApiController {
    private final GetRelationDetailsUseCase getRelationDetailsUseCase;

    @Operation(summary = "단건 관계 정보 조회 Endpoint")
    @GetMapping("/v1/relations/me/{relationId}")
    public ResponseEntity<SingleRelationDetails> getRelation(
            @Auth final Authenticated authenticated,
            @PathVariable(name = "relationId") final Long relationId
    ) {
        final SingleRelationDetails result = getRelationDetailsUseCase.getRelation(new GetSingleRelationDetails(
                authenticated.id(),
                relationId
        ));
        return ResponseEntity.ok(result);
    }
}
