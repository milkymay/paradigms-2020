package expression.generic;

import expression.exceptions.*;

public class IntegerAlgebra extends AbstractAlgebra<Integer> {
    public Integer add(Integer a, Integer b) throws OverflowException {
        String expr = a + " + " + b;
        if (a > 0 && Integer.MAX_VALUE - a < b) {
            throw new OverflowException(expr);
        }
        if (a < 0 && Integer.MIN_VALUE - a > b) {
            throw new UnderflowException(expr);
        }
        return a + b;
    }

    public Integer subtract(Integer a, Integer b) {
        String expr = a + " - " + b;
        if (b < 0 && Integer.MAX_VALUE + b < a) {
            throw new OverflowException(expr);
        }
        if (b > 0 && Integer.MIN_VALUE + b > a) {
            throw new UnderflowException(expr);
        }
        return a - b;
    }

    public Integer multiply(Integer a, Integer b) {
        String expr = a + " * " + b;
        if (a == Integer.MIN_VALUE && b < 0 ||
                b == Integer.MIN_VALUE && a < 0 ||
                a > 0 && b > 0 && a > Integer.MAX_VALUE / b ||
                a < 0 && b < 0 && -a > Integer.MAX_VALUE / -b) {
            throw new OverflowException(expr);
        }
        if (a > 0 && b < 0 && b < Integer.MIN_VALUE / a ||
                a < 0 && b > 0 && a < Integer.MIN_VALUE / b) {
            throw new UnderflowException(expr);
        }
        return a * b;
    }

    public Integer divide(Integer a, Integer b) {
        if (b == 0) { throw new DivisionByZeroException(a.toString()); }
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowException(a + " / " + b);
        }
        return a / b;
    }

    public Integer negate(Integer a) {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("-" + a);
        }
        return -a;
    }

    public Integer max(Integer a, Integer b) {
        return Math.max(a, b);
    }

    public Integer min(Integer a, Integer b) {
        return Math.min(a, b);
    }

    public Integer count(Integer a) {
        // :NOTE: this method should be invoked in static way
        return a.bitCount(a);
    }

    public Integer parse(String a) throws ParserException {
        try {
            return Integer.parseInt(a);
        } catch (NumberFormatException e) {
            throw new ValueIsNot32BitNumber(a + " is't 32 bit number");
        }
    }

}
