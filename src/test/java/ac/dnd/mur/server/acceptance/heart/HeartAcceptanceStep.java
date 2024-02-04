package ac.dnd.mur.server.acceptance.heart;

import ac.dnd.mur.server.heart.presentation.dto.request.ApplyUnrecordedHeartRequest;
import ac.dnd.mur.server.heart.presentation.dto.request.CreateHeartRequest;
import ac.dnd.mur.server.heart.presentation.dto.request.UpdateHeartRequest;
import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static ac.dnd.mur.server.acceptance.CommonRequestFixture.deleteRequestWithAccessToken;
import static ac.dnd.mur.server.acceptance.CommonRequestFixture.patchRequestWithAccessToken;
import static ac.dnd.mur.server.acceptance.CommonRequestFixture.postRequestWithAccessToken;

public class HeartAcceptanceStep {
    public static ValidatableResponse 마음을_생성한다(
            final long relationId,
            final boolean give,
            final long money,
            final LocalDate day,
            final String event,
            final String memo,
            final List<String> tags,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/hearts")
                .build()
                .toUri()
                .getPath();

        final CreateHeartRequest request = new CreateHeartRequest(relationId, give, money, day, event, memo, tags);

        return postRequestWithAccessToken(uri, request, accessToken);
    }

    public static long 마음을_생성하고_ID를_추출한다(
            final long relationId,
            final boolean give,
            final long money,
            final LocalDate day,
            final String event,
            final String memo,
            final List<String> tags,
            final String accessToken
    ) {
        return 마음을_생성한다(relationId, give, money, day, event, memo, tags, accessToken)
                .extract()
                .jsonPath()
                .getLong("result");
    }

    public static ValidatableResponse 지출이_기록되지_않는_일정에_대한_마음을_생성한다(
            final long scheduleId,
            final long money,
            final List<String> tags,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/hearts/unrecorded-schedule")
                .build()
                .toUri()
                .getPath();

        final ApplyUnrecordedHeartRequest request = new ApplyUnrecordedHeartRequest(scheduleId, money, tags);

        return postRequestWithAccessToken(uri, request, accessToken);
    }

    public static long 지출이_기록되지_않는_일정에_대한_마음을_생성하고_ID를_추출한다(
            final long scheduleId,
            final long money,
            final List<String> tags,
            final String accessToken
    ) {
        return 지출이_기록되지_않는_일정에_대한_마음을_생성한다(scheduleId, money, tags, accessToken)
                .extract()
                .jsonPath()
                .getLong("result");
    }

    public static ValidatableResponse 마음을_수정한다(
            final long heartId,
            final long money,
            final LocalDate day,
            final String event,
            final String memo,
            final List<String> tags,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/hearts/{heartId}")
                .build(heartId)
                .getPath();

        final UpdateHeartRequest request = new UpdateHeartRequest(money, day, event, memo, tags);

        return patchRequestWithAccessToken(uri, request, accessToken);
    }

    public static ValidatableResponse 마음을_삭제한다(
            final long heartId,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/hearts/{heartId}")
                .build(heartId)
                .getPath();

        return deleteRequestWithAccessToken(uri, accessToken);
    }
}
