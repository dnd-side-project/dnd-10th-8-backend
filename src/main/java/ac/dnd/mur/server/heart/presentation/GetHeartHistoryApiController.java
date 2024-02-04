package ac.dnd.mur.server.heart.presentation;

import ac.dnd.mur.server.auth.domain.model.Authenticated;
import ac.dnd.mur.server.global.annotation.Auth;
import ac.dnd.mur.server.global.dto.ResponseWrapper;
import ac.dnd.mur.server.heart.application.usecase.GetHeartHistoryUseCase;
import ac.dnd.mur.server.heart.application.usecase.query.GetHeartHistory;
import ac.dnd.mur.server.heart.application.usecase.query.response.HeartHistoryDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<ResponseWrapper<List<HeartHistoryDetails>>> getRelation(
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
}
