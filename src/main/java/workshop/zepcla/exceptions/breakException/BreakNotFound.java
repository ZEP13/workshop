package workshop.zepcla.exceptions.breakException;

public class BreakNotFound extends RuntimeException {
    public BreakNotFound(String message) {
        super("This break is not found" + message);
    }
}