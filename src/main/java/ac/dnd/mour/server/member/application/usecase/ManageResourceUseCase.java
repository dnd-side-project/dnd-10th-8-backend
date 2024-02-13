package ac.dnd.mour.server.member.application.usecase;

import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.member.domain.model.Nickname;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ManageResourceUseCase {
    private final MemberRepository memberRepository;

    public boolean isUniqueNickname(final Nickname nickname) {
        return !memberRepository.existsByNickname(nickname);
    }
}
