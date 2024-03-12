package ac.dnd.mour.server.acceptance.relation;

import ac.dnd.mour.server.auth.domain.model.AuthMember;
import ac.dnd.mour.server.common.AcceptanceTest;
import ac.dnd.mour.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static ac.dnd.mour.server.acceptance.group.GroupAcceptanceStep.관리하고_있는_특정_그룹의_ID를_조회한다_V1;
import static ac.dnd.mour.server.acceptance.relation.RelationAcceptanceStep.관계를_삭제한다_V1;
import static ac.dnd.mour.server.acceptance.relation.RelationAcceptanceStep.관계를_생성하고_ID를_추출한다_V1;
import static ac.dnd.mour.server.acceptance.relation.RelationAcceptanceStep.관계를_생성한다_V1;
import static ac.dnd.mour.server.acceptance.relation.RelationAcceptanceStep.관계를_수정한다_V1;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mour.server.common.fixture.RelationFixture.직장_1;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_1;
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
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다_V1("친구", member.accessToken());

            관계를_생성한다_V1(groupId, 친구_1.getName(), 친구_1.getImageUrl(), 친구_1.getMemo(), member.accessToken())
                    .statusCode(OK.value())
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
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다_V1("친구", member.accessToken());
            final long relationId = 관계를_생성하고_ID를_추출한다_V1(groupId, 친구_1.getName(), 친구_1.getImageUrl(), 친구_1.getMemo(), member.accessToken());

            final long updateGroupId = 관리하고_있는_특정_그룹의_ID를_조회한다_V1("직장", member.accessToken());
            관계를_수정한다_V1(relationId, updateGroupId, 직장_1.getName(), 직장_1.getImageUrl(), 직장_1.getMemo(), member.accessToken())
                    .statusCode(NO_CONTENT.value());
        }
    }

    @Nested
    @DisplayName("관계 삭제 API")
    class Delete {
        @Test
        @DisplayName("관계를 삭제한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다_V1("친구", member.accessToken());
            final long relationId = 관계를_생성하고_ID를_추출한다_V1(groupId, 친구_1.getName(), 친구_1.getImageUrl(), 친구_1.getMemo(), member.accessToken());

            관계를_삭제한다_V1(relationId, member.accessToken())
                    .statusCode(NO_CONTENT.value());
        }
    }
}
