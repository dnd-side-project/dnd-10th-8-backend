package ac.dnd.mour.server.acceptance.statistics;

import ac.dnd.mour.server.auth.domain.model.AuthMember;
import ac.dnd.mour.server.common.AcceptanceTest;
import ac.dnd.mour.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import ac.dnd.mour.server.common.fixture.HeartFixture;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.List;

import static ac.dnd.mour.server.acceptance.group.GroupAcceptanceStep.관리하고_있는_특정_그룹의_ID를_조회한다;
import static ac.dnd.mour.server.acceptance.heart.HeartAcceptanceStep.마음을_생성하고_ID를_추출한다;
import static ac.dnd.mour.server.acceptance.heart.HeartAcceptanceStep.마음을_생성한다;
import static ac.dnd.mour.server.acceptance.relation.RelationAcceptanceStep.관계를_생성하고_ID를_추출한다;
import static ac.dnd.mour.server.acceptance.statistics.StatisticsAcceptanceStep.사용자_트렌드별_평균_행사비_통계를_조회한다;
import static ac.dnd.mour.server.acceptance.statistics.StatisticsAcceptanceStep.자신의_행사별_주고_받은_마음_내역을_조회한다;
import static ac.dnd.mour.server.common.fixture.HeartFixture.개업_선물을_보냈다;
import static ac.dnd.mour.server.common.fixture.HeartFixture.결혼_축의금을_받았다;
import static ac.dnd.mour.server.common.fixture.HeartFixture.결혼_축의금을_보냈다;
import static ac.dnd.mour.server.common.fixture.HeartFixture.돌잔치_선물을_보냈다;
import static ac.dnd.mour.server.common.fixture.HeartFixture.생일_선물을_받았다;
import static ac.dnd.mour.server.common.fixture.HeartFixture.생일_선물을_보냈다;
import static ac.dnd.mour.server.common.fixture.HeartFixture.승진_선물을_보냈다;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_2;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_3;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_4;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_5;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_6;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_7;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_8;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_1;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_2;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_3;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_4;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_5;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_6;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_7;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_8;
import static ac.dnd.mour.server.member.domain.model.Gender.FEMALE;
import static ac.dnd.mour.server.member.domain.model.Gender.MALE;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 통계 정보 조회")
public class HeartStatisticsAcceptanceTest extends AcceptanceTest {
    @Nested
    @DisplayName("자신의 행사별 주고 받은 마음 내역 조회 API")
    class GetPersonalHeartStatistics {
        @Test
        @DisplayName("자신의 행사별 주고 받은 마음 내역을 조회한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId = 관계를_생성하고_ID를_추출한다(groupId, 친구_1.getName(), 친구_1.getImageUrl(), 친구_1.getMemo(), member.accessToken());
            마음을_생성한다(
                    relationId,
                    결혼_축의금을_받았다.isGive(),
                    결혼_축의금을_받았다.getMoney(),
                    LocalDate.of(2024, 2, 1),
                    결혼_축의금을_받았다.getEvent(),
                    결혼_축의금을_받았다.getMemo(),
                    결혼_축의금을_받았다.getTags(),
                    member.accessToken()
            );
            마음을_생성한다(
                    relationId,
                    승진_선물을_보냈다.isGive(),
                    승진_선물을_보냈다.getMoney(),
                    LocalDate.of(2024, 2, 10),
                    승진_선물을_보냈다.getEvent(),
                    승진_선물을_보냈다.getMemo(),
                    승진_선물을_보냈다.getTags(),
                    member.accessToken()
            );
            마음을_생성한다(
                    relationId,
                    승진_선물을_보냈다.isGive(),
                    승진_선물을_보냈다.getMoney(),
                    LocalDate.of(2024, 2, 26),
                    승진_선물을_보냈다.getEvent(),
                    승진_선물을_보냈다.getMemo(),
                    승진_선물을_보냈다.getTags(),
                    member.accessToken()
            );
            마음을_생성한다(
                    relationId,
                    생일_선물을_받았다.isGive(),
                    생일_선물을_받았다.getMoney(),
                    LocalDate.of(2024, 3, 1),
                    생일_선물을_받았다.getEvent(),
                    생일_선물을_받았다.getMemo(),
                    생일_선물을_받았다.getTags(),
                    member.accessToken()
            );

            final ValidatableResponse response1 = 자신의_행사별_주고_받은_마음_내역을_조회한다(2024, 0, member.accessToken()).statusCode(OK.value());
            assertPersonalHeartStatisticsMatch(
                    response1,
                    List.of(0, 0, 0, 0, 0, 2),
                    List.of(1, 1, 0, 0, 0, 0)
            );

            final ValidatableResponse response2 = 자신의_행사별_주고_받은_마음_내역을_조회한다(2024, 1, member.accessToken()).statusCode(OK.value());
            assertPersonalHeartStatisticsMatch(
                    response2,
                    List.of(0, 0, 0, 0, 0, 0),
                    List.of(0, 0, 0, 0, 0, 0)
            );

            final ValidatableResponse response3 = 자신의_행사별_주고_받은_마음_내역을_조회한다(2024, 2, member.accessToken()).statusCode(OK.value());
            assertPersonalHeartStatisticsMatch(
                    response3,
                    List.of(0, 0, 0, 0, 0, 2),
                    List.of(1, 0, 0, 0, 0, 0)
            );

            final ValidatableResponse response4 = 자신의_행사별_주고_받은_마음_내역을_조회한다(2024, 3, member.accessToken()).statusCode(OK.value());
            assertPersonalHeartStatisticsMatch(
                    response4,
                    List.of(0, 0, 0, 0, 0, 0),
                    List.of(0, 1, 0, 0, 0, 0)
            );
        }

        private void assertPersonalHeartStatisticsMatch(
                final ValidatableResponse response,
                final List<Integer> giveCounts,
                final List<Integer> takeCounts
        ) {
            response.body("give.findAll { it.event == '결혼' }.size()", is(giveCounts.get(0)))
                    .body("give.findAll { it.event == '생일' }.size()", is(giveCounts.get(1)))
                    .body("give.findAll { it.event == '출산' }.size()", is(giveCounts.get(2)))
                    .body("give.findAll { it.event == '돌잔치' }.size()", is(giveCounts.get(3)))
                    .body("give.findAll { it.event == '개업' }.size()", is(giveCounts.get(4)))
                    .body("give.findAll { it.event !in ['결혼', '생일', '출산', '돌잔치', '개업'] }.size()", is(giveCounts.get(5)))
                    .body("take.findAll { it.event == '결혼' }.size()", is(takeCounts.get(0)))
                    .body("take.findAll { it.event == '생일' }.size()", is(takeCounts.get(1)))
                    .body("take.findAll { it.event == '출산' }.size()", is(takeCounts.get(2)))
                    .body("take.findAll { it.event == '돌잔치' }.size()", is(takeCounts.get(3)))
                    .body("take.findAll { it.event == '개업' }.size()", is(takeCounts.get(4)))
                    .body("take.findAll { it.event !in ['결혼', '생일', '출산', '돌잔치', '개업'] }.size()", is(takeCounts.get(5)));
        }
    }

    @Nested
    @DisplayName("사용자 트렌드별 평균 행사비 통계 조회 API")
    class GetTrendHeartAverageStatistics {
        @Test
        @DisplayName("사용자 트렌드별 평균 행사비 통계를 조회한다")
        void success() {
            final AuthMember member1 = MEMBER_1.회원가입과_로그인을_진행한다(MALE, LocalDate.now().minusYears(25));
            final AuthMember member2 = MEMBER_2.회원가입과_로그인을_진행한다(MALE, LocalDate.now().minusYears(25));
            final AuthMember member3 = MEMBER_3.회원가입과_로그인을_진행한다(MALE, LocalDate.now().minusYears(35));
            final AuthMember member4 = MEMBER_4.회원가입과_로그인을_진행한다(FEMALE, LocalDate.now().minusYears(45));
            final AuthMember member5 = MEMBER_5.회원가입과_로그인을_진행한다(MALE, LocalDate.now().minusYears(55));
            final AuthMember member6 = MEMBER_6.회원가입과_로그인을_진행한다(FEMALE, LocalDate.now().minusYears(65));
            final AuthMember member7 = MEMBER_7.회원가입과_로그인을_진행한다(MALE, LocalDate.now().minusYears(75));
            final AuthMember member8 = MEMBER_8.회원가입과_로그인을_진행한다(MALE, LocalDate.now().minusYears(85));
            final long groupId1 = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member1.accessToken());
            final long groupId2 = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member2.accessToken());
            final long groupId3 = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member3.accessToken());
            final long groupId4 = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member4.accessToken());
            final long groupId5 = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member5.accessToken());
            final long groupId6 = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member6.accessToken());
            final long groupId7 = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member7.accessToken());
            final long groupId8 = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member8.accessToken());
            final long relationId1 = 관계를_생성하고_ID를_추출한다(groupId1, 친구_1.getName(), 친구_1.getImageUrl(), 친구_1.getMemo(), member1.accessToken());
            final long relationId2 = 관계를_생성하고_ID를_추출한다(groupId2, 친구_2.getName(), 친구_2.getImageUrl(), 친구_2.getMemo(), member2.accessToken());
            final long relationId3 = 관계를_생성하고_ID를_추출한다(groupId3, 친구_3.getName(), 친구_3.getImageUrl(), 친구_3.getMemo(), member3.accessToken());
            final long relationId4 = 관계를_생성하고_ID를_추출한다(groupId4, 친구_4.getName(), 친구_4.getImageUrl(), 친구_4.getMemo(), member4.accessToken());
            final long relationId5 = 관계를_생성하고_ID를_추출한다(groupId5, 친구_5.getName(), 친구_5.getImageUrl(), 친구_5.getMemo(), member5.accessToken());
            final long relationId6 = 관계를_생성하고_ID를_추출한다(groupId6, 친구_6.getName(), 친구_6.getImageUrl(), 친구_6.getMemo(), member6.accessToken());
            final long relationId7 = 관계를_생성하고_ID를_추출한다(groupId7, 친구_7.getName(), 친구_7.getImageUrl(), 친구_7.getMemo(), member7.accessToken());
            final long relationId8 = 관계를_생성하고_ID를_추출한다(groupId8, 친구_8.getName(), 친구_8.getImageUrl(), 친구_8.getMemo(), member8.accessToken());
            createHeart(relationId1, member1.accessToken(), List.of(1_500_000L, 2_500_000L, 542_000L, 530_000L, 158_000L));
            createHeart(relationId2, member2.accessToken(), List.of(2_900_000L, 1_800_000L, 234_000L, 130_000L, 358_000L));
            createHeart(relationId3, member3.accessToken(), List.of(560_000L, 23_500_000L, 1_734_000L, 1_320_000L, 1_558_000L));
            createHeart(relationId4, member4.accessToken(), List.of(1_593_000L, 980_000L, 2_123_000L, 5_430_000L, 10_258_000L));
            createHeart(relationId5, member5.accessToken(), List.of(2_500_000L, 548_000L, 3_123_000L, 430_000L, 100_203_000L));
            createHeart(relationId6, member6.accessToken(), List.of(3_600_000L, 1_430_000L, 127_000L, 293_000L, 102_392_000L));
            createHeart(relationId7, member7.accessToken(), List.of(49_000L, 5_500_000L, 938_000L, 958_000L, 3_293_000L));
            createHeart(relationId8, member8.accessToken(), List.of(1_382_000L, 3_293_000L, 1_828_000L, 358_000L, 39_281_000L));

            final ValidatableResponse response1 = 사용자_트렌드별_평균_행사비_통계를_조회한다(MALE.getValue(), 20, member1.accessToken()).statusCode(OK.value());
            assertTrendHeartAverageStatisticsMatch(response1, List.of(2_200_000F, 2_150_000F, 0F, 388_000F, 330_000F, 258_000F));

            final ValidatableResponse response2 = 사용자_트렌드별_평균_행사비_통계를_조회한다(FEMALE.getValue(), 20, member1.accessToken()).statusCode(OK.value());
            assertTrendHeartAverageStatisticsMatch(response2, List.of(0F, 0F, 0F, 0F, 0F, 0F));

            final ValidatableResponse response3 = 사용자_트렌드별_평균_행사비_통계를_조회한다(MALE.getValue(), 30, member1.accessToken()).statusCode(OK.value());
            assertTrendHeartAverageStatisticsMatch(response3, List.of(560_000F, 23_500_000F, 0F, 1_734_000F, 1_320_000F, 1_558_000F));

            final ValidatableResponse response4 = 사용자_트렌드별_평균_행사비_통계를_조회한다(FEMALE.getValue(), 30, member1.accessToken()).statusCode(OK.value());
            assertTrendHeartAverageStatisticsMatch(response4, List.of(0F, 0F, 0F, 0F, 0F, 0F));

            final ValidatableResponse response5 = 사용자_트렌드별_평균_행사비_통계를_조회한다(MALE.getValue(), 40, member1.accessToken()).statusCode(OK.value());
            assertTrendHeartAverageStatisticsMatch(response5, List.of(0F, 0F, 0F, 0F, 0F, 0F));

            final ValidatableResponse response6 = 사용자_트렌드별_평균_행사비_통계를_조회한다(FEMALE.getValue(), 40, member1.accessToken()).statusCode(OK.value());
            assertTrendHeartAverageStatisticsMatch(response6, List.of(1_593_000F, 980_000F, 0F, 2_123_000F, 5_430_000F, 10_258_000F));

            final ValidatableResponse response7 = 사용자_트렌드별_평균_행사비_통계를_조회한다(MALE.getValue(), 50, member1.accessToken()).statusCode(OK.value());
            assertTrendHeartAverageStatisticsMatch(response7, List.of(1_310_333.3333F, 3_113_666.6667F, 0F, 1_963_000F, 582_000F, 47_592_333.3333F));

            final ValidatableResponse response8 = 사용자_트렌드별_평균_행사비_통계를_조회한다(FEMALE.getValue(), 50, member1.accessToken()).statusCode(OK.value());
            assertTrendHeartAverageStatisticsMatch(response8, List.of(3_600_000F, 1_430_000F, 0F, 127_000F, 293_000F, 102_392_000F));
        }

        private void createHeart(
                final long relationId,
                final String accessToken,
                final List<Long> money
        ) {
            final List<HeartFixture> heartFixtures = List.of(
                    결혼_축의금을_보냈다,
                    생일_선물을_보냈다,
                    돌잔치_선물을_보냈다,
                    개업_선물을_보냈다,
                    승진_선물을_보냈다
            );

            for (int i = 0; i < heartFixtures.size(); i++) {
                final HeartFixture heart = heartFixtures.get(i);
                마음을_생성하고_ID를_추출한다(
                        relationId,
                        heart.isGive(),
                        money.get(i),
                        LocalDate.of(2024, 3, 1),
                        heart.getEvent(),
                        heart.getMemo(),
                        heart.getTags(),
                        accessToken
                );
            }
        }

        private void assertTrendHeartAverageStatisticsMatch(
                final ValidatableResponse response,
                final List<Float> amounts
        ) {
            response.body("result.find { it.event == '결혼' }.amount", (amounts.get(0) != 0) ? is(amounts.get(0)) : nullValue())
                    .body("result.find { it.event == '생일' }.amount", (amounts.get(1) != 0) ? is(amounts.get(1)) : nullValue())
                    .body("result.find { it.event == '출산' }.amount", (amounts.get(2) != 0) ? is(amounts.get(2)) : nullValue())
                    .body("result.find { it.event == '돌잔치' }.amount", (amounts.get(3) != 0) ? is(amounts.get(3)) : nullValue())
                    .body("result.find { it.event == '개업' }.amount", (amounts.get(4) != 0) ? is(amounts.get(4)) : nullValue())
                    .body("result.find { it.event !in ['결혼', '생일', '출산', '돌잔치', '개업'] }.amount", (amounts.get(5) != 0) ? is(amounts.get(5)) : nullValue());
        }
    }
}
