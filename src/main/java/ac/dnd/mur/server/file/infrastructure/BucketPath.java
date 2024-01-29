package ac.dnd.mur.server.file.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BucketPath {
    MEMBER_PROFILE("profiles"),
    ;

    private final String path;

    public String completePath(final String suffix) {
        return path + "/" + suffix;
    }
}
