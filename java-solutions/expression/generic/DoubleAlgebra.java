package expression.generic;

public class DoubleAlgebra extends AbstractAlgebra<Double> {
    public Double parse(String a) {
        return Double.parseDouble(a);
    }

    public Double max(Double a, Double b) {
        return Math.max(a, b);
    }

    public Double min(Double a, Double b) {
        return Math.min(a, b);
    }

    @Override
    public Double count(Double a) {
        // :NOTE: try to do it in alternative way
        return (double) Long.bitCount(Double.doubleToLongBits(a));
    }

    public Double add(Double a, Double b) {
        return a + b;
    }

    public Double subtract(Double a, Double b) {
        return a - b;
    }

    public Double multiply(Double a, Double b) {
        return a * b;
    }

    public Double divide(Double a, Double b) {
        return a / b;
    }

    public Double negate(Double a) {
        return -a;
    }
}
