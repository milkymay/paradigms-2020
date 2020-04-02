package expression;

import expression.generic.AbstractAlgebra;

public class Multiply<T extends Number> extends MathOperations<T> {

    public Multiply(TripleExpression<T> first, TripleExpression<T> second, AbstractAlgebra<T> algebra) {
        super(first, second, algebra);
    }

    @Override
    public T operate(T a, T b) {
        return algebra.multiply(a, b);
    }
}