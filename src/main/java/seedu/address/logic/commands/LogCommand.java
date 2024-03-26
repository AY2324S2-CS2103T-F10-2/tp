package seedu.address.logic.commands;

import com.sun.nio.sctp.MessageInfo;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Log;
import seedu.address.model.person.Person;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class LogCommand extends Command {
    public static final String COMMAND_WORD = "log";
    public static final String MESSAGE_SUCCESS = "Student logged successfully";
    public static final String MESSAGE_NO_SUCH_PERSON = "No student with this id exists!";
    public static final String MESSAGE_POSITIVE_INTEGER_AND_ZERO =
            "The unique ID must be a positive integer and/or zero";

    private final int targetId;
    private final Log logDetails;

    public LogCommand(int targetId, Log log) {
        requireNonNull(targetId);
        requireNonNull(log);
        this.targetId = targetId;
        this.logDetails = log;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetId >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        if (targetId < 0) { // Positive Integer or 0, to discuss
            throw new CommandException(MESSAGE_POSITIVE_INTEGER_AND_ZERO);
        }

        Person targetPerson = lastShownList.get(targetId);

        if (targetPerson == null) {
            throw new CommandException(MESSAGE_NO_SUCH_PERSON);
        }

        model.addLog(targetPerson, logDetails);
    }
}
