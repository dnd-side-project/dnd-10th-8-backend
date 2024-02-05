package ac.dnd.mur.server.schedule.presentation;

import ac.dnd.mur.server.auth.domain.model.Authenticated;
import ac.dnd.mur.server.global.annotation.Auth;
import ac.dnd.mur.server.global.dto.ResponseWrapper;
import ac.dnd.mur.server.schedule.application.usecase.GetCalendarScheduleUseCase;
import ac.dnd.mur.server.schedule.application.usecase.GetUnrecordedScheduleUseCase;
import ac.dnd.mur.server.schedule.application.usecase.query.GetCalendarSchedule;
import ac.dnd.mur.server.schedule.application.usecase.query.response.CalendarScheduleResponse;
import ac.dnd.mur.server.schedule.application.usecase.query.response.UnrecordedScheduleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "등록한 일정 관련 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GetScheduleApiController {
    private final GetUnrecordedScheduleUseCase getUnrecordedScheduleUseCase;
    private final GetCalendarScheduleUseCase getCalendarScheduleUseCase;

    @Operation(summary = "지출(보낸 마음)이 기록되지 않은 일정 조회 Endpoint")
    @GetMapping("/v1/schedules/unrecorded")
    public ResponseEntity<ResponseWrapper<List<UnrecordedScheduleResponse>>> getUnrecordedSchedule(
            @Auth final Authenticated authenticated
    ) {
        final List<UnrecordedScheduleResponse> result = getUnrecordedScheduleUseCase.invoke(authenticated.id());
        return ResponseEntity.ok(ResponseWrapper.from(result));
    }

    @Operation(summary = "캘린더 Year/Month 일정 조회 Endpoint")
    @GetMapping("/v1/schedules/me")
    public ResponseEntity<ResponseWrapper<List<CalendarScheduleResponse>>> getUnrecordedSchedule(
            @Auth final Authenticated authenticated,
            @RequestParam(name = "year") final int year,
            @RequestParam(name = "month") final int month
    ) {
        final List<CalendarScheduleResponse> result = getCalendarScheduleUseCase.invoke(new GetCalendarSchedule(
                authenticated.id(),
                year,
                month
        ));
        return ResponseEntity.ok(ResponseWrapper.from(result));
    }
}
