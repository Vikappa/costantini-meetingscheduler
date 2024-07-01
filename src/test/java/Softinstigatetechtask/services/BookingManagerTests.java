package Softinstigatetechtask.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import vincenzo.costantini.Softinstigatetechtask.services.BookingManager;
import vincenzo.costantini.Softinstigatetechtask.utilities.VariousUtilities;

@SpringBootTest(classes = BookingManager.class)
@ActiveProfiles("test")
public class BookingManagerTests {
    private static final Logger logger = LoggerFactory.getLogger(BookingManagerTests.class);

    @MockBean
    private VariousUtilities variousUtilities;

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

        bookingManager.setWorkingHours("0900 1700");
        logger.info(bookingManager.getOfficeOpeningTime().toString());
        assertTrue(bookingManager.getOfficeOpeningTime()!= null && bookingManager.getOfficeClosingTime()!= null);

    }
}
