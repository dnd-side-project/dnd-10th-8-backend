package ac.dnd.bookkeeping.server.member.application.usecase;

import ac.dnd.bookkeeping.server.global.annotation.UseCase;
import ac.dnd.bookkeeping.server.member.application.usecase.command.RegisterMemberCommand;
import ac.dnd.bookkeeping.server.member.domain.model.Nickname;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import ac.dnd.bookkeeping.server.member.exception.MemberException;
import lombok.RequiredArgsConstructor;

import static ac.dnd.bookkeeping.server.member.exception.MemberExceptionCode.DUPLICATE_NICKNAME;

@UseCase
@RequiredArgsConstructor
public class ManageAccountUseCase {
    private final MemberRepository memberRepository;

    public boolean isUniqueNickname(final Nickname nickname) {
        return !memberRepository.existsByNickname(nickname);
    }

    public long register(final RegisterMemberCommand command) {
        if (memberRepository.existsByNickname(command.nickname())) {
            throw new MemberException(DUPLICATE_NICKNAME);
        }

        return memberRepository.save(command.toDomain()).getId();
    }
}
