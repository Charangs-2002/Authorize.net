const { add, subtract, multiply, divide } = require("../src/calculator");

test("adds two numbers", () => {
  expect(add(2, 3)).toBe(5);
});

test("subtracts two numbers", () => {
  expect(subtract(5, 3)).toBe(2);
});

test("multiplies two numbers", () => {
  expect(multiply(4, 5)).toBe(20);
});

test("divides two numbers", () => {
  expect(divide(10, 2)).toBe(5);
});

test("divide by zero returns error message", () => {
  expect(divide(10, 0)).toBe("Cannot divide by zero");
});
