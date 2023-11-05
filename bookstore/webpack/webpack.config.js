'use strict'

const {WebpackManifestPlugin} = require('webpack-manifest-plugin');
const TerserPlugin = require("terser-webpack-plugin");

const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const AutoPrefixer = require('autoprefixer');

const CssMinimizerPlugin = require('css-minimizer-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");

const path = require('path');
const production = process.env.NODE_ENV === 'production';

const resolveDistPath = (folder = '') =>
    resolveBuildPath(`/dist/${folder}`);

const resolveBuildPath = (folder = '') =>
    path.resolve(__dirname, `../target/ROOT/${folder}`);

const config = {
    entry: {
        'htmx': [
            './src/javascript/libs/htmx.js',
        ],
        'bootstrap': [
            './src/scss/bootstrap.scss',
            './src/javascript/libs/bootstrap.js'
        ],
        'app.styles': './src/scss/base/styles.scss',
        'nav-header.script': [
            './src/javascript/header.progress.bar.ts',
            './src/javascript/header.mobile.ts'
        ],
        'theme-switcher.script': './src/javascript/theme-switcher.ts',
        'browser.script': './src/javascript/browser/script.js',
    },

    module: {
        rules: [
            {
                test: /\.(ts)$/i,
                loader: 'ts-loader',
                exclude: /node_modules/,
            },
            {
                test: /\.s[ac]ss$/i,
                exclude: /node_modules/,
                use: [
                    // Extracts CSS for each JS file that includes CSS
                    { loader: MiniCssExtractPlugin.loader },
                    { // Interprets and resolve @import/url()
                        loader: 'css-loader'
                    },
                    { // Loads a SASS/SCSS file and compiles it to CSS
                        loader: 'sass-loader'
                    },
                    { // Loader for webpack to process CSS with PostCSS
                        loader: 'postcss-loader',
                        options: {
                            postcssOptions: {
                                plugins: [AutoPrefixer]
                            }
                        }
                    },
                ],
            },
        ],
    },

    optimization: production ? {
        minimize: true,
        minimizer: [
            new TerserPlugin({
                parallel: true,
                terserOptions: {
                    compress: {
                        drop_console: true,
                    },
                    mangle: true,
                }
            }),
            new CssMinimizerPlugin({
                parallel: true
            })
        ],
    } : {},

    output: {
        filename: '[name].[contenthash].js',
        path: resolveDistPath(),
        clean: true
    },

    plugins: [
        new MiniCssExtractPlugin({
            filename: '[name].[contenthash].css',
            chunkFilename: '[id].[contenthash].css',
        }),
        new WebpackManifestPlugin({
            fileName: resolveBuildPath('/META-INF/webpack.manifest.json'),
            publicPath: ''
        }),
        new CopyPlugin({
            patterns: [
                {
                    from: './src/assets',
                    to: resolveDistPath('/assets/[name].[contenthash][ext]')
                },
            ],
        }),
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