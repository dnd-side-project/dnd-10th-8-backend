package ac.dnd.mur.server.schedule.domain.repository;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.schedule.domain.model.Schedule;
import ac.dnd.mur.server.schedule.exception.ScheduleException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ac.dnd.mur.server.schedule.exception.ScheduleExceptionCode.SCHEDULE_NOT_FOUND;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    default Schedule getById(final long id) {
        return findById(id)
                .orElseThrow(() -> new ScheduleException(SCHEDULE_NOT_FOUND));
    }

    // @Query
    @MurWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Schedule s WHERE s.id = :id AND s.memberId = :memberId")
    void deleteMemberSchedule(@Param("id") final long id, @Param("memberId") final long memberId);

    @MurWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Schedule s WHERE s.memberId = :memberId")
    void deleteMemberSchedules(@Param("memberId") final long memberId);

    // Query Method
    List<Schedule> findByMemberIdAndDayBefore(final long memberId, final LocalDate day);

    /**
     * 현재 날짜 기준 1일
     */
    default List<Schedule> getPassedSchedules(final long memberId, final LocalDate currentDay) {
        return findByMemberIdAndDayBefore(memberId, currentDay);
    }

    Optional<Schedule> findByIdAndMemberId(final long id, final long memberId);

    default Schedule getMemberSchedule(final long id, final long memberId) {
        return findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new ScheduleException(SCHEDULE_NOT_FOUND));
    }
}
