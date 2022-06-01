/*exported GlobalNavRouter */
/*global
GlobalNavRouter:true
*/
GlobalNavRouter = Backbone.Router.extend( {
    routes: {
        '': 'go',
        ':level1': 'go',
        ':level1/:level2': 'go',
        ':level1/:level2/:level3': 'go',
        ':level1/:level2/:level3/:level4': 'go',
        ':level1/:level2/:level3/:level4/:level5': 'go'
    },

    initialize: function( opts ) {
        'use strict';

        console.log( '[INFO] GlobalNavRouter:', this, opts, this.options );

        G5._globalEvents.on( 'navigate', function( event, routes, opts ) {
            this.navigate( routes.join( '/' ), opts );
        }, this );

        Backbone.history.start();
    },

    go: function() {
        var routes = [];
        _.each( arguments, function( arg ) { routes.push( arg ); } );
        G5._globalEvents.trigger( 'route', {}, routes );
        G5.props.CURRENT_ROUTE = routes;
    }
} );