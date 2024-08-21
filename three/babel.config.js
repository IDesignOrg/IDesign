module.exports = {
  plugins:
    process.env.NODE_ENV === "production" ? ["transform-remove-console"] : [],
  presets: [
    [
      "@babel/preset-env",
      {
        targets: {
          chrome: "58",
          ie: "11",
        },
        useBuiltIns: "usage",
        corejs: {
          version: 3,
        },
      },
    ],
  ],
};
