package expression;

import expression.generic.AbstractAlgebra;

import java.util.Objects;

public abstract class MathOperations<T> implements TripleExpression<T> {
    protected TripleExpression<T> first;
    protected TripleExpression<T> second;
    protected AbstractAlgebra<T> algebra;
    protected abstract T operate(T a, T b);

    public MathOperations(TripleExpression<T> first, TripleExpression<T> second, AbstractAlgebra <T> algebra) {
        this.first = first;
        this.second = second;
        this.algebra = algebra;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return operate(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }

    @Override
    public int hashCode() {
         return Objects.hash(first, second);
    }

    public boolean equals(Object expression) {
        if (expression == this)
            return true;
        if (expression == null || expression.getClass() != this.getClass())
            return false;
        MathOperations exp = (MathOperations) expression;
        return this.first.equals(exp.first) && second.equals(exp.second);
    }
}
