package ac.dnd.bookkeeping.server.auth.application.usecase;

import ac.dnd.bookkeeping.server.auth.application.usecase.command.ReissueTokenCommand;
import ac.dnd.bookkeeping.server.auth.domain.model.AuthToken;
import ac.dnd.bookkeeping.server.auth.domain.service.TokenIssuer;
import ac.dnd.bookkeeping.server.auth.domain.service.TokenProvider;
import ac.dnd.bookkeeping.server.auth.exception.AuthException;
import ac.dnd.bookkeeping.server.common.UnitTest;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ac.dnd.bookkeeping.server.auth.exception.AuthExceptionCode.INVALID_TOKEN;
import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.bookkeeping.server.common.utils.TokenUtils.ACCESS_TOKEN;
import static ac.dnd.bookkeeping.server.common.utils.TokenUtils.REFRESH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Auth -> ReissueTokenUseCase 테스트")
class ReissueTokenUseCaseTest extends UnitTest {
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final TokenProvider tokenProvider = mock(TokenProvider.class);
    private final TokenIssuer tokenIssuer = mock(TokenIssuer.class);
    private final ReissueTokenUseCase sut = new ReissueTokenUseCase(memberRepository, tokenProvider, tokenIssuer);

    private final Member member = MEMBER_1.toDomain().apply(1L);
    private final ReissueTokenCommand command = new ReissueTokenCommand(REFRESH_TOKEN);

    @Test
    @DisplayName("RefreshToken이 유효하지 않으면 토큰 재발급에 실패한다")
    void throwExceptionByInvalidRefreshToken() {
        // given
        given(tokenProvider.getId(command.refreshToken())).willReturn(member.getId());
        given(memberRepository.getById(member.getId())).willReturn(member);
        given(tokenIssuer.isMemberRefreshToken(member.getId(), command.refreshToken())).willReturn(false);

        // when - then
        assertThatThrownBy(() -> sut.invoke(command))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_TOKEN.getMessage());

        assertAll(
                () -> verify(tokenProvider, times(1)).getId(command.refreshToken()),
                () -> verify(memberRepository, times(1)).getById(member.getId()),
                () -> verify(tokenIssuer, times(1)).isMemberRefreshToken(member.getId(), command.refreshToken())
        );
    }

    @Test
    @DisplayName("유효성이 확인된 RefreshToken을 통해서 새로운 AccessToken과 RefreshToken을 재발급받는다")
    void reissueSuccess() {
        // given
        given(tokenProvider.getId(command.refreshToken())).willReturn(member.getId());
        given(memberRepository.getById(member.getId())).willReturn(member);
        given(tokenIssuer.isMemberRefreshToken(member.getId(), command.refreshToken())).willReturn(true);

        final AuthToken authToken = new AuthToken(ACCESS_TOKEN, REFRESH_TOKEN);
        given(tokenIssuer.reissueAuthorityToken(member.getId())).willReturn(authToken);

        // when
        final AuthToken result = sut.invoke(command);

        // then
        assertAll(
                () -> verify(tokenProvider, times(1)).getId(command.refreshToken()),
                () -> verify(memberRepository, times(1)).getById(member.getId()),
                () -> verify(tokenIssuer, times(1)).isMemberRefreshToken(member.getId(), command.refreshToken()),
                () -> assertThat(result.accessToken()).isEqualTo(ACCESS_TOKEN),
                () -> assertThat(result.refreshToken()).isEqualTo(REFRESH_TOKEN)
        );
    }
}
