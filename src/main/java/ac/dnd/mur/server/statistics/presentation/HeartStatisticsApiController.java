package ac.dnd.mur.server.statistics.presentation;

import ac.dnd.mur.server.auth.domain.model.Authenticated;
import ac.dnd.mur.server.global.annotation.Auth;
import ac.dnd.mur.server.statistics.application.usecase.GetPersonalHeartStatisticsUseCase;
import ac.dnd.mur.server.statistics.application.usecase.query.GetPersonalHeartStatistics;
import ac.dnd.mur.server.statistics.application.usecase.query.response.PersonalHeartStatisticsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "통계 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartStatisticsApiController {
    private final GetPersonalHeartStatisticsUseCase getPersonalHeartStatisticsUseCase;

    @Operation(summary = "자신의 행사별 주고 받은 마음 내역 조회 Endpoint")
    @GetMapping("/v1/statistics/me")
    public ResponseEntity<PersonalHeartStatisticsResponse> getPersonalHeartStatistics(
            @Auth final Authenticated authenticated,
            @RequestParam(name = "type") final String type,
            @RequestParam(name = "year") final int year,
            @RequestParam(name = "month", defaultValue = "0") final int month
    ) {
        final PersonalHeartStatisticsResponse result = getPersonalHeartStatisticsUseCase.invoke(new GetPersonalHeartStatistics(
                authenticated.id(),
                type,
                year,
                month
        ));
        return ResponseEntity.ok(result);
    }
}
