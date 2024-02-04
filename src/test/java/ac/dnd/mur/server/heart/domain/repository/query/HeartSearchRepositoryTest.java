package ac.dnd.mur.server.heart.domain.repository.query;

import ac.dnd.mur.server.common.RepositoryTest;
import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.heart.domain.repository.HeartRepository;
import ac.dnd.mur.server.heart.domain.repository.query.response.HeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.spec.SearchHeartCondition;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import ac.dnd.mur.server.relation.domain.model.Relation;
import ac.dnd.mur.server.relation.domain.repository.RelationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.heart.domain.repository.query.spec.SearchHeartCondition.Sort.INTIMACY;
import static ac.dnd.mur.server.heart.domain.repository.query.spec.SearchHeartCondition.Sort.RECENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Import(HeartSearchRepositoryImpl.class)
@DisplayName("Heart -> HeartSearchRepository 테스트")
class HeartSearchRepositoryTest extends RepositoryTest {
    @Autowired
    private HeartSearchRepositoryImpl sut;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private HeartRepository heartRepository;

    private static final LocalDate day = LocalDate.of(2024, 1, 1);

    private Member member;
    private final Group[] groups = new Group[3];
    private Relation[] relations = new Relation[5];

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MEMBER_1.toDomain());

        groups[0] = groupRepository.save(Group.of(member, "친구0"));
        groups[1] = groupRepository.save(Group.of(member, "친구1"));
        groups[2] = groupRepository.save(Group.of(member, "친구2"));
        relations = relationRepository.saveAll(List.of(
                new Relation(member, groups[0], "친구-0", null, null),
                new Relation(member, groups[0], "친구-0", null, null),
                new Relation(member, groups[0], "친구-0", null, null),
                new Relation(member, groups[1], "친구-1", null, null),
                new Relation(member, groups[2], "친구-2", null, null)
        )).toArray(Relation[]::new);

        heartRepository.saveAll(List.of(
                /**
                 * member <-> relations[0]
                 * >> give = 6_000_000
                 * >> take = 1_500_000
                 * >> intimacy = 7_500_000
                 */
                new Heart(member, relations[0], true, 1_000_000, day, "행사..", null, List.of()),
                new Heart(member, relations[0], true, 2_000_000, day, "행사..", null, List.of()),
                new Heart(member, relations[0], true, 3_000_000, day, "행사..", null, List.of()),
                new Heart(member, relations[0], false, 1_500_000, day, "행사..", null, List.of()),

                /**
                 * member <-> relations[1]
                 * >> give = 9_000_000
                 * >> take = 0
                 * >> intimacy = 9_000_000
                 */
                new Heart(member, relations[1], true, 2_000_000, day, "행사..", null, List.of()),
                new Heart(member, relations[1], true, 7_000_000, day, "행사..", null, List.of()),

                /**
                 * member <-> relations[2]
                 * >> give = 1_000_000
                 * >> take = 2_150_000
                 * >> intimacy = 3_150_000
                 */
                new Heart(member, relations[2], true, 1_000_000, day, "행사..", null, List.of()),
                new Heart(member, relations[2], false, 500_000, day, "행사..", null, List.of()),
                new Heart(member, relations[2], false, 1_050_000, day, "행사..", null, List.of()),
                new Heart(member, relations[2], false, 250_000, day, "행사..", null, List.of()),
                new Heart(member, relations[2], false, 350_000, day, "행사..", null, List.of()),

                /**
                 * member <-> relations[3]
                 * >> give = 0
                 * >> take = 35_000_000
                 * >> intimacy = 35_000_000
                 */
                new Heart(member, relations[3], false, 5_000_000, day, "행사..", null, List.of()),
                new Heart(member, relations[3], false, 10_000_000, day, "행사..", null, List.of()),
                new Heart(member, relations[3], false, 20_000_000, day, "행사..", null, List.of()),

                /**
                 * member <-> relations[4]
                 * >> give = 650_000
                 * >> take = 150_000
                 * >> intimacy = 800_000
                 */
                new Heart(member, relations[4], true, 50_000, day, "행사..", null, List.of()),
                new Heart(member, relations[4], false, 100_000, day, "행사..", null, List.of()),
                new Heart(member, relations[4], true, 200_000, day, "행사..", null, List.of()),
                new Heart(member, relations[4], true, 300_000, day, "행사..", null, List.of()),
                new Heart(member, relations[4], true, 100_000, day, "행사..", null, List.of()),
                new Heart(member, relations[4], false, 50_000, day, "행사..", null, List.of())
        ));
    }

    @Test
    @DisplayName("최신순으로 주고받은 마음을 조회한다")
    void fetchWithRecent() {
        /* 이름 조건 X */
        final List<HeartHistory> result1 = sut.fetchHeartsByCondition(new SearchHeartCondition(member.getId(), RECENT, null));
        assertHeartHistoriesMatch(
                result1,
                List.of(relations[4], relations[3], relations[2], relations[1], relations[0]),
                List.of(groups[2], groups[1], groups[0], groups[0], groups[0]),
                List.of(650_000L, 0L, 1_000_000L, 9_000_000L, 6_000_000L),
                List.of(150_000L, 35_000_000L, 2_150_000L, 0L, 1_500_000L)
        );

        /* 이름 조건 O */
        final List<HeartHistory> result2 = sut.fetchHeartsByCondition(new SearchHeartCondition(member.getId(), RECENT, "친구-0"));
        assertHeartHistoriesMatch(
                result2,
                List.of(relations[2], relations[1], relations[0]),
                List.of(groups[0], groups[0], groups[0]),
                List.of(1_000_000L, 9_000_000L, 6_000_000L),
                List.of(2_150_000L, 0L, 1_500_000L)
        );

        final List<HeartHistory> result3 = sut.fetchHeartsByCondition(new SearchHeartCondition(member.getId(), RECENT, "친구-1"));
        assertHeartHistoriesMatch(
                result3,
                List.of(relations[3]),
                List.of(groups[1]),
                List.of(0L),
                List.of(35_000_000L)
        );

        final List<HeartHistory> result4 = sut.fetchHeartsByCondition(new SearchHeartCondition(member.getId(), RECENT, "친구-2"));
        assertHeartHistoriesMatch(
                result4,
                List.of(relations[4]),
                List.of(groups[2]),
                List.of(650_000L),
                List.of(150_000L)
        );

        final List<HeartHistory> result5 = sut.fetchHeartsByCondition(new SearchHeartCondition(member.getId(), RECENT, "친구-3"));
        assertHeartHistoriesMatch(result5, List.of(), List.of(), List.of(), List.of());
    }

    @Test
    @DisplayName("친밀도순으로 주고받은 마음을 조회한다")
    void fetchWithIntimacy() {
        /* 이름 조건 X */
        final List<HeartHistory> result1 = sut.fetchHeartsByCondition(new SearchHeartCondition(member.getId(), INTIMACY, null));
        assertHeartHistoriesMatch(
                result1,
                List.of(relations[3], relations[1], relations[0], relations[2], relations[4]),
                List.of(groups[1], groups[0], groups[0], groups[0], groups[2]),
                List.of(0L, 9_000_000L, 6_000_000L, 1_000_000L, 650_000L),
                List.of(35_000_000L, 0L, 1_500_000L, 2_150_000L, 150_000L)
        );

        /* 이름 조건 O */
        final List<HeartHistory> result2 = sut.fetchHeartsByCondition(new SearchHeartCondition(member.getId(), INTIMACY, "친구-0"));
        assertHeartHistoriesMatch(
                result2,
                List.of(relations[1], relations[0], relations[2]),
                List.of(groups[0], groups[0], groups[0]),
                List.of(9_000_000L, 6_000_000L, 1_000_000L),
                List.of(0L, 1_500_000L, 2_150_000L)
        );

        final List<HeartHistory> result3 = sut.fetchHeartsByCondition(new SearchHeartCondition(member.getId(), INTIMACY, "친구-1"));
        assertHeartHistoriesMatch(
                result3,
                List.of(relations[3]),
                List.of(groups[1]),
                List.of(0L),
                List.of(35_000_000L)
        );

        final List<HeartHistory> result4 = sut.fetchHeartsByCondition(new SearchHeartCondition(member.getId(), INTIMACY, "친구-2"));
        assertHeartHistoriesMatch(
                result4,
                List.of(relations[4]),
                List.of(groups[2]),
                List.of(650_000L),
                List.of(150_000L)
        );

        final List<HeartHistory> result5 = sut.fetchHeartsByCondition(new SearchHeartCondition(member.getId(), INTIMACY, "친구-3"));
        assertHeartHistoriesMatch(result5, List.of(), List.of(), List.of(), List.of());
    }

    private void assertHeartHistoriesMatch(
            final List<HeartHistory> result,
            final List<Relation> relations,
            final List<Group> groups,
            final List<Long> giveMoney,
            final List<Long> takeMoney
    ) {
        assertAll(
                () -> assertThat(result).hasSize(relations.size()),
                () -> assertThat(result)
                        .map(HeartHistory::relationId)
                        .containsExactlyElementsOf(relations.stream().map(Relation::getId).toList()),
                () -> assertThat(result)
                        .map(HeartHistory::relationName)
                        .containsExactlyElementsOf(relations.stream().map(Relation::getName).toList()),
                () -> assertThat(result)
                        .map(HeartHistory::groupid)
                        .containsExactlyElementsOf(groups.stream().map(Group::getId).toList()),
                () -> assertThat(result)
                        .map(HeartHistory::groupName)
                        .containsExactlyElementsOf(groups.stream().map(Group::getName).toList()),
                () -> assertThat(result)
                        .map(HeartHistory::giveMoney)
                        .containsExactlyElementsOf(giveMoney),
                () -> assertThat(result)
                        .map(HeartHistory::takeMoney)
                        .containsExactlyElementsOf(takeMoney)
        );
    }
}
