package workshop.zepcla.exceptions.enterpriseException;

public class EnterpriseNotFound extends RuntimeException {
    public EnterpriseNotFound(String message) {
        super("Enterprise not found " + message);
    }
}
