package Softinstigatetechtask.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import vincenzo.costantini.Softinstigatetechtask.classes.Schedule;
import vincenzo.costantini.Softinstigatetechtask.services.BookingManager;
import vincenzo.costantini.Softinstigatetechtask.utilities.VariousUtilities;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = BookingManager.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)  
public class BookingManagerTests {

    //Since VariuousUtilities is a class cannot be correctly instantiated while testing BookingManager, 
    //i used a mock to always validate the working hours line
    @MockBean
    private VariousUtilities variousUtilitiesMock;

    @InjectMocks
    private BookingManager bookingManager;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks

        // validateWorkingHoursLine() will always return true when input is "0900 1730"
        when(variousUtilitiesMock.validateWorkingHoursLine("0900 1730")).thenReturn(true);

        // Default input 09:00 - 17:30
        bookingManager.setWorkingHours("0900 1730");
    }

    @Test
    public void testAddSchedule_ValidSchedule() {
        String firstLine = "2011-03-17 10:17:06 EMP001";
        String secondLine = "2011-03-21 09:00 2";

        //Fake string validation (so the test does not depend on an other class)
        when(variousUtilitiesMock.validateFirstLineStringFormat(firstLine)).thenReturn(true);
        when(variousUtilitiesMock.validateSecondLineStringFormat(secondLine)).thenReturn(true);
        when(variousUtilitiesMock.isScheduleInsideOfficeHours(
            any(LocalTime.class), any(LocalTime.class), any(Schedule.class))).thenReturn(true);

        bookingManager.addSchedule(firstLine, secondLine);

        assertEquals(1, bookingManager.getSchedules().size());//check if schedule was added
        Schedule schedule = bookingManager.getSchedules().get(0);
        assertEquals("EMP001", schedule.getEmployeeId());
        assertEquals("2011-03-21 09:00", schedule.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        assertEquals(2, schedule.getDuration()); //check if i can get object parameters
    }

    @Test
    public void testAddSchedule_InvalidFirstLine() {
        String firstLine = "Invalid Line";
        String secondLine = "2011-03-21 09:00 2";

        when(variousUtilitiesMock.validateFirstLineStringFormat(firstLine)).thenReturn(false);

        bookingManager.addSchedule(firstLine, secondLine);

        assertEquals(0, bookingManager.getSchedules().size());
    }

    @Test
    public void testAddSchedule_InvalidSecondLine() {
        String firstLine = "2011-03-17 10:17:06 EMP001";
        String secondLine = "Invalid Line";

        when(variousUtilitiesMock.validateFirstLineStringFormat(firstLine)).thenReturn(true);
        when(variousUtilitiesMock.validateSecondLineStringFormat(secondLine)).thenReturn(false);

        bookingManager.addSchedule(firstLine, secondLine);

        assertEquals(0, bookingManager.getSchedules().size());
    }

    @Test
    public void testAddSchedule_OutsideOfficeHours() {
        String firstLine = "2011-03-17 10:17:06 EMP001";
        String secondLine = "2011-03-21 18:00 2";

        when(variousUtilitiesMock.validateFirstLineStringFormat(firstLine)).thenReturn(true);
        when(variousUtilitiesMock.validateSecondLineStringFormat(secondLine)).thenReturn(true);
        when(variousUtilitiesMock.isScheduleInsideOfficeHours(
            any(LocalTime.class), any(LocalTime.class), any(Schedule.class))).thenReturn(false);

        bookingManager.addSchedule(firstLine, secondLine);

        assertEquals(0, bookingManager.getSchedules().size());
    }

    @Test
    public void testAddSchedule_ReplaceExistingSchedule() {
        String firstLine1 = "2011-03-17 10:17:06 EMP001";
        String secondLine1 = "2011-03-21 09:00 2";
        String firstLine2 = "2011-03-16 12:34:56 EMP002";
        String secondLine2 = "2011-03-21 09:00 2";

        when(variousUtilitiesMock.validateFirstLineStringFormat(firstLine1)).thenReturn(true);
        when(variousUtilitiesMock.validateSecondLineStringFormat(secondLine1)).thenReturn(true);
        when(variousUtilitiesMock.isScheduleInsideOfficeHours(
            any(LocalTime.class), any(LocalTime.class), any(Schedule.class))).thenReturn(true);

        when(variousUtilitiesMock.validateFirstLineStringFormat(firstLine2)).thenReturn(true);
        when(variousUtilitiesMock.validateSecondLineStringFormat(secondLine2)).thenReturn(true);

        bookingManager.addSchedule(firstLine1, secondLine1);
        bookingManager.addSchedule(firstLine2, secondLine2);

        List<Schedule> schedules = bookingManager.getSchedules();
        assertEquals(1, schedules.size());
        Schedule schedule = schedules.get(0);
        assertEquals("EMP002", schedule.getEmployeeId());
        assertEquals("2011-03-21 09:00", schedule.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        assertEquals(2, schedule.getDuration());
    }
}
