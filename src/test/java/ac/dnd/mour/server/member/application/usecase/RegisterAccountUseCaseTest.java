package ac.dnd.mour.server.member.application.usecase;

import ac.dnd.mour.server.auth.domain.model.AuthMember;
import ac.dnd.mour.server.auth.domain.model.AuthToken;
import ac.dnd.mour.server.auth.domain.service.TokenIssuer;
import ac.dnd.mour.server.common.UnitTest;
import ac.dnd.mour.server.group.domain.repository.GroupRepository;
import ac.dnd.mour.server.member.application.usecase.command.RegisterMemberCommand;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import ac.dnd.mour.server.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mour.server.common.utils.TokenUtils.ACCESS_TOKEN;
import static ac.dnd.mour.server.common.utils.TokenUtils.REFRESH_TOKEN;
import static ac.dnd.mour.server.member.exception.MemberExceptionCode.DUPLICATE_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Member -> RegisterAccountUseCase 테스트")
class RegisterAccountUseCaseTest extends UnitTest {
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final GroupRepository groupRepository = mock(GroupRepository.class);
    private final TokenIssuer tokenIssuer = mock(TokenIssuer.class);
    private final RegisterAccountUseCase sut = new RegisterAccountUseCase(
            memberRepository,
            groupRepository,
            tokenIssuer
    );

    private final RegisterMemberCommand command = new RegisterMemberCommand(
            MEMBER_1.getPlatform(),
            MEMBER_1.getProfileImageUrl(),
            MEMBER_1.getName(),
            MEMBER_1.getNickname(),
            MEMBER_1.getGender(),
            MEMBER_1.getBirth()
    );
    private final Member member = command.toDomain().apply(1L);

    @Test
    @DisplayName("닉네임이 중복되면 가입을 진행할 수 없다")
    void throwExceptionByDuplicateNickname() {
        // given
        given(memberRepository.existsByNickname(command.nickname())).willReturn(true);

        // when - then
        assertAll(
                () -> assertThatThrownBy(() -> sut.invoke(command))
                        .isInstanceOf(MemberException.class)
                        .hasMessage(DUPLICATE_NICKNAME.getMessage()),
                () -> verify(memberRepository, times(1)).existsByNickname(command.nickname()),
                () -> verify(memberRepository, times(0)).save(any()),
                () -> verify(groupRepository, times(0)).saveAll(any()),
                () -> verify(tokenIssuer, times(0)).provideAuthorityToken(member.getId())
        );
    }

    @Test
    @DisplayName("회원가입 후 로그인 처리를 진행한다")
    void success() {
        // given
        given(memberRepository.existsByNickname(command.nickname())).willReturn(false);
        given(memberRepository.save(any())).willReturn(member);

        final AuthToken token = new AuthToken(ACCESS_TOKEN, REFRESH_TOKEN);
        given(tokenIssuer.provideAuthorityToken(member.getId())).willReturn(token);

        // when
        final AuthMember result = sut.invoke(command);

        // then
        assertAll(
                () -> verify(memberRepository, times(1)).existsByNickname(command.nickname()),
                () -> verify(memberRepository, times(1)).save(any()),
                () -> verify(groupRepository, times(1)).saveAll(any()),
                () -> verify(tokenIssuer, times(1)).provideAuthorityToken(member.getId()),
                () -> assertThat(result.id()).isEqualTo(member.getId()),
                () -> assertThat(result.accessToken()).isEqualTo(token.accessToken()),
                () -> assertThat(result.refreshToken()).isEqualTo(token.refreshToken())
        );
    }
}
