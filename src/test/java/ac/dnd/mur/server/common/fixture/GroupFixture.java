package ac.dnd.mur.server.common.fixture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupFixture {
    친구("친구"),
    가족("가족"),
    지인("지인"),
    직장("직장"),
    거래처("거래처"),
    ;

    private final String name;
}
