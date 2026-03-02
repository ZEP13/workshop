package workshop.zepcla.exceptions.appointmentException;

public class UnauthorizedAppointmentAccessException extends RuntimeException {
    public UnauthorizedAppointmentAccessException(String message) {
        super(message);
    }
}
