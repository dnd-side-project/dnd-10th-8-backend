package ac.dnd.mur.server.member.application.usecase;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.auth.domain.model.AuthToken;
import ac.dnd.mur.server.auth.domain.service.TokenIssuer;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.member.application.usecase.command.RegisterMemberCommand;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.model.Nickname;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import ac.dnd.mur.server.member.exception.MemberException;
import lombok.RequiredArgsConstructor;

import static ac.dnd.mur.server.member.exception.MemberExceptionCode.DUPLICATE_NICKNAME;

@UseCase
@RequiredArgsConstructor
public class RegisterAccountUseCase {
    private final MemberRepository memberRepository;
    private final TokenIssuer tokenIssuer;

    public boolean isUniqueNickname(final Nickname nickname) {
        return !memberRepository.existsByNickname(nickname);
    }

    public AuthMember register(final RegisterMemberCommand command) {
        if (memberRepository.existsByNickname(command.nickname())) {
            throw new MemberException(DUPLICATE_NICKNAME);
        }

        final Member member = memberRepository.save(command.toDomain());
        final AuthToken token = tokenIssuer.provideAuthorityToken(member.getId());
        return AuthMember.of(member, token);
    }
}
