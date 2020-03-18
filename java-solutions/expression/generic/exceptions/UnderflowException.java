package expression.generic.exceptions;

public class UnderflowException extends MathExpressionException {
    public UnderflowException(String msg) {
        super("Underflow when trying to count " + msg);
    }
}
