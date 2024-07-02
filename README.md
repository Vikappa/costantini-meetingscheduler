
# Booking Management System
## Project Structure

I used Java Spring to write a Singleton Bean Class, BookingManager, that holds all the schedules and checks werther a schedule is typed correctly and it's not overlapping with another or booking out of the office opening hours.
This project implements a booking management system using Java Spring. The system processes batches of booking requests for meetings in a boardroom, ensuring all bookings are valid and non-overlapping, and fall within the specified office hours.

I also created a Various Utilities service class to keep the code clean. It holds the validation with regex methods and the conflict checking methods.

## Project Structure

The Schedule class is full of various formatter and parser because the the costructor is based on String typed by the users and validated via regex but later the schedule object is confronted in many other ways and it's better to have a Schedule class that holds all the code in a more structured way.

### BookingManager
A Singleton Bean class that holds all the schedules. It checks whether a schedule is typed correctly, does not overlap with another, and falls within the office opening hours.

The main execution is set in the BookingCommandLineRunner class. It's a simple runner that reads the input file line by line and then calls the BookingManager to add the schedule to the schedule list. It implements the CommandLineRunner interface to run automatically the main method once the application is fully started.

### Various Utilities
A service class to keep the code clean. It includes:
- Validation methods using regex.
- Conflict checking methods.
- Office hour compatibility checking.

## Class Tests
### Various Utilities Test
A test class that ensures that regexes and hour checking methos are working fine

### Booking Manager Test
A test class that ensures that Schedules are added or not following task logic 

## What i used
### Schedule
This class contains various formatters and parsers. The constructor is based on strings typed by users and validated via regex. It ensures the Schedule object is well-structured for comparison and other operations.

I used Java Spring to set up the project.
I used regex expressions to check the input string to be strictly formatted and valid. 
I used lambda functions to iterate the arrays to filter and print objects and give the exact same output as requested and showed in the task instructions.

## Technologies Used

- **Java Spring**: For setting up the project and dependency injection.
- **Regex Expressions**: For strict input string validation.
- **Lambda Functions**: For iterating over arrays, filtering, and printing objects to match the output as requested.
- **J Unit Test 5**: For testing the project.

## Input and Output Example

ex:
```
### Input
```
0900 1730
2011-03-17 10:17:06 EMP001
2011-03-21 09:00 2

@@ -30,6 +41,7 @@ ex:
2011-03-21 16:00 3
```


### Output
```
2011-03-21

@@ -39,26 +51,14 @@ ex:
16:00 17:00 EMP004
```

## Original request:
```

Your employer has an existing system for employees to submit booking requests for meetings in the boardroom. Your employer has now asked you to to implement a system for processing batches of booking requests.
```

### Input

Your processing system must process input as text. The first line of the input text represents the company office hours, in 24 hour clock format, and the remainder of the input represents individual booking requests. Each booking request is in the following format.

```
[request submission time, in the format YYYY-MM-DD HH:MM:SS][arch:employee id]
[meeting start time, in the format YYYY-MM-DD HH:MM][arch:meeting duration in hours]
```
## Requirements

Your processing system must meet the following requirements:

### Tasks

-   No part of a meeting may fall outside office hours ✅
-   Meetings may not overlap. ✅
-   The booking submission system only allows one submission at a time, so submission times are guaranteed to be unique.✅
-   Bookings must be processed in the chronological order in which they were submitted.✅
-   The ordering of booking submissions in the supplied input is not guaranteed.✅