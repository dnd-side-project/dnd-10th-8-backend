package ac.dnd.bookkeeping.server.global.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static ac.dnd.bookkeeping.server.global.log.MdcKey.REQUEST_ID;
import static ac.dnd.bookkeeping.server.global.log.MdcKey.REQUEST_IP;
import static ac.dnd.bookkeeping.server.global.log.MdcKey.REQUEST_METHOD;
import static ac.dnd.bookkeeping.server.global.log.MdcKey.REQUEST_PARAMS;
import static ac.dnd.bookkeeping.server.global.log.MdcKey.REQUEST_TIME;
import static ac.dnd.bookkeeping.server.global.log.MdcKey.REQUEST_URI;
import static ac.dnd.bookkeeping.server.global.log.MdcKey.START_TIME_MILLIS;
import static ac.dnd.bookkeeping.server.global.log.RequestMetadataExtractor.getClientIP;
import static ac.dnd.bookkeeping.server.global.log.RequestMetadataExtractor.getHttpMethod;
import static ac.dnd.bookkeeping.server.global.log.RequestMetadataExtractor.getRequestUriWithQueryString;
import static ac.dnd.bookkeeping.server.global.log.RequestMetadataExtractor.getSeveralParamsViaParsing;

public class MdcLoggingFilter implements Filter {
    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain
    ) throws IOException, ServletException {
        setMdc((HttpServletRequest) request);
        chain.doFilter(request, response);
        MDC.clear();
    }

    private void setMdc(final HttpServletRequest request) {
        MDC.put(REQUEST_ID.name(), UUID.randomUUID().toString());
        MDC.put(REQUEST_IP.name(), getClientIP(request));
        MDC.put(REQUEST_METHOD.name(), getHttpMethod(request));
        MDC.put(REQUEST_URI.name(), getRequestUriWithQueryString(request));
        MDC.put(REQUEST_PARAMS.name(), getSeveralParamsViaParsing(request));
        MDC.put(REQUEST_TIME.name(), LocalDateTime.now().toString());
        MDC.put(START_TIME_MILLIS.name(), String.valueOf(System.currentTimeMillis()));
    }
}
