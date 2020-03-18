package expression.generic.parser;

import expression.exceptions.ParserException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface ExpressionSource {
    boolean hasNext();
    char next();
    String getData();
    ParserException error(final String message);
}
