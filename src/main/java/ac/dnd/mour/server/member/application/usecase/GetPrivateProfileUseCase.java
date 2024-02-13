package ac.dnd.mour.server.member.application.usecase;

import ac.dnd.mour.server.global.annotation.MourReadOnlyTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.member.application.usecase.query.response.MemberPrivateProfile;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetPrivateProfileUseCase {
    private final MemberRepository memberRepository;

    @MourReadOnlyTransactional
    public MemberPrivateProfile invoke(final long id) {
        return MemberPrivateProfile.of(memberRepository.getById(id));
    }
}
