package ac.dnd.mour.server.heart.domain.repository;

import ac.dnd.mour.server.common.RepositoryTest;
import ac.dnd.mour.server.group.domain.model.Group;
import ac.dnd.mour.server.group.domain.repository.GroupRepository;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import ac.dnd.mour.server.relation.domain.model.Relation;
import ac.dnd.mour.server.relation.domain.repository.RelationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ac.dnd.mour.server.common.fixture.GroupFixture.친구;
import static ac.dnd.mour.server.common.fixture.HeartFixture.결혼_축의금을_받았다;
import static ac.dnd.mour.server.common.fixture.HeartFixture.결혼_축의금을_보냈다;
import static ac.dnd.mour.server.common.fixture.HeartFixture.생일_선물을_받았다;
import static ac.dnd.mour.server.common.fixture.HeartFixture.생일_선물을_보냈다;
import static ac.dnd.mour.server.common.fixture.HeartFixture.승진_선물을_보냈다;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Heart -> HeartRepository 테스트")
class HeartRepositoryTest extends RepositoryTest {
    @Autowired
    private HeartRepository sut;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private RelationRepository relationRepository;

    private Member member;
    private Relation relation;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MEMBER_1.toDomain());
        final Group group = groupRepository.save(친구.toDomain(member));
        relation = relationRepository.save(친구_1.toDomain(member, group));
    }

    @Test
    @DisplayName("주고 받은 마음에 대한 금액을 조회한다")
    void fetchInteractionMoney() {
        // none
        assertAll(
                () -> assertThat(sut.fetchGivenMoney(member.getId(), relation.getId())).isEqualTo(0),
                () -> assertThat(sut.fetchTakenMoney(member.getId(), relation.getId())).isEqualTo(0)
        );

        // give 3
        sut.save(생일_선물을_보냈다.toDomain(member, relation));
        sut.save(결혼_축의금을_보냈다.toDomain(member, relation));
        sut.save(승진_선물을_보냈다.toDomain(member, relation));

        assertAll(
                () -> assertThat(sut.fetchGivenMoney(member.getId(), relation.getId())).isEqualTo(
                        생일_선물을_보냈다.getMoney()
                                + 결혼_축의금을_보냈다.getMoney()
                                + 승진_선물을_보냈다.getMoney()
                ),
                () -> assertThat(sut.fetchTakenMoney(member.getId(), relation.getId())).isEqualTo(0)
        );

        // take 2
        sut.save(생일_선물을_받았다.toDomain(member, relation));
        sut.save(결혼_축의금을_받았다.toDomain(member, relation));

        assertAll(
                () -> assertThat(sut.fetchGivenMoney(member.getId(), relation.getId())).isEqualTo(
                        생일_선물을_보냈다.getMoney()
                                + 결혼_축의금을_보냈다.getMoney()
                                + 승진_선물을_보냈다.getMoney()
                ),
                () -> assertThat(sut.fetchTakenMoney(member.getId(), relation.getId())).isEqualTo(
                        생일_선물을_받았다.getMoney()
                                + 결혼_축의금을_받았다.getMoney()
                )
        );
    }
}
