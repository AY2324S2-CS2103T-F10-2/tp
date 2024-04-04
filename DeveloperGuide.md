---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------
s
## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### View all feature

#### Implementation

The mechanism is similar to list feature in `AddressBook`. parser checks for `-all` flag and execute showing the entire list of students.

The feature can return user to the whole list after user uses view -id/view -name function to see specific student. A list of students will only be displayed if there are at least 1 student added to TuteeTally.

Below is a sequence diagram of how view all interacts with multiple classes.

<puml src="diagrams/ViewAllSequenceDiagram.puml" alt="ViewAllSequenceDiagram" />

#### Design considerations:
**Aspect: How view all executes:**
* **Alternative 1 (current choice):** In a view command class with view -id/-name.
    * Pros: Clear execution line.
    * Cons: Several if else checks, more prone to errors.

* **Alternative 2:** Separate classes for each view command
    * Pros: Easy to implement, less merge conflicts.
    * Cons: Large number of files and parsers needed.

### View student feature
The `view` command is a feature that allows the user to find details related to student(s) and retrieve their details.
It consists of 3 variants
1. `view -all` : shows all students currently recorded in TuteeTally.
2. `view -name`: shows all students recorded with their name, or part of their name matching the input.
2. `view -id` : finds (unique) student associated with the unique id.
3. `view -stats` : opens a popup for summary statistics with regard to all students.
#### Proposed Implementation
The checking of which variant of `view` is triggered is detected based on the presence of prefixes in `ViewCommandParser#parse`.

If more than one valid prefix `-all`, `-name`, `-id`, or `stats` are present, `ViewCommandParser` creates `ViewCommands` in the following order of precedence:
* `all` > `name` > `id` > `stats`

Below is an example usage scenario where the `view -stats` command was entered.

**Step 1:** User first calls `view -stats`. The input is passed into ``AddressBookParser`` which instantiates a ``ViewCommandParser`` instance.
The `ViewCommandParser` uses ``ViewCommandParser#arePrefixesPresent`` to check for presence of the ``-add`` prefix.

<puml src="diagrams/ViewParserSequenceDiagram0.puml" />

**Step 2:** The check for ``-add`` prefix returns false, and a similar check routine for prefixes is carried out for ``-name`` and ``-id``
<puml src="diagrams/ViewParserSequenceDiagram1.puml" />

**Step 3:** All checks for prefixes return false, and falls into the default case. A ``CommandResult`` with the ``isStatsCommand`` set to true is returned
<puml src="diagrams/ViewParserSequenceDiagram2.puml" />

For the prefixes ``-name`` and ``-id``, a filtered list containing the search results will be returned.
Both variants utilize a similar logic to of passing in a ``prefix`` to ``model#updateFilteredPersonList`` to adjust the entries displayed by the GUI.
<puml src="diagrams/ViewIdSequenceDiagram.puml" />

#### Design considerations:
**Aspect: How to check which command to execute:**

* **Alternative 1 (current choice):** Goes through checks for presence of prefix variants, and execute the first detected based on the precedence of ``-all`` > ``-name`` > ``-id`` > ``-stats``
    * Pros: Easy to implement.
    * Cons: If variants increase in the future, might take extra time falling through the conditional checks. Precedence of prefixes can also not be aligned with what the user intended.

* **Alternative 2:** Executes all variants given in the command
  itself.
    * Pros: More intuitive for user, gives quick overview of multiple view commands.
    * Cons: Difficult to implement and requires drastic changes in GUI.




--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* a tutor that has a need to manage a significant number of contacts of students
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:

* Easier time managing their contacts (fast retrieval of relevant info)
* More organization & personalisation
* Easier time to track Student’s grades and weaknesses
* Manage parent’s expectations
* Easy tracking of payment



### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​ | I want to …​                              | So that I can…​                                           |
|----------|---------|-------------------------------------------|-----------------------------------------------------------|
| `* * *`  | Tutor   | add a student                             | track the details of the student                          |
| `* * *`  | Tutor   | view student details summary on main page | get a brief idea of the student while navigating the list |
| `* * *`  | Tutor   | delete a person                           | remove entries that I no longer need                      |
| `* * *`  | Tutor   | view single students detail               | see the individual detail for a single student            |
| `* * *`  | Tutor   | view total number of students             | check if I have space for more students                   |


*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add a Student**

**MSS** (Main Success Story)

1. User initiates the command to add a student by providing the student's name, address, contact number, subject, and level.

2. The AddressBook processes the provided information, adds the student particulars into the system, and assigns a unique ID to the student.

3. AddressBook displays a confirmation message along with the details of the newly added student at the top of the list.

   Use case ends.

**Extensions**

* 1a. User inputs the command in an incorrect format.

   * 1a1. AddressBook shows an error message and the correct command format.

   Use case ends.

* 1b. User enters a name that already exists in the AddressBook.

   * 1b1. AddressBook generates and assigns a unique ID to the new student to avoid duplication.

   Use case resumes at step 2.

* 1c. User omits a required field in the command.

   * 1c1. AddressBook shows an error message indicating the missing field.

   Use case ends.

**Use case: View Student Detail**

**MSS**

1. User requests to view details of students either by listing all or searching by name or ID.

2. AddressBook retrieves and shows the relevant student details based on the request.

   Use case ends.

**Extensions**

* 2a. The requested student does not exist or the list is empty.

   * 2a1. AddressBook displays a message indicating no such student exists or the list is empty.

   Use case ends.

* 2b. User inputs an incorrect command format for viewing details.

   * 2b1. AddressBook shows an error message and the correct command format.

Use case ends.

**Use case: View Summary Statistics**

**MSS**

1. User requests to view summary statistics of students.

2. AddressBook processes the request and displays the total number of students along with other relevant statistics.

   Use case ends.

**Extensions**

* 2a. There is an error in processing the request.

   * 2a1. AddressBook displays an error message in red.

   Use case ends.

**Use case: Delete a Student**

**MSS**

1.  User requests to list Student
2.  AddressBook shows a list of Student
3.  User requests to delete a specific Student in the list
4.  AddressBook deletes the Student

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.






### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  System should provide quick responses to user commands.
5.  Should have user-friendly design for interface, so it is intuitive to use.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
