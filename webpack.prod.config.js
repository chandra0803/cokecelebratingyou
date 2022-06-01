var webpack = require( 'webpack' );
var path = require( 'path' );
// var argv = require( 'yargs' ).argv;
// var ExtractTextPlugin = require( 'extract-text-webpack-plugin' );

// var context = argv.context ? argv.context : 'groot';

var ROOT_PATH = path.resolve( __dirname );
var APP_PATH = path.resolve( ROOT_PATH, 'src/fe/components' );
// var BUILD_PATH = path.resolve( ROOT_PATH, 'tomcat/webapps/' + context + '/assets/js' );
// var INDEX_FILE_TEMPLATE_PATH = path.resolve( ROOT_PATH, 'src/index.html' );
var BUILD_PATH = path.resolve( ROOT_PATH, 'wp-out' );

var jsModuleTestRegex = /\.jsx?$/;

// var extractScss = new ExtractTextPlugin( '[name].css' );

// Config object that will get exported
var webpackConfig = {
    context: ROOT_PATH,
    entry: {
        login: [
            path.resolve( APP_PATH, 'pages/login/index.js' )
        ],
        recAdvisor: [
            path.resolve( APP_PATH, 'pages/recognitionAdvisor/index.js' )
        ],
        serAnniversary: [
            path.resolve( APP_PATH, 'pages/newServiceAnniversary/index.js' )
        ],
        manageEvents: [
            path.resolve( APP_PATH, 'pages/admin/manageEvents/index.js' )   
        ]
    },
    output: {
        path: BUILD_PATH,
        filename: '[name].js',
        chunkFilename: '[name].js'
    },
    module: {
        rules: [
            {
                test: jsModuleTestRegex,
                exclude: /(node_modules)/,
                use: [
                    {
                        loader: 'babel-loader',
                        options: {
                            compact: true
                        }
                    }
                ]
            },
            {
                test: /\.s?css$/i,
                use: [
                    'style-loader',
                    {
                        loader: 'css-loader',
                        options: {
                            importLoaders: true
                        }
                    },
                    {
                        loader: 'postcss-loader',
                        options: {
                            sourceMap: 'inline'
                        }
                    },
                    {
                        loader: 'sass-loader'
                    }
                ]
            }
        ]
    },

    devtool: 'source-map',
    plugins: [

         // Framework CSS
        // extractScss,

        // Do not emit webpacked files if any errors are found
        new webpack.NoEmitOnErrorsPlugin(),

        // Add a timestamp to the console whenever a compile happens.
        // Putting this in for --watch mode.
        function () {
            this.plugin( 'watch-run', function ( watching, callback ) {
                console.log( 'Begin compile at ' + new Date() );
                callback();
            } );
        },
        new webpack.optimize.CommonsChunkPlugin( {
            name: 'manifest',
            filename: 'manifest.js',
            minChunks: 2
        } ),
        new webpack.optimize.UglifyJsPlugin( {
            compress: {
                warnings: true
            }
        } )
    ],

};

module.exports = webpackConfig;
