package ac.dnd.mour.server.common.containers.callback;

import ac.dnd.mour.server.common.utils.RedisCleaner;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.ModifierSupport;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class RedisCleanerAllCallbackExtension implements AfterAllCallback {
    @Override
    public void afterAll(final ExtensionContext context) {
        if (context.getTestClass().isPresent()) {
            final Class<?> currentClass = context.getTestClass().get();
            if (isNestedClass(currentClass)) {
                return;
            }
        }
        final RedisCleaner databaseCleaner = SpringExtension.getApplicationContext(context).getBean(RedisCleaner.class);
        databaseCleaner.cleanUpRedis();
    }

    private boolean isNestedClass(final Class<?> currentClass) {
        return !ModifierSupport.isStatic(currentClass) && currentClass.isMemberClass();
    }
}
