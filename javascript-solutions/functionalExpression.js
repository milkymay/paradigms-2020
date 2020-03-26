"use strict";

// :NOTE: for hard modification `parser` required

const abstractOperation = operation => (...operands) => (...values) => {
    let result = [];
    for (let operand of operands) {
        result.push(operand(...values));
    }
    return operation(...result);
}

const cnst = value => () => value;

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
const pi = cnst(Math.PI);
const e = cnst(Math.E);

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
    'avg5'     : [avg5, 5],
    'med3'     : [med3, 3]
};