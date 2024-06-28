package vincenzo.costantini.Softinstigatetechtask.utilities;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;

// Il will keep various parsers and validator here to keep the main classes cleaner
@Service
public class VariousUtilities {

    //Checks if the string is in the format YYYY-MM-DD HH:MM:SS EMP###
    public static boolean validateFirstLineStringFormat(String input) {
        // Regex pattern for the specified format: YYYY-MM-DD HH:MM:SS EMP###
        String regex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} EMP\\d{3}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    //Checks if the string is in the format YYYY-MM-DD HH:MM d (d is the duration in hours)
    public static boolean validateSecondLineStringFormat(String input) {
        String regex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2} \\d+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

}
