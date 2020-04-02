package expression.exceptions;

public class InvalidSymbolException extends ParserException {
    public InvalidSymbolException(int pos, String exp, StringBuilder error) {
        super("Symbol is strange on position: " + pos + "\n" + exp + "\n" + error.toString());
    }
}
