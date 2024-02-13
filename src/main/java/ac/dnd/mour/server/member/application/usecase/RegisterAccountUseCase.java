package ac.dnd.mour.server.member.application.usecase;

import ac.dnd.mour.server.auth.domain.model.AuthMember;
import ac.dnd.mour.server.auth.domain.model.AuthToken;
import ac.dnd.mour.server.auth.domain.service.TokenIssuer;
import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.group.domain.model.Group;
import ac.dnd.mour.server.group.domain.repository.GroupRepository;
import ac.dnd.mour.server.member.application.usecase.command.RegisterMemberCommand;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import ac.dnd.mour.server.member.exception.MemberException;
import lombok.RequiredArgsConstructor;

import static ac.dnd.mour.server.member.exception.MemberExceptionCode.DUPLICATE_NICKNAME;

@UseCase
@RequiredArgsConstructor
public class RegisterAccountUseCase {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final TokenIssuer tokenIssuer;

    @MourWritableTransactional
    public AuthMember invoke(final RegisterMemberCommand command) {
        if (memberRepository.existsByNickname(command.nickname())) {
            throw new MemberException(DUPLICATE_NICKNAME);
        }

        final Member member = memberRepository.save(command.toDomain());
        groupRepository.saveAll(Group.init(member));

        final AuthToken token = tokenIssuer.provideAuthorityToken(member.getId());
        return AuthMember.of(member, token);
    }
}
