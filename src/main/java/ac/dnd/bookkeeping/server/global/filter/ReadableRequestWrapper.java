package ac.dnd.bookkeeping.server.global.filter;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.Part;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

public class ReadableRequestWrapper extends HttpServletRequestWrapper {
    private final Charset encoding;
    private final Map<String, String[]> params = new HashMap<>();
    private final Collection<Part> parts;
    private final byte[] rawData;

    public ReadableRequestWrapper(final HttpServletRequest request) {
        super(request);
        this.params.putAll(request.getParameterMap());

        final String charEncoding = request.getCharacterEncoding();
        this.encoding = getEncoding(charEncoding);

        try {
            this.parts = getMultipartParts(request);
            this.rawData = request.getInputStream().readAllBytes();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Charset getEncoding(final String charEncoding) {
        if (StringUtils.isBlank(charEncoding)) {
            return UTF_8;
        }
        return Charset.forName(charEncoding);
    }

    private Collection<Part> getMultipartParts(final HttpServletRequest request) throws Exception {
        if (isMultipartRequest(request)) {
            return request.getParts();
        }
        return null;
    }

    private static boolean isMultipartRequest(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().startsWith(MULTIPART_FORM_DATA_VALUE);
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                throw new UnsupportedOperationException("[ReadableRequestWrapper] isFinished() not supported");
            }

            @Override
            public boolean isReady() {
                throw new UnsupportedOperationException("[ReadableRequestWrapper] isReady() not supported");
            }

            @Override
            public void setReadListener(final ReadListener listener) {

            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    public byte[] getContentAsByteArray() {
        return rawData;
    }
}
