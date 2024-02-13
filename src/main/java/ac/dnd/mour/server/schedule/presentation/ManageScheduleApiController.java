package ac.dnd.mour.server.schedule.presentation;

import ac.dnd.mour.server.auth.domain.model.Authenticated;
import ac.dnd.mour.server.global.annotation.Auth;
import ac.dnd.mour.server.global.dto.ResponseWrapper;
import ac.dnd.mour.server.schedule.application.usecase.CreateScheduleUseCase;
import ac.dnd.mour.server.schedule.application.usecase.DeleteScheduleUseCase;
import ac.dnd.mour.server.schedule.application.usecase.UpdateScheduleUseCase;
import ac.dnd.mour.server.schedule.application.usecase.command.CreateScheduleCommand;
import ac.dnd.mour.server.schedule.application.usecase.command.DeleteScheduleCommand;
import ac.dnd.mour.server.schedule.application.usecase.command.UpdateScheduleCommand;
import ac.dnd.mour.server.schedule.presentation.dto.request.CreateScheduleRequest;
import ac.dnd.mour.server.schedule.presentation.dto.request.UpdateScheduleRequest;
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

@Tag(name = "일정 생성/수정/삭제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManageScheduleApiController {
    private final CreateScheduleUseCase createScheduleUseCase;
    private final UpdateScheduleUseCase updateScheduleUseCase;
    private final DeleteScheduleUseCase deleteScheduleUseCase;

    @Operation(summary = "일정 생성 Endpoint")
    @PostMapping("/v1/schedules")
    public ResponseEntity<ResponseWrapper<Long>> createSchedule(
            @Auth final Authenticated authenticated,
            @RequestBody @Valid final CreateScheduleRequest request
    ) {
        final long scheduleId = createScheduleUseCase.invoke(new CreateScheduleCommand(
                authenticated.id(),
                request.relationId(),
                request.day(),
                request.event(),
                request.toRepeat(),
                request.alarm(),
                request.time(),
                request.link(),
                request.location(),
                request.memo()
        ));
        return ResponseEntity.ok(ResponseWrapper.from(scheduleId));
    }

    @Operation(summary = "일정 수정 Endpoint")
    @PatchMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<Void> updateSchedule(
            @Auth final Authenticated authenticated,
            @PathVariable(name = "scheduleId") final Long scheduleId,
            @RequestBody @Valid final UpdateScheduleRequest request
    ) {
        updateScheduleUseCase.invoke(new UpdateScheduleCommand(
                authenticated.id(),
                scheduleId,
                request.day(),
                request.event(),
                request.toRepeat(),
                request.alarm(),
                request.time(),
                request.link(),
                request.location(),
                request.memo()
        ));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "일정 삭제 Endpoint")
    @DeleteMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @Auth final Authenticated authenticated,
            @PathVariable(name = "scheduleId") final Long scheduleId
    ) {
        deleteScheduleUseCase.invoke(new DeleteScheduleCommand(
                authenticated.id(),
                scheduleId
        ));
        return ResponseEntity.noContent().build();
    }
}
