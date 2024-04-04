---
layout: default.md
title: "User Guide"
pageNav: 3
---

# TuteeTally User Guide

TuteeTally is a **desktop app for managing student contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, TuteeTally can make student management much easier and faster than traditional GUI apps.

The system includes features for adding students, viewing student details, viewing summary statistics, and deleting student entries. All inputs are case-insensitive, enhancing user accessibility.


<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed on your Computer.
<box type="info" seamless>
Note: If you do not have Java 11 installed on your computer, you can download it from [here](https://www.oracle.com/sg/java/technologies/javase-jdk11-downloads.html).

Note: For MacOS users, a compatible Java 11 version is available [here](https://www.azul.com/core-post-download/?endpoint=zulu&uuid=f3e69a90-5b80-4d6b-b14b-eb117b8ef0b4).
</box>

2. Download the latest `tuteetally.jar` from [here](https://github.com/AY2324S2-CS2103T-F10-2/tp/releases).

3. Copy the file to your desired home folder.

4. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar tuteetally.jar` command to run the application.<br>
   A GUI similar to the one below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

5. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `add` - adding student to list
   * `delete` - deleting student from list
   * `view` - viewing student overview or details or statistics
   * `addpayment` - adding payment to student
   * `markpayment` - marking payment as paid for student
   * `resetpayments` - resetting payment for student

6. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `{UPPER_CASE}` are the parameters to be supplied by the user.<br>
  e.g. in `-name/{NAME}`, `NAME` is a parameter that can be used as `add -name John Doe`.

* Parameters can be in any order.<br>
  e.g. if the command specifies `-name {NAME} -address {ADDRESS}`, `-address {ADDRESS} -name {NAME}` is also acceptable.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>


### Adding a student: `add`

Adds a student's particulars into the address book.

Format: `add -name {NAME} -phone {NUMBER} -email {EMAIL} -address {ADDRESS} -subject {SUBJECT} t/{tag}`

<box type="tip" seamless>
Tip:<br>
If the addition is successful, the new student record will be shown at the top of the list.
</box>
<box type="info" seamless>
Note:<br>
The `t/{tag}` field is optional and can be used to add a tag to the student record and no spaces are allowed in the {tag}.
</box>

Examples:
* `add -name Xiao Ming -address 13, Computing Dr, 117417 -phone 88888888 -subject Math`

### Deleting a student: `delete`

Deletes the specified student from the address book.

Format: `delete -id {ID}`

* Deletes the person at the specified `id`.
* The index refers to the 5-digit code attached to each student entry.

Examples:
* `delete -id 000001 / 1` deletes the student with the id 000001

### View student statistics: `View`
This would display the total number of students and the total amount owed by students.
Format: `view -statistics`

### View student summary on the home page: `View`
This would display a summary of student particulars on the homepage.
Format: `view -all`

### View student particular by name: `View`
This will display a specific student by searching their name
Format: `view -name {NAME}`
Examples:
* `view -name Xiao Ming` would display the student particular of Xiao Ming if existed

### View student particular by id: `View`
This will display a specific particular by searching for its id
Format: `view -id {ID}`
Examples;
* `view -id 88888` would display the student particular for the student whose id is 88888 if it exists.

### Add payment for the student by id: `AddPayment`
This will add a payment to a student by searching for their `id`
Format: `addpayment -id {ID} -payment {AMOUNT}`
Examples;
* `addpayment -id 88888 -payment 100` would add a payment of 100 to the student whose id is 88888 if it exists.

### Mark student's payment as paid by id: `MarkPaid`
This will mark a student's payment as paid by searching for their `id`
Format: `markpayment -id {ID} -payment {AMOUNT}`
Examples;
* `markpayment -id 88888 -payment 100` would mark a payment of 100 as paid for the student whose id is 88888 if it exists.

### Reset student's payment by id: `ResetPayment`
This will reset a student's payment by searching for their `id`
Format: `resetpayments -id {ID}`
Examples;
* `resetpayments -id 88888` would reset the payment for the student whose id is 88888 if it exists.

### More features `[coming in v1.4]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app on the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

**Q**: What are the system requirements for TuteeTally?<br>
**A**: TuteeTally requires Java 11 or above to run. It is compatible with Windows, MacOS, and Linux operating systems.

**Q**: What are the system requirements for TuteeTally?<br>
**A**: TuteeTally requires Java 11 or above to run. It is compatible with Windows, MacOS, and Linux operating systems.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.

--------------------------------------------------------------------------------------------------------------------
## Command summary

| Action          | Format, Examples                                                                                                                                                                                                                         |
|-----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add**         | `add -name {NAME} -email {EMAIL} -phone {number} -address {ADDRESS} -subject {SUBJECT} t/{tag}` <br> e.g., `add -name Xiao Ming -phone 88888888 -email xiaoming@gmail.com -address 13, Computing Dr, 117417  -subject Math t/bestfriend` |
| **Delete**      | `delete -id {id}`<br> e.g., `delete -id 88888`                                                                                                                                                                                           |
| **View**        | `view [-statistics] [-all] [-id ID] [-name NAME]`                                                                                                                                                                                        |
| **AddPayment**  | `addpayment -id {ID} -payment {AMOUNT}`<br> e.g., `addpayment -id 88888 -payment 100`                                                                                                                                                    |
| **MarkPayment** | `markpayment -id {ID} -payment {AMOUNT}`<br> e.g., `markpayment -id 88888 -payment 100`                                                                                                                                                  |
| **ResetPayment**| `resetpayments -id {ID}`<br> e.g., `resetpayments -id 88888`                                                                                                                                                                             |
