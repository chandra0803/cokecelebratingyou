var webpack = require( 'webpack' );
var path = require( 'path' );

var context = 'g5gamma';

var ROOT_PATH = path.resolve( __dirname );
var APP_PATH = path.resolve( ROOT_PATH, 'src/fe/components' );
var BUILD_PATH = path.resolve( ROOT_PATH, 'wp-out' );

var jsModuleTestRegex = /\.jsx?$/;

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
                            compact: false
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

        // Add a timestamp to the console whenever a compile happens.
        // Putting this in for --watch mode.
        function () {
            this.plugin( 'watch-run', function ( watching, callback ) {
                console.log( 'Begin compile at ' + new Date() );
                callback();
            } );
        },

        // new BundleAnalyzerPlugin( {
        //     analyzerMode: 'server',
        //     analyzerHost: '127.0.0.1',
        //     analyzerPort: 8888,
        //     reportFilename: 'report.html',
        //     defaultSizes: 'parsed',
        //     openAnalyzer: true,
        //     generateStatsFile: false,
        //     statsOptions: null,
        //     logLevel: 'info'
        // } ),

        new webpack.optimize.CommonsChunkPlugin( {
            name: 'manifest',
            filename: 'manifest.js',
            minChunks: 2
        } )
    ],

};

module.exports = webpackConfig;
