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
        String newAns = Long.toBinaryString(Double.doubleToRawLongBits(a));
        double ans = 0;
        for (int i = 0; i < newAns.length(); i++) {
            if (newAns.charAt(i) == '1')
                ans++;
        }
        return ans;
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
