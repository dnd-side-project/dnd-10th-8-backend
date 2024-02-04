package ac.dnd.mur.server.acceptance.relation;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.common.AcceptanceTest;
import ac.dnd.mur.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static ac.dnd.mur.server.acceptance.group.GroupAcceptanceStep.관리하고_있는_특정_그룹의_ID를_조회한다;
import static ac.dnd.mur.server.acceptance.heart.HeartAcceptanceStep.마음을_생성한다;
import static ac.dnd.mur.server.acceptance.relation.RelationAcceptanceStep.관계_단건_정보를_조회한다;
import static ac.dnd.mur.server.acceptance.relation.RelationAcceptanceStep.관계를_생성하고_ID를_추출한다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.결혼_축의금을_받았다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.생일_선물을_받았다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.승진_선물을_보냈다;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_1;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 등록한 관계 정보 조회")
public class GetRelationDetailsAcceptanceTest extends AcceptanceTest {
    @Nested
    @DisplayName("등록한 관계 단건 조회 API")
    class GetRelation {
        @Test
        @DisplayName("등록한 단건 관계에 대한 정보를 조회한다")
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
                    결혼_축의금을_받았다.getDay(),
                    결혼_축의금을_받았다.getEvent(),
                    결혼_축의금을_받았다.getMemo(),
                    결혼_축의금을_받았다.getTags(),
                    member.accessToken()
            );
            마음을_생성한다(
                    relationId,
                    승진_선물을_보냈다.isGive(),
                    승진_선물을_보냈다.getMoney(),
                    승진_선물을_보냈다.getDay(),
                    승진_선물을_보냈다.getEvent(),
                    승진_선물을_보냈다.getMemo(),
                    승진_선물을_보냈다.getTags(),
                    member.accessToken()
            );
            마음을_생성한다(
                    relationId,
                    생일_선물을_받았다.isGive(),
                    생일_선물을_받았다.getMoney(),
                    생일_선물을_받았다.getDay(),
                    생일_선물을_받았다.getEvent(),
                    생일_선물을_받았다.getMemo(),
                    생일_선물을_받았다.getTags(),
                    member.accessToken()
            );

            관계_단건_정보를_조회한다(relationId, member.accessToken())
                    .statusCode(OK.value())
                    .body("id", is((int) relationId))
                    .body("name", is(친구_1.getName()))
                    .body("group.id", is((int) groupId))
                    .body("group.name", is("친구"))
                    .body("giveMoney", is((int) 승진_선물을_보냈다.getMoney()))
                    .body("takeMoney", is((int) (결혼_축의금을_받았다.getMoney() + 생일_선물을_받았다.getMoney())));
        }
    }
}
