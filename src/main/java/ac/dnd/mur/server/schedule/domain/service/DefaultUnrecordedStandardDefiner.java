package ac.dnd.mur.server.schedule.domain.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DefaultUnrecordedStandardDefiner implements UnrecordedStandardDefiner {
    /**
     * 현재 날짜 기준 1일 전에 종료된 지출이 기록되지 않은 일정
     */
    @Override
    public LocalDate get() {
        return LocalDate.now().minusDays(1);
    }
}
