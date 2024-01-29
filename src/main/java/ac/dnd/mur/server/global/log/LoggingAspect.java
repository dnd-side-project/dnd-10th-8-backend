package ac.dnd.mur.server.global.log;

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

    @Pointcut("execution(* ac.dnd.mur.server..*(..))")
    private void includeComponent() {
    }

    @Pointcut("""
            !execution(* ac.dnd.mur.server.global.annotation..*(..))
            && !execution(* ac.dnd.mur.server.global.base..*(..))
            && !execution(* ac.dnd.mur.server.global.config..*(..))
            && !execution(* ac.dnd.mur.server.global.decorator..*(..))
            && !execution(* ac.dnd.mur.server.global.dto..*(..))
            && !execution(* ac.dnd.mur.server.global.filter..*(..))
            && !execution(* ac.dnd.mur.server.global.log..*(..))
            && !execution(* ac.dnd.mur.server..*AnonymousRequestExceptionHandler.*(..))
            && !execution(* ac.dnd.mur.server..*Config.*(..))
            && !execution(* ac.dnd.mur.server..*Formatter.*(..))
            && !execution(* ac.dnd.mur.server..*Properties.*(..))
            && !execution(* ac.dnd.mur.server..*RequestTokenExtractor.*(..))
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
