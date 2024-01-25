package ac.dnd.bookkeeping.server.member.application.usecase;

import ac.dnd.bookkeeping.server.common.UnitTest;
import ac.dnd.bookkeeping.server.member.application.usecase.command.CompleteInfoCommand;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("Member -> CompleteInfoUseCase 테스트")
class CompleteInfoUseCaseTest extends UnitTest {
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final CompleteInfoUseCase sut = new CompleteInfoUseCase(memberRepository);

    @Test
    @DisplayName("온보딩 과정에서 사용자 추가 정보를 기입한다")
    void success() {
        // given
        final Member member = new Member(MEMBER_1.getPlatform(), MEMBER_1.getName(), null, null).apply(1L);

        final CompleteInfoCommand command = new CompleteInfoCommand(
                member.getId(),
                MEMBER_2.getName(),
                MEMBER_1.getGender(),
                MEMBER_1.getBirth()
        );
        given(memberRepository.getById(command.memberId())).willReturn(member);

        // when
        sut.invoke(command);

        // then
        assertAll(
                () -> assertThat(member.getName()).isEqualTo(command.name()),
                () -> assertThat(member.getGender()).isEqualTo(command.gender()),
                () -> assertThat(member.getBirth()).isEqualTo(command.birth())
        );
    }
}
