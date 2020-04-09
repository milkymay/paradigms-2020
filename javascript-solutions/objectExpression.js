"use strict";

// :NOTE: why it's not const?
let constConstruct = function(value, str) {
    this.value = value;
    this.str = str;
    let shape = {
        toString: function() {
            return this.str;
        },
        evaluate: function() {
            return this.value;
        }
    };
    Object.setPrototypeOf(this, shape);
};

let varConstruct = function(name) {
    this.name = name;
    let shape = {
        toString: function() {
            return this.name;
        },
        evaluate: function(...args) {
            let ind = (this.name === 'x' ? 0 : this.name === 'y' ? 1 : 2);
            return args[ind];
        }
    };
    Object.setPrototypeOf(this, shape);
};

let abstractOperation = function(sign, operation, args) {
    this.sign = sign;
    this.operation = operation;
    this.args = args;
    let shape = {
        toString: function() {
            return this.args.map(x => x.toString()).join(' ') + ' ' + this.sign;
        },
        evaluate: function(...vars) {
            return this.operation(...this.args.map(x => x.evaluate(...vars)));
        }
    };
    Object.setPrototypeOf(this, shape);
};



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

