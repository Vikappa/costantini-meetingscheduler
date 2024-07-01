package Softinstigatetechtask.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import vincenzo.costantini.Softinstigatetechtask.SoftinstigatetechtaskApplication;
import vincenzo.costantini.Softinstigatetechtask.services.BookingManager;

@SpringBootTest(classes = SoftinstigatetechtaskApplication.class)
@ActiveProfiles("test")
public class BookingManagerTests {
    private static final Logger logger = LoggerFactory.getLogger(BookingManagerTests.class);

    @Autowired
    private BookingManager bookingManager;

    @Test
    void test_set_working_hours_validity(){
        logger.info("---------------------------------------TEST 1----------------------------------------");
        // When created, the object should have null values for the working hours
        assertTrue(bookingManager.getOfficeOpeningTime() == null && bookingManager.getOfficeClosingTime() == null);

        // The method set working hours should not work neither crash te application when invalid input is given
        bookingManager.setWorkingHours("1234 567a");
        assertTrue(bookingManager.getOfficeOpeningTime() == null && bookingManager.getOfficeClosingTime() == null);

        bookingManager.setWorkingHours("0900 1730");
        logger.info(bookingManager.getOfficeOpeningTime().toString());
        assertTrue(bookingManager.getOfficeOpeningTime()!= null && bookingManager.getOfficeClosingTime()!= null);
    }
}
