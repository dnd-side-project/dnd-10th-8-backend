package ac.dnd.mur.server.schedule.domain.repository;

import ac.dnd.mur.server.schedule.domain.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
