package ac.dnd.mur.server.statistics.presentation;

import ac.dnd.mur.server.common.ControllerTest;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.statistics.application.usecase.GetPersonalHeartStatisticsUseCase;
import ac.dnd.mur.server.statistics.application.usecase.query.response.PersonalHeartStatisticsResponse;
import ac.dnd.mur.server.statistics.application.usecase.query.response.PersonalHeartSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.query;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.subsection;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Statistics -> HeartStatisticsApiController 테스트")
class HeartStatisticsApiControllerTest extends ControllerTest {
    @Autowired
    private GetPersonalHeartStatisticsUseCase getPersonalHeartStatisticsUseCase;

    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Nested
    @DisplayName("자신의 행사별 주고 받은 마음 내역 조회 API [GET /api/v1/statistics/me]")
    class GetPersonalHeartStatistics {
        private static final String BASE_URL = "/api/v1/statistics/me";

        @Test
        @DisplayName("자신의 행사별 주고 받은 마음 내역을 조회한다")
        void success() {
            // given
            applyToken(true, member);
            given(getPersonalHeartStatisticsUseCase.invoke(any())).willReturn(new PersonalHeartStatisticsResponse(
                    List.of(
                            Map.of(
                                    "결혼", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 6, 1), "메모..")),
                                    "생일", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 5, 1), "메모..")),
                                    "출산", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 4, 1), "메모..")),
                                    "돌잔치", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 3, 1), "메모..")),
                                    "개업", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 2, 1), "메모..")),
                                    "기타", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 1, 1), "메모.."))
                            )
                    ),
                    List.of(
                            Map.of(
                                    "결혼", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 6, 1), "메모..")),
                                    "생일", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 5, 1), "메모..")),
                                    "출산", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 4, 1), "메모..")),
                                    "돌잔치", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 3, 1), "메모..")),
                                    "개업", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 2, 1), "메모..")),
                                    "기타", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 1, 1), "메모.."))
                            )
                    )
            ));

            // when - then
            successfulExecute(
                    getRequestWithAccessToken(BASE_URL, Map.of(
                            "type", "year",
                            "year", "2024"
                    )),
                    status().isOk(),
                    successDocsWithAccessToken("StatisticsApi/PersonalHeartHistories", createHttpSpecSnippets(
                            queryParameters(
                                    query("type", "연도별 or 월별", "year / month", true),
                                    query("year", "Year", true),
                                    query("month", "Month", false)
                            ),
                            responseFields(
                                    subsection("give", "보낸 마음 내역"),
                                    subsection("take", "받은 마음 내역")
                            )
                    ))
            );
        }
    }
}
