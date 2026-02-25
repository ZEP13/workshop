package workshop.zepcla.exceptions.appointmentException;

public class NoAvaibleAppointment extends RuntimeException {
    public NoAvaibleAppointment(String message) {

        super("Sorry, no avaible appointment " + message);
    }
}
