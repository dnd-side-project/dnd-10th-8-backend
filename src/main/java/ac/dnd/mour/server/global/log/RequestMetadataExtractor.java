package ac.dnd.mour.server.global.log;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.stream.Collectors;

public class RequestMetadataExtractor {
    public static String getClientIP(final HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static String getHttpMethod(final HttpServletRequest request) {
        return request.getMethod();
    }

    public static String getRequestUriWithQueryString(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        final String queryString = request.getQueryString();
        if (queryString != null) {
            requestURI += "?" + queryString;
        }

        return requestURI;
    }

    public static String getSeveralParamsViaParsing(final HttpServletRequest request) {
        return Collections.list(request.getParameterNames())
                .stream()
                .map(it -> "%s = %s".formatted(it, request.getParameter(it)))
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
