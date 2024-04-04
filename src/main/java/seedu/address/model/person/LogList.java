package seedu.address.model.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogList {
    private List<Log> logs;
    public LogList(List<Log> logs) {
        this.logs = logs;
    }

    public void addEntry(Log log) {
        logs.add(log);
    }

    public List<Log> getList() {
        if (this.logs != null) {
            return this.logs;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        int index = 1;
        StringBuilder builder = new StringBuilder();
        Collections.reverse(logs);
        for (Log log : logs) {
            String entry = String.format("%d\n%s\n", index, log.toString());
            builder.append(entry);
            index++;
        }
        if (logs.isEmpty()) {
            builder.append("No logs yet!");
        }
        Collections.reverse(logs);
        return builder.toString();
    }
}
