package expression.parser;

import expression.exceptions.ParserException;
import expression.exceptions.UnexpectedSymbolException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseParser {
    private final ExpressionSource source;
    protected char ch;

    protected BaseParser(final ExpressionSource source) {
        this.source = source;
    }

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : '\0';
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    public String getSource() {
        return source.getData();
    }

    protected void expect(final char c) throws ParserException {
        if (ch != c) {
            throw new UnexpectedSymbolException(getSource(), c, ch);
        }
        nextChar();
    }

    protected void expect(final String value) throws ParserException {
        for (char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected Exception error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
