package ac.dnd.bookkeeping.server.member.application.usecase;

import ac.dnd.bookkeeping.server.auth.application.adapter.TokenStore;
import ac.dnd.bookkeeping.server.global.annotation.UseCase;
import ac.dnd.bookkeeping.server.global.annotation.WritableTransactional;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteAccountUseCase {
    private final MemberRepository memberRepository;
    private final TokenStore tokenStore;

    // TODO Member 이외 다른 도메인이 확장되고 Member간의 결합이 존재할 경우 Event 처리를 통해서 다른 도메인 Repo와의 결합도 낮추기
    @WritableTransactional
    public void invoke(final long memberId) {
        final Member member = memberRepository.getById(memberId);
        tokenStore.deleteRefreshToken(member.getId());
        member.delete();
    }
}
