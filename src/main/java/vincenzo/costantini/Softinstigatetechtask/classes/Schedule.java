package vincenzo.costantini.Softinstigatetechtask.classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Schedule {

    private LocalDateTime bookedAt; // [request submission time, in the format YYYY-MM-DD HH:MM:SS]
    private String employeeId; // [arch:employee id]
    private LocalDateTime startAt; // [meeting start time, in the format YYYY-MM-DD HH:MM]
    private int duration; // [arch:meeting duration in hours]

    //The following are used to parse the strings to LocalDateTime and TimeUnit objects
    private DateTimeFormatter bookedAtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter startAtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public Schedule(String bookedAt, String employeeId, String startAt, int duration) {
        //The Shedule is supposed to be created from strings to parse from user input to LocalDateTime and TimeUnit objects using the formatters i wrote above
        this.bookedAt = LocalDateTime.parse(bookedAt, bookedAtFormatter);
        this.employeeId = employeeId;
        this.startAt = LocalDateTime.parse(startAt, startAtFormatter);
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "bookedAt=" + bookedAt +
                ", employeeId='" + employeeId + '\'' +
                ", startAt=" + startAt +
                ", timeUnit=" + duration +
                '}';
    }
}
