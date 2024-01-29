package ac.dnd.mur.server.common.containers.callback;

import ac.dnd.mur.server.common.utils.DatabaseCleaner;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.ModifierSupport;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DatabaseCleanerAllCallbackExtension implements AfterAllCallback {
    @Override
    public void afterAll(final ExtensionContext context) {
        if (context.getTestClass().isPresent()) {
            final Class<?> currentClass = context.getTestClass().get();
            if (isNestedClass(currentClass)) {
                return;
            }
        }
        final DatabaseCleaner databaseCleaner = SpringExtension.getApplicationContext(context).getBean(DatabaseCleaner.class);
        databaseCleaner.cleanUpDatabase();
    }

    private boolean isNestedClass(final Class<?> currentClass) {
        return !ModifierSupport.isStatic(currentClass) && currentClass.isMemberClass();
    }
}
