package ac.dnd.mour.server.schedule.presentation;

import ac.dnd.mour.server.auth.domain.model.Authenticated;
import ac.dnd.mour.server.global.annotation.Auth;
import ac.dnd.mour.server.global.dto.ResponseWrapper;
import ac.dnd.mour.server.schedule.application.usecase.GetCalendarScheduleUseCase;
import ac.dnd.mour.server.schedule.application.usecase.GetScheduleDetailsUseCase;
import ac.dnd.mour.server.schedule.application.usecase.GetSchedulesForAlarmUseCase;
import ac.dnd.mour.server.schedule.application.usecase.GetUnrecordedScheduleUseCase;
import ac.dnd.mour.server.schedule.application.usecase.query.GetCalendarSchedule;
import ac.dnd.mour.server.schedule.application.usecase.query.GetScheduleDetails;
import ac.dnd.mour.server.schedule.application.usecase.query.response.CalendarScheduleResponse;
import ac.dnd.mour.server.schedule.application.usecase.query.response.ScheduleDetailsResponse;
import ac.dnd.mour.server.schedule.application.usecase.query.response.SchedulesForAlarmResponse;
import ac.dnd.mour.server.schedule.application.usecase.query.response.UnrecordedScheduleResponse;
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

@Tag(name = "등록한 일정 관련 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GetScheduleApiController {
    private final GetScheduleDetailsUseCase getScheduleDetailsUseCase;
    private final GetUnrecordedScheduleUseCase getUnrecordedScheduleUseCase;
    private final GetCalendarScheduleUseCase getCalendarScheduleUseCase;
    private final GetSchedulesForAlarmUseCase getSchedulesForAlarmUseCase;

    @Operation(summary = "일정 상세 조회 Endpoint")
    @GetMapping("/v1/schedules/me/{scheduleId}")
    public ResponseEntity<ScheduleDetailsResponse> getScheduleDetails(
            @Auth final Authenticated authenticated,
            @PathVariable(name = "scheduleId") final Long scheduleId
    ) {
        final ScheduleDetailsResponse result = getScheduleDetailsUseCase.invoke(new GetScheduleDetails(
                authenticated.id(),
                scheduleId
        ));
        return ResponseEntity.ok(result);
    }

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
    public ResponseEntity<ResponseWrapper<List<CalendarScheduleResponse>>> getCalendarSchedules(
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

    @Operation(summary = "알람 동기화를 위한 일정 조회 Endpoint")
    @GetMapping("/v1/schedules/me/alarm")
    public ResponseEntity<ResponseWrapper<List<SchedulesForAlarmResponse>>> getSchedulesForAlarm(
            @Auth final Authenticated authenticated
    ) {
        final List<SchedulesForAlarmResponse> result = getSchedulesForAlarmUseCase.invoke(authenticated.id());
        return ResponseEntity.ok(ResponseWrapper.from(result));
    }
}
