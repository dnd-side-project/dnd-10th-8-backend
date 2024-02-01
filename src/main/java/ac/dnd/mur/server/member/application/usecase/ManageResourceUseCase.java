package ac.dnd.mur.server.member.application.usecase;

import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.member.domain.model.Nickname;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ManageResourceUseCase {
    private final MemberRepository memberRepository;

    public boolean isUniqueNickname(final Nickname nickname) {
        return !memberRepository.existsByNickname(nickname);
    }
}
