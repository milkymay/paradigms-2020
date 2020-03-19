"use strict";

const abstractOperation = operation => (...operands) => (...values) => {
    let result = [];
    for (let operand of operands) {
        result.push(operand(...values));
    }
    return operation(...result);
}

const cnst = value => x => value;

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

const operations = {
    '+'        : [add, 2],
    '-'        : [subtract, 2],
    '*'        : [multiply, 2],
    '/'        : [divide, 2],
    'negate'   : [negate, 1],
    'cube'     : [cube, 1],
    'cuberoot' : [cuberoot, 1]
};