package ac.dnd.mur.server.acceptance.statistics;

import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static ac.dnd.mur.server.acceptance.CommonRequestFixture.getRequestWithAccessToken;

public class StatisticsAcceptanceStep {
    public static ValidatableResponse 자신의_행사별_주고_받은_마음_내역을_조회한다(
            final String standard,
            final int year,
            final int month,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/statistics/me?standard={standard}&year={year}&month={month}")
                .build(standard, year, month)
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }

    public static ValidatableResponse 사용자_트렌드별_평균_행사비_통계를_조회한다(
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
