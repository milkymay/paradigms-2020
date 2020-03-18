package expression.generic.exceptions;

public class DivisionByZeroException extends MathExpressionException {
    public DivisionByZeroException(String value) {
        super("Tried to divide " + value + " by 0");
    }
}
