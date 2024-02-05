package ac.dnd.mur.server.acceptance.statistics;

import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static ac.dnd.mur.server.acceptance.CommonRequestFixture.getRequestWithAccessToken;

public class StatisticsAcceptanceStep {
    public static ValidatableResponse 자신의_행사별_주고_받은_마음_내역을_조회한다(
            final String type,
            final int year,
            final int month,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/statistics/me?type={type}&year={year}&month={month}")
                .build(type, year, month)
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }
}
