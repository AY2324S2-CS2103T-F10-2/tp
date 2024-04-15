---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# TuteeTally Developer Guide

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

**`Main`** (consisting of classes [`Main`](https://github.com/AY2324S2-CS2103T-F10-2/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2324S2-CS2103T-F10-2/tp/blob/master/src/main/java/seedu/address/Main.java)) is in charge of the app launch and shut down.
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

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2324S2-CS2103T-F10-2/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2324S2-CS2103T-F10-2/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2324S2-CS2103T-F10-2/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2324S2-CS2103T-F10-2/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

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
**API** : [`Model.java`](https://github.com/AY2324S2-CS2103T-F10-2/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" alt="Model Class Diagram of the Person" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list and a `AllExamList` in the `AddressBook`, which `Person` references. 
This allows `AddressBook` to only require one `Tag` object per unique tag and correspondingly one `Exam` object per unique exam, instead of each `Person` needing their own `Tag` and `Exam` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2324S2-CS2103T-F10-2/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

The Commons package in the `seedu.address` contains classes that are shared across various components of the application. 
This ensures that common functionalities are easily accessible across the system and thus promote code reuse. This also simplifies the task of 
updating or enhancing functionality in one place. 

Below is a breakdown of the main categories within this package:

-`Core`
This category includes essential classes that are central to the application's operation:

    -Config: Manages configuration settings of the application, such as file paths and application-level settings. 
            It helps in maintaining a flexible codebase that can adapt to different deployment environments without requiring code changes.

    -GuiSettings: Holds GUI configuration details which can be serialized for persistence across sessions. This class includes settings such as window size, window position, and other UI-related preferences that enhance the user's experience by maintaining a consistent application state.

    -LogsCenter: Provides a central management facility for logging messages throughout the application. It configures the logging libraries and specifies the uniform format and logging levels, making the debugging process and monitoring of runtime behaviors more systematic.


- `Exceptions`
This category defines custom exceptions that handle specific error situations unique to the application:
  
- `Util`
Utility classes that provide helper functions and shared functionalities used by multiple components:

--------------------------------------------------------------------------------------------------------------------

# **Implementation**

## Student Details Retrieval System

### Introduction

This section of the developer guide covers the functionalities provided for retrieving student related details. This includes finding a student with a specific id or name, adding and checking past logs of a student, and retrieving summary statistics of all students.

### Features Overview

This section describes some noteworthy details on how certain features are implemented.
- **Viewing all**: Displays full student list.
- **Viewing by name**: Displays students with at least one keyword in their name.
- **Viewing by unique ID**: Displays specific student with a matching unique ID.
- **Adding log of a student**: Adds a log to the log list attached to the student.
- **Viewing logs of a student**: Displays the specific student's log list.
- **Viewing summary statistics**: Displays summary statistics such as number of students and total outstanding payment.

### View student feature
The `view` command is a feature that allows the user to find and retrieve details related to student(s).
It consists of 4 variants
1. `view -all` : shows all students currently recorded in TuteeTally.
2. `view -name`: shows all students recorded with their name, or part of their name matching the input.
2. `view -id` : finds (unique) student associated with the unique id, and opens the log window.
3. `view -stats` : opens a popup for summary statistics with regard to all students.

#### Implementation
The checking of which variant of `view` is triggered is detected based on the presence of prefixes in `ViewCommandParser#parse`.

Only one prefix is allowed to be in the command format at once. If more than one prefix is present, the user will receive an error message to remind them only one prefix is allowed. 

#### Design considerations:
**Aspect: What to do when encountering more than 1 prefix:**

* **Alternative 1 (current choice):** Throws an error and prompts the user that only 1 prefix is allowed
    * Pros: Easy to implement, unambiguous and structured.
    * Cons: User will only be able to check the details provided by one view command at one time.

* **Alternative 2:** Executes all variants given in the command.
    * Pros: Gives quick overview of multiple view commands at once.
    * Cons: Difficult to implement and requires drastic changes in GUI.

### Example invocation procedure for view -stats command
**Step 1:** User first calls `view -stats`. The input is passed into ``AddressBookParser`` which instantiates a ``ViewCommandParser`` instance.
The `ViewCommandParser` uses ``ViewCommandParser#arePrefixesPresent`` to check for presence of the ``-add`` prefix.

<puml src="diagrams/ViewParserSequenceDiagram0.puml" />

**Step 2:** The check for ``-add`` prefix returns false, and a similar check routine for prefixes is carried out for ``-name`` and ``-id``
<puml src="diagrams/ViewParserSequenceDiagram1.puml" />

**Step 3:** The check for ``-stats`` return true, and a ``StatCommmand`` instance is returned to the ``LogicManager``.
The ``LogicManager`` then executes ``StatCommand`` which returns a  ``CommandResult`` with the ``isStatsCommand`` set to true.
<puml src="diagrams/ViewParserSequenceDiagram2.puml" />

### View all feature
This feature allows the user to see all current students stored in the app.
#### Implementation

The mechanism is similar to list feature in `AddressBook`. Parser checks for `-all` flag using the sequence above and execute showing the entire list of students.

The feature can return user to the whole list after user uses `view -id/view -name` function to see specific student. A list of students will only be displayed if there is at least 1 student added to TuteeTally.

Below is a sequence diagram of how view all interacts with multiple classes.

<puml src="diagrams/ViewAllSequenceDiagram.puml" alt="ViewAllSequenceDiagram" />

#### Design considerations:
**Aspect: How view all executes:**
* **Alternative 1 (current choice):** In a view command class with view -id/-name.
    * Pros: Easy to implement, clear execution line.
    * Cons: Several if else checks, more prone to errors.

* **Alternative 2:** Separate classes for view -all and other view commands
    * Pros: Easy to implement, less merge conflicts.
    * Cons: More files and parsers needed, might be difficult to navigate.

### Filtering for students using view
For the prefixes ``-name`` and ``-id``, a filtered list containing the search results will be returned.
Both variants utilize a similar logic to of passing in a ``prefix`` to ``model#updateFilteredPersonList`` to adjust the entries displayed by the GUI.
<puml src="diagrams/ViewNameSequenceDiagram.puml" alt="ViewNameSequenceDiagram" />

### View Name
This feature allows the user to find all students with at least one matching keyword in their name.

#### Implementation
This mechanism is similar to the `find` command in `AddressBook`. Parser checks for the `-name` flag using the sequence above and places the keywords into a `NameContainsKeywordsPredicate`.<br>
This command will display any student with at least **one keyword** fully matching with a part of the name. (e.g. `John` keyword will display `John Lim` but not `Joh Ng`). If there are no such students, an empty list will be displayed.<br>

#### Design considerations:
**Aspect: How view name finds students to display:**
* **Alternative 1 (current choice):** Returns students only if a part of their name **fully** matches a keyword.
    * Pros: Easy to implement, only needs to check for equality of Strings.
    * Cons: Prone to typos by the user.

* **Alternative 2:** Returns students as long as their name contains **all the characters** of any keyword, and they appear in the **right order**.
    * Pros: Enables more extensive searching, will allow room for typos.
    * Cons: Harder to implement, user need to look through redundant names. (e.g. User wants to find student called `John`, but has to first scroll through `Jo` `Jon` and even `James Ong`)

### View student and their logs by ID Feature
This feature allows the user to search for a specific student with the corresponding ID. The list of all logs of the target student will also be displayed in a popup.

#### Implementation
Parser checks for the `-id` flag using the sequence above and checks if the id is a valid id. Then, it passes the id into a `IsSameIdPredicate` to filter for the student. <br>
This command will display the student with the matching id, and open a popup containing their log information. If such a student does not exist, a prompt will be given to the user to retry.
Below is the activity diagram of how `view -id` executes.
<puml src="diagrams/ViewIdActivityDiagram.puml"/>

### View Stats feature
This feature supports the viewing of summary statistics, it currently shows the 
- the total number of students
- the total amount owed by students (Currently shows the exact amount)
- the number of upcoming exams in following 1 month period (from today up to the same day of the next month)

#### Implementation
Currently, the Summary Stats Window can be accessed in 3 ways.
1. Typing `view -stats` in the command box.
2. Pressing the `F2` key on your keyboard.
3. Clicking the `Stats` dropdown menu on the top of TuteeTally and accessing the Summary stats from there.

The Summary Stats window fetches the necessary stats from `logic`, which fetches the necessary data to update the statistics.
* getTotalPersons() to retrieve the total number of students.
* getTotalOwings() to calculate the total tuition fees owed.
* getUpcomingMonthExamCount() to count the exams scheduled for the upcoming month.

A Sequence Diagram can be seen below to show the interaction between the different class once "view -stats" is called:

<puml src="diagrams/ViewStatsSequenceDiagramMain.puml" alt="ViewStatsSequenceDiagramMain" />

The frame below shows how the logic class gets the command result.

<puml src="diagrams/ViewStatsSequenceDiagramGetCommandResult.puml" alt="ViewStatsSequenceDiagramGetCommandResult" />

The CommandResult will then be returned to the UIManager and a SummaryStatsWindow Instance will be created, the Sequence diagram below shows how
it get the SummaryStats from `Logic`the respective frame will show `SummaryStatsWindow::updateSummaryStats` clearly. 

<puml src="diagrams/ViewStatsSequenceDiagramUpdateTotalCountofPersons.puml" alt="ViewStatsSequenceDiagramUpdateTotalCountofPerson" />
<puml src="diagrams/ViewStatsSequenceDiagramUpdateTotalOwingsOfPerson.puml" alt="ViewStatsSequenceDiagramUpdateTotalOwingsOfPerson" />
<puml src="diagrams/ViewStatsSequenceDiagramUpdateUpcomingExams.puml" alt="ViewStatsSequenceDiagramUpdateUpcomingExams" />











#### Design Considerations
**Aspect: Where to store the SummaryStats:**

* **Alternative 1 (current choice):** The Summary Stats is stored in the `UniquePersonsList` and is updated everytime their respective field get updated.
  * Pros: Easy access to this information and ensures that the statistics are always updated.
  * Cons: High coupling, changes in the UniquePersonsList may have ripple effects on the parts of the application that depend on summary statistics.

* **Alternative 2:** Compute statistics on demand
    * Pros: Decreases the coupling between the data management and data viewing functionalities.
    * Cons: Could lead to a delay in presenting statistics to the user since computations are performed at the time of request.

**User Experience**

* **Useful Information**: We want the user to have useful information in a quick manner to aid their tuition administration.
* **Many ways to access**: We want the user to have many ways to open the Summary Stats Window.

### Add Log feature
This command is has a similar mechanism to the `add` feature, but targets a specific student instead. <br>
This feature enables tutors to log session specific details for record to a specific student. Each log entry includes the total hours of the lesson, lesson content, the learning style of the student, as well as any additional notes. The date of the log entry is recorded as the system time when the user added the log.

#### Implementation
The parser first checks if the there exists a student with the `ID` specified using the `-id` in current records. Then, the app adds the log entry to the end of the log list attached as a field to the student.
Below is the sequence diagram of how the `log` command interacts with multiple classes.
<puml src="diagrams/LogSequenceDiagram.puml" alt="LogSequenceDiagram.puml" />

#### Design Considerations
**Aspect: Whether all fields in log should be compulsory**
* **Alternative 1 (current choice):** All fields are compulsory
    * Pros: Reminds user of all possible fields, gives structure to the display format of the log entries.
    * Cons: Troublesome for user to enter all fields, mandates some content even if they deem it unnecessary
* **Alternative 2:** Only some fields like hours are compulsory
    * Pros: Allows flexibility in user input, saves time for user
    * Cons: Log entries will not be structured. Tutor may also accidentally forget important information to log, such as a change in learning style or lesson content covered.
      As we are currently in the early stage of development and have yet to carry out user testing for what fields should be compulsory, we chose to mandate every field so tutors are reminded of all possibly important aspects to record.

**Aspect: Restrictions on log feature fields' contents**
* **Alternative 1 (current choice):** All fields are strings, and can be empty
    * Pros: Easy to implement, flexible, allowing customised inputs like `45 minutes` for hours instead of restricting to an integer. Allowing empty fields partially mitigates aforementioned con of being forced to enter content even if the user deems it unnecessary, all while still providing a reminder to possibly important fields.
    * Cons: Prone to user errors, which can make the log entry messy.
* **Alternative 2:** All fields have their own required format and data types.
    * Pros: More systematic, less prone to user errors and typos.
    * Cons: More complicated to implement and use.

## Student Payment Management System

### Introduction

This section of the developer guide covers the functionalities provided for managing student payments. It includes adding payments, marking payments as paid, and resetting payment statuses for students. These features are integral to maintaining accurate and up-to-date financial records for each student.

### Features Overview

- **Add Payment**: Allows the addition of payment records to student accounts using unique identifiers.
- **Mark Payment**: Marks payments as completed for students, indicating that a payment has been made.
- **Reset Payments**: Resets the payment status of students, useful in scenarios where the total payment amount is fulfilled or adjustments are needed.

### Add Payment Feature

The `AddPaymentCommand` enables users to add payment records to students by specifying a unique student ID and the payment amount.
<puml src="diagrams/AddPaymentSequenceDiagram.puml" alt="AddPaymentSequenceDiagram" />


#### Implementation

1. The user inputs a command with the `-addpayment` flag, followed by the student's `uniqueId` and the amount.
2. The system parses this command, extracting the necessary details.
3. A new payment record is created and added to the student's account in the system.
   <puml src="diagrams/AddPaymentActivityDiagram.puml" alt="AddPaymentActivityDiagram" />

### Mark Payment Feature

The `MarkPaymentCommand` allows marking a student's payment as completed. This is typically used once a payment has been processed or received.
<puml src="diagrams/MarkPaymentSequenceDiagram.puml" alt="MarkPaymentSequenceDiagram" />


#### Implementation

1. The user inputs a command with the `-markpayment` flag, followed by the student's `uniqueId`.
2. The system identifies the corresponding student record and updates the payment status to reflect that it has been paid.
3. A confirmation is returned to the user upon successful update.
   <puml src="diagrams/MarkPaymentActivityDiagram.puml" alt="MarkPaymentActivityDiagram" />


### Reset Payments Feature

This feature enables the system to reset the payment status of students, which is useful when a student has fully paid their dues or when adjustments to their payment records are needed.
<puml src="diagrams/ResetPaymentsSequenceDiagram.puml" alt="ResetPaymentsSequenceDiagram" />


#### Implementation

1. A specific command with the `-resetpayments` flag and the student's `uniqueId` is issued by the user.
2. The system locates the student's record and resets the payment information, clearing any completed payments or dues.
3. A success message is sent to the user, confirming the reset. <br>
   <puml src="diagrams/ResetPaymentsActivityDiagram.puml" alt="ResetPaymentsActivityDiagram" />

### Conclusion

This guide provides a concise overview of the payment management functionalities within the system, designed to assist developers in understanding and utilizing these features effectively. For further details or clarification, please refer to the system documentation or contact the development team.

## Student Exam Management

### Introduction

This section covers the exam management system including add exam and delete exam.

### Features Overview

Add Exam: Allows the addition of exam records to student accounts using unique identifiers.
Delete Exam: Enables the deletion of exam records from student accounts.

### Add Exam Feature

The AddExamCommand enables users to add exam records to students by specifying a unique student ID, exam name, exam date, and optionally, exam time.
<puml src="diagrams/AddExamSequenceDiagram.puml" alt="AddExamSequenceDiagram" />

#### Implementation

1. The user inputs a command with the -addexam flag, followed by the student's uniqueId, exam name, exam date, and optionally, exam time. 
2. The system parses this command, extracting the necessary details. 
3. A new exam record is created and added to the student and the AllExamsList in the system.
<puml src="diagrams/AddExamActivityDiagram.puml" alt="AddExamActivityDiagram" />

### Delete Exam Feature

The DeleteExamCommand allows deleting a specific exam record from a student.
<puml src="diagrams/DeleteExamSequenceDiagram.puml" alt="DeleteExamSequenceDiagram" />

#### Implementation

The user inputs a command with the -deleteexam flag, followed by the student's uniqueId, exam name, exam date, and optionally, exam time.
The system identifies the corresponding student and the specified exam.
The system removes the specified exam record from the student and AllExamsList.
<puml src="diagrams/DeleteExamActivityDiagram.puml" alt="DeleteExamActivityDiagram" />

### Conclusion

These descriptions provide an overview of the exam management features, their purposes, and how they are implemented in the system. They also include sequence diagrams illustrating the interactions between the user and the system for each feature. For further details or clarification, please refer to the system documentation or contact the development team.


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
* Logging of Lessons for retrieval in the future
* Easily track Exams dates



### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​ | I want to …​                        | So that I can…​                                           |
|----------|---------|-------------------------------------|-----------------------------------------------------------|
| `* * *`  | Tutor   | add a student                       | track the details of the student                          |
| `* * *`  | Tutor   | view student details summary on main page | get a brief idea of the student while navigating the list |
| `* * *`  | Tutor   | delete a person                     | remove entries that I no longer need                      |
| `* * *`  | Tutor   | view single students detail         | see the individual detail for a single student            |
| `* * *`  | Tutor   | view total number of students       | check if I have space for more students                   |
| `* * *`  | Tutor   | track my payments                   | won't miss out on any payments                            |
| `* * *`  | Tutor   | track my students' exams            | personalise and plan better for lessons                   |


*{More to be added}*

### Use cases

<puml src="diagrams/UseCaseDiagram.puml" alt="UseCaseDiagram" />
(For all use cases below, the **System** is`TuteeTally` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add a Student**

**MSS** (Main Success Story)

1. User initiates the command to add a student by providing the student's name, address, contact number, subject, and level.

2. TuteeTally processes the provided information, adds the student particulars into the system, and assigns a unique ID to the student.

3. TuteeTally displays a confirmation message along with the details of the newly added student at the top of the list.

   Use case ends.

**Extensions**

* 1a. User inputs the command in an incorrect format.

    * 1a1. TuteeTally shows an error message and the correct command format.

  Use case ends.

* 1b. User enters a name that already exists in TuteeTally.

    * 1b1. TuteeTally generates and assigns a unique ID to the new student to avoid duplication.

  Use case resumes at step 2.

* 1c. User omits a required field in the command.

    * 1c1. TuteeTally shows an error message indicating the missing field.

  Use case ends.

**Use case: View Student Detail**

**MSS**

1. User requests to view details of students either by listing all or searching by name or ID.

2. TuteeTally retrieves and shows the relevant student details based on the request.

3. TuteeTally opens a popup and shows the past log entries of the student.

   Use case ends.

**Extensions**

* 2a. The requested student does not exist or the list is empty.

    * 2a1. TuteeTally displays a message indicating no such student exists or the list is empty.

  Use case ends.

* 2b. User inputs an incorrect command format for viewing details.

    * 2b1. TuteeTally shows an error message and the correct command format.

Use case ends.
* 3a. The requested student does not have valid logs recorded.
    * 3a1. TuteeTally opens a popup containing "No logs yet!" instead.
Use case ends.

**Use case: View Summary Statistics**

**MSS**

1. User requests to view summary statistics of students.

2. TuteeTally processes the request and displays the total number of students along with other relevant statistics.

   Use case ends.

**Extensions**

* 2a. There is an error in processing the request.

    * 2a1. TuteeTally displays an error message in red.

  Use case ends.

**Use case: Delete a Student**

**MSS**

1.  User requests to list Student
2.  TuteeTally shows a list of Student
3.  User requests to delete a specific Student in the list
4.  TuteeTally deletes the Student

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. TuteeTally shows an error message.

      Use case resumes at step 2.

**Use case: Add Payment**

**MSS** (Main Success Story)
1. The user selects the option to add a payment.
2. The system prompts the user to enter the student's unique ID and the payment amount.
3. The user enters the required information and submits the command.
4. The system validates the information and confirms the student's account exists.
5. The system adds the payment to the student's account and updates the balance.
6. The system notifies the user that the payment has been successfully added.

   Use case ends.

**Extensions**
* 1a. The user enters an invalid student ID.
    * 1a1. The system displays an error message and prompts the user to re-enter the student ID.

  Use case resumes at step 2.

* 2a. The user enters an invalid payment amount.
    * 2a1. The system displays an error message and prompts the user to re-enter the payment amount.

  Use case resumes at step 3.

**Use case: Mark Payment**

**MSS** (Main Success Story)
1. The user selects the option to mark a payment as complete.
2. The system prompts the user to enter the student's unique ID and details of the payment to be marked as complete.
3. The user enters the required information and submits the command.
4. The system validates the information and confirms the student's account and pending payment exist.
5. The system marks the specified payment as complete and updates the account status.
6. The system notifies the user that the payment has been successfully marked as complete.

   Use case ends.

**Extensions**
* 1a. The user enters an invalid student ID.
    * 1a1. The system displays an error message and prompts the user to re-enter the student ID.

      Use case resumes at step 2.

* 2a. The user enters an invalid payment amount.
    * 2a1. The system displays an error message and prompts the user to re-enter the payment amount.

      Use case resumes at step 3.

* 3a. There are no outstanding payments for the student.
    * 3a1. The system informs the user there are no payments to mark as complete.

      Use case ends.

**Use case: Add Exam**

**MSS** (Main Success Story)

1. The user selects the option to add an exam. 
2. The system prompts the user to enter the student's unique ID, exam name, exam date, and optional exam time. 
3. The user enters the required information and submits the command. 
4. The system validates the information and confirms the student exists with student ID. 
5. The system adds the exam to the student. 
6. The system notifies the user that the exam has been successfully added.

   Use case ends.

**Extensions**

* 1a. The user enters an invalid student ID. 
    * 1a1. The system displays an error message and prompts the user to re-enter the student ID.
      
      Use case resumes at step 2.

* 2a. The user enters an invalid exam date or time. 
    * 2a1. The system displays an error message and prompts the user to re-enter the exam date or time in the command. 
      
      Use case resumes at step 3.

**Use case: Delete Exam**

**MSS (Main Success Story)**
1. The user selects the option to delete an exam. 
2. The system prompts the user to enter the student's unique ID, exam name, exam date, and optional exam time. 
3. The user enters the required information and submits the command. 
4. The system validates the information and confirms the student exists and the specified exam exist. 
5. The system removes the exam from the student. 
6. The system notifies the user that the exam has been successfully deleted.

   Use case ends.

**Extensions**

* 1a. The user enters an invalid student ID. 
    * 1a1. The system displays an error message and prompts the user to re-enter the student ID.
      
      Use case resumes at step 2.
  
* 2a. The user enters incorrect exam details.
    * 2a1. The system displays an error message and prompts the user to re-enter the exam details.

      Use case resumes at step 3.

### Planned Enhancements
Team size: 4 <br>
1. Update Edit command to use ID instead of index.
2. Improve the UI such as removing all white spaces from it.
3. Learning styles can be tagged to Person so that it doesn't need to be logged every lesson.
4. Logging of a lesson will automatically update the payment info.
5. View -id or -name should automatically filter and show the exams of the person on the right.
6. Log window will update its content to respond to addition of logs to a student.
7. Log entry can be deleted and its fields can be edited, including editing the date and time of the log to a custom date to match the lesson time.
8. Implement checking of duplicate persons to ensure the same person is not recorded multiple times.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` to support JavaFx.
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

### Launch and Shutdown

#### 1. Initial Launch
- Download the jar file and copy it into an empty folder.
- Double-click the jar file.
  **Expected**: Shows the GUI with a set of sample contacts. The window size may not be optimum.

#### 2. Saving Window Preferences
- Resize the window to an optimum size. Move the window to a different location. Close the window.
- Re-launch the app by double-clicking the jar file.
  **Expected**: The most recent window size and location is retained.

#### 3. _{ more test cases …​ }_

### Deleting a Person

#### 1. Deleting a Person While All Persons Are Being Shown
- **Prerequisites**: List/view all persons using the `list` or `view -all` command. Multiple persons in the list.
- **Test Case**: `delete 000001`
  **Expected**: Contact with the ID #000001 is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.
- **Test Case**: `delete 000000`
  **Expected**: No person is deleted. Error details shown in the status message. Status bar remains the same.
- **Other Incorrect Delete Commands to Try**: `delete`, `delete x`, `...` (where x is larger than the list size)
  **Expected**: Similar to previous.

#### 2. _{ more test cases …​ }_

### Payment Commands

#### Adding a Payment to a Student Account

- **Command**: `addpayment`
- **Description**: Adds a payment record to a specified student's account by their unique identifier. This command allows for specifying the payment amount either as a numerical value or as a text string for more descriptive purposes.

##### Prerequisites
- User must be logged in with sufficient privileges to add payments.
- The student account associated with the given unique identifier must exist within the system.

##### Usage
- **To add a payment with a numerical amount**:
  `addpayment -id ID -amount 100.00`
  **Expected Outcome**: A payment of $100.00 is added to the student's account identified by `ID`. The system confirms the addition with a success message and updates the student's payment history accordingly.

#### Marking a Student's Payment

- **Command**: `markpayment`
- **Description**: Marks a specified payment as complete for a student's account. This command is used to update the status of a student's payments to reflect that they have been successfully processed or received.

##### Prerequisites
- User must be logged in with sufficient privileges to mark payments as complete.
- The student account associated with the given unique identifier must exist and have outstanding payments.

##### Usage
- **To mark a payment as complete**:
  `markpayment -id ID -payment PAYMENT_AMOUNT`
  **Expected Outcome**: The payment identified by `PAYMENT_AMOUNT` for the student `ID` is marked as complete. The system updates the payment status and provides a confirmation message.

#### Extensions and Error Handling
- **Invalid ID**: If an invalid `ID` or `PAYMENT_AMOUNT` is provided, the system will return an error message indicating the issue and suggesting corrective actions.
- **No Outstanding Payments**: If there are no outstanding payments to mark as complete, the system will notify the user accordingly.
- **Permission Denied**: If the user attempts to execute a command without sufficient privileges, the system will deny the request and provide an appropriate warning message.

### Additional Notes
- These commands are designed to interact seamlessly with the system's payment management module, ensuring accurate tracking and reporting of student payment statuses.
- Always ensure that the unique identifiers (`ID` and `PAYMENT_AMOUNT`) are correctly entered to avoid discrepancies or errors in payment processing.

#### Resetting Payments for a Student Account

- **Command**: `resetpayments`
- **Description**: Resets the payment status for all recorded payments in a specified student's account. This command is used when a student's payment record needs to be cleared, typically after all dues have been settled or in case of account adjustments.

##### Prerequisites
- User must be logged in with sufficient privileges to reset payment records.
- The student account associated with the given unique identifier must exist and have one or more payments recorded.

##### Usage
- **To reset all payments for a student**:
  `resetpayments -id ID`
  **Expected Outcome**: All payments recorded for the student account identified by `ID` are reset. The system confirms the reset with a success message, indicating that the student's payment history is now cleared.

#### Extensions and Error Handling
- **Invalid ID**: If an invalid `ID` is provided, the system will return an error message indicating that the student account could not be found.
- **No Recorded Payments**: If the student account does not have any recorded payments, the system will notify the user that there are no payments to reset.
- **Permission Denied**: Similar to the other commands, if the user attempts to execute the `resetpayments` command without sufficient privileges, the system will deny the request and display an appropriate warning message.

### Additional Notes
- Use the `resetpayments` command with caution, as it will clear all payment records for the specified student, potentially impacting their payment history and account status.
- Ensure accuracy when entering the `ID` to avoid unintentional resets of payment information for the wrong student account.

### Exam commands

#### Adding an Exam to a Student Account
- **Command**: `addexam`
- **Description**: Adds an exam record to a specified student by their unique id. This command allows for specifying the exam name, exam date, and optionally, exam time.

##### Prerequisites
- The student associated with the given unique id must exist within the system.

##### Usage
- **To add an exam**:
    `addexam -id ID -examname EXAM_NAME -date EXAM_DATE [-time EXAM_TIME]`
- Only EXAM_DATE from current date onwards can be used. 
- EXAM_DATE should be in the format of yyyy-MM-DD.
- EXAM_TIME should be in the format of HH:mm
    **Expected Outcome**: An exam with the specified name, date, and optionally time is added to the student identified by ID. The system confirms the addition with a success message and updates the student's exam records accordingly.

#### Deleting an Exam from a Student Account
- **Command**: `deleteexam`
- **Description**: Deletes a specific exam record from a student by specifying the student's unique identifier, exam name, exam date, and optionally, exam time.

##### Prerequisites
- The student associated with the given unique id must exist and have the specified exam recorded.

##### Usage
- **To delete an exam**:
    `deleteexam -id ID -examname EXAM_NAME -date EXAM_DATE [-time EXAM_TIME]`
    **Expected Outcome**: The exam with the specified name, date, and optionally time is removed from the student identified by ID. The system confirms the deletion with a success message, and the student's exam records are updated accordingly.

#### Extensions and Error Handling
- **Invalid ID**: If an invalid ID is provided, the system will return an error message indicating the issue and suggesting corrective actions.
- **No Recorded Exam**: If the student does not have the specified exam recorded, the system will notify the user that there are no exams to delete.

### Additional Notes
Always ensure that the unique id, exam name, and date are correctly entered to avoid discrepancies or errors in exam management.
These commands are designed to interact seamlessly with the system's exam management module, ensuring accurate tracking and reporting of student exam records.

### Saving Data

#### 1. Dealing with Missing/Corrupted Data Files
- _{explain how to simulate a missing/corrupted file, and the expected behavior}_

#### 2. _{ more test cases …​ }_

