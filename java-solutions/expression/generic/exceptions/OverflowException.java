package expression.generic.exceptions;

public class OverflowException extends MathExpressionException {
    public OverflowException(String msg) {
        super("Overflow when trying to count " + msg);
    }
}
