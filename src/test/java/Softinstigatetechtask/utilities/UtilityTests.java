package Softinstigatetechtask.utilities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import vincenzo.costantini.Softinstigatetechtask.classes.Schedule;
import vincenzo.costantini.Softinstigatetechtask.utilities.VariousUtilities;

@SpringBootTest(classes = VariousUtilities.class)
@ActiveProfiles("test")
public class UtilityTests {

    @Autowired
    private VariousUtilities variousUtilities;

    @Test // CHECKS IF THE REGEX WORKS CORRECTLY (before parsing the hour values)
    void test_Input_working_hours_regex(){
        assertTrue(variousUtilities.validateWorkingHoursLine("1234 7894"));
        assertFalse(variousUtilities.validateWorkingHoursLine("null"));
        assertFalse(variousUtilities.validateWorkingHoursLine(null));
        assertFalse(variousUtilities.validateWorkingHoursLine("abcd 1234"));
        assertFalse(variousUtilities.validateWorkingHoursLine("12:34 56:78"));
        assertFalse(variousUtilities.validateWorkingHoursLine("12 1234"));
        assertTrue(variousUtilities.validateWorkingHoursLine("0900 1700"));
    }

    @Test // CHECKS IF THE REGEX WORKS CORRECTLY for first line format
    void test_validateFirstLineStringFormat(){
        assertTrue(variousUtilities.validateFirstLineStringFormat("2011-03-17 10:17:06 EMP001"));
        assertFalse(variousUtilities.validateFirstLineStringFormat("2011-03-17 10:17 EMP001"));
        assertFalse(variousUtilities.validateFirstLineStringFormat("2011-03-17 10:17:06 EMP1"));
        assertFalse(variousUtilities.validateFirstLineStringFormat("2011-03-17 EMP001"));
        assertFalse(variousUtilities.validateFirstLineStringFormat("2011-03-17 10:17:06EMP001"));
        assertFalse(variousUtilities.validateFirstLineStringFormat(null));
    }

    @Test // CHECKS IF THE REGEX WORKS CORRECTLY for second line format
    void test_validateSecondLineStringFormat(){
        assertTrue(variousUtilities.validateSecondLineStringFormat("2011-03-21 09:00 2"));
        assertFalse(variousUtilities.validateSecondLineStringFormat("2011-03-21 09:002"));
        assertFalse(variousUtilities.validateSecondLineStringFormat("2011-03-21 9:00 2"));
        assertFalse(variousUtilities.validateSecondLineStringFormat("2011-03-21 09:00"));
        assertFalse(variousUtilities.validateSecondLineStringFormat("2011-03-21 09:00 two"));
        assertFalse(variousUtilities.validateSecondLineStringFormat(null));
    }

    @Test // CHECKS SCHEDULE CONFLICTS
    void test_checkScheduleConflicts(){
        // Create and add 2 schedules which do not conflict
        ArrayList<Schedule> schedules = new ArrayList<>();
        schedules.add(new Schedule("2011-03-16 09:00:00", "EMP001", "2011-03-21 09:00", 2)); 
        schedules.add(new Schedule("2011-03-17 10:00:00", "EMP002", "2011-03-21 11:00", 2));

        // New schedule that conflicts with the first one
        Schedule newSchedule1 = new Schedule("2011-03-18 09:00:00", "EMP003", "2011-03-21 10:00", 1);
        // New schedule that does not conflict with any existing schedule
        Schedule newSchedule2 = new Schedule("2011-03-18 09:00:00", "EMP004", "2012-03-21 13:00", 1);

        assertTrue(variousUtilities.checkScheduleConflicts(schedules, newSchedule1)); // conflict expected
        assertFalse(variousUtilities.checkScheduleConflicts(schedules, newSchedule2)); // no conflict expected
    }

    @Test // CHECKS IF SCHEDULE IS INSIDE OFFICE HOURS
    void test_isScheduleInsideOfficeHours(){
        LocalTime openingTime = LocalTime.parse("09:00", DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime closingTime = LocalTime.parse("17:00", DateTimeFormatter.ofPattern("HH:mm"));

        Schedule schedule1 = new Schedule("2011-03-16 09:00:00", "EMP001", "2011-03-21 09:00", 2); // Inside office hours
        Schedule schedule2 = new Schedule("2011-03-16 08:00:00", "EMP002", "2011-03-21 08:00", 2); // Outside office hours
        Schedule schedule3 = new Schedule("2011-03-16 09:00:00", "EMP003", "2011-03-21 16:00", 2); // End outside office hours
        Schedule schedule4 = new Schedule("2011-03-16 09:00:00", "EMP004", "2011-03-21 18:00", 2); // Start outside office hours

        assertTrue(variousUtilities.isScheduleInsideOfficeHours(openingTime, closingTime, schedule1));
        assertFalse(variousUtilities.isScheduleInsideOfficeHours(openingTime, closingTime, schedule2));
        assertFalse(variousUtilities.isScheduleInsideOfficeHours(openingTime, closingTime, schedule3));
        assertFalse(variousUtilities.isScheduleInsideOfficeHours(openingTime, closingTime, schedule4));
    }
}
