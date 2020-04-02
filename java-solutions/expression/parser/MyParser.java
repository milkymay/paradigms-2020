//package expression.parser;
//
//import expression.*;
//import expression.exceptions.*;
//import expression.generic.AbstractAlgebra;
//import expression.parser.BaseParser;
//import expression.parser.ExpressionSource;
//import expression.parser.StringSource;
//
//public class MyParser<T> implements Parser {
//    private static AbstractAlgebra<T> myAlgebra;
//
//    public MyParser(AbstractAlgebra<T> algebra) {
//        myAlgebra = algebra;
//    }
//
//    public TripleExpression parse(final String source) throws ParserException {
//        return parse(new StringSource(source));
//    }
//
//    public CommonExpression parse(ExpressionSource source) throws ParserException {
//        return new Expression(source).parseExpression();
//    }
//
//    private static class Expression extends BaseParser {
//        public Expression(final ExpressionSource source) {
//            super(source);
//            nextChar();
//        }
//
//        public CommonExpression parseExpression() throws ParserException {
//            final CommonExpression result = parseTerm();
//            if (test('\0')) {
//                return result;
//            }
//            throw new InvalidInputException("Parsed " + getSource() + " and expected the end of Expression");
//        }
//
//        private CommonExpression parseTerm() throws ParserException {
//            skipWhitespace();
//            CommonExpression result = parseFactor();
//            skipWhitespace();
//            while(ch == '+' || ch == '-') {
//                if (test('+')) {
//                    result = new Add(result, parseFactor(), MyParser.myAlgebra);
//                } else if (test('-')) {
//                    result = new Subtract(result, parseFactor());
//                }
//                skipWhitespace();
//            }
//            return result;
//        }
//
//        private CommonExpression parseFactor() throws ParserException {
//            skipWhitespace();
//            CommonExpression result = parseValue();
//            skipWhitespace();
//            while(ch == '*' || ch == '/') {
//                if (test('*')) {
//                    skipWhitespace();
//                    result = new Multiply(result, parseValue());
//                } else if (test('/')) {
//                    skipWhitespace();
//                    result = new Divide(result, parseValue());
//                }
//                skipWhitespace();
//            }
//            return result;
//        }
//
//        private CommonExpression parseValue() throws ParserException {
//            if (test('(')) {
//                CommonExpression cur = parseTerm();
//                expect(')');
//                return cur;
//            } else if (test('-')) {
//                return parseUnary();
//            } else if (ch == 'x' || ch =='y' || ch == 'z') {
//                char cur = ch;
//                nextChar();
//                return new Variable(String.valueOf(cur));
//            } else if (test('l')) {
//                expect("og2");
//                if (test('-')) {
//                    return new Log2(new Negate(parseValue()));
//                } else if (test(' ')) {
//                    return new Log2(parseValue());
//                } else if (ch == '(') {
//                    return new Log2(parseTerm());
//                }
//            } else if (test('p')) {
//                expect("ow2");
//                skipWhitespace();
//                return new Pow2(parseValue());
//            } else if (Character.isDigit(ch)) {
//                return parseNumber();
//            }
//            throw new InvalidSymbolsException(getSource(), String.valueOf(ch));
//        }
//
//        private CommonExpression parseUnary() throws ParserException {
//            skipWhitespace();
//            final StringBuilder sb = new StringBuilder("-");
//            copyDigits(sb);
//            if (sb.length() == 1) {
//                return new Negate(parseValue());
//            } else {
//                try {
//                    return new Const(Integer.parseInt(sb.toString()));
//                } catch (NumberFormatException e) {
//                    throw new OverflowException(sb.toString());
//                }
//            }
//        }
//
//        private CommonExpression parseNumber() throws ParserException {
//            final StringBuilder sb = new StringBuilder();
//            copyDigits(sb);
//            try {
//                return new Const(Integer.parseInt(sb.toString()));
//            } catch (NumberFormatException e) {
//                throw new OverflowException(sb.toString());
//            }
//        }
//
//        private void copyDigits(final StringBuilder sb) {
//            while (between('0', '9')) {
//                sb.append(ch);                nextChar();
//            }
//        }
//
//        private void skipWhitespace() {
//            while (test(' ') || test('\r') || test('\n') || test('\t')) {}
//        }
//    }
//}
