package ac.dnd.mur.server.member.application.usecase;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.member.application.usecase.query.response.MemberPrivateProfile;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetPrivateProfileUseCase {
    private final MemberRepository memberRepository;

    @MurReadOnlyTransactional
    public MemberPrivateProfile invoke(final long id) {
        return MemberPrivateProfile.of(memberRepository.getById(id));
    }
}
