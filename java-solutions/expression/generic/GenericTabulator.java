package expression.generic;

import expression.TripleExpression;
import expression.exceptions.MathExpressionException;
import expression.exceptions.UnsupportedTypeException;
import expression.parser.ExpressionParser;

import java.math.BigInteger;
import java.util.function.Function;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        switch (mode) {
            case "i":
                return setTable(this::applyInteger, 
                        new ExpressionParser<>(new IntegerAlgebra()).parse(expression),
                        x1, x2, y1, y2, z1, z2);
            case "d":
                return setTable(this::applyDouble, 
                        new ExpressionParser<>(new DoubleAlgebra()).parse(expression), 
                        x1, x2, y1, y2, z1, z2);
            case "bi":
                return setTable(this::applyBigInteger,
                        new ExpressionParser<>(new BigIntegerAlgebra()).parse(expression), 
                        x1, x2, y1, y2, z1, z2);
            default:
                throw new UnsupportedTypeException(mode);
        }
    }

    private <T extends Number> Object[][][] setTable(Function<Integer, T> cast, TripleExpression<T> expression, int x1, int x2, int y1, int  y2, int z1, int z2) {
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int x = 0; x <= x2 - x1; x++) {
            for (int y = 0; y <= y2 - y1; y++) {
                for (int z = 0; z <= z2 - z1; z++) {
                    try {
                        result[x][y][z] = expression.evaluate(cast.apply(x + x1), cast.apply(y + y1), cast.apply(z + z1));
                    } catch (MathExpressionException e) {
                        result[x][y][z] = null;
                    }
                }
            }
        }
        return result;
    }

    private Integer applyInteger(int a) {
        return a;
    }

    private Double applyDouble(int a) {
        return (double) a;
    }

    private  BigInteger applyBigInteger(int a) {
        return BigInteger.valueOf(a);
    }
}
