package Softinstigatetechtask.utilities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import vincenzo.costantini.Softinstigatetechtask.utilities.VariousUtilities;

public class UtilityTests {

    @Autowired
    private VariousUtilities variousUtilities;

    @Test // CHECKS IF THE REGEX WORKS CORRECTLY (before parsing the hour values)
    void test_Input_working_hours_regex(){
        assertTrue(variousUtilities.validateFirstLineTimeFormat("1234 5678"));
        assertFalse(variousUtilities.validateFirstLineStringFormat("null"));
        assertFalse(variousUtilities.validateFirstLineStringFormat(null));
        assertFalse(variousUtilities.validateFirstLineStringFormat("abcd 1234"));
        assertFalse(variousUtilities.validateFirstLineStringFormat("12:34 56:78"));
        assertFalse(variousUtilities.validateFirstLineStringFormat("12 1234"));
    }

}
