package ac.dnd.mur.server.acceptance.group;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.common.AcceptanceTest;
import ac.dnd.mur.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static ac.dnd.mur.server.acceptance.group.GroupAcceptanceStep.관리하고_있는_그룹을_조회한다;
import static ac.dnd.mur.server.acceptance.group.GroupAcceptanceStep.그룹을_삭제한다;
import static ac.dnd.mur.server.acceptance.group.GroupAcceptanceStep.그룹을_추가하고_ID를_추출한다;
import static ac.dnd.mur.server.acceptance.group.GroupAcceptanceStep.그룹을_추가한다;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.group.exception.GroupExceptionCode.GROUP_ALREADY_EXISTS;
import static ac.dnd.mur.server.group.exception.GroupExceptionCode.GROUP_NOT_FOUND;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 사용자별 그룹 관리 기능")
public class ManageGroupAcceptanceTest extends AcceptanceTest {
    @Nested
    @DisplayName("그룹 추가 API")
    class AddGroup {
        @Test
        @DisplayName("중복된 그룹은 추가할 수 없다")
        void throwExceptionByGroupAlreadyExists() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            그룹을_추가한다("친구", member.accessToken())
                    .statusCode(CONFLICT.value())
                    .body("code", is(GROUP_ALREADY_EXISTS.getCode()))
                    .body("message", is(GROUP_ALREADY_EXISTS.getMessage()));
        }

        @Test
        @DisplayName("그룹을 추가한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            그룹을_추가한다("거래처", member.accessToken())
                    .statusCode(OK.value())
                    .body("result", notNullValue(Long.class));
        }
    }

    @Nested
    @DisplayName("그룹 제거 API")
    class RemoveGroup {
        @Test
        @DisplayName("존재하지 않는 그룹은 제거할 수 없다")
        void throwExceptionByGroupNotFound() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            그룹을_삭제한다(99999L, member.accessToken())
                    .statusCode(NOT_FOUND.value())
                    .body("code", is(GROUP_NOT_FOUND.getCode()))
                    .body("message", is(GROUP_NOT_FOUND.getMessage()));
        }

        @Test
        @DisplayName("그룹을 제거한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 그룹을_추가하고_ID를_추출한다("거래처", member.accessToken());
            그룹을_삭제한다(groupId, member.accessToken())
                    .statusCode(NO_CONTENT.value());
        }
    }

    @Nested
    @DisplayName("사용자가 관리하고 있는 그룹 조회 API")
    class GetMemberGroups {
        @Test
        @DisplayName("사용자가 관리하고 있는 그룹을 조회한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            관리하고_있는_그룹을_조회한다(member.accessToken())
                    .statusCode(OK.value())
                    .body("result.name", contains(List.of("친구", "가족", "지인", "직장").toArray()));

            그룹을_추가한다("거래처", member.accessToken());

            관리하고_있는_그룹을_조회한다(member.accessToken())
                    .statusCode(OK.value())
                    .body("result.name", contains(List.of("친구", "가족", "지인", "직장", "거래처").toArray()));
        }
    }
}
