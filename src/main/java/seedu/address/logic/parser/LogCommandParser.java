package seedu.address.logic.parser;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.LogCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;

public class LogCommandParser {
    public LogCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_HOURS, PREFIX_NOTES, PREFIX_STYLE, PREFIX_CONTENT);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ID, PREFIX_HOURS, PREFIX_NOTES, PREFIX_STYLE, PREFIX_CONTENT);

        if (!arePrefixesPresent(argMultimap, PREFIX_ID, PREFIX_HOURS, PREFIX_NOTES, PREFIX_STYLE, PREFIX_CONTENT)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LogCommand.MESSAGE_USAGE));
        }
        Id id = ParserUtil.parseId(argMultimap.getValue(PREFIX_ID).get());
        String hours = argMultimap.getValue(PREFIX_HOURS).get().trim();
        String content = argMultimap.getValue(PREFIX_CONTENT).get().trim();
        String style = argMultimap.getValue(PREFIX_STYLE).get().trim();
        String notes = argMultimap.getValue(PREFIX_NOTES).get().trim();

        Log logEntry = new Log(style, hours, content, notes, null);

        return new LogCommand(id.getInt(), logEntry);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
