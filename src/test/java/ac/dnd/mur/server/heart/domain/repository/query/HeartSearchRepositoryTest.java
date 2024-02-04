package ac.dnd.mur.server.heart.domain.repository.query;

import ac.dnd.mur.server.common.RepositoryTest;
import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.heart.domain.repository.HeartRepository;
import ac.dnd.mur.server.heart.domain.repository.query.response.HeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.spec.SearchHeartCondition;
import ac.dnd.mur.server.heart.domain.repository.query.spec.SearchSpecificRelationHeartCondition;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import ac.dnd.mur.server.relation.domain.model.Relation;
import ac.dnd.mur.server.relation.domain.repository.RelationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
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
    private Heart[] hearts = new Heart[20];

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

        hearts = heartRepository.saveAll(List.of(
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
        )).toArray(Heart[]::new);
    }

    @Nested
    @DisplayName("메인 홈 마음 내역 조회")
    class FetchHeartsByCondition {
        @Test
        @DisplayName("최신순으로 주고받은 마음을 조회한다")
        void fetchWithRecent() {
            /* 이름 조건 X */
            final List<HeartHistory> result1 = sut.fetchHeartsByCondition(new SearchHeartCondition(
                    member.getId(),
                    SearchHeartCondition.Sort.RECENT,
                    null
            ));
            assertHeartHistoriesMatch(
                    result1,
                    List.of(relations[4], relations[3], relations[2], relations[1], relations[0]),
                    List.of(groups[2], groups[1], groups[0], groups[0], groups[0]),
                    List.of(
                            List.of(50_000L, 200_000L, 300_000L, 100_000L),
                            List.of(),
                            List.of(1_000_000L),
                            List.of(2_000_000L, 7_000_000L),
                            List.of(1_000_000L, 2_000_000L, 3_000_000L)
                    ),
                    List.of(
                            List.of(100_000L, 50_000L),
                            List.of(5_000_000L, 10_000_000L, 20_000_000L),
                            List.of(500_000L, 1_050_000L, 250_000L, 350_000L),
                            List.of(),
                            List.of(1_500_000L)
                    )
            );

            /* 이름 조건 O */
            final List<HeartHistory> result2 = sut.fetchHeartsByCondition(new SearchHeartCondition(
                    member.getId(),
                    SearchHeartCondition.Sort.RECENT,
                    "친구-0"
            ));
            assertHeartHistoriesMatch(
                    result2,
                    List.of(relations[2], relations[1], relations[0]),
                    List.of(groups[0], groups[0], groups[0]),
                    List.of(
                            List.of(1_000_000L),
                            List.of(2_000_000L, 7_000_000L),
                            List.of(1_000_000L, 2_000_000L, 3_000_000L)
                    ),
                    List.of(
                            List.of(500_000L, 1_050_000L, 250_000L, 350_000L),
                            List.of(),
                            List.of(1_500_000L)
                    )
            );

            final List<HeartHistory> result3 = sut.fetchHeartsByCondition(new SearchHeartCondition(
                    member.getId(),
                    SearchHeartCondition.Sort.RECENT,
                    "친구-1"
            ));
            assertHeartHistoriesMatch(
                    result3,
                    List.of(relations[3]),
                    List.of(groups[1]),
                    List.of(List.of()),
                    List.of(List.of(5_000_000L, 10_000_000L, 20_000_000L))
            );

            final List<HeartHistory> result4 = sut.fetchHeartsByCondition(new SearchHeartCondition(
                    member.getId(),
                    SearchHeartCondition.Sort.RECENT,
                    "친구-2"
            ));
            assertHeartHistoriesMatch(
                    result4,
                    List.of(relations[4]),
                    List.of(groups[2]),
                    List.of(List.of(50_000L, 200_000L, 300_000L, 100_000L)),
                    List.of(List.of(100_000L, 50_000L))
            );

            final List<HeartHistory> result5 = sut.fetchHeartsByCondition(new SearchHeartCondition(
                    member.getId(),
                    SearchHeartCondition.Sort.RECENT,
                    "친구-3"
            ));
            assertHeartHistoriesMatch(result5, List.of(), List.of(), List.of(), List.of());
        }

        @Test
        @DisplayName("친밀도순으로 주고받은 마음을 조회한다")
        void fetchWithIntimacy() {
            /* 이름 조건 X */
            final List<HeartHistory> result1 = sut.fetchHeartsByCondition(new SearchHeartCondition(
                    member.getId(),
                    SearchHeartCondition.Sort.INTIMACY,
                    null
            ));
            assertHeartHistoriesMatch(
                    result1,
                    List.of(relations[3], relations[1], relations[0], relations[2], relations[4]),
                    List.of(groups[1], groups[0], groups[0], groups[0], groups[2]),
                    List.of(
                            List.of(),
                            List.of(2_000_000L, 7_000_000L),
                            List.of(1_000_000L, 2_000_000L, 3_000_000L),
                            List.of(1_000_000L),
                            List.of(50_000L, 200_000L, 300_000L, 100_000L)
                    ),
                    List.of(
                            List.of(5_000_000L, 10_000_000L, 20_000_000L),
                            List.of(),
                            List.of(1_500_000L),
                            List.of(500_000L, 1_050_000L, 250_000L, 350_000L),
                            List.of(100_000L, 50_000L)
                    )
            );

            /* 이름 조건 O */
            final List<HeartHistory> result2 = sut.fetchHeartsByCondition(new SearchHeartCondition(
                    member.getId(),
                    SearchHeartCondition.Sort.INTIMACY,
                    "친구-0"
            ));
            assertHeartHistoriesMatch(
                    result2,
                    List.of(relations[1], relations[0], relations[2]),
                    List.of(groups[0], groups[0], groups[0]),
                    List.of(
                            List.of(2_000_000L, 7_000_000L),
                            List.of(1_000_000L, 2_000_000L, 3_000_000L),
                            List.of(1_000_000L)
                    ),
                    List.of(
                            List.of(),
                            List.of(1_500_000L),
                            List.of(500_000L, 1_050_000L, 250_000L, 350_000L)
                    )
            );

            final List<HeartHistory> result3 = sut.fetchHeartsByCondition(new SearchHeartCondition(
                    member.getId(),
                    SearchHeartCondition.Sort.INTIMACY,
                    "친구-1"
            ));
            assertHeartHistoriesMatch(
                    result3,
                    List.of(relations[3]),
                    List.of(groups[1]),
                    List.of(List.of()),
                    List.of(List.of(5_000_000L, 10_000_000L, 20_000_000L))
            );

            final List<HeartHistory> result4 = sut.fetchHeartsByCondition(new SearchHeartCondition(
                    member.getId(),
                    SearchHeartCondition.Sort.INTIMACY,
                    "친구-2"
            ));
            assertHeartHistoriesMatch(
                    result4,
                    List.of(relations[4]),
                    List.of(groups[2]),
                    List.of(List.of(50_000L, 200_000L, 300_000L, 100_000L)),
                    List.of(List.of(100_000L, 50_000L))
            );

            final List<HeartHistory> result5 = sut.fetchHeartsByCondition(new SearchHeartCondition(
                    member.getId(),
                    SearchHeartCondition.Sort.INTIMACY,
                    "친구-3"
            ));
            assertHeartHistoriesMatch(result5, List.of(), List.of(), List.of(), List.of());
        }

        private void assertHeartHistoriesMatch(
                final List<HeartHistory> result,
                final List<Relation> relations,
                final List<Group> groups,
                final List<List<Long>> giveHistories,
                final List<List<Long>> takeHistories
        ) {
            final int totalSize = relations.size();
            assertThat(result).hasSize(totalSize);

            for (int i = 0; i < totalSize; i++) {
                final HeartHistory heartHistory = result.get(i);
                final Relation relation = relations.get(i);
                final Group group = groups.get(i);
                final List<Long> giveHistory = giveHistories.get(i);
                final List<Long> takeHistory = takeHistories.get(i);

                assertAll(
                        () -> assertThat(heartHistory.relationId()).isEqualTo(relation.getId()),
                        () -> assertThat(heartHistory.relationName()).isEqualTo(relation.getName()),
                        () -> assertThat(heartHistory.groupid()).isEqualTo(group.getId()),
                        () -> assertThat(heartHistory.groupName()).isEqualTo(group.getName()),
                        () -> assertThat(heartHistory.giveHistories()).containsExactlyInAnyOrderElementsOf(giveHistory),
                        () -> assertThat(heartHistory.takeHistories()).containsExactlyInAnyOrderElementsOf(takeHistory)
                );
            }
        }
    }

    @Nested
    @DisplayName("특정 관계간에 주고받은 마음 내역")
    class FetchHeartsWithSpecificRelation {
        @Test
        @DisplayName("최신순으로 특정 관계간에 주고받은 마음 내역을 조회한다")
        void recent() {
            final List<Heart> result1 = sut.fetchHeartsWithSpecificRelation(new SearchSpecificRelationHeartCondition(
                    member.getId(),
                    relations[0].getId(),
                    SearchSpecificRelationHeartCondition.Sort.RECENT
            ));
            assertThat(result1).containsExactly(hearts[3], hearts[2], hearts[1], hearts[0]);

            final List<Heart> result2 = sut.fetchHeartsWithSpecificRelation(new SearchSpecificRelationHeartCondition(
                    member.getId(),
                    relations[1].getId(),
                    SearchSpecificRelationHeartCondition.Sort.RECENT
            ));
            assertThat(result2).containsExactly(hearts[5], hearts[4]);

            final List<Heart> result3 = sut.fetchHeartsWithSpecificRelation(new SearchSpecificRelationHeartCondition(
                    member.getId(),
                    relations[2].getId(),
                    SearchSpecificRelationHeartCondition.Sort.RECENT
            ));
            assertThat(result3).containsExactly(hearts[10], hearts[9], hearts[8], hearts[7], hearts[6]);

            final List<Heart> result4 = sut.fetchHeartsWithSpecificRelation(new SearchSpecificRelationHeartCondition(
                    member.getId(),
                    relations[3].getId(),
                    SearchSpecificRelationHeartCondition.Sort.RECENT
            ));
            assertThat(result4).containsExactly(hearts[13], hearts[12], hearts[11]);

            final List<Heart> result5 = sut.fetchHeartsWithSpecificRelation(new SearchSpecificRelationHeartCondition(
                    member.getId(),
                    relations[4].getId(),
                    SearchSpecificRelationHeartCondition.Sort.RECENT
            ));
            assertThat(result5).containsExactly(hearts[19], hearts[18], hearts[17], hearts[16], hearts[15], hearts[14]);
        }

        @Test
        @DisplayName("과거순으로 특정 관계간에 주고받은 마음 내역을 조회한다")
        void old() {
            final List<Heart> result1 = sut.fetchHeartsWithSpecificRelation(new SearchSpecificRelationHeartCondition(
                    member.getId(),
                    relations[0].getId(),
                    SearchSpecificRelationHeartCondition.Sort.OLD
            ));
            assertThat(result1).containsExactly(hearts[0], hearts[1], hearts[2], hearts[3]);

            final List<Heart> result2 = sut.fetchHeartsWithSpecificRelation(new SearchSpecificRelationHeartCondition(
                    member.getId(),
                    relations[1].getId(),
                    SearchSpecificRelationHeartCondition.Sort.OLD
            ));
            assertThat(result2).containsExactly(hearts[4], hearts[5]);

            final List<Heart> result3 = sut.fetchHeartsWithSpecificRelation(new SearchSpecificRelationHeartCondition(
                    member.getId(),
                    relations[2].getId(),
                    SearchSpecificRelationHeartCondition.Sort.OLD
            ));
            assertThat(result3).containsExactly(hearts[6], hearts[7], hearts[8], hearts[9], hearts[10]);

            final List<Heart> result4 = sut.fetchHeartsWithSpecificRelation(new SearchSpecificRelationHeartCondition(
                    member.getId(),
                    relations[3].getId(),
                    SearchSpecificRelationHeartCondition.Sort.OLD
            ));
            assertThat(result4).containsExactly(hearts[11], hearts[12], hearts[13]);

            final List<Heart> result5 = sut.fetchHeartsWithSpecificRelation(new SearchSpecificRelationHeartCondition(
                    member.getId(),
                    relations[4].getId(),
                    SearchSpecificRelationHeartCondition.Sort.OLD
            ));
            assertThat(result5).containsExactly(hearts[14], hearts[15], hearts[16], hearts[17], hearts[18], hearts[19]);
        }
    }
}
