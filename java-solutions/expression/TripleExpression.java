package expression;

// :NOTE: where is a bound on generic type T? Now I can divide String by String
public interface TripleExpression <T extends Number> {
    T evaluate(T x, T y, T z);
}
