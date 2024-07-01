package Softinstigatetechtask.utilities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertFalse(variousUtilities.validateWorkingHoursLine("1234 7894"));
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
}
