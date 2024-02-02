package ac.dnd.mur.server.acceptance.relation;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.common.AcceptanceTest;
import ac.dnd.mur.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static ac.dnd.mur.server.acceptance.group.GroupAcceptanceStep.관리하고_있는_특정_그룹의_ID를_조회한다;
import static ac.dnd.mur.server.acceptance.relation.RelationAcceptanceStep.관계를_삭제한다;
import static ac.dnd.mur.server.acceptance.relation.RelationAcceptanceStep.관계를_생성하고_ID를_추출한다;
import static ac.dnd.mur.server.acceptance.relation.RelationAcceptanceStep.관계를_생성한다;
import static ac.dnd.mur.server.acceptance.relation.RelationAcceptanceStep.관계를_수정한다;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.직장_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_1;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 관계 도메인 생명주기 관리 (생성, 수정, 삭제)")
public class ManageRelationAcceptanceTest extends AcceptanceTest {
    @Nested
    @DisplayName("관계 생성 API")
    class Create {
        @Test
        @DisplayName("관계를 생성한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();

            관계를_생성한다(
                    관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken()),
                    친구_1.getName(),
                    친구_1.getImageUrl(),
                    친구_1.getMemo(),
                    member.accessToken()
            ).statusCode(OK.value())
                    .body("result", notNullValue(Long.class));
        }
    }

    @Nested
    @DisplayName("관계 수정 API")
    class Update {
        @Test
        @DisplayName("관계를 수정한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long relationId = 관계를_생성하고_ID를_추출한다(
                    관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken()),
                    친구_1.getName(),
                    친구_1.getImageUrl(),
                    친구_1.getMemo(),
                    member.accessToken()
            );

            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("직장", member.accessToken());
            관계를_수정한다(
                    relationId,
                    groupId,
                    직장_1.getName(),
                    직장_1.getImageUrl(),
                    직장_1.getMemo(),
                    member.accessToken()
            ).statusCode(NO_CONTENT.value());
        }
    }

    @Nested
    @DisplayName("관계 삭제 API")
    class Delete {
        @Test
        @DisplayName("관계를 삭제한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long relationId = 관계를_생성하고_ID를_추출한다(
                    관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken()),
                    친구_1.getName(),
                    친구_1.getImageUrl(),
                    친구_1.getMemo(),
                    member.accessToken()
            );

            관계를_삭제한다(relationId, member.accessToken())
                    .statusCode(NO_CONTENT.value());
        }
    }
}
