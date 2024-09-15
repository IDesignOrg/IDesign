const path = require("path");
const glob = require("glob");
const webpack = require("webpack");
const CopyWebpackPlugin = require("copy-webpack-plugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const TerserPlugin = require("terser-webpack-plugin");

const webpackMode = process.env.NODE_ENV || "development";

module.exports = {
  mode: webpackMode,
  // entry: {
  //   dashboard: "./dimension/src/dashboard.js",
  //   three: "./dimension/src/three.js",
  // },
  entry: glob
    .sync(path.resolve(__dirname, "src/*.js"))
    .reduce((entries, filePath) => {
      const entryName = path.basename(filePath, path.extname(filePath)); // 파일명만 추출
      entries[entryName] = filePath;
      return entries;
    }, {}),
  // entry: glob.sync("./src/*.js").reduce((entries, filePath) => {
  //   // console.log("entries", entries);
  //   const entryName = path.basename(filePath, path.extname(filePath)); // 파일명만 추출
  //   entries[entryName] = filePath;
  //   return entries;
  // }, {}),
  output: {
    path: path.resolve("./dist/src"),
    filename: "[name].min.js",
    publicPath: "/",
  },
  devServer: {
    port: 3000,
    liveReload: true,
    static: {
      directory: path.join(__dirname),
    },
    historyApiFallback: true, // For single-page applications
  },
  optimization: {
    minimizer:
      webpackMode === "production"
        ? [
            new TerserPlugin({
              terserOptions: {
                compress: {
                  drop_console: true,
                },
              },
            }),
          ]
        : [],
    splitChunks: {
      chunks: "all",
    },
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        loader: "babel-loader",
        exclude: /node_modules/,
      },
      {
        test: /\.js$/,
        enforce: "pre",
        use: ["source-map-loader"],
      },
    ],
  },
  plugins: [
    // new HtmlWebpackPlugin({
    //   filename: "three.html",
    //   template: "./dimension/page/three.html",
    //   chunks: ["three"],
    //   minify:
    //     process.env.NODE_ENV === "production"
    //       ? {
    //           collapseWhitespace: true,
    //           removeComments: true,
    //         }
    //       : false,
    // }),
    // new HtmlWebpackPlugin({
    //   filename: "dashboard.html",
    //   template: "./dimension/page/dashboard.html",
    //   chunks: ["dashboard"],
    //   minify:
    //     process.env.NODE_ENV === "production"
    //       ? {
    //           collapseWhitespace: true,
    //           removeComments: true,
    //         }
    //       : false,
    // }),
    ...glob.sync("./pages/*.html").map((file) => {
      const fileName = path.basename(file); // 파일명 추출 (확장자 포함)
      const chunkName = path.basename(file, path.extname(file)); // 확장자 제외한 파일명으로 chunk 설정

      return new HtmlWebpackPlugin({
        filename: fileName, // 출력할 HTML 파일 이름
        template: file, // 원본 HTML 파일 경로
        chunks: [chunkName], // 해당 HTML에 포함될 JS 번들 (entry에서 같은 이름을 사용해야 함)
        minify:
          process.env.NODE_ENV === "production"
            ? {
                collapseWhitespace: true,
                removeComments: true,
              }
            : false,
      });
    }),
    new CleanWebpackPlugin(),
    // CopyWebpackPlugin: 그대로 복사할 파일들을 설정하는 플러그인
    // 아래 patterns에 설정한 파일/폴더는 빌드 시 dist 폴더에 자동으로 생성됩니다.
    // patterns에 설정한 경로에 해당 파일이 없으면 에러가 발생합니다.
    // 사용하는 파일이나 폴더 이름이 다르다면 변경해주세요.
    // 그대로 사용할 파일들이 없다면 CopyWebpackPlugin을 통째로 주석 처리 해주세요.
    // new CopyWebpackPlugin({
    //   patterns: [
    //     { from: "./src/main.css", to: "./main.css" },
    //     { from: "./src/images", to: "./images" },
    //   ],
    // }),
  ],
};
