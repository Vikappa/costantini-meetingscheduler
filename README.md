# Project Structure

I used Java Spring to write a Singleton Bean Class, BookingManager, that holds all the schedules and checks werther a schedule is typed correctly and it's not overlapping with another or booking out of the office opening hours.

I also created a Various Utilities service class to keep the code clean. It hold the validation with regex methods and the conflict checking methods.

The Schedule class is full of various formatter and parser because the the costructor is based on String typed by the users and validated via regex but later the schedule object is confronted in many other ways and it's better to have a Schedule class that holds all the code in a more structured way.

The main execution loop is set in the BookingCommandLiner class. It's a simple while loop that reads the input file line by line and then calls the BookingManager to add the schedule to the schedule list. It implements the CommandLineRunner interface to run automatically the main method once the application is fully started.

## What i used

I used Java Spring to set up the project.
I used regex expressions to check the input string to be strictly formatted and valid. 
I used lambda functions to iterate the arrays to filter and print objects and give the exact same output as requested and showed in the task instructions.

ex:
```
### Input
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


### Tasks

-   No part of a meeting may fall outside office hours ✅
-   Meetings may not overlap. ✅
-   The booking submission system only allows one submission at a time, so submission times are guaranteed to be unique.✅
-   Bookings must be processed in the chronological order in which they were submitted.✅
-   The ordering of booking submissions in the supplied input is not guaranteed.✅
