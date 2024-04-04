package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class StatCommandTest {

    @Test
    public void execute_statCommand_success() {
        StatCommand statCommand = new StatCommand();
        Model model = new ModelManager();
        CommandResult expectedCommandResult = new CommandResult(StatCommand.MESSAGE_SUCCESS, null, false, false, true, false);

        assertCommandSuccess(statCommand, model, expectedCommandResult, model);
    }

    @Test
    public void testEquals() {
        StatCommand statCommand1 = new StatCommand();
        StatCommand statCommand2 = new StatCommand();

        // same object -> returns true
        assertEquals(statCommand1, statCommand1);

        // different objects -> returns false
        assertNotEquals(statCommand1, statCommand2);

        // different types -> returns false
        assertNotEquals(statCommand1, new ViewCommand());

        // null -> returns false
        assertNotEquals(statCommand1, null);
    }
}
