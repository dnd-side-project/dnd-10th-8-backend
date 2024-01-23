package ac.dnd.bookkeeping.server.global.decorator;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

public class MdcTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(final Runnable runnable) {
        final Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        return () -> {
            if (copyOfContextMap != null) {
                MDC.setContextMap(copyOfContextMap);
            }
            runnable.run();
        };
    }
}
