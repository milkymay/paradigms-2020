package expression.exceptions;

public class WrongBracketsBalanceException extends ParserException {
    public WrongBracketsBalanceException(String expression) {
        super("Incorrect brackets: " + expression);
    }
}
