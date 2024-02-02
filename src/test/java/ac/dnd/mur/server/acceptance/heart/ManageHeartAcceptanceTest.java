package ac.dnd.mur.server.acceptance.heart;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.common.AcceptanceTest;
import ac.dnd.mur.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static ac.dnd.mur.server.acceptance.group.GroupAcceptanceStep.관리하고_있는_특정_그룹의_ID를_조회한다;
import static ac.dnd.mur.server.acceptance.heart.HeartAcceptanceStep.마음을_삭제한다;
import static ac.dnd.mur.server.acceptance.heart.HeartAcceptanceStep.마음을_생성하고_ID를_추출한다;
import static ac.dnd.mur.server.acceptance.heart.HeartAcceptanceStep.마음을_생성한다;
import static ac.dnd.mur.server.acceptance.heart.HeartAcceptanceStep.마음을_수정한다;
import static ac.dnd.mur.server.acceptance.relation.RelationAcceptanceStep.관계를_생성하고_ID를_추출한다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.결혼_축의금을_받았다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.승진_선물을_보냈다;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_1;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 마음 도메인 생명주기 관리 (생성, 수정, 삭제)")
public class ManageHeartAcceptanceTest extends AcceptanceTest {
    @Nested
    @DisplayName("마음 생성 API")
    class Create {
        @Test
        @DisplayName("마음을 생성한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long relationId = 관계를_생성하고_ID를_추출한다(
                    관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken()),
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
            ).statusCode(OK.value())
                    .body("result", notNullValue(Long.class));
        }
    }

    @Nested
    @DisplayName("마음 수정 API")
    class Update {
        @Test
        @DisplayName("마음을 수정한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long relationId = 관계를_생성하고_ID를_추출한다(
                    관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken()),
                    친구_1.getName(),
                    친구_1.getImageUrl(),
                    친구_1.getMemo(),
                    member.accessToken()
            );
            final long heartId = 마음을_생성하고_ID를_추출한다(
                    relationId,
                    결혼_축의금을_받았다.isGive(),
                    결혼_축의금을_받았다.getMoney(),
                    결혼_축의금을_받았다.getDay(),
                    결혼_축의금을_받았다.getEvent(),
                    결혼_축의금을_받았다.getMemo(),
                    결혼_축의금을_받았다.getTags(),
                    member.accessToken()
            );

            마음을_수정한다(
                    heartId,
                    승진_선물을_보냈다.getMoney(),
                    승진_선물을_보냈다.getDay(),
                    승진_선물을_보냈다.getEvent(),
                    승진_선물을_보냈다.getMemo(),
                    승진_선물을_보냈다.getTags(),
                    member.accessToken()
            ).statusCode(NO_CONTENT.value());
        }
    }

    @Nested
    @DisplayName("마음 삭제 API")
    class Delete {
        @Test
        @DisplayName("마음을 삭제한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long relationId = 관계를_생성하고_ID를_추출한다(
                    관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken()),
                    친구_1.getName(),
                    친구_1.getImageUrl(),
                    친구_1.getMemo(),
                    member.accessToken()
            );
            final long heartId = 마음을_생성하고_ID를_추출한다(
                    relationId,
                    결혼_축의금을_받았다.isGive(),
                    결혼_축의금을_받았다.getMoney(),
                    결혼_축의금을_받았다.getDay(),
                    결혼_축의금을_받았다.getEvent(),
                    결혼_축의금을_받았다.getMemo(),
                    결혼_축의금을_받았다.getTags(),
                    member.accessToken()
            );

            마음을_삭제한다(heartId, member.accessToken())
                    .statusCode(NO_CONTENT.value());
        }
    }
}
