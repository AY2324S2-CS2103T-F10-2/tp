package seedu.address.model.person;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private final String date;
    private final String learningStyle;
    private final String hours;
    private final String lessonContent;
    private final String notes;

    public Log(String learningStyle, String hours, String lessonContent, String notes, String date) {
        if (date == null) {
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = dateFormat.format(currentDate);
        }
        this.date = date;
        this.learningStyle = learningStyle;
        this.hours = hours;
        this.lessonContent = lessonContent;
        this.notes = notes;
    }

    public String getDate() {
        return this.date;
    }

    public String getLearningStyle() {
        return this.learningStyle;
    }

    public String getHours() {
        return this.hours;
    }

    public String getLessonContent() {
        return this.lessonContent;
    }

    public String getNotes() {
        return this.notes;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Log on %s\n", date))
                .append("Hours: " + hours + "\n")
                .append("Learning Style: " + learningStyle + "\n")
                .append("Lesson Content: " + lessonContent + "\n")
                .append("Notes: " + notes + "\n");
        return builder.toString();
    }
}
