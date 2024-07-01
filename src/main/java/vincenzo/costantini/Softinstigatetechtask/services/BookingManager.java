package vincenzo.costantini.Softinstigatetechtask.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import vincenzo.costantini.Softinstigatetechtask.classes.Schedule;
import vincenzo.costantini.Softinstigatetechtask.runners.BookingCommandLineRunner;
import vincenzo.costantini.Softinstigatetechtask.utilities.VariousUtilities;

@Service
@Getter
@Setter
public class BookingManager {
    private static final Logger logger = LoggerFactory.getLogger(BookingCommandLineRunner.class);

    @Autowired
    private VariousUtilities variousUtilities;

    private ArrayList<Schedule> schedules;
    private LocalTime officeOpeningTime;
    private LocalTime officeClosingTime;

    public BookingManager() {
        this.schedules = new ArrayList<Schedule>();
        this.officeOpeningTime = null;
        this.officeClosingTime = null;
    }

    public void setWorkingHours(String line){
        if (variousUtilities.validateWorkingHoursLine(line)) {
            LocalTime openingTime = LocalTime.parse(line.substring(0, 4), DateTimeFormatter.ofPattern("HHmm"));
            LocalTime closingTime = LocalTime.parse(line.substring(5, 9), DateTimeFormatter.ofPattern("HHmm"));
            if (openingTime.isBefore(closingTime)) {
                this.officeOpeningTime = openingTime;
                this.officeClosingTime = closingTime;
                logger.info("Working hours set to: " + openingTime + " - " + closingTime);
            } else {
                logger.error("The closing time must be after the opening time.");
            }
        } else{
            logger.error("The input is not in the correct format. Please type the working hours in the following format: HHmm HHmm.");
        }
    }

    public void addSchedule(String firstLine, String secondLine) {
        // Scanner scheduleInputScanner = new Scanner(System.in);
        // logger.info("SCHEDULE REQUEST DATE TIME AND EMPLOYEE:");
        // logger.info("Please type the schedule request time in the following format: [request submission time, in the format YYYY-MM-DD HH:MM:SS][arch:employee id]");
        // logger.info("Example: 2011-03-16 09:28:23 EMP003");

        //i prepare schedule object arguments
        String scheduleRequestDateTime = "";
        String scheduleEmployee = "";
        String scheduleStartTime = "";
        int scheduleDuration = 0;

        //i ask for the first line of the schedule until a valid format is typed
        firstLine = firstLine.trim();
            if (variousUtilities.validateFirstLineStringFormat(firstLine)) {
                scheduleRequestDateTime = firstLine.substring(0, 19);
                scheduleEmployee = firstLine.substring(20);
            } 

        //Check if the request registration time is unique, if not exits method execution
        LocalDateTime requestDateTime = LocalDateTime.parse(scheduleRequestDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (this.schedules.stream().anyMatch(sched -> sched.getBookedAt().equals(requestDateTime))) {
            return;
        }

        secondLine = secondLine.trim();
        if (variousUtilities.validateSecondLineStringFormat(secondLine)) {
            scheduleStartTime = secondLine.substring(0, 16);
            scheduleDuration = Integer.parseInt(secondLine.substring(17));
        }   

        //Since the prompt string were safe, i can parse the date and time
        Schedule schedule = new Schedule(scheduleRequestDateTime, scheduleEmployee, scheduleStartTime, scheduleDuration);

        //Check whether the schedule is overlapping or outside of the office hours
        if (!schedule.getStartHour().isBefore(officeOpeningTime) && !schedule.getStartHour().isAfter(officeClosingTime) && schedule.getEndHour().isAfter(officeOpeningTime) && !schedule.getEndHour().isAfter(officeClosingTime)) {
            if (!variousUtilities.checkScheduleConflicts(schedule, this.schedules)) {
                this.schedules.add(schedule);
                this.schedules.sort(Comparator.comparing(Schedule::getStartAt));
                logger.info("The schedule has been added");
            } else {
                logger.info("The schedule is overlapping with another schedule. Please, retry");
                return;
            }
        } else {
            logger.info("The schedule is not valid because it's outside opening hours [" + this.officeOpeningTime + " - " + this.officeClosingTime + "]. Please, insert a valid schedule");
            return;
        }
    }

    //Spicy tostring method
    @Override
    public String toString() {
        if (this.schedules.isEmpty()) {
            return "No schedules available.";
        }

        StringBuilder returnString = new StringBuilder();
        returnString.append("SCHEDULES:\n");

        HashSet<LocalDate> hashsetDate = new HashSet<>();
        this.schedules.forEach(schedule -> {
            hashsetDate.add(schedule.getStartDate());
        });

        List<LocalDate> sortedDates = hashsetDate.stream().sorted().collect(Collectors.toList());

        sortedDates.forEach(date -> {
            returnString.append(date.toString() + "\n");
            this.schedules.stream()
                .filter(schedule -> schedule.getStartDate().isEqual(date))
                .sorted(Comparator.comparing(Schedule::getStartAt))
                .forEach(schedule -> {
                    returnString.append(schedule.toString() + "\n");
                });
        });

        return returnString.toString();
    }

    //Method to paste a bulk schedule request 
    public void addBulkSchedules() {
        logger.info("Paste a bulk schedule requests in the following format:");
        logger.info("YYYY-MM-DD HH:MM:SS EMP###");
        logger.info("YYYY-MM-DD HH:MM #");
        logger.info("After may be necessary to press enter twice.");

        Scanner scanner = new Scanner(System.in);

        //I read all the aivable lines
        ArrayList<String> lines = new ArrayList<>();
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                break;
            }
            lines.add(line);
        }

        //I create a list of schedule objects to parse
        ArrayList<Schedule> requestList = new ArrayList<>();

        //I parse the lines and add them to the list. If the line format is invalid its skipped
        for (int i = 0; i < lines.size(); i++) {
            try {
                if (variousUtilities.validateFirstLineStringFormat(lines.get(i)) && variousUtilities.validateSecondLineStringFormat(lines.get(i + 1))) {
                    //both checks are valid, create and add the new schedule to the list
                    Schedule schedule = new Schedule(
                        lines.get(i).substring(0, 19), 
                        lines.get(i).substring(20),
                        lines.get(i + 1).substring(0, 16),
                        Integer.parseInt(lines.get(i + 1).substring(17))
                    );
                    requestList.add(schedule);
                    logger.info("Line checked: " + lines.get(i) + " and " + lines.get(i+1) + " are valid.");
                    i++; // Skip the next line as it's already processed
                }
            } catch(Exception e) {
                logger.info("Line checked: " + lines.get(i) + " is invalid.");
            }
        }

        //Sort the list by the request registration date
        requestList.sort(Comparator.comparing(Schedule::getBookedAt));
        logger.info("Inserting valid schedules...");

        //Check the schedule and add it to the list if it's valid
        for (Schedule schedule : requestList) {
            LocalTime startHour = schedule.getStartHour();
            LocalTime endHour = schedule.getEndHour();

            if (startHour.isBefore(officeOpeningTime) || endHour.isAfter(officeClosingTime)) {
                logger.info("The schedule is outside of the office opening hours. Not inserted.");
                continue;
            }

            if (this.schedules.stream().anyMatch(sched -> sched.getBookedAt().equals(schedule.getBookedAt()))) {
                logger.info("The schedule request time is not unique. Not inserted.");
                continue;
            }

            if (!variousUtilities.checkScheduleConflicts(schedule, this.schedules)) {
                this.schedules.add(schedule);
                this.schedules.sort(Comparator.comparing(Schedule::getStartAt));
                logger.info("The schedule has been added.");
            } else {
                logger.info("The schedule is overlapping with another schedule. Not inserted.");
            }
        }

        logger.info("OUTPUT");
        logger.info(this.toString());
    }
}
