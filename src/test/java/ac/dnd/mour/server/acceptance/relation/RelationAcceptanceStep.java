package ac.dnd.mour.server.acceptance.relation;

import ac.dnd.mour.server.relation.presentation.v1.request.CreateRelationRequest;
import ac.dnd.mour.server.relation.presentation.v1.request.UpdateRelationRequest;
import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static ac.dnd.mour.server.acceptance.CommonRequestFixture.deleteRequestWithAccessToken;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.getRequestWithAccessToken;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.patchRequestWithAccessToken;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.postRequestWithAccessToken;

public class RelationAcceptanceStep {
    public static ValidatableResponse 관계를_생성한다_V1(
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

    public static long 관계를_생성하고_ID를_추출한다_V1(
            final long groupId,
            final String name,
            final String phone,
            final String memo,
            final String accessToken
    ) {
        return 관계를_생성한다_V1(groupId, name, phone, memo, accessToken)
                .extract()
                .jsonPath()
                .getLong("result");
    }

    public static ValidatableResponse 관계를_수정한다_V1(
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

    public static ValidatableResponse 관계를_삭제한다_V1(
            final long relationId,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/relations/{relationId}")
                .build(relationId)
                .getPath();

        return deleteRequestWithAccessToken(uri, accessToken);
    }

    public static ValidatableResponse 관계_단건_정보를_조회한다_V1(
            final long relationId,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/relations/me/{relationId}")
                .build(relationId)
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }

    public static ValidatableResponse 관계_N건_정보를_조회한다_V1(
            final String name,
            final String accessToken
    ) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/relations/me?name={name}")
                .build(name)
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }
}
