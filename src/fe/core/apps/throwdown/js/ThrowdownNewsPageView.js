/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported ThrowdownNewsPageView*/
/*global
_,
Backbone,
PageView,
ThrowdownNewsCollectionView,
ThrowdownNewsPageDetailView,
ThrowdownNewsPageView:true
*/
ThrowdownNewsPageView = PageView.extend( {
    initialize: function( opts ) {
        var thisView = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'commissionerNews';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        //retrieve the news data
        //add the news collection view
        this.throwdownNewsCollectionView = new ThrowdownNewsCollectionView( {
            el: this.$el,
            loadOnly: true
        } );

        this.$el.append( '<span class="spin" />' )
                .find( '.spin' )
                .spin();

        // create a router to handle individual story loads
         if ( !Backbone.History.started ) {
            this.router = new Backbone.Router( {
                routes: {
                    '': 'storyIndex',
                    'index': 'storyIndex',
                    'story/:story': 'selectStory'
                }
            } );
            this.router.on( 'route:storyIndex', function() {
                thisView.routedStory = null;
                thisView.renderStoryList();
                Backbone.history.navigate( 'index', { silent: true } );
            } );
            this.router.on( 'route:selectStory', function( story ) {
                thisView.routedStory = story;
                thisView.renderSingleStory();
            } );
            Backbone.history.start();
        }else{
            G5._globalEvents.on( 'route', this.handleRoute, this );
            this.navigate( '', { trigger: false } ).navigate( G5.props.CURRENT_ROUTE.length ? G5.props.CURRENT_ROUTE : [ 'index' ] );

        }

        // listen to clicks on our back link in the pageView
        this.pageNav.on( 'pageBackLinkClicked', function( view, event ) {
            event.preventDefault();
            history.go( -1 );
        } );
    },
    // THIS SHOULD BE CHECKED - JUST MAKING IT WORK FOR g6 NOW
    handleRoute: function( event, route ) {
        console.log( 'ThrowdownNewsPageView : handleRoute -', event, route );
        // replaces the remainder of the router.on handlers
        switch( route[ 0 ] ) {
            case '':
                this.routedStory = null;
                this.renderStoryList();
                break;
            case 'index':
                this.routedStory = null;
                this.renderStoryList();
                this.navigate( 'index', { silent: true } );
                break;
            case 'story':
                if( route[ 1 ] ) {
                    this.routedStory = route[ 1 ] ;
                    this.renderSingleStory();
                } else {
                    this.navigate( 'index' );
                }

                break;

        }
    },
    navigate: function ( path, opts ) {
        'use strict';

        path = _.isArray( path )  ? path : [ path ];

        /**
         * G5 version
            trigger: true,
            replace: true
        }));
         */

        /**
         * G6 version
         */
        G5._globalEvents.trigger( 'navigate', {}, path, _.defaults( opts || {}, {
            trigger: true,
            replace: false
        } ) );

        return this;
    },
    renderSingleStory: function() {
        if( !this.throwdownNewsPageDetailView ) {
            this.throwdownNewsPageDetailView = new ThrowdownNewsPageDetailView( {
                singleStory: this.routedStory,
                singleStoryView: this.throwdownNewsCollectionView,
                $singleStoryCont: this.$el
            } );
        }
        else {
            this.throwdownNewsPageDetailView.render( this.throwdownNewsCollectionView.throwdownNewsStoryCollection.get( this.routedStory ) );
        }
    },
    renderStoryList: function() {
        this.throwdownNewsCollectionView.render();
    }
} );
