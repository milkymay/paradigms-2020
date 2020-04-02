package expression;

import expression.generic.AbstractAlgebra;

public class Negate<T extends Number> implements TripleExpression<T> {
    private TripleExpression<T> expression;
    private AbstractAlgebra<T> algebra;
    public Negate(TripleExpression<T> expression, AbstractAlgebra<T> algebra) {
        this.expression = expression;
        this.algebra = algebra;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return algebra.negate(expression.evaluate(x, y, z));
    }

}
