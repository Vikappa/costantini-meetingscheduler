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

    public void setWorkingHours(String line){ //tested
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


    //This method checks all the conditions to add a new schedule to the list of schedules
    //if the condition are not met the schedule is not added but execution is not interrupted
    public void addSchedule(String firstLine, String secondLine) {
        // i prepare schedule object arguments
        String scheduleRequestDateTime = "";
        String scheduleEmployee = "";
        String scheduleStartTime = "";
        int scheduleDuration = 0;
    
        // First input line verification and splitting
        firstLine = firstLine.trim();
        if (variousUtilities.validateFirstLineStringFormat(firstLine)) {
            scheduleRequestDateTime = firstLine.substring(0, 19);
            scheduleEmployee = firstLine.substring(20);
        } else {
            logger.error("Invalid format for the first line");
            return;
        }
    
        secondLine = secondLine.trim();
    
        // Second input line verification and splitting
        if (variousUtilities.validateSecondLineStringFormat(secondLine)) {
            scheduleStartTime = secondLine.substring(0, 16);
            scheduleDuration = Integer.parseInt(secondLine.substring(17));
        } else {
            logger.error("Invalid format for the second line");
            return;
        }
    
        // Since the prompt strings were safe, uses them to create a new schedule object
        Schedule schedule = new Schedule(scheduleRequestDateTime, scheduleEmployee, scheduleStartTime, scheduleDuration);
    
        // Check whether the schedule is inside or outside of the office hours
        if (!variousUtilities.isScheduleInsideOfficeHours(this.officeOpeningTime, this.officeClosingTime, schedule)) {
            return;
        }
    
        // Check whether the schedule is overlapping with another schedule
        if (this.schedules.stream().anyMatch(sched -> variousUtilities.checkScheduleConflicts(this.schedules, schedule))) {
            return;
        }
    
        // Check if the request registration time is unique, if the new line was scheduled before the previous one the newer will be removed, else it will not be added
        boolean isUnique = true;
        for (Schedule sched : this.schedules) {
            if (sched.getBookedAt().equals(schedule.getBookedAt())) {
                if (schedule.getBookedAt().isBefore(sched.getBookedAt())) {
                    // Remove the older schedule if the new one is booked before
                    this.schedules.remove(sched);
                } else {
                    // Newer schedule request time is not unique and didn't came before the registered one
                    isUnique = false;
                }
                break;
            }
        }
    
        if (isUnique) {
            this.schedules.add(schedule);
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
}
