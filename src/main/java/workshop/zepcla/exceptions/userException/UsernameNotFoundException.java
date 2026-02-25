package workshop.zepcla.exceptions.userException;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String username) {
        super("User with username '" + username + "' not found. Please check the username and try again.");
    }
}
