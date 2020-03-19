package expression.exceptions;

public class UnsupportedTypeException extends Exception {
    public UnsupportedTypeException(String mode) {
        super("Unsupported type: " + mode);
    }
}
