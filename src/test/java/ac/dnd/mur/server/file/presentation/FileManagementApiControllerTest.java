package ac.dnd.mur.server.file.presentation;

import ac.dnd.mur.server.common.ControllerTest;
import ac.dnd.mur.server.file.application.usecase.CreatePresignedUrlUseCase;
import ac.dnd.mur.server.file.domain.model.PresignedUrlDetails;
import ac.dnd.mur.server.file.exception.FileException;
import ac.dnd.mur.server.file.exception.FileExceptionCode;
import ac.dnd.mur.server.member.domain.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.query;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.failureDocsWithAccessToken;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("File -> FileManagementApiController 테스트")
class FileManagementApiControllerTest extends ControllerTest {
    @Autowired
    private CreatePresignedUrlUseCase createPresignedUrlUseCase;

    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Nested
    @DisplayName("이미지 업로드에 대한 Presigned Url 응답 API -> [POST /api/files/presigned/image]")
    class GetImagePresignedUrl {
        private static final String BASE_URL = "/api/files/presigned/image";

        @Test
        @DisplayName("이미지 파일[JPG, JPEG, PNG]이 아니면 Presigned Url을 얻을 수 없다")
        void throwExceptionByNotImage() {
            // given
            applyToken(true, member.getId());
            doThrow(new FileException(FileExceptionCode.INVALID_FILE_EXTENSION))
                    .when(createPresignedUrlUseCase)
                    .invoke(any());

            // when - then
            failedExecute(
                    getRequestWithAccessToken(BASE_URL, Map.of("fileName", "cat.pdf")),
                    status().isBadRequest(),
                    ExceptionSpec.of(FileExceptionCode.INVALID_FILE_EXTENSION),
                    failureDocsWithAccessToken("FileApi/GetPresignedUrl/Image/Failure", createHttpSpecSnippets(
                            queryParameters(
                                    query("fileName", "프로필 이미지 사진 파일명", "JPG, JPEG, PNG", true)
                            )
                    ))
            );
        }

        @Test
        @DisplayName("Presigned Url을 얻는다")
        void success() {
            // given
            applyToken(true, member.getId());
            given(createPresignedUrlUseCase.invoke(any())).willReturn(new PresignedUrlDetails(
                    "https://storage-url/path/fileName.png?X-xxx=xxx",
                    "https://storage-url/path/fileName.png"
            ));

            // when - then
            successfulExecute(
                    getRequestWithAccessToken(BASE_URL, Map.of("fileName", "cat.png")),
                    status().isOk(),
                    successDocsWithAccessToken("FileApi/GetPresignedUrl/Image/Success", createHttpSpecSnippets(
                            queryParameters(
                                    query("fileName", "프로필 이미지 사진 파일명", "JPG, JPEG, PNG", true)
                            ),
                            responseFields(
                                    body("preSignedUrl", "Presigned Url", "PUT 요청으로 이미지 업로드 (URL + File)"),
                                    body("uploadFileUrl", "스토리지 저장 URL", "스토리지 이미지 업로드 후 서버로 전송할 URL")
                            )
                    ))
            );
        }
    }
}
