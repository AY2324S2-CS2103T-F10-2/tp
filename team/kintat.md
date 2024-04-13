---
layout: default.md
title: "Kin Tat's Project Portfolio Page"
---

### Personal Portfolio
Hi I'm Kin Tat, I am a Year 2 Computer Science student in NUS. I am a developer in the team and I am responsible for integration.

### Project: TuteeTally
TuteeTally is a desktop address book application used for private tutors to help keep track of their students' individual needs. The user interacts with it using a CLI, and it has a GUI created with JavaFX. It is written in Java, and has about 20 kLoC.

## New Feature: Payment Commands for Private Tutors

### Overview
Implemented a set of payment commands within the application to help private tutors manage their students' payments more effectively. These commands include `addpayment`, `markpayment`, and `resetpayments`.

### What It Does
- **`addpayment`**: Allows tutors to add payment records for students, capturing payment amounts and associating them with unique student identifiers.
- **`markpayment`**: Enables tutors to mark a student's payment as complete, updating the payment status to reflect that it has been received or processed.
- **`resetpayments`**: Provides the functionality to reset the payment status for a student, useful for clearing payment records once a student has completed all due payments or in case of account adjustments.

### Justification
This feature substantially enhances the application by offering a streamlined way for tutors to track and manage payments. It addresses the need for keeping accurate financial records and simplifies the process of updating payment statuses, which is crucial for private tutoring businesses.

### Highlights
- The implementation of these commands required a comprehensive understanding of the application's existing structure and the development of a robust mechanism for tracking financial transactions.
- These commands are designed to be future-proof, easily accommodating additional features or modifications to the payment management system.
- The development process involved an in-depth analysis of potential use cases and user interactions to ensure that the commands are intuitive and meet the users' needs.

### Credits
While the payment commands feature was inspired by general best practices in financial transaction management, the implementation details, including the command structure and the integration with the existing application, were developed independently.

- Code contributed: [RepoSense link](https://nus-cs2103-ay2324s2.github.io/tp-dashboard/?search=kintatho&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2024-02-23&tabOpen=true&tabType=authorship&tabAuthor=KinTatHo&tabRepo=AY2324S2-CS2103T-F10-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Documentation
- User Guide: [User Guide](https://ay2324s2-cs2103t-f10-2.github.io/tp/index.html)
  - Added documentation for the new payment commands, including detailed instructions on how to use them effectively.
  - Added all the diagrams and screenshots needed to illustrate the entire TuteeTally management process.
- Developer Guide: [Developer Guide](https://ay2324s2-cs2103t-f10-2.github.io/tp/index.html)

