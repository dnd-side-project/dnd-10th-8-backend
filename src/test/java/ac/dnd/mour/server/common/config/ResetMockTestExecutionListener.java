package ac.dnd.mour.server.common.config;

import org.mockito.internal.util.MockUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResetMockTestExecutionListener implements TestExecutionListener {
    private static long applicationContextStartupDate;
    private static final List<Object> mockCache = new ArrayList<>();

    @Override
    public void afterTestMethod(final TestContext testContext) {
        final ApplicationContext applicationContext = testContext.getApplicationContext();
        if (mockCache.isEmpty() || isNewContext(applicationContext)) {
            mockCache.clear();
            applicationContextStartupDate = applicationContext.getStartupDate();
            initMocks(applicationContext);
        }
        mockCache.forEach(MockUtil::resetMock);
    }

    private boolean isNewContext(final ApplicationContext applicationContext) {
        return applicationContext.getStartupDate() != applicationContextStartupDate;
    }

    private void initMocks(final ApplicationContext applicationContext) {
        Arrays.stream(applicationContext.getBeanDefinitionNames())
                .map(applicationContext::getBean)
                .filter(MockUtil::isMock)
                .forEach(mockCache::add);
    }
}
