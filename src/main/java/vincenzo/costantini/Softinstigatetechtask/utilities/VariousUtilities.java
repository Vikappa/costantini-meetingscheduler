package vincenzo.costantini.Softinstigatetechtask.utilities;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import vincenzo.costantini.Softinstigatetechtask.classes.Schedule;
import vincenzo.costantini.Softinstigatetechtask.runners.BookingCommandLineRunner;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;

// Il will keep various parsers and validator here to keep the main classes cleaner
@Service
public class VariousUtilities {
    private static final Logger logger = LoggerFactory.getLogger(BookingCommandLineRunner.class);

    //Checks if the string is in the format YYYY-MM-DD HH:MM:SS EMP###
    public boolean validateFirstLineStringFormat(String input) {
        if(input == null){
            return false;
        }
        // Regex pattern for the specified format: YYYY-MM-DD HH:MM:SS EMP###
        String regex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} EMP\\d{3}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    //Checks if the string is in the format YYYY-MM-DD HH:MM d (d is the duration in hours)
    public boolean validateSecondLineStringFormat(String input) {
        if(input == null){
            return false;
        }
        String regex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2} \\d+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    //Checks if the time is typed in format #### #### for the first line input
    public boolean validateWorkingHoursLine(String input) {
        if(input == null){
            return false;
        }
        String regex = "^\\d{2}\\d{2} \\d{2}\\d{2}$";

        try {
            LocalTime openingTime = LocalTime.parse(input.substring(0, 4), DateTimeFormatter.ofPattern("HHmm"));
            LocalTime closingTime = LocalTime.parse(input.substring(5, 9), DateTimeFormatter.ofPattern("HHmm"));
        } catch (Exception e) {
            //The time is not in the correct format (example: 12:78 is an impossible hours)

            return false;
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    //checks if any of the schedules in the list has a conflict with the new one
    public boolean checkScheduleConflicts(ArrayList<Schedule> schedules, Schedule schedule) {
        return schedules.stream().anyMatch(existingSchedule -> hasConflict(schedule, existingSchedule));
    }
    
    // This is a helper method that defines the conflict condition
    private boolean hasConflict(Schedule schedule1, Schedule schedule2) {
        return schedule1.getStartAt().isBefore(schedule2.getCalculatedEndAt()) && 
               schedule1.getCalculatedEndAt().isAfter(schedule2.getStartAt());
    }
    public boolean isScheduleInsideOfficeHours(LocalTime openingTime, LocalTime closingTime, Schedule schedule) {
        // Check if the schedule's start and end times are within the office hours
        return (!schedule.getStartHour().isBefore(openingTime) && 
                !schedule.getEndHour().isAfter(closingTime));
    }
    
}
