package ac.dnd.mur.server.heart.domain.repository.query;

import ac.dnd.mur.server.common.RepositoryTest;
import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.heart.domain.repository.HeartRepository;
import ac.dnd.mur.server.heart.domain.repository.query.response.PersonalHeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.spec.PersonalStatisticsCondition;
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

import static ac.dnd.mur.server.common.fixture.HeartFixture.개업_선물을_받았다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.개업_선물을_보냈다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.결혼_축의금을_받았다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.결혼_축의금을_보냈다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.돌잔치_선물을_받았다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.돌잔치_선물을_보냈다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.생일_선물을_받았다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.생일_선물을_보냈다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.승진_선물을_받았다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.승진_선물을_보냈다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.출산_선물을_받았다;
import static ac.dnd.mur.server.common.fixture.HeartFixture.출산_선물을_보냈다;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.가족_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.직장_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_2;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_3;
import static ac.dnd.mur.server.heart.domain.repository.query.spec.PersonalStatisticsCondition.Type.MONTH;
import static ac.dnd.mur.server.heart.domain.repository.query.spec.PersonalStatisticsCondition.Type.YEAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Import(HeartStatisticsRepositoryImpl.class)
@DisplayName("Heart -> HeartStatisticsRepository [fetchPersonalHeartHistories] 테스트")
class HeartStatisticsRepositoryFetchPersonalHeartHistoriesTest extends RepositoryTest {
    @Autowired
    private HeartStatisticsRepositoryImpl sut;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private HeartRepository heartRepository;

    private Member member;
    private Group[] groups = new Group[3];
    private Relation[] relations = new Relation[5];
    private Heart[] hearts = new Heart[30];

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MEMBER_1.toDomain());
        groups = groupRepository.saveAll(List.of(
                Group.of(member, "친구"),
                Group.of(member, "가족"),
                Group.of(member, "직장")
        )).toArray(Group[]::new);
        relations = relationRepository.saveAll(List.of(
                친구_1.toDomain(member, groups[0]),
                친구_2.toDomain(member, groups[0]),
                친구_3.toDomain(member, groups[0]),
                가족_1.toDomain(member, groups[1]),
                직장_1.toDomain(member, groups[2])
        )).toArray(Relation[]::new);

        hearts = heartRepository.saveAll(List.of(
                // 2024년
                결혼_축의금을_보냈다.toDomain(member, relations[0], 100_000, LocalDate.of(2024, 2, 3)),
                결혼_축의금을_보냈다.toDomain(member, relations[3], 200_000, LocalDate.of(2024, 5, 3)),
                결혼_축의금을_보냈다.toDomain(member, relations[4], 500_000, LocalDate.of(2024, 6, 3)),
                결혼_축의금을_받았다.toDomain(member, relations[0], 100_000, LocalDate.of(2024, 7, 1)),
                결혼_축의금을_받았다.toDomain(member, relations[3], 200_000, LocalDate.of(2024, 7, 1)),
                결혼_축의금을_받았다.toDomain(member, relations[4], 500_000, LocalDate.of(2024, 7, 1)),

                생일_선물을_보냈다.toDomain(member, relations[2], 500_000, LocalDate.of(2024, 5, 3)),
                생일_선물을_받았다.toDomain(member, relations[2], 500_000, LocalDate.of(2024, 9, 3)),

                출산_선물을_보냈다.toDomain(member, relations[1], 1_000_000, LocalDate.of(2024, 3, 1)),
                출산_선물을_보냈다.toDomain(member, relations[4], 2_000_000, LocalDate.of(2024, 5, 1)),
                출산_선물을_받았다.toDomain(member, relations[1], 1_000_000, LocalDate.of(2024, 9, 14)),
                출산_선물을_받았다.toDomain(member, relations[4], 2_000_000, LocalDate.of(2024, 9, 14)),

                돌잔치_선물을_보냈다.toDomain(member, relations[1], 500_000, LocalDate.of(2024, 6, 8)),
                돌잔치_선물을_보냈다.toDomain(member, relations[4], 500_000, LocalDate.of(2024, 8, 8)),
                돌잔치_선물을_받았다.toDomain(member, relations[1], 500_000, LocalDate.of(2024, 12, 22)),
                돌잔치_선물을_받았다.toDomain(member, relations[4], 500_000, LocalDate.of(2024, 12, 22)),

                개업_선물을_보냈다.toDomain(member, relations[0], 300_000, LocalDate.of(2024, 3, 3)),
                개업_선물을_보냈다.toDomain(member, relations[1], 500_000, LocalDate.of(2024, 5, 3)),
                개업_선물을_보냈다.toDomain(member, relations[2], 700_000, LocalDate.of(2024, 7, 3)),
                개업_선물을_받았다.toDomain(member, relations[0], 300_000, LocalDate.of(2024, 12, 15)),
                개업_선물을_받았다.toDomain(member, relations[1], 500_000, LocalDate.of(2024, 12, 15)),
                개업_선물을_받았다.toDomain(member, relations[2], 700_000, LocalDate.of(2024, 12, 15)),

                승진_선물을_보냈다.toDomain(member, relations[3], 5_000_000, LocalDate.of(2024, 5, 20)),
                승진_선물을_받았다.toDomain(member, relations[0], 2_000_000, LocalDate.of(2024, 11, 20)),
                승진_선물을_받았다.toDomain(member, relations[3], 5_000_000, LocalDate.of(2024, 11, 20)),

                // 2025년
                생일_선물을_보냈다.toDomain(member, relations[2], 500_000, LocalDate.of(2025, 5, 3)),
                생일_선물을_받았다.toDomain(member, relations[2], 500_000, LocalDate.of(2025, 9, 3)),

                승진_선물을_보냈다.toDomain(member, relations[3], 5_000_000, LocalDate.of(2025, 5, 20)),
                승진_선물을_받았다.toDomain(member, relations[0], 2_000_000, LocalDate.of(2025, 11, 20)),
                승진_선물을_받았다.toDomain(member, relations[3], 5_000_000, LocalDate.of(2025, 11, 20))
        )).toArray(Heart[]::new);
    }

    @Nested
    @DisplayName("자신의 연도별 주고받은 마음 통계")
    class FetchPersonalHeartHistoriesWithYear {
        @Test
        @DisplayName("자신의 연도별 보낸 마음 통계를 조회한다 [2024년]")
        void year2024Give() {
            final List<PersonalHeartHistory> result = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(YEAR, 2024, 0, true));
            assertPersonalHeartHistoriesMatch(
                    result,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(
                            List.of(hearts[2], hearts[1], hearts[0]),
                            List.of(hearts[6]),
                            List.of(hearts[9], hearts[8]),
                            List.of(hearts[13], hearts[12]),
                            List.of(hearts[18], hearts[17], hearts[16]),
                            List.of(hearts[22])
                    ),
                    List.of(
                            List.of(relations[4], relations[3], relations[0]),
                            List.of(relations[2]),
                            List.of(relations[4], relations[1]),
                            List.of(relations[4], relations[1]),
                            List.of(relations[2], relations[1], relations[0]),
                            List.of(relations[3])
                    ),
                    List.of(
                            List.of(groups[2], groups[1], groups[0]),
                            List.of(groups[0]),
                            List.of(groups[2], groups[0]),
                            List.of(groups[2], groups[0]),
                            List.of(groups[0], groups[0], groups[0]),
                            List.of(groups[1])
                    )
            );
        }

        @Test
        @DisplayName("자신의 연도별 받은 마음 통계를 조회한다 [2024년]")
        void year2024Take() {
            final List<PersonalHeartHistory> result = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(YEAR, 2024, 0, false));
            assertPersonalHeartHistoriesMatch(
                    result,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(
                            List.of(hearts[5], hearts[4], hearts[3]),
                            List.of(hearts[7]),
                            List.of(hearts[11], hearts[10]),
                            List.of(hearts[15], hearts[14]),
                            List.of(hearts[21], hearts[20], hearts[19]),
                            List.of(hearts[24], hearts[23])
                    ),
                    List.of(
                            List.of(relations[4], relations[3], relations[0]),
                            List.of(relations[2]),
                            List.of(relations[4], relations[1]),
                            List.of(relations[4], relations[1]),
                            List.of(relations[2], relations[1], relations[0]),
                            List.of(relations[3], relations[0])
                    ),
                    List.of(
                            List.of(groups[2], groups[1], groups[0]),
                            List.of(groups[0]),
                            List.of(groups[2], groups[0]),
                            List.of(groups[2], groups[0]),
                            List.of(groups[0], groups[0], groups[0]),
                            List.of(groups[1], groups[0])
                    )
            );
        }

        @Test
        @DisplayName("자신의 연도별 보낸 마음 통계를 조회한다 [2025년]")
        void year2025Give() {
            final List<PersonalHeartHistory> result = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(YEAR, 2025, 0, true));
            assertPersonalHeartHistoriesMatch(
                    result,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(), List.of(hearts[25]), List.of(), List.of(), List.of(), List.of(hearts[27])),
                    List.of(List.of(), List.of(relations[2]), List.of(), List.of(), List.of(), List.of(relations[3])),
                    List.of(List.of(), List.of(groups[0]), List.of(), List.of(), List.of(), List.of(groups[1]))
            );
        }

        @Test
        @DisplayName("자신의 연도별 받은 마음 통계를 조회한다 [2025년]")
        void year2025Take() {
            final List<PersonalHeartHistory> result = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(YEAR, 2025, 0, false));
            assertPersonalHeartHistoriesMatch(
                    result,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(), List.of(hearts[26]), List.of(), List.of(), List.of(), List.of(hearts[29], hearts[28])),
                    List.of(List.of(), List.of(relations[2]), List.of(), List.of(), List.of(), List.of(relations[3], relations[0])),
                    List.of(List.of(), List.of(groups[0]), List.of(), List.of(), List.of(), List.of(groups[1], groups[0]))
            );
        }
    }

    @Nested
    @DisplayName("자신의 월별 주고받은 마음 통계")
    class FetchPersonalHeartHistoriesWithYearMonth {
        @Test
        @DisplayName("자신의 월별 보낸 마음 통계를 조회한다 [2024년-x월]")
        void year2024Give() {
            final List<PersonalHeartHistory> result1 = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(MONTH, 2024, 2, true));
            assertPersonalHeartHistoriesMatch(
                    result1,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(hearts[0]), List.of(), List.of(), List.of(), List.of(), List.of()),
                    List.of(List.of(relations[0]), List.of(), List.of(), List.of(), List.of(), List.of()),
                    List.of(List.of(groups[0]), List.of(), List.of(), List.of(), List.of(), List.of())
            );

            final List<PersonalHeartHistory> result2 = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(MONTH, 2024, 3, true));
            assertPersonalHeartHistoriesMatch(
                    result2,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(), List.of(), List.of(hearts[8]), List.of(), List.of(hearts[16]), List.of()),
                    List.of(List.of(), List.of(), List.of(relations[1]), List.of(), List.of(relations[0]), List.of()),
                    List.of(List.of(), List.of(), List.of(groups[0]), List.of(), List.of(groups[0]), List.of())
            );

            final List<PersonalHeartHistory> result3 = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(MONTH, 2024, 5, true));
            assertPersonalHeartHistoriesMatch(
                    result3,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(hearts[1]), List.of(hearts[6]), List.of(hearts[9]), List.of(), List.of(hearts[17]), List.of(hearts[22])),
                    List.of(List.of(relations[3]), List.of(relations[2]), List.of(relations[4]), List.of(), List.of(relations[1]), List.of(relations[3])),
                    List.of(List.of(groups[1]), List.of(groups[0]), List.of(groups[2]), List.of(), List.of(groups[0]), List.of(groups[1]))
            );

            final List<PersonalHeartHistory> result4 = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(MONTH, 2024, 6, true));
            assertPersonalHeartHistoriesMatch(
                    result4,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(hearts[2]), List.of(), List.of(), List.of(hearts[12]), List.of(), List.of()),
                    List.of(List.of(relations[4]), List.of(), List.of(), List.of(relations[1]), List.of(), List.of()),
                    List.of(List.of(groups[2]), List.of(), List.of(), List.of(groups[0]), List.of(), List.of())
            );

            final List<PersonalHeartHistory> result5 = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(MONTH, 2024, 7, true));
            assertPersonalHeartHistoriesMatch(
                    result5,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(), List.of(), List.of(), List.of(), List.of(hearts[18]), List.of()),
                    List.of(List.of(), List.of(), List.of(), List.of(), List.of(relations[2]), List.of()),
                    List.of(List.of(), List.of(), List.of(), List.of(), List.of(groups[0]), List.of())
            );

            final List<PersonalHeartHistory> result6 = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(MONTH, 2024, 8, true));
            assertPersonalHeartHistoriesMatch(
                    result6,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(), List.of(), List.of(), List.of(hearts[13]), List.of(), List.of()),
                    List.of(List.of(), List.of(), List.of(), List.of(relations[4]), List.of(), List.of()),
                    List.of(List.of(), List.of(), List.of(), List.of(groups[2]), List.of(), List.of())
            );
        }

        @Test
        @DisplayName("자신의 월별 받은 마음 통계를 조회한다 [2024년-x월]")
        void year2024Take() {
            final List<PersonalHeartHistory> result1 = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(MONTH, 2024, 7, false));
            assertPersonalHeartHistoriesMatch(
                    result1,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(hearts[5], hearts[4], hearts[3]), List.of(), List.of(), List.of(), List.of(), List.of()),
                    List.of(List.of(relations[4], relations[3], relations[0]), List.of(), List.of(), List.of(), List.of(), List.of()),
                    List.of(List.of(groups[2], groups[1], groups[0]), List.of(), List.of(), List.of(), List.of(), List.of())
            );

            final List<PersonalHeartHistory> result2 = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(MONTH, 2024, 9, false));
            assertPersonalHeartHistoriesMatch(
                    result2,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(), List.of(hearts[7]), List.of(hearts[11], hearts[10]), List.of(), List.of(), List.of()),
                    List.of(List.of(), List.of(relations[2]), List.of(relations[4], relations[1]), List.of(), List.of(), List.of()),
                    List.of(List.of(), List.of(groups[0]), List.of(groups[2], groups[0]), List.of(), List.of(), List.of())
            );

            final List<PersonalHeartHistory> result3 = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(MONTH, 2024, 11, false));
            assertPersonalHeartHistoriesMatch(
                    result3,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(hearts[24], hearts[23])),
                    List.of(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(relations[3], relations[0])),
                    List.of(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(groups[1], groups[0]))
            );

            final List<PersonalHeartHistory> result4 = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(MONTH, 2024, 12, false));
            assertPersonalHeartHistoriesMatch(
                    result4,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(), List.of(), List.of(), List.of(hearts[15], hearts[14]), List.of(hearts[21], hearts[20], hearts[19]), List.of()),
                    List.of(List.of(), List.of(), List.of(), List.of(relations[4], relations[1]), List.of(relations[2], relations[1], relations[0]), List.of()),
                    List.of(List.of(), List.of(), List.of(), List.of(groups[2], groups[0]), List.of(groups[0], groups[0], groups[0]), List.of())
            );
        }

        @Test
        @DisplayName("자신의 월별 보낸 마음 통계를 조회한다 [2025년-x월]")
        void year2025Give() {
            final List<PersonalHeartHistory> result = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(MONTH, 2025, 5, true));
            assertPersonalHeartHistoriesMatch(
                    result,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(), List.of(hearts[25]), List.of(), List.of(), List.of(), List.of(hearts[27])),
                    List.of(List.of(), List.of(relations[2]), List.of(), List.of(), List.of(), List.of(relations[3])),
                    List.of(List.of(), List.of(groups[0]), List.of(), List.of(), List.of(), List.of(groups[1]))
            );
        }

        @Test
        @DisplayName("자신의 월별 받은 마음 통계를 조회한다 [2025년-x월]")
        void year2025Take() {
            final List<PersonalHeartHistory> result1 = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(MONTH, 2025, 9, false));
            assertPersonalHeartHistoriesMatch(
                    result1,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(), List.of(hearts[26]), List.of(), List.of(), List.of(), List.of()),
                    List.of(List.of(), List.of(relations[2]), List.of(), List.of(), List.of(), List.of()),
                    List.of(List.of(), List.of(groups[0]), List.of(), List.of(), List.of(), List.of())
            );

            final List<PersonalHeartHistory> result2 = sut.fetchPersonalHeartHistories(new PersonalStatisticsCondition(MONTH, 2025, 11, false));
            assertPersonalHeartHistoriesMatch(
                    result2,
                    List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                    List.of(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(hearts[29], hearts[28])),
                    List.of(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(relations[3], relations[0])),
                    List.of(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(groups[1], groups[0]))
            );
        }
    }

    private void assertPersonalHeartHistoriesMatch(
            final List<PersonalHeartHistory> result,
            final List<String> events,
            final List<List<Heart>> heartSummaries,
            final List<List<Relation>> relationSummaries,
            final List<List<Group>> groupSummaries
    ) {
        for (int i = 0; i < events.size(); i++) {
            final List<PersonalHeartHistory> histories = extractByEvent(result, events.get(i));
            final List<Heart> hearts = heartSummaries.get(i);
            final List<Relation> relations = relationSummaries.get(i);
            final List<Group> groups = groupSummaries.get(i);

            final int totalSize = hearts.size();
            assertThat(histories).hasSize(totalSize);

            for (int j = 0; j < totalSize; j++) {
                final PersonalHeartHistory history = histories.get(j);
                final Heart heart = hearts.get(j);
                final Relation relation = relations.get(j);
                final Group group = groups.get(j);

                assertAll(
                        () -> assertThat(history.event()).isEqualTo(heart.getEvent()),
                        () -> assertThat(history.relationName()).isEqualTo(relation.getName()),
                        () -> assertThat(history.groupName()).isEqualTo(group.getName()),
                        () -> assertThat(history.money()).isEqualTo(heart.getMoney()),
                        () -> assertThat(history.day()).isEqualTo(heart.getDay()),
                        () -> assertThat(history.memo()).isEqualTo(heart.getMemo())
                );
            }
        }
    }

    private List<PersonalHeartHistory> extractByEvent(
            final List<PersonalHeartHistory> result,
            final String event
    ) {
        if ("ETC".equals(event)) {
            final List<String> notEtcEvents = List.of("결혼", "생일", "출산", "돌잔치", "개업");
            return result.stream()
                    .filter(it -> !notEtcEvents.contains(it.event()))
                    .toList();
        }

        return result.stream()
                .filter(it -> it.event().equals(event))
                .toList();
    }
}
