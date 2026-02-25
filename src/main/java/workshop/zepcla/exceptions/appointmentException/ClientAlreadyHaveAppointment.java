package workshop.zepcla.exceptions.appointmentException;

public class ClientAlreadyHaveAppointment extends RuntimeException {
    public ClientAlreadyHaveAppointment(String message) {
        super("The client is already have an appointment: " + message);
    }
}
