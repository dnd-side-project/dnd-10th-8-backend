package ac.dnd.mur.server.acceptance.relation;

import ac.dnd.mur.server.relation.presentation.dto.request.CreateRelationRequest;
import ac.dnd.mur.server.relation.presentation.dto.request.UpdateRelationRequest;
import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static ac.dnd.mur.server.acceptance.CommonRequestFixture.deleteRequestWithAccessToken;
import static ac.dnd.mur.server.acceptance.CommonRequestFixture.getRequestWithAccessToken;
import static ac.dnd.mur.server.acceptance.CommonRequestFixture.patchRequestWithAccessToken;
import static ac.dnd.mur.server.acceptance.CommonRequestFixture.postRequestWithAccessToken;

public class RelationAcceptanceStep {
    public static ValidatableResponse 관계를_생성한다(
            final long groupId,
            final String name,
            final String phone,
            final String memo,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/relations")
                .build()
                .toUri()
                .getPath();

        final CreateRelationRequest request = new CreateRelationRequest(groupId, name, phone, memo);

        return postRequestWithAccessToken(uri, request, accessToken);
    }

    public static long 관계를_생성하고_ID를_추출한다(
            final long groupId,
            final String name,
            final String phone,
            final String memo,
            final String accessToken
    ) {
        return 관계를_생성한다(groupId, name, phone, memo, accessToken)
                .extract()
                .jsonPath()
                .getLong("result");
    }

    public static ValidatableResponse 관계를_수정한다(
            final long relationId,
            final long groupId,
            final String name,
            final String phone,
            final String memo,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/relations/{relationId}")
                .build(relationId)
                .getPath();

        final UpdateRelationRequest request = new UpdateRelationRequest(groupId, name, phone, memo);

        return patchRequestWithAccessToken(uri, request, accessToken);
    }

    public static ValidatableResponse 관계를_삭제한다(
            final long relationId,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/relations/{relationId}")
                .build(relationId)
                .getPath();

        return deleteRequestWithAccessToken(uri, accessToken);
    }

    public static ValidatableResponse 관계_단건_정보를_조회한다(
            final long relationId,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/relations/me/{relationId}")
                .build(relationId)
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }
}
