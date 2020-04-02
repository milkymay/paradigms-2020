package expression;

public class Variable<T extends Number> implements TripleExpression<T> {
    private String arg;
    public Variable (String arg) {
        this.arg = arg;
    }

    public T evaluate(T x, T y, T z) {
        if (!(arg.equals("x") || arg.equals("y") || arg.equals("z"))) {
            throw new IllegalArgumentException("Unexpected variable " + arg);
        } else {
            return arg.equals("x") ? x : arg.equals("y") ? y : z;
        }
    }
}
