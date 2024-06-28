package vincenzo.costantini.Softinstigatetechtask.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
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


    //All the schedules
    private ArrayList<Schedule> schedules;

    //Office opening and closing times (used to check conflicts)
    private LocalTime officeOpeningTime;
    private LocalTime officeClosingTime;


    public BookingManager(){
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

    //Note that the first line inquires about the schedule request time and employee and the second line inquires about the schedule start and duration
   //This method asks and pharses inputs to create a schedule object, and prints direct responses if the schedule is valid or not. This is meant to be used real time during the loop iteration
    public void addSchedule() {
        Scanner scheduleInputScanner = new Scanner(System.in);
        logger.info("SCHEDULE REQUEST DATE TIME AND EMPLOYEE:");
        logger.info("Please type the schedule request time in the following format: [request submission time, in the format YYYY-MM-DD HH:MM:SS][arch:employee id]");
        logger.info("Example: 2011-03-16 09:28:23 EMP003 (please note that if you don't type 'EMP' at the beginning of the employee id, it will not be recognized)");
        
        //I prepare the values to create a schedule object
        String scheduleRequestDateTime = "";
        String scheduleEmployee = "";
        String scheduleStartTime = "";
        int scheduleDuration = 0;
        
        boolean firstLineIsValid = false;

        while (!firstLineIsValid) {

            String input = scheduleInputScanner.nextLine().trim();

            if(variousUtilities.validateFirstLineStringFormat(input)){ //Validation via regex

                scheduleRequestDateTime = input.substring(0, 19); // Since the string is checked i can take the first part
                scheduleEmployee = input.substring(20); //The rest of the string must be EMP###
                
                firstLineIsValid = true;
            } else {
                logger.info("ERROR");
                logger.info("Invalid input. Please, insert the schedule request time in the format YYYY-MM-DD HH:MM:SS EMP###");
            } 
        }

        //Parse the request date time to check if it is unique
        LocalDateTime requestDateTime = LocalDateTime.parse(scheduleRequestDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //If there is any match in the schedules list throw an error
        if(this.schedules.stream().anyMatch(sched -> {
            return sched.getBookedAt().equals(requestDateTime);
        })){
            logger.info("ERROR");
            logger.info("The schedule request time is not unique. Please, insert a different time");
            return;
        }


        logger.info("SUCCES!");
        boolean secondLineIsValid = false;

        logger.info("SCHEDULE START DATE AND DURATION:");
        logger.info("Please type the schedule starting time and duration in the following format:");
        logger.info("HHHH-MMM-DD HH:MM #  where the # is the duration in hours, example: '2011-03-22 14:00 2'");

        while (!secondLineIsValid) {
            String input = scheduleInputScanner.nextLine().trim();

            if(variousUtilities.validateSecondLineStringFormat(input)){
                scheduleStartTime = input.substring(0, 16);
                scheduleDuration = Integer.parseInt(input.substring(17));//the remeaning chars will be parsed as an int (even if type 999!)
                secondLineIsValid = true;
            } else {
                logger.info("Invalid input. Please, insert the schedule in the format YYYY-MM-DD HH:MM:SS EMP###");
            }
        }

        //I checked out that the string are valid, now i can create the schedule object and check if it creates clonflicts
        Schedule schedule = new Schedule(scheduleRequestDateTime, scheduleEmployee, scheduleStartTime, scheduleDuration);

        //check the schedule is inside the office opening hours
        if(
            !schedule.getStartHour().isBefore(officeOpeningTime) && // non before opening === >= opening time        
            !schedule.getStartHour().isAfter(officeClosingTime) && // non after closing === <= closing time
            schedule.getEndHour().isAfter(officeOpeningTime) && //before opening
            !schedule.getEndHour().isAfter(officeClosingTime) //non after closing === <= closing time
        ) {

            //check the schedule is not overlapping with other schedules
            if(!variousUtilities.checkScheduleConflicts(schedule, this.schedules)) {
                this.schedules.add(schedule);
                this.schedules.sort(Comparator.comparing(Schedule::getStartAt));//sort the schedules by start time every time
                logger.info("The schedule has been added");
            } else {
                logger.info("The schedule is overlapping with an other schedule. Please, retry");
                return;
            }

        } else {
            logger.info("The schedule is not valid because it's outside opening hours [" + this.officeOpeningTime + " - " + this.officeClosingTime + "]. Please, insert a valid schedule");
            return;
        }
    }

    //Override method to get the task expected output
    @Override
    public String toString() {
        if (this.schedules.isEmpty()) {
            return "No schedules available.";
        }

        StringBuilder returnString = new StringBuilder();
        returnString.append("SCHEDULES:\n");

        //I get an hashset of every date that has a schedule to better manage the output
        HashSet<LocalDate> hashsetDate = new HashSet<>();

        this.schedules.forEach(schedule -> {
            hashsetDate.add(schedule.getStartDate());
        });

        //create the final output string
        hashsetDate.forEach(date -> {
            returnString.append(date.toString() + "\n");
            this.schedules.stream().filter(schedule -> schedule.getStartDate().isEqual(date)).forEach(schedule -> {
                returnString.append(schedule.toString() + "\n");
            });
        });

        return returnString.toString();
    }
    

}
