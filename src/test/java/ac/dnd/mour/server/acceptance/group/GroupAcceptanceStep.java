package ac.dnd.mour.server.acceptance.group;

import ac.dnd.mour.server.group.domain.model.GroupResponse;
import ac.dnd.mour.server.group.presentation.v1.request.AddGroupRequest;
import ac.dnd.mour.server.group.presentation.v1.request.UpdateGroupRequest;
import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static ac.dnd.mour.server.acceptance.CommonRequestFixture.deleteRequestWithAccessToken;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.getRequestWithAccessToken;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.patchRequestWithAccessToken;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.postRequestWithAccessToken;

public class GroupAcceptanceStep {
    public static ValidatableResponse 그룹을_추가한다_V1(final String name, final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/groups")
                .build()
                .toUri()
                .getPath();

        final AddGroupRequest request = new AddGroupRequest(name);

        return postRequestWithAccessToken(uri, request, accessToken);
    }

    public static long 그룹을_추가하고_ID를_추출한다_V1(final String name, final String accessToken) {
        return 그룹을_추가한다_V1(name, accessToken)
                .extract()
                .jsonPath()
                .getLong("result");
    }

    public static ValidatableResponse 그룹을_수정한다_V1(final long groupId, final String name, final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/groups/{groupId}")
                .build(groupId)
                .getPath();

        final UpdateGroupRequest request = new UpdateGroupRequest(name);

        return patchRequestWithAccessToken(uri, request, accessToken);
    }

    public static ValidatableResponse 그룹을_삭제한다_V1(
            final long groupId,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/groups/{groupId}")
                .build(groupId)
                .getPath();

        return deleteRequestWithAccessToken(uri, accessToken);
    }

    public static ValidatableResponse 관리하고_있는_그룹을_조회한다_V1(final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/groups/me")
                .build()
                .toUri()
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }

    public static long 관리하고_있는_특정_그룹의_ID를_조회한다_V1(final String name, final String accessToken) {
        final List<GroupResponse> result = 관리하고_있는_그룹을_조회한다_V1(accessToken)
                .extract()
                .jsonPath()
                .getList("result", GroupResponse.class);

        return result.stream()
                .filter(it -> it.name().equals(name))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .id();
    }
}
