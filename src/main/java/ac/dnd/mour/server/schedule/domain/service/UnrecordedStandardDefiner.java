package ac.dnd.mour.server.schedule.domain.service;

import java.time.LocalDate;

@FunctionalInterface
public interface UnrecordedStandardDefiner {
    LocalDate get();
}
