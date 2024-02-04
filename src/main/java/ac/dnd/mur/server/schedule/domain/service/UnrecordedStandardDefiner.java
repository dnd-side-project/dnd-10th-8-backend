package ac.dnd.mur.server.schedule.domain.service;

import java.time.LocalDate;

@FunctionalInterface
public interface UnrecordedStandardDefiner {
    LocalDate get();
}
