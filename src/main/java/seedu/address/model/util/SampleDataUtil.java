package seedu.address.model.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                    getTagSet("friends"), new Subject("test"),
                    new Id("000000"), getSampleExams(),
                    new Payment(0.0), new LogList(new ArrayList<>()))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    public static Set<Exam> getSampleExams() {
        // Sample exams
        Exam exam1 = new Exam(
                "exam1",
                LocalDate.of(2024, 12, 13), // Sample LocalDate
                Optional.of(LocalTime.of(13, 0)) // Sample LocalTime
        );

        Exam exam2 = new Exam(
                "exam2",
                LocalDate.of(2024, 12, 14), // Sample LocalDate
                Optional.empty() // Sample LocalTime
        );

        Exam exam3 = new Exam(
                "exam3",
                LocalDate.of(2024, 12, 15), // Sample LocalDate
                Optional.of(LocalTime.of(10, 0)) // Sample LocalTime
        );

        // Return a set containing all sample exams
        return Set.of(exam1, exam2, exam3);
    }

    public static LogList getSampleLogs() {
        Log log1 = new Log("visual", "2", "English Essay",
                "Page 20-30", "2023-09-18 12:05:05");
        Log log2 = new Log("audio", "3", "Math problems",
                "Finished workbook pg 26-30", "2023-10-09 13:09:32");
        Log log3 = new Log("combination", "2", "Organic Chemistry",
                "Finished 1 paper from pyp", "2023-12-24 14:03:18");
        List<Log> logList = List.of(log1, log2, log3);
        return new LogList(logList);
    }

}
