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
        // Until explicit shutdown request, this method will ask the user to input a new schedule o print the schedules informations
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                logger.info("Please type a schedule, type 'INFO' to print schedules information or type 'EXIT' to shut down application");
                try {
                    String inputTyped = scanner.nextLine();
                                        
                    switch (inputTyped) {
                        case "EXIT":
                            running = false;
                            logger.info("Shutting down the application...");
                            System.exit(0);
                        case "INFO":
                            logger.info(bookingManager.toString());
                            break;
                        default:
                            try {
                                bookingManager.addSchedule();
                            } catch (Exception e) {
                                logger.error("Schedule was not registered", e);
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
