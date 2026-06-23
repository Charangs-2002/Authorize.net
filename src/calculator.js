const readline = require("readline");
const math = require("./math");

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

rl.question("Enter first number: ", (a) => {
    rl.question("Enter operation (+, -, *, /): ", (op) => {
        rl.question("Enter second number: ", (b) => {

            a = Number(a);
            b = Number(b);

            let result;

            switch (op) {
                case "+":
                    result = math.add(a, b);
                    break;
                case "-":
                    result = math.subtract(a, b);
                    break;
                case "*":
                    result = math.multiply(a, b);
                    break;
                case "/":
                    result = math.divide(a, b);
                    break;
                default:
                    result = "Invalid operation";
            }

            console.log("Result =", result);
            rl.close();
        });
    });
});