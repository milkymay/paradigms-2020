package expression.generic.exceptions;

public class UnsupportedTypeException extends Exception {
    public UnsupportedTypeException(String mode) {
        super("Unsupported type: " + mode);
    }
}
