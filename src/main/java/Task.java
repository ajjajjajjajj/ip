import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Task {
    protected String description;
    protected boolean isDone;
    protected String symbol;
    protected static final List<DateTimeFormatter> SUPPORTED_DATE_TIME_INPUT = new ArrayList<>();
    protected static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("EEE, dd MMM yy hh:mma");
    static {
        SUPPORTED_DATE_TIME_INPUT.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        SUPPORTED_DATE_TIME_INPUT.add(DateTimeFormatter.ofPattern("yyyy/MM/dd HHmm"));
    }

    public Task(String description)  {
        this.description = description;
        this.isDone = false;
    }

    // constructor for preloaded tasks
    public Task(String description, String isDone) {
        this.description = description;
        this.isDone = Integer.valueOf(isDone) == 1 ? true : false;
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

    public String getDataFormat() {
        String doneValue = isDone == true ? "1" : "0";
        return combineData(this.symbol, doneValue, this.description);
    }

    public String combineData(Object... objs) {
        String SEPARATOR = " | ";
        if (Objects.isNull(objs)) {
            return "";
        }
        StringBuilder result = new StringBuilder(objs[0].toString());
        for (int i = 1; i < objs.length; i++) {
            result.append(SEPARATOR);
            result.append(objs[i]);
        }
        result.append("\n");
        return result.toString();
    }

    @Override
    public String toString() {
        return "[" + this.symbol + "][" + this.getStatusIcon() +"] " + this.description;
    }

}