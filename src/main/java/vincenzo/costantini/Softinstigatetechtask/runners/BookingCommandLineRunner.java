package vincenzo.costantini.Softinstigatetechtask.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vincenzo.costantini.Softinstigatetechtask.services.BookingManager;

import java.util.Scanner;

@Component
public class BookingCommandLineRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BookingCommandLineRunner.class);

    @Autowired
    private BookingManager bookingManager;

    @Override
    public void run(String... args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean continua = true;
            while (continua) {
                logger.info("Please type a schedule, type 'INFO' to print schedules information or type 'EXIT' to shut down application");

                // Debug log before reading input
                logger.debug("Waiting for user input...");
                
                try {
                    String scelta = scanner.nextLine();
                    
                    // Debug log after reading input
                    logger.debug("User input received: {}", scelta);
                    
                    switch (scelta) {
                        case "EXIT":
                            continua = false;
                            logger.info("Shutting down the application...");
                            break;
                        case "INFO":
                            logger.info(bookingManager.toString());
                            break;
                        default:
                            try {
                                bookingManager.addSchedule(scelta);
                            } catch (Exception e) {
                                logger.error("Invalid input. Please try again", e);
                            }
                            break;
                    }
                } catch (Exception e) {
                    logger.error("An error occurred while reading input.", e);
                }
            }
        } catch (Exception e) {
            logger.error("An error occurred while processing input.", e);
        }
    }
}
