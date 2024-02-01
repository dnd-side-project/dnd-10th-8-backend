package ac.dnd.mur.server.member.domain.event;

import ac.dnd.mur.server.global.base.BaseEventModel;

public record MemberRegisteredEvent(
        long memberId
) implements BaseEventModel {
}
