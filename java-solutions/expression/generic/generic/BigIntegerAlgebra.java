package expression.generic.generic;

import expression.exceptions.DivisionByZeroException;

import java.math.BigInteger;

public class BigIntegerAlgebra extends AbstractAlgebra<BigInteger> {
    public BigInteger max(BigInteger a, BigInteger b) {
        return a.max(b);
    }

    public BigInteger min(BigInteger a, BigInteger b) {
        return a.min(b);
    }

    public BigInteger parse(String a) {
        return new BigInteger(a);
    }

    public BigInteger count(BigInteger a) {
        return new BigInteger(String.valueOf(a.bitCount()));
    }

    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    public BigInteger subtract(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    public BigInteger multiply(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    public BigInteger divide(BigInteger a, BigInteger b) {
        if (b.equals(new BigInteger(String.valueOf(0)))) {
            throw new DivisionByZeroException(a.toString());
        }
        return a.divide(b);
    }

    public BigInteger negate(BigInteger a) {
        return a.negate();
    }
}