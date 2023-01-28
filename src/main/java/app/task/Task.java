package app.task;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import app.chatbot.Storage;

public abstract class Task {
    protected static final List<DateTimeFormatter> SUPPORTED_DATE_TIME_INPUT = new ArrayList<>();
    protected static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("EEE, dd MMM yy hh:mma");

    protected String description;
    protected boolean isDone;
    protected String symbol;
    static {
        SUPPORTED_DATE_TIME_INPUT.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        SUPPORTED_DATE_TIME_INPUT.add(DateTimeFormatter.ofPattern("yyyy/MM/dd HHmm"));
        SUPPORTED_DATE_TIME_INPUT.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public boolean isDone() {
        return this.isDone;
    }

    public String getDesc() {
        return this.description;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void unmarkDone() {
        isDone = false;
    }

    public abstract String asDataFormat();

    protected String asDataFormat(String... fields) {
        String base = String.join(Storage.SEPARATOR, this.symbol, this.isDone() ? "1" : "0", this.description);
        for (String s : fields) {
            if (!s.isBlank()) {
                base = String.join(Storage.SEPARATOR, base, s);
            }
        }
        return base;
    }

    @Override
    public String toString() {
        return "[" + this.symbol + "][" + this.getStatusIcon() + "] " + this.description;
    }
}
