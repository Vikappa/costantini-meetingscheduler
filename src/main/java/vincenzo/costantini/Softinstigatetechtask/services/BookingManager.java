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
        if (variousUtilities.validateWorkingHoursLine(line.trim())) {
            LocalTime openingTime = LocalTime.parse(line.substring(0, 4), DateTimeFormatter.ofPattern("HHmm"));
            LocalTime closingTime = LocalTime.parse(line.substring(5, 9), DateTimeFormatter.ofPattern("HHmm"));

            if (openingTime.isBefore(closingTime)) {
                this.officeOpeningTime = openingTime;
                this.officeClosingTime = closingTime;
            } 
        } 
    }


    //This method checks all the conditions to add a new schedule to the list of schedules
    //if the condition are not met the schedule is not added but execution is not
    public void addSchedule(String firstLine, String secondLine) {
        // Prepare schedule object arguments
        String scheduleRequestDateTime = "";
        String scheduleEmployee = "";
        String scheduleStartTime = "";
        int scheduleDuration = 0;
    
        // First input line verification and splitting
        firstLine = firstLine.trim();
        if (variousUtilities.validateFirstLineStringFormat(firstLine)) {
            scheduleRequestDateTime = firstLine.substring(0, 19);
            scheduleEmployee = firstLine.substring(20);
            logger.debug("First line validated: " + scheduleRequestDateTime + ", " + scheduleEmployee);
        } else {
            return;
        }
    
        secondLine = secondLine.trim();
    
        // Second input line verification and splitting
        if (variousUtilities.validateSecondLineStringFormat(secondLine)) {
            scheduleStartTime = secondLine.substring(0, 16);
            scheduleDuration = Integer.parseInt(secondLine.substring(17));
            logger.debug("Second line validated: " + scheduleStartTime + ", duration: " + scheduleDuration);
        } else {
            return;
        }
    
        // Create a new schedule object
        Schedule newSchedule = new Schedule(scheduleRequestDateTime, scheduleEmployee, scheduleStartTime, scheduleDuration);
    
        // Check if the schedule is within office hours
        if (!variousUtilities.isScheduleInsideOfficeHours(this.officeOpeningTime, this.officeClosingTime, newSchedule)) {
            return;
        }
    
        // Find and replace an existing schedule if necessary
        Schedule scheduleToReplace = null;
        for (Schedule existingSchedule : this.schedules) {
            if (existingSchedule.getStartAt().equals(newSchedule.getStartAt())) {
                // If the new schedule has an earlier bookedAt time, mark the existing schedule for replacement
                if (newSchedule.getBookedAt().isBefore(existingSchedule.getBookedAt())) {
                    scheduleToReplace = existingSchedule;
                    break;
                } else {
                    // If the existing schedule has an earlier bookedAt time, do not add the new schedule
                    return;
                }
            }
        }
    
        // Replace the schedule if one was found to replace (outside for loops to avoid errors)
        if (scheduleToReplace != null) {
            this.schedules.remove(scheduleToReplace);
        }
    
        // Add the new schedule
        this.schedules.add(newSchedule);
    }

    

    //Spicy tostring method
    @Override
    public String toString() {
        if (this.schedules.isEmpty()) {
            return "No schedules available.";
        }

        StringBuilder returnString = new StringBuilder();

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
