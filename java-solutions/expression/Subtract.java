package expression;

import expression.generic.AbstractAlgebra;

public class Subtract<T extends Number> extends MathOperations<T> {
    public Subtract(TripleExpression<T> first, TripleExpression<T> second, AbstractAlgebra<T> algebra) {
        super(first, second, algebra);
    }

    @Override
    public T operate(T a, T b) {
        return algebra.subtract(a, b);
    }
}
