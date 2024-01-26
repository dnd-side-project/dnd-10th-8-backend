package ac.dnd.bookkeeping.server.member.application.usecase;

import ac.dnd.bookkeeping.server.auth.domain.model.AuthToken;
import ac.dnd.bookkeeping.server.auth.domain.service.TokenIssuer;
import ac.dnd.bookkeeping.server.global.annotation.UseCase;
import ac.dnd.bookkeeping.server.member.application.usecase.command.RegisterMemberCommand;
import ac.dnd.bookkeeping.server.member.application.usecase.command.response.RegisterMemberResponse;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.model.Nickname;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import ac.dnd.bookkeeping.server.member.exception.MemberException;
import lombok.RequiredArgsConstructor;

import static ac.dnd.bookkeeping.server.member.exception.MemberExceptionCode.DUPLICATE_NICKNAME;

@UseCase
@RequiredArgsConstructor
public class ManageAccountUseCase {
    private final MemberRepository memberRepository;
    private final TokenIssuer tokenIssuer;

    public boolean isUniqueNickname(final Nickname nickname) {
        return !memberRepository.existsByNickname(nickname);
    }

    public RegisterMemberResponse register(final RegisterMemberCommand command) {
        if (memberRepository.existsByNickname(command.nickname())) {
            throw new MemberException(DUPLICATE_NICKNAME);
        }

        final Member member = memberRepository.save(command.toDomain());
        final AuthToken token = tokenIssuer.provideAuthorityToken(member.getId());
        return new RegisterMemberResponse(member.getId(), token.accessToken(), token.refreshToken());
    }
}
