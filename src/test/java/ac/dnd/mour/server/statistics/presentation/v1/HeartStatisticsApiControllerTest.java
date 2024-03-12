package ac.dnd.mour.server.statistics.presentation.v1;

import ac.dnd.mour.server.common.ControllerTest;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.statistics.application.usecase.GetPersonalHeartStatisticsUseCase;
import ac.dnd.mour.server.statistics.application.usecase.GetTrendHeartAverageStatisticsUseCase;
import ac.dnd.mour.server.statistics.application.usecase.query.response.PersonalHeartStatisticsResponse;
import ac.dnd.mour.server.statistics.application.usecase.query.response.PersonalHeartSummary;
import ac.dnd.mour.server.statistics.application.usecase.query.response.TrendHeartAverageSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.query;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.subsection;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Statistics -> HeartStatisticsApiController 테스트")
class HeartStatisticsApiControllerTest extends ControllerTest {
    @Autowired
    private GetPersonalHeartStatisticsUseCase getPersonalHeartStatisticsUseCase;

    @Autowired
    private GetTrendHeartAverageStatisticsUseCase getTrendHeartAverageStatisticsUseCase;

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
                            new PersonalHeartSummary("결혼", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 6, 1), "메모.."),
                            new PersonalHeartSummary("생일", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 5, 1), "메모.."),
                            new PersonalHeartSummary("출산", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 4, 1), "메모.."),
                            new PersonalHeartSummary("돌잔치", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 3, 1), "메모.."),
                            new PersonalHeartSummary("개업", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 2, 1), "메모.."),
                            new PersonalHeartSummary("승진", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 1, 1), "메모.."),
                            new PersonalHeartSummary("졸업", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 1, 1), "메모..")
                    ),
                    List.of(
                            new PersonalHeartSummary("결혼", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 6, 1), "메모.."),
                            new PersonalHeartSummary("생일", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 5, 1), "메모.."),
                            new PersonalHeartSummary("출산", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 4, 1), "메모.."),
                            new PersonalHeartSummary("돌잔치", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 3, 1), "메모.."),
                            new PersonalHeartSummary("개업", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 2, 1), "메모.."),
                            new PersonalHeartSummary("승진", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 1, 1), "메모.."),
                            new PersonalHeartSummary("졸업", "관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 1, 1), "메모..")
                    )

//                    List.of(
//                            Map.of(
//                                    "결혼", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 6, 1), "메모..")),
//                                    "생일", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 5, 1), "메모..")),
//                                    "출산", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 4, 1), "메모..")),
//                                    "돌잔치", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 3, 1), "메모..")),
//                                    "개업", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 2, 1), "메모..")),
//                                    "기타", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 1, 1), "메모.."))
//                            )
//                    ),
//                    List.of(
//                            Map.of(
//                                    "결혼", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 6, 1), "메모..")),
//                                    "생일", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 5, 1), "메모..")),
//                                    "출산", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 4, 1), "메모..")),
//                                    "돌잔치", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 3, 1), "메모..")),
//                                    "개업", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 2, 1), "메모..")),
//                                    "기타", List.of(new PersonalHeartSummary("관계-이름-XXX", "친구", 100_000, LocalDate.of(2024, 1, 1), "메모.."))
//                            )
//                    )
            ));

            // when - then
            successfulExecute(
                    getRequestWithAccessToken(BASE_URL, Map.of("year", "2024")),
                    status().isOk(),
                    successDocsWithAccessToken("StatisticsApi/PersonalHeartHistories", createHttpSpecSnippets(
                            queryParameters(
                                    query("year", "Year", true),
                                    query("month", "Month", "안보내거나 0으로 보내면 연도별 조회", false)
                            ),
                            responseFields(
                                    subsection("give", "보낸 마음 내역"),
                                    subsection("take", "받은 마음 내역")
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("사용자 트렌드별 평균 행사비 통계 조회 API [GET /api/v1/statistics/users]")
    class GetTrendHeartAverageStatistics {
        private static final String BASE_URL = "/api/v1/statistics/users";

        @Test
        @DisplayName("사용자 트렌드별 평균 행사비 통계를 조회한다")
        void success() {
            // given
            applyToken(true, member);
            given(getTrendHeartAverageStatisticsUseCase.invoke(any())).willReturn(List.of(
                    new TrendHeartAverageSummary("결혼", BigDecimal.valueOf(100_000.3333D)),
                    new TrendHeartAverageSummary("생일", BigDecimal.valueOf(200_000.3333D)),
                    new TrendHeartAverageSummary("출산", BigDecimal.valueOf(300_000.3333D)),
                    new TrendHeartAverageSummary("돌잔치", BigDecimal.valueOf(400_000.3333D)),
                    new TrendHeartAverageSummary("개업", BigDecimal.valueOf(500_000.3333D)),
                    new TrendHeartAverageSummary("승진", BigDecimal.valueOf(600_000.3333D)),
                    new TrendHeartAverageSummary("졸업", BigDecimal.valueOf(700_000.3333D))
            ));
//                    new TrendHeartAverageStatisticsResponse(Map.of(
//                            "결혼", BigDecimal.valueOf(100_000.3333D),
//                            "생일", BigDecimal.valueOf(200_000.3333D),
//                            "출산", BigDecimal.valueOf(300_000.3333D),
//                            "돌잔치", BigDecimal.valueOf(400_000.3333D),
//                            "개업", BigDecimal.valueOf(500_000.3333D),
//                            "기타", BigDecimal.valueOf(600_000.3333D)
//                    ))
//            );

            // when - then
            successfulExecute(
                    getRequestWithAccessToken(BASE_URL, Map.of(
                            "gender", "male",
                            "range", "20"
                    )),
                    status().isOk(),
                    successDocsWithAccessToken("StatisticsApi/TrendHeartAverage", createHttpSpecSnippets(
                            queryParameters(
                                    query("gender", "성별", "male / female", true),
                                    query(
                                            "range",
                                            "연령대",
                                            "- 20대 = 20" + ENTER
                                                    + "- 30대 = 30" + ENTER
                                                    + "- 40대 = 40" + ENTER
                                                    + "- 50대 이상 = 50" + ENTER,
                                            true
                                    )
                            ),
                            responseFields(
                                    subsection("result", "트렌드 통계")
                            )
                    ))
            );
        }
    }
}
