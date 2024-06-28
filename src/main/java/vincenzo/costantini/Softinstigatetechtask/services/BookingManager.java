package vincenzo.costantini.Softinstigatetechtask.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import vincenzo.costantini.Softinstigatetechtask.classes.Schedule;
import vincenzo.costantini.Softinstigatetechtask.runners.BookingCommandLineRunner;
import vincenzo.costantini.Softinstigatetechtask.utilities.VariousUtilities;

//This class is meant as a service that stores the schedules, checks conflicts, organizes them and returns
//resumes of the results.

@Service
@Getter
@Setter
public class BookingManager {
    private static final Logger logger = LoggerFactory.getLogger(BookingCommandLineRunner.class);

    @Autowired
    private VariousUtilities variousUtilities;

    //Date of start of tracking time
    private LocalDateTime managerCreationDate;

    //All the schedules
    private ArrayList<Schedule> schedules;

    //Office opening and closing times (used to check conflicts)
    private LocalTime officeOpeningTime;
    private LocalTime officeClosingTime;


    public BookingManager(){
        //Initialize the manager creation date
        this.managerCreationDate = LocalDateTime.now();
        this.schedules = new ArrayList<Schedule>();
        this.askOfficeOpeningTime();
    }


    //only call this method into the costructur of the class to finalize data creation
    private void askOfficeOpeningTime() {

        //boolean to exit the next loops
        boolean validOpening = false;
        boolean validClosing = false;

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        Scanner openingTimeScanner = new Scanner(System.in);

        LocalTime openingHour = null;
        LocalTime closingHour = null;

        logger.info("-------------------------------------------------------------------------------------");

        //Asks the opening time until it is valid
        while(!validOpening){
            logger.info("Please, insert office opening time in the format HH:MM");
            String input = openingTimeScanner.nextLine();
            try {
                openingHour = LocalTime.parse(input, timeFormatter);
                this.officeOpeningTime = openingHour;
                logger.info("-------------------------------------------------------------------------------------");
                validOpening = true;
            } catch (DateTimeParseException e) {
                logger.warn("Invalid input. Please, insert the opening time in the format HH:MM");
            }
        }

        //Asks the closing time until it is valid
        while(!validClosing){
            logger.info("Please, insert office closing time in the format HH:MM");
            String input = openingTimeScanner.nextLine();
            try {
                closingHour = LocalTime.parse(input, timeFormatter);
                if(closingHour.isBefore(this.officeOpeningTime)){
                    logger.warn("The closing time must be after the opening time");
                }else{
                    this.officeClosingTime = closingHour;
                    validClosing = true;
                }
            } catch (DateTimeParseException e) {
                logger.warn("Invalid input. Please, insert the opening time in the format HH:MM");
            }
        }

        //Close the scanner and check method results
        logger.info("Opening time set: " + this.officeOpeningTime + " Closing time set: " + this.officeClosingTime);
        logger.info("The office is supposed to be open for " + (this.officeClosingTime.getHour() - this.officeOpeningTime.getHour()) + " hours");
    }

    public void addSchedule() {
        Scanner scheduleInputScanner = new Scanner(System.in);
        logger.info("Please type the schedule in the following format: request submission time, in the format YYYY-MM-DD HH:MM:SS][arch:employee id]");
        logger.info("Example: 2011-03-16 09:28:23 EMP003 (please note that if you don't type 'EMP' at the beginning of the employee id, it will not be recognized)");
        
        //I prepare the values to create a schedule object
        String scheduleRequestDateTime = "";
        String scheduleEmployee = "";
        String scheduleStartTime = "";
        int scheduleDuration = 0;
        
        boolean firstLineIsValid = false;

        while (!firstLineIsValid) {

            String input = scheduleInputScanner.nextLine();

            if(variousUtilities.validateFirstLineStringFormat(input)){
                logger.info("Valid input.");
                firstLineIsValid = true;
            } else {
                logger.info("Invalid input. Please, insert the schedule in the format YYYY-MM-DD HH:MM:SS EMP###");
            } 
            scheduleInputScanner.nextLine();//Consume the remeaning input
        }
    }
}
