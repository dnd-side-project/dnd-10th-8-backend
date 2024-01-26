package ac.dnd.bookkeeping.server.auth.application.usecase;

import ac.dnd.bookkeeping.server.auth.application.usecase.command.LoginCommand;
import ac.dnd.bookkeeping.server.auth.domain.model.AuthMember;
import ac.dnd.bookkeeping.server.auth.domain.model.AuthToken;
import ac.dnd.bookkeeping.server.auth.domain.service.TokenIssuer;
import ac.dnd.bookkeeping.server.common.UnitTest;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import ac.dnd.bookkeeping.server.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.bookkeeping.server.common.utils.TokenUtils.ACCESS_TOKEN;
import static ac.dnd.bookkeeping.server.common.utils.TokenUtils.REFRESH_TOKEN;
import static ac.dnd.bookkeeping.server.member.exception.MemberExceptionCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Member -> LoginUseCase 테스트")
class LoginUseCaseTest extends UnitTest {
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final TokenIssuer tokenIssuer = mock(TokenIssuer.class);
    private final LoginUseCase sut = new LoginUseCase(memberRepository, tokenIssuer);

    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Test
    @DisplayName("DB에 존재하지 않는 사용자라면 예외가 발생하고 회원가입 플로우로 이동한다")
    void throwExceptionByMemberNotFound() {
        // given
        final LoginCommand command = new LoginCommand(MEMBER_1.getPlatform().getSocialId(), MEMBER_1.getPlatform().getEmail());
        given(memberRepository.findByPlatformSocialId(command.socialId())).willReturn(Optional.empty());

        // when - then
        assertAll(
                () -> assertThatThrownBy(() -> sut.invoke(command))
                        .isInstanceOf(MemberException.class)
                        .hasMessage(MEMBER_NOT_FOUND.getMessage()),
                () -> verify(memberRepository, times(1)).findByPlatformSocialId(command.socialId()),
                () -> verify(tokenIssuer, times(0)).provideAuthorityToken(member.getId())
        );
    }

    @Test
    @DisplayName("DB에 존재하는 사용자면 로그인 처리를 진행하고 토큰을 발급한다")
    void success() {
        // given
        final LoginCommand command = new LoginCommand(MEMBER_1.getPlatform().getSocialId(), MEMBER_1.getPlatform().getEmail());
        given(memberRepository.findByPlatformSocialId(command.socialId())).willReturn(Optional.of(member));

        final AuthToken token = new AuthToken(ACCESS_TOKEN, REFRESH_TOKEN);
        given(tokenIssuer.provideAuthorityToken(member.getId())).willReturn(token);

        // when
        final AuthMember response = sut.invoke(command);

        // then
        assertAll(
                () -> verify(memberRepository, times(1)).findByPlatformSocialId(command.socialId()),
                () -> verify(tokenIssuer, times(1)).provideAuthorityToken(member.getId()),
                () -> assertThat(response.id()).isEqualTo(member.getId()),
                () -> assertThat(response.accessToken()).isEqualTo(token.accessToken()),
                () -> assertThat(response.refreshToken()).isEqualTo(token.refreshToken())
        );
    }
}
