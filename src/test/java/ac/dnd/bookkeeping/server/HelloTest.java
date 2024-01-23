package ac.dnd.bookkeeping.server;

import ac.dnd.bookkeeping.server.common.UnitTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloTest extends UnitTest {
    @Test
    void execute() {
        assertThat(1 + 1).isEqualTo(2);
    }
}
