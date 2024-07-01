package vincenzo.costantini.Softinstigatetechtask.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vincenzo.costantini.Softinstigatetechtask.services.BookingManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

@Component
public class BookingCommandLineRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BookingCommandLineRunner.class);

    @Autowired
    private BookingManager bookingManager;

    @Override
    public void run(String... args) throws Exception {
        StringBuilder allInput = new StringBuilder();
        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("Paste input and press ENTER. Type 'END' and press ENTER.");
            ArrayList<String> allLines = new ArrayList<>();
        
            while (scanner.hasNextLine()) {//loop untile all the lines are read
                String line = scanner.nextLine();
                if (line.equals("")) {// No more input from the console, stop the loop
                    break;
                }
                allLines.add(line.trim());//trim for safety        
            }
            
            scanner.close();
        
            //print the input for verification
            for (String line : allLines) {
                logger.info(line+"\n");
            }

        } catch (Exception e) {
            logger.error("An error occurred while processing input.", e);
        }

        // Output the final string to verify
        String result = allInput.toString();
        logger.info("Captured Input:\n" + result);
    }
}
