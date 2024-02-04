package ac.dnd.mur.server.schedule.application.usecase;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.heart.domain.repository.HeartRepository;
import ac.dnd.mur.server.schedule.application.usecase.query.response.UnrecordedScheduleResponse;
import ac.dnd.mur.server.schedule.domain.model.Schedule;
import ac.dnd.mur.server.schedule.domain.repository.ScheduleRepository;
import ac.dnd.mur.server.schedule.domain.repository.query.ScheduleQueryRepository;
import ac.dnd.mur.server.schedule.domain.service.UnrecordedStandardDefiner;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@UseCase
@RequiredArgsConstructor
public class GetUnrecordedScheduleUseCase {
    private final UnrecordedStandardDefiner unrecordedStandardDefiner;
    private final ScheduleRepository scheduleRepository;
    private final HeartRepository heartRepository;
    private final ScheduleQueryRepository scheduleQueryRepository;

    @MurReadOnlyTransactional
    public List<UnrecordedScheduleResponse> invoke(final long memberId) {
        final List<Schedule> passedSchedules = scheduleRepository.getPassedSchedules(memberId, unrecordedStandardDefiner.get());
        final List<Heart> registeredHearts = getRegisteredHeart(passedSchedules);

        final List<Long> filteredScheduleIds = filteringPassedSchedules(passedSchedules, registeredHearts);
        return scheduleQueryRepository.fetchUnrecordedSchedules(filteredScheduleIds)
                .stream()
                .map(UnrecordedScheduleResponse::from)
                .toList();
    }

    private List<Heart> getRegisteredHeart(final List<Schedule> passedSchedules) {
        final List<Long> relationIds = passedSchedules.stream()
                .map(Schedule::getRelationId)
                .toList();
        return heartRepository.findByRelationIdIn(relationIds);
    }

    private List<Long> filteringPassedSchedules(
            final List<Schedule> passedSchedules,
            final List<Heart> registeredHearts
    ) {
        final List<Long> registeredIds = new ArrayList<>();
        passedSchedules.forEach(schedule ->
                registeredHearts.stream()
                        .filter(sameRelation(schedule))
                        .filter(sameDayAndEvent(schedule))
                        .map(Heart::getRelationId)
                        .forEach(registeredIds::add)
        );

        return passedSchedules.stream()
                .filter(notRegisteredSchedules(registeredIds))
                .map(Schedule::getId)
                .toList();
    }

    private Predicate<Heart> sameRelation(final Schedule schedule) {
        return heart -> schedule.getRelationId().equals(heart.getRelationId());
    }

    private Predicate<Heart> sameDayAndEvent(final Schedule schedule) {
        return heart -> schedule.getDay().equals(heart.getDay()) && schedule.getEvent().equals(heart.getEvent());
    }

    private Predicate<Schedule> notRegisteredSchedules(final List<Long> registeredIds) {
        return schedule -> !registeredIds.contains(schedule.getRelationId());
    }
}
