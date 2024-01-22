package ac.dnd.bookkeeping.server.global.config.etc;

import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import static com.p6spy.engine.logging.Category.STATEMENT;
import static java.util.Locale.ROOT;
import static org.hibernate.engine.jdbc.internal.FormatStyle.BASIC;
import static org.hibernate.engine.jdbc.internal.FormatStyle.DDL;

public class P6SpyFormatter extends JdbcEventListener implements MessageFormattingStrategy {
    @Override
    public String formatMessage(
            final int connectionId,
            final String now,
            final long elapsed,
            final String category,
            final String prepared,
            String sql,
            final String url
    ) {
        final StringBuilder sb = new StringBuilder();

        sql = format(category, sql);
        sb.append(category)
                .append(" -> ")
                .append("[쿼리 수행시간 = ")
                .append(elapsed).append("ms")
                .append(" | DB 커넥션 정보(Connection ID) = ")
                .append(connectionId)
                .append("]")
                .append(sql);

        return sb.toString();
    }

    private String format(final String category, String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return sql;
        }

        if (STATEMENT.getName().equals(category)) {
            final String tmpsql = sql.trim().toLowerCase(ROOT);
            if (tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                sql = DDL.getFormatter().format(sql);
            } else {
                sql = BASIC.getFormatter().format(sql);
            }
        }

        return sql;
    }
}
