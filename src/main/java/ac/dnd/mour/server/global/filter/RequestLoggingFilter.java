package ac.dnd.mour.server.global.filter;

import ac.dnd.mour.server.global.log.LoggingStatusManager;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static ac.dnd.mour.server.global.log.MdcKey.REQUEST_ID;
import static ac.dnd.mour.server.global.log.MdcKey.REQUEST_IP;
import static ac.dnd.mour.server.global.log.MdcKey.REQUEST_METHOD;
import static ac.dnd.mour.server.global.log.MdcKey.REQUEST_PARAMS;
import static ac.dnd.mour.server.global.log.MdcKey.REQUEST_TIME;
import static ac.dnd.mour.server.global.log.MdcKey.REQUEST_URI;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class RequestLoggingFilter implements Filter {
    private static final String EMPTY_RESULT = "{ Empty }";

    private final LoggingStatusManager loggingStatusManager;
    private final Set<String> ignoredUrls = new HashSet<>();

    public RequestLoggingFilter(final LoggingStatusManager loggingStatusManager, final String... ignoredUrls) {
        this.loggingStatusManager = loggingStatusManager;
        this.ignoredUrls.addAll(Arrays.asList(ignoredUrls));
    }

    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain
    ) throws ServletException, IOException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (CorsUtils.isPreFlightRequest(httpRequest) || isIgnoredUrl(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        final StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();

            loggingStatusManager.syncStatus();
            loggingRequestInfo(httpRequest);

            chain.doFilter(request, response);
        } finally {
            stopWatch.stop();

            loggingResponseInfo(httpResponse, stopWatch);
            loggingStatusManager.clearResource();
        }
    }

    private boolean isIgnoredUrl(final HttpServletRequest request) {
        return PatternMatchUtils.simpleMatch(ignoredUrls.toArray(String[]::new), request.getRequestURI());
    }

    private void loggingRequestInfo(final HttpServletRequest httpRequest) {
        log.info(
                "[Request START] = [Task ID = {}, IP = {}, HTTP Method = {}, Uri = {}, Params = {}, 요청 시작 시간 = {}]",
                MDC.get(REQUEST_ID.name()),
                MDC.get(REQUEST_IP.name()),
                MDC.get(REQUEST_METHOD.name()),
                MDC.get(REQUEST_URI.name()),
                MDC.get(REQUEST_PARAMS.name()),
                MDC.get(REQUEST_TIME.name())
        );
        log.info("Request Body = {}", readRequestData(httpRequest));
    }

    private String readRequestData(final HttpServletRequest request) {
        if (request instanceof final ReadableRequestWrapper requestWrapper) {
            final byte[] bodyContents = requestWrapper.getContentAsByteArray();

            if (bodyContents.length == 0) {
                return EMPTY_RESULT;
            }
            return new String(bodyContents, StandardCharsets.UTF_8);
        }
        return EMPTY_RESULT;
    }

    private void loggingResponseInfo(final HttpServletResponse httpResponse, final StopWatch stopWatch) {
        log.info("Response Body = {}", readResponseData(httpResponse));
        log.info(
                "[Request END] = [Task ID = {}, IP = {}, HTTP Method = {}, Uri = {}, HTTP Status = {}, 요청 처리 시간 = {}ms]",
                MDC.get(REQUEST_ID.name()),
                MDC.get(REQUEST_IP.name()),
                MDC.get(REQUEST_METHOD.name()),
                MDC.get(REQUEST_URI.name()),
                httpResponse.getStatus(),
                stopWatch.getTotalTimeMillis()
        );
    }

    private Object readResponseData(final HttpServletResponse response) {
        if (response instanceof final ContentCachingResponseWrapper responseWrapper) {
            final byte[] bodyContents = responseWrapper.getContentAsByteArray();

            if (bodyContents.length == 0) {
                return EMPTY_RESULT;
            }
            return createResponse(bodyContents);
        }
        return EMPTY_RESULT;
    }

    private String createResponse(final byte[] bodyContents) {
        final String result = new String(bodyContents, UTF_8);
        if (result.contains("</html>")) {
            return EMPTY_RESULT;
        }
        return result;
    }
}
