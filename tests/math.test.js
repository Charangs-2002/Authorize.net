const { sum } = require("../src/math");

test("adds two numbers", () => {
  expect(sum(2, 3)).toBe(5);
});
