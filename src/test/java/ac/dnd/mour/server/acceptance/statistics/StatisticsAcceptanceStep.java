package ac.dnd.mour.server.acceptance.statistics;

import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static ac.dnd.mour.server.acceptance.CommonRequestFixture.getRequestWithAccessToken;

public class StatisticsAcceptanceStep {
    public static ValidatableResponse 자신의_행사별_주고_받은_마음_내역을_조회한다_V1(
            final int year,
            final int month,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/statistics/me?year={year}&month={month}")
                .build(year, month)
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }

    public static ValidatableResponse 사용자_트렌드별_평균_행사비_통계를_조회한다_V1(
            final String gender,
            final int range,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/statistics/users?gender={gender}&range={range}")
                .build(gender, range)
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }
}
