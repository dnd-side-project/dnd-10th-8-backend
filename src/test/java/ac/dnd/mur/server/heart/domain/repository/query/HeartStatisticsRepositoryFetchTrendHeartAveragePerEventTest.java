package ac.dnd.mur.server.heart.domain.repository.query;

import ac.dnd.mur.server.common.RepositoryTest;
import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.heart.domain.repository.HeartRepository;
import ac.dnd.mur.server.heart.domain.repository.query.response.TrendHeartStatistics;
import ac.dnd.mur.server.heart.domain.repository.query.spec.TrendHeartStatisticsCondition;
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

import static ac.dnd.mur.server.common.fixture.HeartFixture.개업_선물을_보냈다;
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
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_2;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_3;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_4;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_5;
import static ac.dnd.mur.server.common.fixture.RelationFixture.가족_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_1;
import static ac.dnd.mur.server.heart.domain.repository.query.spec.TrendRange.RANGE_20;
import static ac.dnd.mur.server.heart.domain.repository.query.spec.TrendRange.RANGE_30;
import static ac.dnd.mur.server.member.domain.model.Gender.FEMALE;
import static ac.dnd.mur.server.member.domain.model.Gender.MALE;
import static org.assertj.core.api.Assertions.assertThat;

@Import(HeartStatisticsRepositoryImpl.class)
@DisplayName("Heart -> HeartStatisticsRepository [트렌드 연령대별 이벤트당 평균 경사비] 테스트")
public class HeartStatisticsRepositoryFetchTrendHeartAveragePerEventTest extends RepositoryTest {
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

    @BeforeEach
    void setUp() {
        final Member[] members = memberRepository.saveAll(List.of(
                MEMBER_1.toDomain(MALE, LocalDate.now().minusYears(25)),
                MEMBER_2.toDomain(MALE, LocalDate.now().minusYears(25)),
                MEMBER_3.toDomain(FEMALE, LocalDate.now().minusYears(25)),
                MEMBER_4.toDomain(MALE, LocalDate.now().minusYears(35)),
                MEMBER_5.toDomain(FEMALE, LocalDate.now().minusYears(35))
        )).toArray(Member[]::new);
        final Group[] groups = groupRepository.saveAll(List.of(
                Group.of(members[0], "친구"), Group.of(members[0], "가족"),
                Group.of(members[1], "친구"), Group.of(members[1], "가족"),
                Group.of(members[2], "친구"), Group.of(members[2], "가족"),
                Group.of(members[3], "친구"), Group.of(members[3], "가족"),
                Group.of(members[4], "친구"), Group.of(members[4], "가족")
        )).toArray(Group[]::new);
        final Relation[] relations = relationRepository.saveAll(List.of(
                친구_1.toDomain(members[0], groups[0]), 가족_1.toDomain(members[0], groups[1]),
                친구_1.toDomain(members[1], groups[0]), 가족_1.toDomain(members[1], groups[1]),
                친구_1.toDomain(members[2], groups[0]), 가족_1.toDomain(members[2], groups[1]),
                친구_1.toDomain(members[3], groups[0]), 가족_1.toDomain(members[3], groups[1]),
                친구_1.toDomain(members[4], groups[0]), 가족_1.toDomain(members[4], groups[1])
        )).toArray(Relation[]::new);
        heartRepository.saveAll(List.of(
                // 20대 남성 -> members[0], members[1]
                결혼_축의금을_보냈다.toDomain(members[0], relations[0], 100_000),
                결혼_축의금을_보냈다.toDomain(members[0], relations[1], 370_000),
                생일_선물을_보냈다.toDomain(members[0], relations[0], 200_000),
                생일_선물을_보냈다.toDomain(members[0], relations[1], 500_000),
                생일_선물을_보냈다.toDomain(members[0], relations[1], 490_000),
                출산_선물을_보냈다.toDomain(members[0], relations[0], 300_000),
                돌잔치_선물을_보냈다.toDomain(members[0], relations[0], 150_000),
                돌잔치_선물을_보냈다.toDomain(members[0], relations[1], 550_000),
                개업_선물을_보냈다.toDomain(members[0], relations[0], 180_000),
                개업_선물을_보냈다.toDomain(members[0], relations[1], 470_000),
                승진_선물을_보냈다.toDomain(members[0], relations[0], 1_000_000),
                승진_선물을_받았다.toDomain(members[0], relations[0], 2_000_000),
                승진_선물을_받았다.toDomain(members[0], relations[1], 3_000_000),

                결혼_축의금을_보냈다.toDomain(members[1], relations[2], 580_000),
                생일_선물을_보냈다.toDomain(members[1], relations[2], 80_000),
                생일_선물을_보냈다.toDomain(members[1], relations[3], 180_000),
                생일_선물을_받았다.toDomain(members[1], relations[2], 380_000),
                생일_선물을_받았다.toDomain(members[1], relations[3], 580_000),
                출산_선물을_보냈다.toDomain(members[1], relations[2], 10_000_000),
                출산_선물을_보냈다.toDomain(members[1], relations[2], 20_000_000),
                출산_선물을_보냈다.toDomain(members[1], relations[2], 37_000_000),
                출산_선물을_보냈다.toDomain(members[1], relations[3], 19_000_000),
                돌잔치_선물을_보냈다.toDomain(members[1], relations[2], 100_000),
                개업_선물을_보냈다.toDomain(members[1], relations[2], 5_000_000),
                승진_선물을_보냈다.toDomain(members[1], relations[2], 150_000),
                승진_선물을_보냈다.toDomain(members[1], relations[3], 5_150_000),

                // 20대 여성 -> members[2]
                결혼_축의금을_보냈다.toDomain(members[2], relations[4], 50_000),
                생일_선물을_보냈다.toDomain(members[2], relations[4], 140_000),
                생일_선물을_보냈다.toDomain(members[2], relations[5], 370_000),
                출산_선물을_보냈다.toDomain(members[2], relations[4], 290_000),
                출산_선물을_보냈다.toDomain(members[2], relations[5], 530_000),
                출산_선물을_받았다.toDomain(members[2], relations[4], 230_000),
                출산_선물을_받았다.toDomain(members[2], relations[5], 330_000),
                돌잔치_선물을_보냈다.toDomain(members[2], relations[4], 120_000),
                개업_선물을_보냈다.toDomain(members[2], relations[4], 1_700_000),
                개업_선물을_보냈다.toDomain(members[2], relations[5], 5_550_000),
                승진_선물을_보냈다.toDomain(members[2], relations[4], 100_000),
                승진_선물을_받았다.toDomain(members[2], relations[4], 150_000),

                // 30대 남성 -> members[3]
                결혼_축의금을_보냈다.toDomain(members[3], relations[6], 1_500_000),
                결혼_축의금을_보냈다.toDomain(members[3], relations[7], 380_000),
                생일_선물을_보냈다.toDomain(members[3], relations[6], 670_000),
                생일_선물을_보냈다.toDomain(members[3], relations[7], 10_070_000),
                출산_선물을_보냈다.toDomain(members[3], relations[6], 530_000),
                출산_선물을_보냈다.toDomain(members[3], relations[7], 840_000),
                출산_선물을_보냈다.toDomain(members[3], relations[7], 890_000),
                돌잔치_선물을_보냈다.toDomain(members[3], relations[6], 150_000),
                돌잔치_선물을_보냈다.toDomain(members[3], relations[6], 390_000),
                돌잔치_선물을_보냈다.toDomain(members[3], relations[7], 250_000),
                돌잔치_선물을_받았다.toDomain(members[3], relations[6], 350_000),
                돌잔치_선물을_받았다.toDomain(members[3], relations[6], 550_000),
                개업_선물을_보냈다.toDomain(members[3], relations[6], 380_000),
                개업_선물을_보냈다.toDomain(members[3], relations[6], 978_000),
                개업_선물을_보냈다.toDomain(members[3], relations[7], 1_380_000),
                승진_선물을_보냈다.toDomain(members[3], relations[6], 150_000),
                승진_선물을_보냈다.toDomain(members[3], relations[6], 173_000),
                승진_선물을_보냈다.toDomain(members[3], relations[6], 450_000),
                승진_선물을_보냈다.toDomain(members[3], relations[6], 980_000),
                승진_선물을_보냈다.toDomain(members[3], relations[7], 150_000),

                // 30대 여성 -> members[4]
                결혼_축의금을_보냈다.toDomain(members[4], relations[8], 150_000),
                결혼_축의금을_보냈다.toDomain(members[4], relations[9], 290_000),
                생일_선물을_보냈다.toDomain(members[4], relations[8], 300_000),
                생일_선물을_보냈다.toDomain(members[4], relations[9], 350_000),
                생일_선물을_보냈다.toDomain(members[4], relations[9], 490_000),
                출산_선물을_보냈다.toDomain(members[4], relations[8], 230_000),
                돌잔치_선물을_보냈다.toDomain(members[4], relations[8], 150_000),
                돌잔치_선물을_보냈다.toDomain(members[4], relations[9], 190_000),
                돌잔치_선물을_보냈다.toDomain(members[4], relations[9], 280_000),
                개업_선물을_보냈다.toDomain(members[4], relations[8], 120_000),
                개업_선물을_보냈다.toDomain(members[4], relations[8], 190_000),
                승진_선물을_보냈다.toDomain(members[4], relations[8], 270_000)
        )).toArray(Heart[]::new);
    }

    @Test
    @DisplayName("연령대별 이벤트 경사비 평균을 조회한다")
    void fetchTrendHeartAveragePerEvent() {
        final List<TrendHeartStatistics> result1 = sut.fetchTrendHeartAveragePerEvent(new TrendHeartStatisticsCondition(MALE, RANGE_20));
        assertTrendHeartStatisticsMatch(
                result1,
                List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                List.of(350_000D, 290_000D, 17_260_000D, 266_666.666700D, 1_883_333.333300D, 2_100_000D)
        );

        final List<TrendHeartStatistics> result2 = sut.fetchTrendHeartAveragePerEvent(new TrendHeartStatisticsCondition(FEMALE, RANGE_20));
        assertTrendHeartStatisticsMatch(
                result2,
                List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                List.of(50_000D, 255_000D, 410_000D, 120_000D, 3_625_000D, 100_000D)
        );

        final List<TrendHeartStatistics> result3 = sut.fetchTrendHeartAveragePerEvent(new TrendHeartStatisticsCondition(MALE, RANGE_30));
        assertTrendHeartStatisticsMatch(
                result3,
                List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                List.of(940_000D, 5_370_000D, 753_333.333300D, 263_333.333300D, 912_666.666700D, 380_600D)
        );

        final List<TrendHeartStatistics> result4 = sut.fetchTrendHeartAveragePerEvent(new TrendHeartStatisticsCondition(FEMALE, RANGE_30));
        assertTrendHeartStatisticsMatch(
                result4,
                List.of("결혼", "생일", "출산", "돌잔치", "개업", "ETC"),
                List.of(220_000D, 380_000D, 230_000D, 206_666.666700D, 155_000D, 270_000D)
        );
    }

    private void assertTrendHeartStatisticsMatch(
            final List<TrendHeartStatistics> result,
            final List<String> events,
            final List<Double> averages
    ) {
        for (int i = 0; i < result.size(); i++) {
            final String event = events.get(i);
            final double average = extractAverage(result, event);
            assertThat(average).isEqualTo(averages.get(i));
        }
    }

    private double extractAverage(
            final List<TrendHeartStatistics> result,
            final String event
    ) {
        if ("ETC".equals(event)) {
            final List<String> notEtcEvents = List.of("결혼", "생일", "출산", "돌잔치", "개업");
            final List<Double> etcAverages = result.stream()
                    .filter(it -> !notEtcEvents.contains(it.event()))
                    .map(TrendHeartStatistics::average)
                    .toList();
            return etcAverages.stream()
                    .mapToDouble(it -> it)
                    .sum() / etcAverages.size();
        }

        return result.stream()
                .filter(it -> it.event().equals(event))
                .map(TrendHeartStatistics::average)
                .findFirst()
                .orElse(0D);
    }
}
