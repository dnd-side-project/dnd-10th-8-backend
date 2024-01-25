package ac.dnd.bookkeeping.server.auth.application.usecase;

import ac.dnd.bookkeeping.server.auth.application.usecase.command.LoginCommand;
import ac.dnd.bookkeeping.server.auth.application.usecase.command.response.LoginResponse;
import ac.dnd.bookkeeping.server.auth.domain.model.AuthToken;
import ac.dnd.bookkeeping.server.auth.domain.service.TokenIssuer;
import ac.dnd.bookkeeping.server.common.UnitTest;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.bookkeeping.server.common.utils.TokenUtils.ACCESS_TOKEN;
import static ac.dnd.bookkeeping.server.common.utils.TokenUtils.REFRESH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
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
    private final LoginCommand command = new LoginCommand(member.getPlatform(), member.getName());

    @Test
    @DisplayName("처음 로그인하는 사용자는 회원가입 프로세스를 진행하고 로그인 처리를 한다")
    void first() {
        // given
        given(memberRepository.findByPlatformSocialId(command.platform().getSocialId())).willReturn(Optional.empty());
        given(memberRepository.save(any())).willReturn(member);

        final AuthToken token = new AuthToken(ACCESS_TOKEN, REFRESH_TOKEN);
        given(tokenIssuer.provideAuthorityToken(member.getId())).willReturn(token);

        // when
        final LoginResponse response = sut.invoke(command);

        // then
        assertAll(
                () -> verify(memberRepository, times(1)).findByPlatformSocialId(command.platform().getSocialId()),
                () -> verify(memberRepository, times(1)).save(any()),
                () -> verify(tokenIssuer, times(1)).provideAuthorityToken(member.getId()),
                () -> assertThat(response.isNew()).isTrue(),
                () -> assertThat(response.info().name()).isEqualTo(member.getName()),
                () -> assertThat(response.token().accessToken()).isEqualTo(token.accessToken()),
                () -> assertThat(response.token().refreshToken()).isEqualTo(token.refreshToken())
        );
    }

    @Test
    @DisplayName("이미 가입된 사용자는 추가적인 회원가입 없이 로그인 처리를 진행한다")
    void alreadyExists() {
        // given
        given(memberRepository.findByPlatformSocialId(command.platform().getSocialId())).willReturn(Optional.of(member));

        final AuthToken token = new AuthToken(ACCESS_TOKEN, REFRESH_TOKEN);
        given(tokenIssuer.provideAuthorityToken(member.getId())).willReturn(token);

        // when
        final LoginResponse response = sut.invoke(command);

        // then
        assertAll(
                () -> verify(memberRepository, times(1)).findByPlatformSocialId(command.platform().getSocialId()),
                () -> verify(memberRepository, times(0)).save(any()),
                () -> verify(tokenIssuer, times(1)).provideAuthorityToken(member.getId()),
                () -> assertThat(response.isNew()).isFalse(),
                () -> assertThat(response.info().name()).isEqualTo(member.getName()),
                () -> assertThat(response.token().accessToken()).isEqualTo(token.accessToken()),
                () -> assertThat(response.token().refreshToken()).isEqualTo(token.refreshToken()),
                () -> assertThat(member.getPlatform().getEmail().getValue()).isEqualTo(command.platform().getEmail().getValue())
        );
    }
}
