package vincenzo.costantini.Softinstigatetechtask.utilities;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import vincenzo.costantini.Softinstigatetechtask.classes.Schedule;

import java.util.ArrayList;
import java.util.regex.Matcher;

// Il will keep various parsers and validator here to keep the main classes cleaner
@Service
public class VariousUtilities {

    //Checks if the string is in the format YYYY-MM-DD HH:MM:SS EMP###
    public boolean validateFirstLineStringFormat(String input) {
        // Regex pattern for the specified format: YYYY-MM-DD HH:MM:SS EMP###
        String regex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} EMP\\d{3}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    //Checks if the string is in the format YYYY-MM-DD HH:MM d (d is the duration in hours)
    public boolean validateSecondLineStringFormat(String input) {
        String regex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2} \\d+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    //checks if any of the schedules in the list has a conflict with the new one
    public boolean checkScheduleConflicts(Schedule schedule, ArrayList<Schedule> schedules) {
        return schedules.stream().anyMatch(existingSchedule -> hasConflict(schedule, existingSchedule));
    }
    
    // This is a helper method that defines the conflict condition
    private boolean hasConflict(Schedule schedule1, Schedule schedule2) {
        return (schedule1.getStartDate() != schedule2.getStartDate() ) || (schedule1.getStartHour().isBefore(schedule2.getEndHour()) &&
               schedule2.getStartHour().isBefore(schedule1.getEndHour()));
    }
    

}
