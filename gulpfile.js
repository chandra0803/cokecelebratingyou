'use strict';
var fs            = require( 'fs-extra' );
var gulp          = require( 'gulp' );
var concat        = require( 'gulp-concat' );
var expect        = require( 'gulp-expect-file' );
var sassGraph     = require( 'sass-graph' );
var gutil         = require( 'gulp-util' );
var sass          = require( 'gulp-sass' );
var sassVariables = require( 'gulp-sass-variables' );
var compass       = require( 'compass-importer' );
var path          = require( 'path' );
var duration      = require( 'gulp-duration' );
var argv          = require( 'yargs' ).argv;
var flatten       = require( 'gulp-flatten' );
var sourcemaps    = require( 'gulp-sourcemaps' );
var autoprefixer  = require( 'gulp-autoprefixer' );
var uglify        = require( 'gulp-uglify' );
var notifier      = require( 'node-notifier' );
var stripDebug    = require( 'gulp-strip-debug' );

var mode = argv.env ? argv.env : 'dev';
var context = argv.context ? argv.context : 'g5gamma';
var skins = argv.skin ? argv.skin.split( ',' ) : [ 'default' ];

var envConfig = {
    dev: {
        sourceComments: true,
        outputStyle: 'expanded'
    },
    docker: {
        distRoot: process.env.G_TOMCAT_WEBAPPS_DIR + '/' + context,
        sourceComments: false,
        outputStyle: 'compressed'
    },
    build: {
        distRoot: 'src/fe/build',
        sourceComments: false,
        outputStyle: 'compressed'
    }
};


var config = envConfig[ mode ];
var cwd = process.cwd() + '/';
var scssPathToSkinfileMapping = {};
var jsPathToModuleMapping = {};

function scssOptions( includePaths ) {
    return {
        importer: compass,
        includePaths: includePaths,
        sourceComments: config.sourceComments,
        outputStyle: config.outputStyle
    };
}

/**************************************
  gulp.src/dest paths
 **************************************/
var paths = {
    core: {
        scripts: {
            watchGlob: [
                'src/fe/core/**/*.js',
                'src/fe/custom/**/*.js',
                'wp-out/**/*.js'
            ],
            src: [
                'src/fe/core/**/*.js',
                'wp-out/**/*.js'
            ],
            dest: config.distRoot + '/assets'
        },
        rsrc: {
            watchGlob: [
                'src/fe/**/*.ttf',
                'src/fe/**/*.woff',
                'src/fe/**/*.woff2',
                'src/fe/**/*.eot',
                'src/fe/**/*.svg'
            ],
            src: 'src/fe/core/base/rsrc/**',
            dest: config.distRoot + '/assets/rsrc'
        },
        img: {
            watchGlob: 'src/fe/core/base/img/*',
            src: 'src/fe/core/base/img/**/*',
            dest: config.distRoot + '/assets/img'
        },
        tpl: {
            watchGlob: [
            	'src/fe/core/base/tpl/cert1.html',
            	'src/fe/core/base/tpl/embed.html',
            	'src/fe/core/base/tpl/nominationsApprovalTablePDFExtract.html',
            	'src/fe/core/base/tpl/nominationsWinnersDetailPagePDF.html'
            ],
            src: [
            	'src/fe/core/base/tpl/cert1.html',
            	'src/fe/core/base/tpl/embed.html',
            	'src/fe/core/base/tpl/nominationsApprovalTablePDFExtract.html',
            	'src/fe/core/base/tpl/nominationsWinnersDetailPagePDF.html'
            ],
            dest: config.distRoot + '/assets/tpl'
        }
    },
    skins: {
        scss: {
            watchGlob: 'src/fe/**/*.scss',
            src: function( skin ) {
                return 'src/fe/skins/' + skin + '/scss/*.scss';
            },
            scssDir: function( skin ) {
                return 'src/fe/skins/' + skin + '/scss/';
            },
            includePaths: function( skin ) {
                return [
                    'src/fe/core/base/scss/',
                    'src/fe/core/apps/',
                    'src/fe/skins/' + skin + '/scss/',
                    'wp-out/'
                ];
            },
            dest: function( skin ) {
                return config.distRoot ? config.distRoot + '/assets/skins/' + skin + '/css' : 'src/fe/skins/' + skin + '/css';
            }
        },
        img: {
            watchGlob: 'src/fe/skins/**/img/*',
            src: function( skin ) {
                return 'src/fe/skins/' + skin + '/img/**/*';
            },
            dest: function( skin ) {
                return config.distRoot + '/assets/skins/' + skin + '/img';
            }
        },
        rsrc: {
            watchGlob: 'src/fe/skins/**/rsrc/*',
            src: function( skin ) {
                return 'src/fe/skins/' + skin + '/rsrc/*';
            },
            dest: function( skin ) {
                return config.distRoot + '/assets/skins/' + skin + '/rsrc';
            }
        }
    },
    java: {
        jsp: {
            watchGlob: 'src/main/webapp/**/*',
            src: 'src/main/webapp/**/*',
            dest: config.distRoot
        }
    }
};

gutil.log( gutil.colors.cyan( '***************************************' ) );
gutil.log( gutil.colors.cyan( 'gulp config' ) );
gutil.log( gutil.colors.cyan( '---------------------------------------' ) );
gutil.log( gutil.colors.cyan( '  Mode:                     ' + mode ) );
gutil.log( gutil.colors.cyan( '  Dist Root:                ' + ( config.distRoot ? config.distRoot : 'n/a' ) ) );
gutil.log( gutil.colors.cyan( '  SCSS source comments:     ' + config.sourceComments ) );
gutil.log( gutil.colors.cyan( '  SCSS output style:        ' + config.outputStyle ) );
gutil.log( gutil.colors.cyan( '  Skin(s):                  ' ) );

skins.forEach( function( skin ) {

    gutil.log( gutil.colors.cyan( '    ' + skin ) );
    gutil.log( gutil.colors.cyan( '      Directory:        ' + paths.skins.scss.scssDir( skin ) ) );
    gutil.log( gutil.colors.cyan( '      Include paths:    ' + paths.skins.scss.includePaths( skin ) ) );
    gutil.log( gutil.colors.cyan( '      Output directory: ' + paths.skins.scss.dest( skin ) ) );

} );
gutil.log( gutil.colors.cyan( '***************************************' ) );


/**************************************
  js bundling config
 **************************************/
var modules = require( './js-modules.config.json' );

/**************************************
  JS dependency map
 **************************************/
Object.keys( modules ).forEach( function( module ) {
    modules[ module ].files.forEach( function( filepath ) {
        jsPathToModuleMapping[ path.resolve( filepath ) ] = module;
    } );
}, this );

function compileSingleJsModule( module ) {
    var compileTimer = duration( 'Compiling ' + module + ' module to ' + modules[ module ].dest.directory );

    if( mode === 'dev' || modules[ module ].uglify === false ) {

        return gulp.src( modules[ module ].files )
            .pipe( expect( modules[ module ].files ) )
            .pipe( concat( modules[ module ].dest.filename ) )
            .pipe( compileTimer )
            .pipe( gulp.dest( config.distRoot + '/assets' + modules[ module ].dest.directory ) );

    } else {

        return gulp.src( modules[ module ].files )
            .pipe( expect( modules[ module ].files ) )
            .pipe( sourcemaps.init() )
            .pipe( concat( modules[ module ].dest.filename ) )
            .pipe( stripDebug() )
            .pipe( uglify() )
            .pipe( sourcemaps.write() )
            .pipe( compileTimer )
            .pipe( gulp.dest( config.distRoot + '/assets' + modules[ module ].dest.directory ) );

    }
}


/**************************************
  SCSS dependency map creation
 **************************************/
skins.forEach( function( skin ) {
    fs.readdir( cwd + paths.skins.scss.scssDir( skin ), function( err, items ) {
        items.forEach( function( item ) {
            var parseResults = sassGraph.parseFile( cwd + paths.skins.scss.scssDir( skin ) + item, { loadPaths: paths.skins.scss.includePaths( skins ) } );
            if( parseResults ) {
                Object.keys( parseResults.index ).forEach( function( child ) {
                    scssPathToSkinfileMapping[ child ] = [
                        child.replace( cwd, '' ),
                        paths.skins.scss.scssDir( skin ) + item
                    ];
                } );
            }else{
                if( item !== 'apps' && item !== 'base' ) {
                    console.log( 'something went wrong', item, parseResults );
                }
            }
        } );
    } );
} );

function compileSingleScssFile( file ) {

    var compileTimer = duration( 'Compiling ' + file + ' scss file' );
    var src = scssPathToSkinfileMapping[ file ];
    var pathChunks = file.split( '/' );
    var skin = pathChunks.indexOf( 'skins' ) > 0 ? pathChunks[ pathChunks.indexOf( 'skins' ) + 1 ] : skins[ 0 ];
    console.log( src );
    return gulp.src( src )
        .pipe( expect( src ) )
        .pipe( sassVariables( {
            $env: mode
        } ) )
        .pipe( sourcemaps.init() )
        .pipe( sass( scssOptions( paths.skins.scss.includePaths( skin ) ) ) )
        .on( 'error', sass.logError )
        .on( 'error', function() {
            notifier.notify( {
                'title': 'SCSS',
                'message': 'Compile error. See log for details.',
                'sound': 'Blow',
                'wait': true,
                'activate': 'com.apple.Terminal'
            } );
        } )
        .pipe( autoprefixer() )
        .pipe( sourcemaps.write() )
        .pipe( compileTimer )
        .pipe( gulp.dest( paths.skins.scss.dest( skin ) ) );

}

/**************************************
  CHILD TASKS -- listed alphabetically
 **************************************/
gulp.task( 'clean', function() {

    fs.emptyDirSync( config.distRoot, function( error ) {
        if ( error ) {
            console.error( error );
        } else {
            console.log( config.distRoot + ' directory cleaned for build' );
        }
    } );

} );

gulp.task( 'core:img', function() {

    return gulp.src( paths.core.img.src )
        .pipe( expect( paths.core.img.src ) )
        .pipe( gulp.dest( paths.core.img.dest ) );

} );

gulp.task( 'core:tpl', function() {

    return gulp.src( paths.core.tpl.src )
        .pipe( expect( paths.core.tpl.src ) )
        .pipe( gulp.dest( paths.core.tpl.dest ) );

} );

gulp.task( 'core:rsrc', function() {

    return gulp.src( paths.core.rsrc.src )
        .pipe( expect( paths.core.rsrc.src ) )
        .pipe( gulp.dest( paths.core.rsrc.dest ) );

} );

gulp.task( 'core:scripts', function() {

    Object.keys( modules ).forEach( function( module ) {
        compileSingleJsModule( module );
    }, this );

    if( mode === 'build' ) {
        Object.keys( modules ).forEach( function( module ) {
            return gulp.src( modules[ module ].files )
                .pipe( expect( modules[ module ].files ) )
                .pipe( flatten() )
                .pipe( gulp.dest( paths.core.scripts.dest + modules[ module ].dest.directory ) );
        } );
    }
} );

gulp.task( 'skins:img', function() {

    skins.forEach( function( skin ) {

        return gulp.src( paths.skins.img.src( skin ) )
            .pipe( expect( paths.skins.img.src( skin ) ) )
            .pipe( gulp.dest( paths.skins.img.dest( skin ) ) );

    } );

} );

gulp.task( 'skins:rsrc', function() {

    skins.forEach( function( skin ) {

            return gulp.src( paths.skins.rsrc.src( skin ) )
                .pipe( expect( paths.skins.rsrc.src( skin ) ) )
                .pipe( gulp.dest( paths.skins.rsrc.dest( skin ) ) );

    } );

} );

gulp.task( 'skins:scss', function() {

    skins.forEach( function( skin ) {
        if( mode === 'build' ) {
            return gulp.src( paths.skins.scss.src( skin ) )
                .pipe( expect( paths.skins.scss.src( skin ) ) )
                .pipe( sassVariables( {
                    $env: mode
                } ) )
                .pipe( sass( scssOptions( paths.skins.scss.includePaths( skin ) ) ).on( 'error', sass.logError ) )
                .pipe( autoprefixer() )
                .pipe( gulp.dest( paths.skins.scss.dest( skin ) ) );
        } else {
            return gulp.src( paths.skins.scss.src( skin ) )
                .pipe( expect( paths.skins.scss.src( skin ) ) )
                .pipe( sassVariables( {
                    $env: mode
                } ) )
                .pipe( sourcemaps.init() )
                .pipe( sass( scssOptions( paths.skins.scss.includePaths( skin ) ) ).on( 'error', sass.logError ) )
                .pipe( autoprefixer() )
                .pipe( sourcemaps.write() )
                .pipe( gulp.dest( paths.skins.scss.dest( skin ) ) );
        }


    } );


} );

gulp.task( 'java:jsp', function() {

    return gulp.src( paths.java.jsp.src )
        .pipe( expect( paths.java.jsp.src ) )
        .pipe( gulp.dest( paths.java.jsp.dest ) );

} );

/**************************************
  PARENT TASKS -- listed alphabetically
 **************************************/
gulp.task( 'build', [ 'clean', 'core:img', 'core:tpl', 'core:rsrc', 'core:scripts', 'skins:img', 'skins:rsrc', 'skins:scss' ] );

gulp.task( 'default', [ 'skins:scss' ], function() {

    gulp.watch( paths.skins.scss.watchGlob )
        .on( 'change', function( evt ) {
            compileSingleScssFile( evt.path );
            console.log(
                '[watcher] File ' + evt.path.replace( /.*(?=\/scss)/, '' ) + ' was ' + evt.type + ', compiling...'
            );
        } );

} );

gulp.task( 'docker', [ 'core:img', 'core:tpl', 'core:rsrc', 'core:scripts', 'skins:img', 'skins:rsrc', 'skins:scss' ], function() {

    gulp.watch( paths.core.img.watchGlob, [ 'core:img' ] )
        .on( 'change', function( evt ) {
            console.log(
                '[watcher] File ' + evt.path.replace( /.*(?=\/woff)/, '' ) + ' was ' + evt.type + ', compiling...'
            );
        } );

    gulp.watch( paths.core.rsrc.watchGlob, [ 'core:rsrc' ] )
        .on( 'change', function( evt ) {
            console.log(
                '[watcher] File ' + evt.path.replace( /.*(?=\/woff)/, '' ) + ' was ' + evt.type + ', compiling...'
            );
        } );

    gulp.watch( paths.core.scripts.watchGlob )
        .on( 'change', function( evt ) {
            compileSingleJsModule( jsPathToModuleMapping[ evt.path ] );
            console.log(
                '[watcher] File ' + evt.path.replace( /.*(?=\/js)/, '' ) + ' was ' + evt.type + ', compiling...'
            );
        } );

    gulp.watch( paths.skins.img.watchGlob, [ 'skins:img' ] )
        .on( 'change', function( evt ) {
            console.log(
                '[watcher] File ' + evt.path.replace( /.*(?=\/scss)/, '' ) + ' was ' + evt.type + ', compiling...'
            );
        } );

    gulp.watch( paths.skins.rsrc.watchGlob, [ 'skins:rsrc' ] )
        .on( 'change', function( evt ) {
            console.log(
                '[watcher] File ' + evt.path.replace( /.*(?=\/scss)/, '' ) + ' was ' + evt.type + ', compiling...'
            );
        } );

    gulp.watch( paths.skins.scss.watchGlob )
        .on( 'change', function( evt ) {
            compileSingleScssFile( evt.path );
            console.log(
                '[watcher] File ' + evt.path.replace( /.*(?=\/scss)/, '' ) + ' was ' + evt.type + ', compiling...'
            );
        } );

    gulp.watch( paths.java.jsp.watchGlob, [ 'java:jsp' ] )
        .on( 'change', function( evt ) {
            console.log(
                '[watcher] File ' + evt.path.replace( /.*(?=\/woff)/, '' ) + ' was ' + evt.type + ', compiling...'
            );
        } );

} );
