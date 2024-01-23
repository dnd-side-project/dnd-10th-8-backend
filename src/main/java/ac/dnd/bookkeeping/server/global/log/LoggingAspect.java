package ac.dnd.bookkeeping.server.global.log;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private final LoggingTracer loggingTracer;

    @Pointcut("execution(* ac.dnd.bookkeeping.server..*(..))")
    private void includeComponent() {
    }

    @Pointcut("""
            !execution(* ac.dnd.bookkeeping.server.global.annotation..*(..))
            && !execution(* ac.dnd.bookkeeping.server.global.base..*(..))
            && !execution(* ac.dnd.bookkeeping.server.global.config..*(..))
            && !execution(* ac.dnd.bookkeeping.server.global.decorator..*(..))
            && !execution(* ac.dnd.bookkeeping.server.global.dto..*(..))
            && !execution(* ac.dnd.bookkeeping.server.global.filter..*(..))
            && !execution(* ac.dnd.bookkeeping.server.global.log..*(..))
            && !execution(* ac.dnd.bookkeeping.server..*Config.*(..))
            && !execution(* ac.dnd.bookkeeping.server..*Formatter.*(..))
            && !execution(* ac.dnd.bookkeeping.server..*Properties.*(..))
            && !execution(* ac.dnd.bookkeeping.server..*RequestTokenExtractor.*(..))
            """)
    private void excludeComponent() {
    }

    @Around("includeComponent() && excludeComponent()")
    public Object doLogging(final ProceedingJoinPoint joinPoint) throws Throwable {
        final String methodSignature = joinPoint.getSignature().toShortString();
        final Object[] args = joinPoint.getArgs();
        loggingTracer.methodCall(methodSignature, args);
        try {
            final Object result = joinPoint.proceed();
            loggingTracer.methodReturn(methodSignature);
            return result;
        } catch (final Throwable e) {
            loggingTracer.throwException(methodSignature, e);
            throw e;
        }
    }
}
