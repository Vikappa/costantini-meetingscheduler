package vincenzo.costantini.Softinstigatetechtask.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vincenzo.costantini.Softinstigatetechtask.services.BookingManager;
import vincenzo.costantini.Softinstigatetechtask.utilities.VariousUtilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

@Component
public class BookingCommandLineRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BookingCommandLineRunner.class);

    @Autowired
    private BookingManager bookingManager;

    @Autowired
    private VariousUtilities variousUtilities;

    @Override
    public void run(String... args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("Paste input and press ENTER. (may be necessary pressing it twice)");
            ArrayList<String> allLines = new ArrayList<>();
        
            while (scanner.hasNextLine()) { // loop until all the lines are read
                String line = scanner.nextLine();
                if (line.equals("")) { // No more input from the console, stop the loop
                    break;
                }
                allLines.add(line.trim()); // trim for safety        
            }
        
    
            
            if (variousUtilities.validateWorkingHoursLine(allLines.get(0).trim())) { // If the First line is valid the rest will be processed
                bookingManager.setWorkingHours(allLines.get(0));
                for (int i = 1; i < allLines.size(); i += 2) { // fix the loop to iterate correctly
                    bookingManager.addSchedule(allLines.get(i), allLines.get(i + 1));
                }
            } 
            
            logger.info("OUTPUT");
            logger.info(bookingManager.toString());
    
        } catch (Exception e) {
            logger.error("An error occurred while processing input.", e);
        }
    }
    
}
