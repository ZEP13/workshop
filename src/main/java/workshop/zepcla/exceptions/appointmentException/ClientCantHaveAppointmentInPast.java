package workshop.zepcla.exceptions.appointmentException;

public class ClientCantHaveAppointmentInPast extends RuntimeException {
    public ClientCantHaveAppointmentInPast(String message) {
        super("Impossible, the client can't have an appointment in the past : " + message);
    }
}
