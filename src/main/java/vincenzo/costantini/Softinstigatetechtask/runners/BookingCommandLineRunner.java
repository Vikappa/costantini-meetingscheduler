package vincenzo.costantini.Softinstigatetechtask.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BookingCommandLineRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BookingCommandLineRunner.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("--------------------------------------------------------------------------");
        logger.info("Hello world!");
        logger.info("--------------------------------------------------------------------------");
    }
}
