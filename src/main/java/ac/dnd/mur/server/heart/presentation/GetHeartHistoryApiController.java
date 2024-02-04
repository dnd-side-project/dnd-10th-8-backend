package ac.dnd.mur.server.heart.presentation;

import ac.dnd.mur.server.auth.domain.model.Authenticated;
import ac.dnd.mur.server.global.annotation.Auth;
import ac.dnd.mur.server.global.dto.ResponseWrapper;
import ac.dnd.mur.server.heart.application.usecase.GetHeartHistoryUseCase;
import ac.dnd.mur.server.heart.application.usecase.query.GetHeartHistory;
import ac.dnd.mur.server.heart.application.usecase.query.GetHeartHistoryWithRelation;
import ac.dnd.mur.server.heart.application.usecase.query.response.HeartHistoryDetails;
import ac.dnd.mur.server.heart.application.usecase.query.response.SpecificRelationHeartHistoryDetails;
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

@Tag(name = "마음 조회 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GetHeartHistoryApiController {
    private final GetHeartHistoryUseCase getHeartHistoryUseCase;

    @Operation(summary = "메인 홈 마음 내역 조회 Endpoint")
    @GetMapping("/v1/hearts/me")
    public ResponseEntity<ResponseWrapper<List<HeartHistoryDetails>>> getHeartHistories(
            @Auth final Authenticated authenticated,
            @RequestParam(name = "sort") final String sort,
            @RequestParam(name = "name", required = false) final String name
    ) {
        final List<HeartHistoryDetails> result = getHeartHistoryUseCase.getHeartHistories(new GetHeartHistory(
                authenticated.id(),
                sort,
                name
        ));
        return ResponseEntity.ok(ResponseWrapper.from(result));
    }

    @Operation(summary = "특정 관계간에 주고받은 마음 내역 조회 Endpoint")
    @GetMapping("/v1/hearts/me/{relationId}")
    public ResponseEntity<ResponseWrapper<List<SpecificRelationHeartHistoryDetails>>> getHeartHistoriesWithRelation(
            @Auth final Authenticated authenticated,
            @PathVariable(name = "relationId") final Long relationId,
            @RequestParam(name = "sort") final String sort
    ) {
        final List<SpecificRelationHeartHistoryDetails> result = getHeartHistoryUseCase.getHeartHistoriesWithRelation(new GetHeartHistoryWithRelation(
                authenticated.id(),
                relationId,
                sort
        ));
        return ResponseEntity.ok(ResponseWrapper.from(result));
    }
}
