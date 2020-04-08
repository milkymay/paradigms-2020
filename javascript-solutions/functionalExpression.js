"use strict";

const abstractOperation = operation => (...operands) => (...values) => {
    let result = [];
    for (let operand of operands) {
        result.push(operand(...values));
    }
    return operation(...result);
}

const cnst = value => x => {
    switch (value) {
        case 'pi':
            return Math.PI;
        case 'e':
            return Math.E;
        default:
            return parseInt(value, 10);
    }
}

const variable = name => {
    let ind = (name === "x") ? 0 : (name === "y") ? 1 : 2;
    return (...values) => values[ind];
};

const add = abstractOperation((a, b) => a + b);
const subtract = abstractOperation((a, b) => a - b);
const multiply = abstractOperation((a, b) => a * b);
const divide = abstractOperation((a, b) => a / b);
const negate = abstractOperation(a => -a);
const cube = abstractOperation(a => a * a * a);
const cuberoot = abstractOperation(a => Math.cbrt(a));
const sin = abstractOperation(a => Math.sin(a));
const cos = abstractOperation(a => Math.cos(a));
const pi = cnst("pi");
const e = cnst("e");

const avg5 = abstractOperation((...operands) => {
    let sum = operands.reduce((sum, currentSum) => sum + currentSum);
    return sum / operands.length;
});

const med3 = abstractOperation((...operands) => {
    operands.sort((a, b) => a - b);
    return operands[1];
});

const operations = {
    '+'        : [add, 2],
    '-'        : [subtract, 2],
    '*'        : [multiply, 2],
    '/'        : [divide, 2],
    'negate'   : [negate, 1],
    'cube'     : [cube, 1],
    'cuberoot' : [cuberoot, 1],
    'sin'      : [sin, 1],
    'cos'      : [cos, 1],
    'avg5'     : [avg5, 5],
    'med3'     : [med3, 3]
};

const variables = {
    'x' : variable("x"),
    'y' : variable("y"),
    'z' : variable("z")
};

let parse = function(str) {
    let tokens = str.trim().split(/[' ']+/);
    let res = tokens.reduce(function(stack, cur) {
        if (cur in operations) {
            let operation = operations[cur];
            let args = stack.slice(stack.length - operation[1]);
            stack.length -= operation[1];
            stack.push(operation[0](...args));
        } else if (cur in variables) {
            stack.push(variables[cur]);
        } else {
            stack.push(cnst(cur));
        }
        return stack;
    }, []);
    return res.pop();
};

