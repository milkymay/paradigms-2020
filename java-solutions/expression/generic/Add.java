package expression.generic;

import expression.generic.AbstractAlgebra;

public class Add<T> extends MathOperations<T>{

    public Add(TripleExpression<T> first, TripleExpression<T> second, AbstractAlgebra<T> algebra) {
        super(first, second, algebra);
    }

    public T operate(T a, T b) {
        return algebra.add(a, b);
    }

}
