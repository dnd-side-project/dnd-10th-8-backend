package ac.dnd.mur.server.schedule.domain.repository;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.schedule.domain.model.Schedule;
import ac.dnd.mur.server.schedule.exception.ScheduleException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static ac.dnd.mur.server.schedule.exception.ScheduleExceptionCode.SCHEDULE_NOT_FOUND;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    default Schedule getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new ScheduleException(SCHEDULE_NOT_FOUND));
    }

    @MurWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Schedule s WHERE s.memberId = :memberId")
    void deleteMemberSchedules(@Param("memberId") final Long memberId);
}
