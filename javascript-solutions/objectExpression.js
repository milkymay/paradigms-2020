"use strict";

const constConstruct = function(value, str) {
    this.value = value;
    this.str = str;
    let shape = {
        toString: function() {
            return this.str;
        },
        prefix: function() {
            return this.str;
        },
        evaluate: function() {
            return this.value;
        }
    };
    Object.setPrototypeOf(this, shape);
};

const varConstruct = function(name) {
    this.name = name;
    let shape = {
        toString: function() {
            return this.name;
        },
        prefix: function() {
            return this.name;
        },
        evaluate: function(...args) {
            return args[VARIABLES[this.name]];
        }
    };
    Object.setPrototypeOf(this, shape);
};

const abstractOperation = function(sign, operation, args) {
    this.sign = sign;
    this.operation = operation;
    this.args = args;
    let shape = {
        toString: function() {
            return this.args.map(x => x.toString()).join(' ') + ' ' + this.sign;
        },
        prefix: function() {
            return "(" + this.sign + " " + this.args.map(function (arg) {
                return arg.prefix();
            }).join(" ") + ")";
        },
        evaluate: function(...vars) {
            return this.operation(...this.args.map(x => x.evaluate(...vars)));
        }
    };
    Object.setPrototypeOf(this, shape);
};

const VARIABLES = {
    "x": 0,
    "y": 1,
    "z" : 2
}

let Const = function(val) {
    constConstruct.call(this, val, val.toString());
}
let Variable = function(name) {
    varConstruct.call(this, name);
}
let Add = function(...args) {
    abstractOperation.call(this, "+", (a, b) => a + b, args);
}
let Subtract = function(...args) {
    abstractOperation.call(this, "-", (a, b) => a - b, args);
}
let Multiply = function(...args) {
    abstractOperation.call(this, "*", (a, b) => a * b, args);
}
let Divide = function(...args) {
    abstractOperation.call(this, "/", (a, b) => a / b, args);
}
let Negate = function(...args) {
    abstractOperation.call(this, "negate", a => -a, args);
}

let Sinh = function(...args) {
    abstractOperation.call(this, "sinh", a => Math.sinh(a), args)
}
let Cosh = function(...args) {
    abstractOperation.call(this, "cosh", a => Math.cosh(a), args)
}


const OPERATIONS = {
    "+": [Add, 2],
    "-": [Subtract, 2],
    "/": [Divide, 2],
    "*": [Multiply, 2],
    "negate": [Negate, 1],
    "sinh" : [Sinh, 1],
    "cosh" : [Cosh, 1]
};
const isDigit = function (symbol) {
    return "0" <= symbol && symbol <= "9";
};

const isBracket = function (symbol) {
    return symbol === ")" || symbol === "(";
};

const Parser = function (expression, parse) {
    this.expression = expression;
    this.parse = parse;
    this.curInd = 0;
    this.curToken = "";
};

const isNumber = function (number) {
    let index = 0;
    if (number[index] === "-") { index++; }
    if (index === number.length) {
        return false;
    }
    while (isDigit(number[index]) && index < number.length) { index++; }
    return index === number.length;
};

Parser.prototype.skipWhitespace = function () {
    while (this.curInd < this.expression.length && this.expression[this.curInd] === " ") {this.curInd++; }
};

Parser.prototype.nextToken = function() {
    let res = "";
    if (isBracket(this.expression[this.curInd])) { //????
        res = this.expression[this.curInd];
        this.curInd++;
    } else {
        let end = this.curInd;
        while (!isBracket(this.expression[end]) &&
        this.expression[end] !== " " &&
        end < this.expression.length) {end++; }
        res = this.expression.slice(this.curInd, end);
        this.curInd = end;
    }
    this.skipWhitespace();
    this.curToken = res;
};

const parsePrefix = function (expression) {
    let parser = new Parser(expression, function() {
        if (this.curToken === "(") {
            this.nextToken();
            if (this.curToken in OPERATIONS) {
                let args = [];
                let operation = this.curToken;
                this.nextToken();
                while (this.curToken !== ")" && this.curInd < expression.length) {
                    args.push(this.parse());
                    this.nextToken();
                }
                if (this.curToken !== ")") {
                    throw new MissingCloseBracketException(this.curInd);
                }
                if (OPERATIONS[operation][1] > args.length) {
                    throw new MissingOperandException(operation);
                } else if (OPERATIONS[operation][1] < args.length) {
                    throw new ExtraOperandsException(this.curInd);
                }
                return new OPERATIONS[operation][0](...args);
            } else {
                throw new MissingOperationException(this.curInd);
            }
        } else if (this.curToken in VARIABLES) {
            return new Variable(this.curToken);
        } else if (isNumber(this.curToken)) {
            return new Const(parseInt(this.curToken));
        } else {
            throw new UnexpectedSymbolException(this.curInd);
        }
    });
    parser.skipWhitespace();
    parser.nextToken();
    let res = parser.parse();
    if (parser.curInd < expression.length) {
        throw new EndOfExpressionExpected(parser.curInd);
    }
    return res;
}

const Exception = function (name, text) {
    const result = function (ind) {
        this.message = text + ind;
        this.name = name;
    };
    result.prototype = new Error;
    return result;
};

const MissingCloseBracketException = Exception("MissingCloseBracketException", "Close bracket expected at ")
const MissingOperandException = Exception("MissingOperandException", "Not enough operands for operation at ");
const ExtraOperandsException = Exception("TooMuchOperandsException", "Too much operands for operation at ");
const MissingOperationException = Exception("MissingOperationException", "Missing operation at ");
const UnexpectedSymbolException = Exception("UnexpectedSymbolException", "Unexpected symbol at ");
const EndOfExpressionExpected = Exception("EndOfExpressionExpected", "End of expression expected at ");

