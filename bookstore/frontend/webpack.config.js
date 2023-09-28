const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const CssMinimizerPlugin = require("css-minimizer-webpack-plugin");

const {WebpackManifestPlugin} = require('webpack-manifest-plugin');
const production = process.env.NODE_ENV === 'production';

const path = require('path');

const config = {
    entry: {
        "main.bundle": [
            "./src/libs/htmx/htmx.js",
            "./src/libs/htmx/ext/hx.head.support.js",
            "./src/layout/header.menu.ts"
        ],
        "layout.bundle": [
            "./src/layout/reset.css",
            "./src/layout/root.css",
            "./src/layout/header.css",
            "./src/layout/container.css",
            "./src/layout/footer.css",
        ],
        "demo.bundle": [
            "./src/components/demo.css",
        ]
    },
    output: {
        filename: "[name].[contenthash].js",
        path: path.resolve(__dirname, "../target/ROOT/dist"),
        clean: true
    },
    module: {
        rules: [
            {
                test: /\.(ts)$/i,
                loader: "ts-loader",
                exclude: ["/node_modules/"],
            },
            {
                test: /\.css$/,
                use: [MiniCssExtractPlugin.loader, "css-loader"],
            },
        ],
    },
    optimization: {
        minimize: true,
        minimizer: [
            new CssMinimizerPlugin({
                parallel: true
            }),
        ],
    },
    plugins: [
        new MiniCssExtractPlugin({
            filename: "[name].[contenthash].css",
            chunkFilename: "[id].[contenthash].css",
        }),
        new WebpackManifestPlugin({
            fileName: path.resolve(__dirname, "../target/ROOT/META-INF/webpack.manifest.json"),
            publicPath: ""
        })
    ],
    resolve: {
        extensions: [".ts", ".js", "..."],
    },
    watchOptions: {
        poll: 100,
        ignored: /node_modules/,
    },
};

module.exports = () => {
    if (production) {
        config.mode = "production";
    } else {
        config.mode = "development";
    }
    return config;
};