package ac.dnd.bookkeeping.server.global.exception;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ac.dnd.bookkeeping.server.global.log.RequestMetadataExtractor.getClientIP;
import static ac.dnd.bookkeeping.server.global.log.RequestMetadataExtractor.getRequestUriWithQueryString;
import static com.slack.api.webhook.WebhookPayloads.payload;

@Slf4j
@Component
public class SlackAlertManager {
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String LOG_COLOR = "FF0000";
    private static final String TITLE_REQUEST_IP = "[Request IP]";
    private static final String TITLE_REQUEST_URL = "[Request URL]";
    private static final String TITLE_ERROR_MESSAGE = "[Error Message]";

    private static final Slack slackClient = Slack.getInstance();

    private final String slackWebhookUrl;

    public SlackAlertManager(@Value("${slack.webhook.url}") final String slackWebhookUrl) {
        this.slackWebhookUrl = slackWebhookUrl;
    }

    public void sendErrorLog(final HttpServletRequest request, final Exception e) {
        try {
            slackClient.send(
                    slackWebhookUrl,
                    payload(it -> it
                            .text("서버 에러 발생!!")
                            .attachments(List.of(
                                    generateSlackErrorAttachments(e, request)
                            ))
                    ));
        } catch (final IOException ex) {
            log.error("Slack API 통신 간 에러 발생", ex);
        }
    }

    private Attachment generateSlackErrorAttachments(final Exception e, final HttpServletRequest request) {
        final String requestTime = DateTimeFormatter.ofPattern(TIME_FORMAT).format(LocalDateTime.now());
        return Attachment.builder()
                .color(LOG_COLOR)
                .title(requestTime + " 발생 에러 로그")
                .fields(List.of(
                        generateSlackField(TITLE_REQUEST_IP, getClientIP(request)),
                        generateSlackField(TITLE_REQUEST_URL, String.format(
                                "[%s] %s",
                                request.getMethod(),
                                getRequestUriWithQueryString(request)
                        )),
                        generateSlackField(TITLE_ERROR_MESSAGE, e.toString())
                ))
                .build();
    }

    private Field generateSlackField(final String title, final String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }
}
