package ac.dnd.bookkeeping.server.global.log;

import lombok.Getter;

@Getter
public class LoggingStatus {
    private final String taskId;
    private final long startTimeMillis;

    private int depthLevel = 0;

    public LoggingStatus(final String taskId) {
        this.taskId = taskId;
        this.startTimeMillis = System.currentTimeMillis();
    }

    public void increaseDepth() {
        depthLevel++;
    }

    public void decreaseDepth() {
        depthLevel--;
    }

    public String depthPrefix(final String prefixString) {
        if (depthLevel == 1) {
            return "|" + prefixString;
        }
        final String bar = "|" + " ".repeat(prefixString.length());
        return bar.repeat(depthLevel - 1) + "|" + prefixString;
    }
}
