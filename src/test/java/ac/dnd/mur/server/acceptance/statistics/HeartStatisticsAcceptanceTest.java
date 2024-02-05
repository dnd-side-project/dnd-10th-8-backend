package ac.dnd.mur.server.acceptance.statistics;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.common.AcceptanceTest;
import ac.dnd.mur.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;

import static ac.dnd.mur.server.acceptance.group.GroupAcceptanceStep.관리하고_있는_특정_그룹의_ID를_조회한다;
import static ac.dnd.mur.server.acceptance.heart.HeartAcceptanceStep.마음을_생성한다;
import static ac.dnd.mur.server.acceptance.relation.RelationAcceptanceStep.관계를_생성하고_ID를_추출한다;
import static ac.dnd.mur.server.acceptance.statistics.StatisticsAcceptanceStep.자신의_행사별_주고_받은_마음_내역을_조회한다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.결혼_축의금을_받았다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.생일_선물을_받았다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.승진_선물을_보냈다;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_1;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 통계 정보 조회")
public class HeartStatisticsAcceptanceTest extends AcceptanceTest {
    @Nested
    @DisplayName("자신의 행사별 주고 받은 마음 내역 조회 API")
    class GetRelation {
        @Test
        @DisplayName("자신의 행사별 주고 받은 마음 내역을 조회한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId = 관계를_생성하고_ID를_추출한다(
                    groupId,
                    친구_1.getName(),
                    친구_1.getImageUrl(),
                    친구_1.getMemo(),
                    member.accessToken()
            );
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

            자신의_행사별_주고_받은_마음_내역을_조회한다("year", 2024, 0, member.accessToken())
                    .statusCode(OK.value())
                    .body("give.find { it.keySet().contains('결혼') }['결혼']", hasSize(0))
                    .body("give.find { it.keySet().contains('생일') }['생일']", hasSize(0))
                    .body("give.find { it.keySet().contains('출산') }['출산']", hasSize(0))
                    .body("give.find { it.keySet().contains('돌잔치') }['돌잔치']", hasSize(0))
                    .body("give.find { it.keySet().contains('개업') }['개업']", hasSize(0))
                    .body("give.find { it.keySet().contains('기타') }['기타']", hasSize(2))
                    .body("take.find { it.keySet().contains('결혼') }['결혼']", hasSize(1))
                    .body("take.find { it.keySet().contains('생일') }['생일']", hasSize(1))
                    .body("take.find { it.keySet().contains('출산') }['출산']", hasSize(0))
                    .body("take.find { it.keySet().contains('돌잔치') }['돌잔치']", hasSize(0))
                    .body("take.find { it.keySet().contains('개업') }['개업']", hasSize(0))
                    .body("take.find { it.keySet().contains('기타') }['기타']", hasSize(0));

            자신의_행사별_주고_받은_마음_내역을_조회한다("month", 2024, 1, member.accessToken())
                    .statusCode(OK.value())
                    .body("give.find { it.keySet().contains('결혼') }['결혼']", hasSize(0))
                    .body("give.find { it.keySet().contains('생일') }['생일']", hasSize(0))
                    .body("give.find { it.keySet().contains('출산') }['출산']", hasSize(0))
                    .body("give.find { it.keySet().contains('돌잔치') }['돌잔치']", hasSize(0))
                    .body("give.find { it.keySet().contains('개업') }['개업']", hasSize(0))
                    .body("give.find { it.keySet().contains('기타') }['기타']", hasSize(0))
                    .body("take.find { it.keySet().contains('결혼') }['결혼']", hasSize(0))
                    .body("take.find { it.keySet().contains('생일') }['생일']", hasSize(0))
                    .body("take.find { it.keySet().contains('출산') }['출산']", hasSize(0))
                    .body("take.find { it.keySet().contains('돌잔치') }['돌잔치']", hasSize(0))
                    .body("take.find { it.keySet().contains('개업') }['개업']", hasSize(0))
                    .body("take.find { it.keySet().contains('기타') }['기타']", hasSize(0));

            자신의_행사별_주고_받은_마음_내역을_조회한다("month", 2024, 2, member.accessToken())
                    .statusCode(OK.value())
                    .body("give.find { it.keySet().contains('결혼') }['결혼']", hasSize(0))
                    .body("give.find { it.keySet().contains('생일') }['생일']", hasSize(0))
                    .body("give.find { it.keySet().contains('출산') }['출산']", hasSize(0))
                    .body("give.find { it.keySet().contains('돌잔치') }['돌잔치']", hasSize(0))
                    .body("give.find { it.keySet().contains('개업') }['개업']", hasSize(0))
                    .body("give.find { it.keySet().contains('기타') }['기타']", hasSize(2))
                    .body("take.find { it.keySet().contains('결혼') }['결혼']", hasSize(1))
                    .body("take.find { it.keySet().contains('생일') }['생일']", hasSize(0))
                    .body("take.find { it.keySet().contains('출산') }['출산']", hasSize(0))
                    .body("take.find { it.keySet().contains('돌잔치') }['돌잔치']", hasSize(0))
                    .body("take.find { it.keySet().contains('개업') }['개업']", hasSize(0))
                    .body("take.find { it.keySet().contains('기타') }['기타']", hasSize(0));

            자신의_행사별_주고_받은_마음_내역을_조회한다("month", 2024, 3, member.accessToken())
                    .statusCode(OK.value())
                    .body("give.find { it.keySet().contains('결혼') }['결혼']", hasSize(0))
                    .body("give.find { it.keySet().contains('생일') }['생일']", hasSize(0))
                    .body("give.find { it.keySet().contains('출산') }['출산']", hasSize(0))
                    .body("give.find { it.keySet().contains('돌잔치') }['돌잔치']", hasSize(0))
                    .body("give.find { it.keySet().contains('개업') }['개업']", hasSize(0))
                    .body("give.find { it.keySet().contains('기타') }['기타']", hasSize(0))
                    .body("take.find { it.keySet().contains('결혼') }['결혼']", hasSize(0))
                    .body("take.find { it.keySet().contains('생일') }['생일']", hasSize(1))
                    .body("take.find { it.keySet().contains('출산') }['출산']", hasSize(0))
                    .body("take.find { it.keySet().contains('돌잔치') }['돌잔치']", hasSize(0))
                    .body("take.find { it.keySet().contains('개업') }['개업']", hasSize(0))
                    .body("take.find { it.keySet().contains('기타') }['기타']", hasSize(0));
        }
    }
}
