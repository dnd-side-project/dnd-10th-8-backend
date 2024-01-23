package ac.dnd.bookkeeping.server.global.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static ac.dnd.bookkeeping.server.global.log.MdcKey.START_TIME_MILLIS;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingTracer {
    private static final String REQUEST_PREFIX = "--->";
    private static final String RESPONSE_PREFIX = "<---";
    private static final String EXCEPTION_PREFIX = "<X--";

    private final LoggingStatusManager loggingStatusManager;

    public void methodCall(final String methodSignature, final Object[] args) {
        loggingStatusManager.syncStatus();

        final LoggingStatus loggingStatus = loggingStatusManager.getExistLoggingStatus();
        loggingStatusManager.increaseDepth();
        if (log.isInfoEnabled()) {
            log.info(
                    "{} args={}",
                    loggingStatus.depthPrefix(REQUEST_PREFIX) + methodSignature,
                    args
            );
        }
    }

    public void methodReturn(final String methodSignature) {
        final LoggingStatus loggingStatus = loggingStatusManager.getExistLoggingStatus();
        if (log.isInfoEnabled()) {
            log.info(
                    "{} time={}ms",
                    loggingStatus.depthPrefix(RESPONSE_PREFIX) + methodSignature,
                    calculateTakenTime()
            );
        }
        loggingStatusManager.decreaseDepth();
    }

    public void throwException(final String methodSignature, final Throwable exception) {
        final LoggingStatus loggingStatus = loggingStatusManager.getExistLoggingStatus();
        if (log.isInfoEnabled()) {
            log.info(
                    "{} time={}ms ex={}",
                    loggingStatus.depthPrefix(EXCEPTION_PREFIX) + methodSignature,
                    calculateTakenTime(),
                    exception.toString()
            );
        }
        loggingStatusManager.decreaseDepth();
    }

    private long calculateTakenTime() {
        final String key = MDC.get(START_TIME_MILLIS.name());

        if (StringUtils.hasText(key)) {
            return System.currentTimeMillis() - Long.parseLong(key);
        }
        return 0;
    }
}
