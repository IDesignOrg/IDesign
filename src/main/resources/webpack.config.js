const path = require("path");
const webpack = require("webpack");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const TerserPlugin = require("terser-webpack-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin");

const webpackMode = process.env.NODE_ENV || "development";

module.exports = {
  mode: webpackMode,
  entry: {
    dashboard: "./dimension/dashboard/src/dashboard.js",
    three: "./dimension/three/src/three.js",
  },
  output: {
    path: path.resolve("./dist"),
    filename: "[name].min.js",
  },
  // es5로 빌드 해야 할 경우 주석 제거
  // 단, 이거 설정하면 webpack-dev-server 3번대 버전에서 live reloading 동작 안함
  // target: ['web', 'es5'],
  devServer: {
    port: 3000,
    static: [
      {
        directory: path.join(__dirname),
        publicPath: "/",
        serveIndex: true,
      },
      // {
      //   directory: path.join(__dirname, "three"),
      //   publicPath: "/",
      // },
      // {
      //   directory: path.join(__dirname, "dashboard"),
      //   publicPath: "/",
      // },
    ],
    liveReload: true,
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
      // {
      //   test: /\.(png|jpe?g|gif|svg)$/i, // 이미지 파일 로더
      //   type: "public/resource", // Webpack 5에서는 기본적으로 asset/resource를 사용하여 이미지를 처리
      // },
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
      {
        test: /\.css$/,
        include: path.resolve(__dirname, "dimension/public"), // 특정 폴더만 포함
        use: ["style-loader", "css-loader"],
      },
    ],
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: "./dimension/three/three.html", // 상대 경로로 수정
      filename: "three.html",
      chunks: ["three", "reset"],
      minify:
        process.env.NODE_ENV === "production"
          ? {
              collapseWhitespace: true,
              removeComments: true,
            }
          : false,
    }),
    new HtmlWebpackPlugin({
      template: "./dimension/dashboard/dashboard.html", // 상대 경로로 수정
      filename: "dashboard.html",
      chunks: ["dashboard", "public"],
      minify:
        process.env.NODE_ENV === "production"
          ? {
              collapseWhitespace: true,
              removeComments: true,
            }
          : false,
    }),

    new CleanWebpackPlugin(),
    // CopyWebpackPlugin: 그대로 복사할 파일들을 설정하는 플러그인
    // 아래 patterns에 설정한 파일/폴더는 빌드 시 dist 폴더에 자동으로 생성됩니다.
    // patterns에 설정한 경로에 해당 파일이 없으면 에러가 발생합니다.
    // 사용하는 파일이나 폴더 이름이 다르다면 변경해주세요.
    // 그대로 사용할 파일들이 없다면 CopyWebpackPlugin을 통째로 주석 처리 해주세요.
    new CopyWebpackPlugin({
      patterns: [
        { from: "./dimension/public/reset.css", to: "public/reset.css" }, // 공용 CSS 파일은 별도로 복사
        { from: "./dimension/public/three.css", to: "public/three.css" },
        {
          from: "./dimension/public/",
          to: "public",
        },
      ],
    }),
  ],
};
