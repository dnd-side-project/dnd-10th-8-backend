package ac.dnd.bookkeeping.server.common.config;

import ac.dnd.bookkeeping.server.global.log.LoggingStatusManager;
import ac.dnd.bookkeeping.server.global.log.LoggingTracer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@TestConfiguration
@EnableAspectJAutoProxy
public class TestAopConfig {
    @Bean
    public LoggingStatusManager loggingStatusManager() {
        return new LoggingStatusManager();
    }

    @Bean
    public LoggingTracer loggingTracer() {
        return new LoggingTracer(loggingStatusManager());
    }
}
