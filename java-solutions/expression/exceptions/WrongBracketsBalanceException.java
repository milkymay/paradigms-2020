package expression.exceptions;

public class WrongBracketsBalanceException extends Exception {
    public WrongBracketsBalanceException(String expression) {
        super("Incorrect brackets: " + expression);
    }
}
