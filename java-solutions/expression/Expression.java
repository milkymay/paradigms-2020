package expression;

import expression.ToMiniString;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Expression extends ToMiniString {
    int evaluate(int x);
}
