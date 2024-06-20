const path = require("path"); // 노드에서 제공하는 path 모듈을 활용

module.exports = {
  entry: "./src/main.js",
  output: {
    filename: "bundle.js",
    path: path.resolve(__dirname, "dist"), // 의미: 현재 경로 하위에 dist폴더
  },
};
