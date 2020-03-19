package expression;

public class Variable<T> implements TripleExpression<T> {
    private String argName;
    public Variable (String argName) {
        this.argName = argName;
    }

    public T evaluate(T x, T y, T z) {
        if (!(argName.equals("x") || argName.equals("y") || argName.equals("z"))) {
            throw new IllegalArgumentException("Unexpected variable " + argName);
        } else {
            return argName.equals("x") ? x : argName.equals("y") ? y : z;
        }
    }
}
