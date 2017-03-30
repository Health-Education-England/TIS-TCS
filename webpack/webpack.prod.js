const commonConfig = require('./webpack.common.js');
const webpackMerge = require('webpack-merge');
const ArchivePlugin = require('webpack-archive-plugin');
const WebpackCleanPlugin = require('webpack-clean-plugin');
const ExtractTextPlugin = require("extract-text-webpack-plugin");
const Visualizer = require('webpack-visualizer-plugin');
const ENV = 'prod';

module.exports = webpackMerge(commonConfig({env: ENV}), {
	devtool: 'source-map',
	output: {
		path: './ui-build/tcs',
		filename: '[hash].[name].bundle.js',
		chunkFilename: '[hash].[id].chunk.js'
	},
	plugins: [
		new ExtractTextPlugin('[hash].styles.css'),
		new Visualizer({
			// Webpack statistics in target folder
			filename: '../stats.html'
		}),
		new ArchivePlugin(),
		new WebpackCleanPlugin({
			on: "emit",
			path: ['./ui-build']
		})
	]
});
