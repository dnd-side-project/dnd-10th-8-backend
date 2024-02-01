package ac.dnd.mur.server.group.domain.model;

public record GroupResponse(
        long id,
        String name
) {
    public static GroupResponse of(final Group group) {
        return new GroupResponse(group.getId(), group.getName());
    }
}
