# Booking Management System

This project implements a booking management system using Java Spring. The system processes batches of booking requests for meetings in a boardroom, ensuring all bookings are valid and non-overlapping, and fall within the specified office hours.

## Project Structure

### BookingManager
A Singleton Bean class that holds all the schedules. It checks whether a schedule is typed correctly, does not overlap with another, and falls within the office opening hours.

### Various Utilities
A service class to keep the code clean. It includes:
- Validation methods using regex.
- Conflict checking methods.

### Schedule
This class contains various formatters and parsers. The constructor is based on strings typed by users and validated via regex. It ensures the Schedule object is well-structured for comparison and other operations.

### BookingCommandLiner
The main execution loop is set in this class. It is a simple while loop that reads the input file line by line and then calls the `BookingManager` to add the schedule to the schedule list. This class implements the `CommandLineRunner` interface to run the main method automatically once the application is fully started.

## Technologies Used

- **Java Spring**: For setting up the project and dependency injection.
- **Regex Expressions**: For strict input string validation.
- **Lambda Functions**: For iterating over arrays, filtering, and printing objects to match the output as requested.

## Input and Output Example

### Input
```
0900 1730
2011-03-17 10:17:06 EMP001
2011-03-21 09:00 2
2011-03-16 12:34:56 EMP002
2011-03-21 09:00 2
2011-03-16 09:28:23 EMP003
2011-03-22 14:00 2
2011-03-17 11:23:45 EMP004
2011-03-22 16:00 1
2011-03-15 17:29:12 EMP005
2011-03-21 16:00 3
```


### Output
```
2011-03-21
09:00 11:00 EMP002
2011-03-22
14:00 16:00 EMP003
16:00 17:00 EMP004
```


## Requirements

Your processing system must meet the following requirements:

- **No part of a meeting may fall outside office hours.** ✅
- **Meetings may not overlap.** ✅
- **The booking submission system only allows one submission at a time, so submission times are guaranteed to be unique.** ✅
- **Bookings must be processed in the chronological order in which they were submitted.** ✅
- **The ordering of booking submissions in the supplied input is not guaranteed.** ✅

