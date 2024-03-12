package ac.dnd.mour.server.heart.presentation.v1;

import ac.dnd.mour.server.auth.domain.model.Authenticated;
import ac.dnd.mour.server.global.annotation.Auth;
import ac.dnd.mour.server.global.dto.ResponseWrapper;
import ac.dnd.mour.server.heart.application.usecase.GetHeartHistoryUseCase;
import ac.dnd.mour.server.heart.application.usecase.query.response.HeartHistoryDetails;
import ac.dnd.mour.server.heart.application.usecase.query.response.SpecificRelationHeartHistoryDetails;
import ac.dnd.mour.server.heart.presentation.v1.request.GetHeartHistoriesRequest;
import ac.dnd.mour.server.heart.presentation.v1.request.GetHeartHistoriesWithRelationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "마음 조회 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class GetHeartHistoryApiController {
    private final GetHeartHistoryUseCase getHeartHistoryUseCase;

    @Operation(summary = "메인 홈 마음 내역 조회 Endpoint")
    @GetMapping("/hearts/me")
    public ResponseEntity<ResponseWrapper<List<HeartHistoryDetails>>> getHeartHistories(
            @Auth final Authenticated authenticated,
            @ModelAttribute @Valid final GetHeartHistoriesRequest request
    ) {
        final List<HeartHistoryDetails> result = getHeartHistoryUseCase.getHeartHistories(request.toQuery(authenticated.id()));
        return ResponseEntity.ok(ResponseWrapper.from(result));
    }

    @Operation(summary = "특정 관계간에 주고받은 마음 내역 조회 Endpoint")
    @GetMapping("/hearts/me/{relationId}")
    public ResponseEntity<ResponseWrapper<List<SpecificRelationHeartHistoryDetails>>> getHeartHistoriesWithRelation(
            @Auth final Authenticated authenticated,
            @PathVariable(name = "relationId") final Long relationId,
            @ModelAttribute @Valid final GetHeartHistoriesWithRelationRequest request
    ) {
        final List<SpecificRelationHeartHistoryDetails> result = getHeartHistoryUseCase.getHeartHistoriesWithRelation(request.toQuery(
                authenticated.id(),
                relationId
        ));
        return ResponseEntity.ok(ResponseWrapper.from(result));
    }
}
