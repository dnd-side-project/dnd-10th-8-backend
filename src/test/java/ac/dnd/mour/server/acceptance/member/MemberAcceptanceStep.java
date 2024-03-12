package ac.dnd.mour.server.acceptance.member;

import ac.dnd.mour.server.common.fixture.MemberFixture;
import ac.dnd.mour.server.member.domain.model.Gender;
import ac.dnd.mour.server.member.presentation.v1.request.RegisterMemberRequest;
import ac.dnd.mour.server.member.presentation.v1.request.UpdateMemberRequest;
import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

import static ac.dnd.mour.server.acceptance.CommonRequestFixture.deleteRequestWithAccessToken;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.getRequest;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.getRequestWithAccessToken;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.patchRequestWithAccessToken;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.postRequest;

public class MemberAcceptanceStep {
    public static ValidatableResponse 닉네임_중복_체크를_진행한다_V1(final String nickname) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/members/check-nickname?nickname={nickname}")
                .build(nickname)
                .getPath();

        return getRequest(uri);
    }

    public static ValidatableResponse 회원가입을_진행한다_V1(final MemberFixture fixture) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/members")
                .build()
                .toUri()
                .getPath();

        final RegisterMemberRequest request = new RegisterMemberRequest(
                fixture.getPlatform().getSocialId(),
                fixture.getPlatform().getEmail().getValue(),
                fixture.getProfileImageUrl(),
                fixture.getName(),
                fixture.getNickname().getValue(),
                fixture.getGender().getValue(),
                fixture.getBirth()
        );

        return postRequest(uri, request);
    }

    public static ValidatableResponse 회원가입을_진행한다_V1(final MemberFixture fixture, final Gender gender, final LocalDate birth) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/members")
                .build()
                .toUri()
                .getPath();

        final RegisterMemberRequest request = new RegisterMemberRequest(
                fixture.getPlatform().getSocialId(),
                fixture.getPlatform().getEmail().getValue(),
                fixture.getProfileImageUrl(),
                fixture.getName(),
                fixture.getNickname().getValue(),
                gender.getValue(),
                birth
        );

        return postRequest(uri, request);
    }

    public static ValidatableResponse 회원가입을_진행한다_V1(final RegisterMemberRequest request) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/members")
                .build()
                .toUri()
                .getPath();

        return postRequest(uri, request);
    }

    public static ValidatableResponse 내_정보를_수정한다_V1(final MemberFixture fixture, final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/members/me")
                .build()
                .toUri()
                .getPath();

        final UpdateMemberRequest request = new UpdateMemberRequest(
                fixture.getProfileImageUrl(),
                fixture.getNickname().getValue(),
                fixture.getGender().getValue(),
                fixture.getBirth()
        );

        return patchRequestWithAccessToken(uri, request, accessToken);
    }

    public static ValidatableResponse 내_정보를_수정한다_V1(final UpdateMemberRequest request, final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/members/me")
                .build()
                .toUri()
                .getPath();

        return patchRequestWithAccessToken(uri, request, accessToken);
    }

    public static ValidatableResponse 서비스_탈퇴를_진행한다_V1(final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/members/me")
                .build()
                .toUri()
                .getPath();

        return deleteRequestWithAccessToken(uri, accessToken);
    }

    public static ValidatableResponse 마이페이지_내_정보를_조회한다_V1(final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/members/me")
                .build()
                .toUri()
                .getPath();

        return getRequestWithAccessToken(uri, accessToken);
    }
}
