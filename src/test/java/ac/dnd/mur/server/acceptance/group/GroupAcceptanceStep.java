package ac.dnd.mur.server.acceptance.group;

import ac.dnd.mur.server.group.presentation.dto.request.AddGroupRequest;
import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static ac.dnd.mur.server.acceptance.CommonRequestFixture.deleteRequestWithAccessToken;
import static ac.dnd.mur.server.acceptance.CommonRequestFixture.getRequestWithAccessToken;
import static ac.dnd.mur.server.acceptance.CommonRequestFixture.postRequestWithAccessToken;

public class GroupAcceptanceStep {
    public static ValidatableResponse 그룹을_추가한다(final String name, final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/groups")
                .build()
                .toUri()
                .getPath();

        final AddGroupRequest request = new AddGroupRequest(name);

        return postRequestWithAccessToken(uri, request, accessToken);
    }

    public static long 그룹을_추가하고_ID를_추출한다(final String name, final String accessToken) {
        return 그룹을_추가한다(name, accessToken)
                .extract()
                .jsonPath()
                .getLong("result");
    }

    public static ValidatableResponse 그룹을_삭제한다(
            final long groupId,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/groups/{groupId}")
                .build(groupId)
                .getPath();

        return deleteRequestWithAccessToken(uri, accessToken);
    }

    public static ValidatableResponse 관리하고_있는_그룹을_조회한다(final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/groups/me")
                .build()
                .toUri()
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }
}
