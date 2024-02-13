package ac.dnd.mour.server.acceptance.file;

import ac.dnd.mour.server.auth.domain.model.AuthMember;
import ac.dnd.mour.server.common.AcceptanceTest;
import ac.dnd.mour.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static ac.dnd.mour.server.acceptance.file.FileAcceptanceStep.이미지_업로드에_대한_Presigned_Url을_응답받는다;
import static ac.dnd.mour.server.common.config.BlackboxLogicControlConfig.BUCKET_UPLOAD_PREFIX;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 파일 관련 기능")
public class FileManagementAcceptanceTest extends AcceptanceTest {
    private static final String BUCKET_URL_PREFIX = "https://mour-upload.s3.amazonaws.com/profiles/";

    @Nested
    @DisplayName("이미지 업로드에 대한 PresignedUrl 응답 API")
    class GetImagePresignedUrl {
        @Test
        @DisplayName("이미지 업로드에 대한 PresignedUrl을 응답받는다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            이미지_업로드에_대한_Presigned_Url을_응답받는다("cat.png", member.accessToken())
                    .statusCode(OK.value())
                    .body("preSignedUrl", startsWith(BUCKET_URL_PREFIX + BUCKET_UPLOAD_PREFIX + "cat.png"))
                    .body("uploadFileUrl", is(BUCKET_URL_PREFIX + BUCKET_UPLOAD_PREFIX + "cat.png"));
        }
    }
}
