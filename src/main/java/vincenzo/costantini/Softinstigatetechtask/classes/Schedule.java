package vincenzo.costantini.Softinstigatetechtask.classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

@Getter
public class Schedule {

    private LocalDateTime bookedAt; // [request submission time, in the format YYYY-MM-DD HH:MM:SS]
    private String employeeId; // [arch:employee id]
    private LocalDateTime startAt; // [meeting start time, in the format YYYY-MM-DD HH:MM]
    private int duration; // [arch:meeting duration in hours]

    //The following are used to parse the strings to LocalDateTime and TimeUnit objects
    private DateTimeFormatter bookedAtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter startAtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");


    public Schedule(String bookedAt, String employeeId, String startAt, int duration) {
        //The Shedule is supposed to be created from strings to parse from user input to LocalDateTime and TimeUnit objects using the formatters i wrote above
        this.bookedAt = LocalDateTime.parse(bookedAt, bookedAtFormatter);
        this.employeeId = employeeId;
        this.startAt = LocalDateTime.parse(startAt, startAtFormatter);
        this.duration = duration;
    }

    public LocalDateTime getCalculatedEndAt() {
        //Calculate the end time by adding the duration to the startAt time
        return this.startAt.plusHours(this.duration);
    }

    @Override
    public String toString() {
        // Format start time
        String startTimeFormatted = this.startAt.format(timeFormatter);

        // Calculate end time by adding duration to startAt
        LocalDateTime endAt = this.startAt.plusHours(this.duration);
        
        // Format end time
        String endTimeFormatted = endAt.format(timeFormatter);

        // Return the schedule object as request output of the task
        return startTimeFormatted + " " + endTimeFormatted;
    }
}
