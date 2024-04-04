package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.*;

public class JsonAdaptedLog {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Log's %s field is missing!";

    private final String date;
    private final String learningStyle;
    private final String hours;
    private final String lessonContent;
    private final String notes;

    /**
     * Constructs a {@code JsonAdaptedLog} with the given log details.
     */
    @JsonCreator
    public JsonAdaptedLog(@JsonProperty("date") String date, @JsonProperty("learningStyle") String learningStyle,
                             @JsonProperty("hours") String hours, @JsonProperty("lessonContent") String lessonContent,
                             @JsonProperty("notes") String notes) {
        this.date = date;
        this.learningStyle = learningStyle;
        this.hours = hours;
        this.lessonContent = lessonContent;
        this.notes = notes;
    }

    /**
     * Converts a given {@code Log} into this class for Jackson use.
     */
    public JsonAdaptedLog(Log source) {
        date = source.getDate();
        learningStyle = source.getLearningStyle();
        hours = source.getHours();
        lessonContent = source.getLessonContent();
        notes = source.getNotes();
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Log} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Log toModelType() throws IllegalValueException {
        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "date"));
        }

        if (learningStyle == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,"learning style"));
        }

        if (hours == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "hours"));
        }

        if (lessonContent == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "lesson content"));
        }

        if (notes == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "notes"));
        }

        return new Log(learningStyle, hours, lessonContent, notes, date);
    }

}
