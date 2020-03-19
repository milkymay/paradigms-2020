package expression.parser;

import expression.TripleExpression;
import expression.exceptions.ExpectedArithmeticActionException;
import expression.exceptions.ParserException;
import expression.exceptions.WrongBracketsBalanceException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Parser<T> {
    TripleExpression<T> parse(String expression) throws ParserException, WrongBracketsBalanceException;

}
