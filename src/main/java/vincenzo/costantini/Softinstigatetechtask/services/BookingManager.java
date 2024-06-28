package vincenzo.costantini.Softinstigatetechtask.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import vincenzo.costantini.Softinstigatetechtask.classes.Schedule;
import vincenzo.costantini.Softinstigatetechtask.runners.BookingCommandLineRunner;

//This class is meant as a service that stores the schedules, checks conflicts, organizes them and returns
//resumes of the results.

@Service
@Getter
@Setter
public class BookingManager {
    private static final Logger logger = LoggerFactory.getLogger(BookingCommandLineRunner.class);

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
                    logger.info("-------------------------------------------------------------------------------------");
                    validClosing = true;
                }
            } catch (DateTimeParseException e) {
                logger.warn("Invalid input. Please, insert the opening time in the format HH:MM");
            }
        }

        //Close the scanner and check method results
        openingTimeScanner.close();
        logger.info("Opening time set: " + this.officeOpeningTime + " Closing time set: " + this.officeClosingTime);
        logger.info("The office is supposed to be open for " + (this.officeClosingTime.getHour() - this.officeOpeningTime.getHour()) + " hours");
    }

}
