package expression.parser;

import expression.TripleExpression;
import expression.exceptions.ExpectedArithmeticActionException;
import expression.exceptions.ParserException;
import expression.exceptions.WrongBracketsBalanceException;

public interface Parser<T extends Number> {
    TripleExpression<T> parse(String expression) throws ParserException;
}
