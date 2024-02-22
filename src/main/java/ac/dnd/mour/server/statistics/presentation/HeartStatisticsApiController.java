package ac.dnd.mour.server.statistics.presentation;

import ac.dnd.mour.server.auth.domain.model.Authenticated;
import ac.dnd.mour.server.global.annotation.Auth;
import ac.dnd.mour.server.global.dto.ResponseWrapper;
import ac.dnd.mour.server.heart.presentation.dto.request.GetPersonalHeartStatisticsRequest;
import ac.dnd.mour.server.heart.presentation.dto.request.GetTrendHeartAverageStatisticsRequest;
import ac.dnd.mour.server.statistics.application.usecase.GetPersonalHeartStatisticsUseCase;
import ac.dnd.mour.server.statistics.application.usecase.GetTrendHeartAverageStatisticsUseCase;
import ac.dnd.mour.server.statistics.application.usecase.query.response.PersonalHeartStatisticsResponse;
import ac.dnd.mour.server.statistics.application.usecase.query.response.TrendHeartAverageSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "통계 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartStatisticsApiController {
    private final GetPersonalHeartStatisticsUseCase getPersonalHeartStatisticsUseCase;
    private final GetTrendHeartAverageStatisticsUseCase getTrendHeartAverageStatisticsUseCase;

    @Operation(summary = "자신의 행사별 주고 받은 마음 내역 조회 Endpoint")
    @GetMapping("/v1/statistics/me")
    public ResponseEntity<PersonalHeartStatisticsResponse> getPersonalHeartStatistics(
            @Auth final Authenticated authenticated,
            @ModelAttribute @Valid final GetPersonalHeartStatisticsRequest request
    ) {
        final PersonalHeartStatisticsResponse result = getPersonalHeartStatisticsUseCase.invoke(request.toQuery(authenticated.id()));
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "사용자 트렌드별 평균 행사비 통계 조회 Endpoint")
    @GetMapping("/v1/statistics/users")
    public ResponseEntity<ResponseWrapper<List<TrendHeartAverageSummary>>> getTrendHeartAverageStatistics(
            @Auth final Authenticated authenticated,
            @ModelAttribute @Valid final GetTrendHeartAverageStatisticsRequest request
    ) {
        final List<TrendHeartAverageSummary> result = getTrendHeartAverageStatisticsUseCase.invoke(request.toQuery());
        return ResponseEntity.ok(ResponseWrapper.from(result));
    }
}
