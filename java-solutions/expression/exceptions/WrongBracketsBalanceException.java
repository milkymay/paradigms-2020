package expression.exceptions;

public class WrongBracketsBalanceException extends Exception {
    public WrongBracketsBalanceException(String expression) {
        super("Brackets did't place correctly in expression: " + expression);
    }
}
