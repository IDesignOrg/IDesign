import { describe } from "node:test";
import { fs } from "fs";
test("1", () => {
  expect(1).toBe(1);
});

describe("nav bar", () => {
  const html = fs.readFileSync(
    path.resolve(__dirname, "./dashboard/dashboard.html"),
    "utf8"
  );
  console.log(html);
});
