package ac.dnd.bookkeeping.server.common;

import ac.dnd.bookkeeping.server.common.containers.MySqlTestContainersExtension;
import ac.dnd.bookkeeping.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("Integrate")
@SpringBootTest
@ExtendWith({
        DatabaseCleanerEachCallbackExtension.class,
        MySqlTestContainersExtension.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class IntegrateTest {
}
