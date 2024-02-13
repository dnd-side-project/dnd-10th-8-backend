package ac.dnd.mour.server.schedule.domain.repository.query;

import ac.dnd.mour.server.common.RepositoryTest;
import ac.dnd.mour.server.group.domain.model.Group;
import ac.dnd.mour.server.group.domain.repository.GroupRepository;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import ac.dnd.mour.server.relation.domain.model.Relation;
import ac.dnd.mour.server.relation.domain.repository.RelationRepository;
import ac.dnd.mour.server.schedule.domain.model.Schedule;
import ac.dnd.mour.server.schedule.domain.repository.ScheduleRepository;
import ac.dnd.mour.server.schedule.domain.repository.query.response.CalendarSchedule;
import ac.dnd.mour.server.schedule.domain.repository.query.spec.SearchCalendarScheduleCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mour.server.common.fixture.RelationFixture.가족_1;
import static ac.dnd.mour.server.common.fixture.RelationFixture.직장_1;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_1;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_2;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_3;
import static ac.dnd.mour.server.common.fixture.ScheduleFixture.특별한_일정_XXX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Import(ScheduleQueryRepositoryImpl.class)
@DisplayName("Schedule -> ScheduleQueryRepository 테스트")
class ScheduleQueryRepositoryImplTest extends RepositoryTest {
    @Autowired
    private ScheduleQueryRepositoryImpl sut;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private Member member;
    private Group[] groups = new Group[3];
    private Relation[] relations = new Relation[5];

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
    }

    @Test
    @DisplayName("Year/Month에 등록한 일정 정보를 조회한다")
    void fetchCalendarSchedules() {
        // given
        final Schedule[] schedules = scheduleRepository.saveAll(List.of(
                특별한_일정_XXX.toDomain(member, relations[0], LocalDate.of(2024, 1, 3)),
                특별한_일정_XXX.toDomain(member, relations[1], LocalDate.of(2024, 1, 15)),
                특별한_일정_XXX.toDomain(member, relations[2], LocalDate.of(2024, 1, 19)),
                특별한_일정_XXX.toDomain(member, relations[3], LocalDate.of(2024, 1, 25)),
                특별한_일정_XXX.toDomain(member, relations[4], LocalDate.of(2024, 1, 31)),

                특별한_일정_XXX.toDomain(member, relations[1], LocalDate.of(2024, 2, 2)),
                특별한_일정_XXX.toDomain(member, relations[2], LocalDate.of(2024, 2, 23)),
                특별한_일정_XXX.toDomain(member, relations[4], LocalDate.of(2024, 2, 27)),

                특별한_일정_XXX.toDomain(member, relations[0], LocalDate.of(2024, 3, 1)),
                특별한_일정_XXX.toDomain(member, relations[3], LocalDate.of(2024, 3, 3)),
                특별한_일정_XXX.toDomain(member, relations[3], LocalDate.of(2024, 3, 15)),
                특별한_일정_XXX.toDomain(member, relations[4], LocalDate.of(2024, 3, 20))
        )).toArray(Schedule[]::new);

        /* 2023-12 */
        final List<CalendarSchedule> result1 = sut.fetchCalendarSchedules(new SearchCalendarScheduleCondition(member.getId(), 2023, 12));
        assertCalendarSchedulesMatch(result1, List.of(), List.of(), List.of());

        /* 2024-01 */
        final List<CalendarSchedule> result2 = sut.fetchCalendarSchedules(new SearchCalendarScheduleCondition(member.getId(), 2024, 1));
        assertCalendarSchedulesMatch(
                result2,
                List.of(schedules[0], schedules[1], schedules[2], schedules[3], schedules[4]),
                List.of(relations[0], relations[1], relations[2], relations[3], relations[4]),
                List.of(groups[0], groups[0], groups[0], groups[1], groups[2])
        );

        /* 2024-02 */
        final List<CalendarSchedule> result3 = sut.fetchCalendarSchedules(new SearchCalendarScheduleCondition(member.getId(), 2024, 2));
        assertCalendarSchedulesMatch(
                result3,
                List.of(schedules[5], schedules[6], schedules[7]),
                List.of(relations[1], relations[2], relations[4]),
                List.of(groups[0], groups[0], groups[2])
        );

        /* 2024-03 */
        final List<CalendarSchedule> result4 = sut.fetchCalendarSchedules(new SearchCalendarScheduleCondition(member.getId(), 2024, 3));
        assertCalendarSchedulesMatch(
                result4,
                List.of(schedules[8], schedules[9], schedules[10], schedules[11]),
                List.of(relations[0], relations[3], relations[3], relations[4]),
                List.of(groups[0], groups[1], groups[1], groups[2])
        );

        /* 2024-04 */
        final List<CalendarSchedule> result5 = sut.fetchCalendarSchedules(new SearchCalendarScheduleCondition(member.getId(), 2024, 4));
        assertCalendarSchedulesMatch(result5, List.of(), List.of(), List.of());
    }

    private void assertCalendarSchedulesMatch(
            final List<CalendarSchedule> result,
            final List<Schedule> schedules,
            final List<Relation> relations,
            final List<Group> groups
    ) {
        final int totalSize = schedules.size();
        assertThat(result).hasSize(totalSize);

        for (int i = 0; i < totalSize; i++) {
            final CalendarSchedule calendarSchedule = result.get(i);
            final Schedule schedule = schedules.get(i);
            final Relation relation = relations.get(i);
            final Group group = groups.get(i);

            assertAll(
                    () -> assertThat(calendarSchedule.id()).isEqualTo(schedule.getId()),
                    () -> assertThat(calendarSchedule.relationId()).isEqualTo(relation.getId()),
                    () -> assertThat(calendarSchedule.relationName()).isEqualTo(relation.getName()),
                    () -> assertThat(calendarSchedule.groupid()).isEqualTo(group.getId()),
                    () -> assertThat(calendarSchedule.groupName()).isEqualTo(group.getName()),
                    () -> assertThat(calendarSchedule.day()).isEqualTo(schedule.getDay()),
                    () -> assertThat(calendarSchedule.event()).isEqualTo(schedule.getEvent()),
                    () -> assertThat(calendarSchedule.time()).isEqualTo(schedule.getTime()),
                    () -> assertThat(calendarSchedule.link()).isEqualTo(schedule.getLink()),
                    () -> assertThat(calendarSchedule.location()).isEqualTo(schedule.getLocation())
            );
        }
    }
}
