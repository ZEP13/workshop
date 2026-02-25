package workshop.zepcla.exceptions.appointmentException;

public class AppointmentNotFound extends RuntimeException {
    public AppointmentNotFound(String message) {
        super("This appointment is not found" + message);
    }
}
