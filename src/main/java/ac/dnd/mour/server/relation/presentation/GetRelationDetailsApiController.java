package ac.dnd.mour.server.relation.presentation;

import ac.dnd.mour.server.auth.domain.model.Authenticated;
import ac.dnd.mour.server.global.annotation.Auth;
import ac.dnd.mour.server.global.dto.ResponseWrapper;
import ac.dnd.mour.server.relation.application.usecase.GetRelationDetailsUseCase;
import ac.dnd.mour.server.relation.application.usecase.query.GetMultipleRelationDetails;
import ac.dnd.mour.server.relation.application.usecase.query.GetSingleRelationDetails;
import ac.dnd.mour.server.relation.application.usecase.query.response.MultipleRelationDetails;
import ac.dnd.mour.server.relation.application.usecase.query.response.SingleRelationDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Operation(summary = "N건 관계 정보 조회 Endpoint")
    @GetMapping("/v1/relations/me")
    public ResponseEntity<ResponseWrapper<List<MultipleRelationDetails>>> getRelations(
            @Auth final Authenticated authenticated,
            @RequestParam(name = "name", required = false) final String name
    ) {
        final List<MultipleRelationDetails> result = getRelationDetailsUseCase.getRelations(new GetMultipleRelationDetails(
                authenticated.id(),
                name
        ));
        return ResponseEntity.ok(ResponseWrapper.from(result));
    }
}
