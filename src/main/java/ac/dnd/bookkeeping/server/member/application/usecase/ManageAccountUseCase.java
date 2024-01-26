package ac.dnd.bookkeeping.server.member.application.usecase;

import ac.dnd.bookkeeping.server.global.annotation.UseCase;
import ac.dnd.bookkeeping.server.member.domain.model.Nickname;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ManageAccountUseCase {
    private final MemberRepository memberRepository;

    public boolean isUniqueNickname(final Nickname nickname) {
        return !memberRepository.existsByNickname(nickname);
    }
}
