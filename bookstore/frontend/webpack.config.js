const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const CssMinimizerPlugin = require('css-minimizer-webpack-plugin');

const {WebpackManifestPlugin} = require('webpack-manifest-plugin');
const production = process.env.NODE_ENV === 'production';

const path = require('path');

const config = {
    entry: {
        'main.bundle': [
            './src/javascript/libs/htmx/htmx.js',
            './src/javascript/libs/htmx/htmx.ext.js',
            './src/javascript/header.menu.ts',
        ],
        'layout.bundle': [
            './src/css/layout/base.scss',
        ]
    },
    output: {
        filename: '[name].[contenthash].js',
        path: path.resolve(__dirname, '../target/ROOT/dist'),
        clean: true
    },
    module: {
        rules: [
            {
                test: /\.(ts)$/i,
                loader: 'ts-loader',
                exclude: '/node_modules/',
            },
            {
                test: /\.s[ac]ss$/i,
                use: [
                    MiniCssExtractPlugin.loader,
                    'css-loader',
                    'sass-loader'
                ],
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
            filename: '[name].[contenthash].css',
            chunkFilename: '[id].[contenthash].css',
        }),
        new WebpackManifestPlugin({
            fileName: path.resolve(__dirname, '../target/ROOT/META-INF/webpack.manifest.json'),
            publicPath: ''
        })
    ],
    resolve: {
        extensions: ['.ts', '.js', '...'],
    },
    watchOptions: {
        poll: 100,
        ignored: /node_modules/,
    },
};

module.exports = () => {
    if (production) {
        config.mode = 'production';
    } else {
        config.mode = 'development';
    }
    return config;
};