package workshop.zepcla.exceptions.userException;

public class UserPasswordNotMatchException extends RuntimeException {
    public UserPasswordNotMatchException() {
        super("The password you entered does not match our records. Please try again.");
    }
}
