package ac.dnd.bookkeeping.server.member.application.usecase;

import ac.dnd.bookkeeping.server.auth.application.adapter.TokenStore;
import ac.dnd.bookkeeping.server.common.UnitTest;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.bookkeeping.server.member.domain.model.Member.Status.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("Member -> DeleteAccountUseCase 테스트")
class DeleteAccountUseCaseTest extends UnitTest {
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final TokenStore tokenStore = mock(TokenStore.class);
    private final DeleteAccountUseCase sut = new DeleteAccountUseCase(memberRepository, tokenStore);

    @Test
    @DisplayName("서비스 탈퇴를 진행한다")
    void delete() {
        // given
        final Member member = MEMBER_1.toDomain().apply(1L);
        given(memberRepository.getById(member.getId())).willReturn(member);

        // when
        sut.invoke(member.getId());

        // then
        assertAll(
                () -> assertThat(member.getId()).isEqualTo(1L),
                () -> assertThat(member.getPlatform()).isNull(),
                () -> assertThat(member.getProfileImageUrl()).isNull(),
                () -> assertThat(member.getNickname()).isNull(),
                () -> assertThat(member.getGender()).isNull(),
                () -> assertThat(member.getBirth()).isNull(),
                () -> assertThat(member.getStatus()).isEqualTo(INACTIVE)
        );
    }
}
