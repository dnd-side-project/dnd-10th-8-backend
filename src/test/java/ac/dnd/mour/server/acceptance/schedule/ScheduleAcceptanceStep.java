package ac.dnd.mour.server.acceptance.schedule;

import ac.dnd.mour.server.common.fixture.ScheduleFixture;
import ac.dnd.mour.server.schedule.presentation.dto.request.CreateScheduleRequest;
import ac.dnd.mour.server.schedule.presentation.dto.request.UpdateScheduleRequest;
import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

import static ac.dnd.mour.server.acceptance.CommonRequestFixture.deleteRequestWithAccessToken;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.getRequestWithAccessToken;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.patchRequestWithAccessToken;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.postRequestWithAccessToken;

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

    public static ValidatableResponse 일정을_생성한다(
            final long relationId,
            final ScheduleFixture fixture,
            final LocalDate day,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/schedules")
                .build()
                .toUri()
                .getPath();

        final CreateScheduleRequest request = new CreateScheduleRequest(
                relationId,
                day,
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

    public static ValidatableResponse 일정을_생성한다(
            final long relationId,
            final LocalDate day,
            final String event,
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
                day,
                event,
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

    public static long 일정을_생성하고_ID를_추출한다(
            final long relationId,
            final ScheduleFixture fixture,
            final LocalDate day,
            final String accessToken
    ) {
        return 일정을_생성한다(relationId, fixture, day, accessToken)
                .extract()
                .jsonPath()
                .getLong("result");
    }

    public static long 일정을_생성하고_ID를_추출한다(
            final long relationId,
            final LocalDate day,
            final String event,
            final ScheduleFixture fixture,
            final String accessToken
    ) {
        return 일정을_생성한다(relationId, day, event, fixture, accessToken)
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

    public static ValidatableResponse 일정을_수정한다(
            final long scheduleId,
            final LocalDate day,
            final String event,
            final ScheduleFixture fixture,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/schedules/{scheduleId}")
                .build(scheduleId)
                .getPath();

        final UpdateScheduleRequest request = new UpdateScheduleRequest(
                day,
                event,
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

    public static ValidatableResponse 일정을_숨긴다(
            final long scheduleId,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/schedules/{scheduleId}/hide")
                .build(scheduleId)
                .getPath();

        return patchRequestWithAccessToken(uri, accessToken);
    }

    public static ValidatableResponse 지출이_기록되지_않은_일정을_조회한다(final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/schedules/unrecorded")
                .build()
                .toUri()
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }

    public static ValidatableResponse 캘린더_Year_Month에_해당하는_일정을_조회한다(
            final int year,
            final int month,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/schedules/me?year={year}&month={month}")
                .build(year, month)
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }

    public static ValidatableResponse 알람_동기화를_위한_일정을_조회한다(final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/schedules/me/alarm")
                .build()
                .toUri()
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }

    public static ValidatableResponse 일정_상세_정보를_조회한다(final long scheduleId, final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/schedules/me/{scheduleId}")
                .build(scheduleId)
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }
}
