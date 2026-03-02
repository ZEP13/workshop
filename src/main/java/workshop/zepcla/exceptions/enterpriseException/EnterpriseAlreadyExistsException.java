package workshop.zepcla.exceptions.enterpriseException;

public class EnterpriseAlreadyExistsException extends RuntimeException {
    public EnterpriseAlreadyExistsException(String message) {
        super("Enterprise already exists with name "+ message);
    }
}
