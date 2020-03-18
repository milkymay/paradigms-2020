package expression.generic.exceptions;

public class UnexpectedSymbolException extends ParserException {
    public UnexpectedSymbolException(String parsed, char c, char ch) {
        super("Parsed " + parsed + " and expected '" + c + "', found '" + ch + "'");
    }
}
