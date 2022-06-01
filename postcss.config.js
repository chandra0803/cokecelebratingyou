module.exports = {
    plugins: [
        require( 'autoprefixer' )( { browsers: [ 'last 2 version', 'Explorer > 10', 'iOS > 8', 'Android > 3' ] } )
    ]
};
