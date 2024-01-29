package ac.dnd.mur.server.acceptance.file;

import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static ac.dnd.mur.server.acceptance.CommonRequestFixture.getRequestWithAccessToken;

public class FileAcceptanceStep {
    public static ValidatableResponse 이미지_업로드에_대한_Presigned_Url을_응답받는다(
            final String fileName,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/files/presigned/image?fileName={fileName}")
                .build(fileName)
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }
}
