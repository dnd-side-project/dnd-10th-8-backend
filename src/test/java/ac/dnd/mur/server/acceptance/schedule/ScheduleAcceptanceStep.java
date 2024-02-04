package ac.dnd.mur.server.acceptance.schedule;

import ac.dnd.mur.server.common.fixture.ScheduleFixture;
import ac.dnd.mur.server.schedule.presentation.dto.request.CreateScheduleRequest;
import ac.dnd.mur.server.schedule.presentation.dto.request.UpdateScheduleRequest;
import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static ac.dnd.mur.server.acceptance.CommonRequestFixture.deleteRequestWithAccessToken;
import static ac.dnd.mur.server.acceptance.CommonRequestFixture.patchRequestWithAccessToken;
import static ac.dnd.mur.server.acceptance.CommonRequestFixture.postRequestWithAccessToken;

public class ScheduleAcceptanceStep {
    public static ValidatableResponse 일정을_생성한다(
            final long relationId,
            final ScheduleFixture fixture,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/schedules")
                .build()
                .toUri()
                .getPath();

        final CreateScheduleRequest request = new CreateScheduleRequest(
                relationId,
                fixture.getDay(),
                fixture.getEvent(),
                (fixture.getRepeat() != null) ? fixture.getRepeat().getType().getValue() : null,
                (fixture.getRepeat() != null) ? fixture.getRepeat().getFinish() : null,
                fixture.getAlarm(),
                fixture.getTime(),
                fixture.getLink(),
                fixture.getLocation(),
                fixture.getMemo()
        );

        return postRequestWithAccessToken(uri, request, accessToken);
    }

    public static long 일정을_생성하고_ID를_추출한다(
            final long relationId,
            final ScheduleFixture fixture,
            final String accessToken
    ) {
        return 일정을_생성한다(relationId, fixture, accessToken)
                .extract()
                .jsonPath()
                .getLong("result");
    }

    public static ValidatableResponse 일정을_수정한다(
            final long scheduleId,
            final ScheduleFixture fixture,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/schedules/{scheduleId}")
                .build(scheduleId)
                .getPath();

        final UpdateScheduleRequest request = new UpdateScheduleRequest(
                fixture.getDay(),
                fixture.getEvent(),
                (fixture.getRepeat() != null) ? fixture.getRepeat().getType().getValue() : null,
                (fixture.getRepeat() != null) ? fixture.getRepeat().getFinish() : null,
                fixture.getAlarm(),
                fixture.getTime(),
                fixture.getLink(),
                fixture.getLocation(),
                fixture.getMemo()
        );

        return patchRequestWithAccessToken(uri, request, accessToken);
    }

    public static ValidatableResponse 일정을_삭제한다(
            final long scheduleId,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/schedules/{scheduleId}")
                .build(scheduleId)
                .getPath();

        return deleteRequestWithAccessToken(uri, accessToken);
    }
}