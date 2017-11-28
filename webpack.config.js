var path = require('path');
var ROOT = path.resolve(__dirname, 'src/main/resources');
var SRC  = path.resolve(ROOT, 'jsx');
var DEST = path.resolve(ROOT, 'static/js');

module.exports = {
    entry: SRC,
    resolve: {
        extensions: ['', '.js', '.jsx' ]
    },
    output: {
        path: DEST,
        filename: 'bundle.js',
        publicPath: '/js/',
        library: "MainApp"
    },
    devServer: {
            hot: true,
            inline: true,
            host: '0.0.0.0',
            port: 7777,
            contentBase: DEST
    },
    module: {
        loaders: [
                 {
                 test: /\.js$/,
                 loader: ['react-hot-loader', 'babel-loader?' + JSON.stringify({
                     cacheDirectory: true,
                     presets: ['es2015', 'react'],
                     plugins: ["transform-object-rest-spread"]
                 })],
                 exclude: /node_modules/
             },
             {
                test: /\.css$/,
                loader: 'style-loader!css-loader?modules&importLoaders=1&localIdentName=[name]__[local]___[hash:base64:5]'
             }
        ]
    },
    plugins: [
        new webpack.HotModuleReplacementPlugin(),
        new CopyWebpackPlugin([
             {
                 from: 'node_modules/monaco-editor/min/vs',
                 to: 'vs',
             }
         ])
    ]
};