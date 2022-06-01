/*exported NewsPageView */
/*global
PageView,
NewsCollectionView,
NewsPageDetailView,
NewsPageView:true
*/
NewsPageView = PageView.extend( {

    //override super-class initialize function
    initialize: function() {
        //set the appname (getTpl() method uses this)
        this.appName = 'news';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        // because of the lack of tpl file for the page view, we clone and store the contents of the View for later
        // this.$view = this.$el.html().clone(true);

        //retrieve the news data
        //add the news collection view
        this.newsCollectionView = new NewsCollectionView( {
            el: this.$el,
            loadOnly: true
        } );

        this.$el
            .append( '<span class="spin" />' )
            .find( '.spin' ).spin();

        // ROUTER IS NOW GLOBALNAV and it triggers route change
        G5._globalEvents.on( 'route', this.handleRoute, this );

        // call to set initial route
        G5._globalEvents.trigger( 'route', {}, G5.props.CURRENT_ROUTE.length ? G5.props.CURRENT_ROUTE : [ 'index' ], { trigger: true } );

        // listen to clicks on our back link in the pageView
        this.pageNav.on( 'pageBackLinkClicked', function( view, event ) {
            event.preventDefault();
            history.go( -1 );
        } );

        G5._globalEvents.on( 'breakpointChanged', this.responsiveImages, this );
    },

    handleRoute: function( event, route ) {
        if( route.length === 0 || ( route && route[ 0 ] == 'index' ) ) {
            this.routedStory = null;
            this.renderStoryList();
        }
        if( route && route[ 0 ] == 'story' && route[ 1 ] ) {
            this.routedStory = route[ 1 ];
            this.renderSingleStory();
        }
    },

    renderSingleStory: function() {
        if( !this.newsPageDetailView ) {
            this.newsPageDetailView = new NewsPageDetailView( {
                singleStory: this.routedStory,
                singleStoryView: this.newsCollectionView,
                $singleStoryCont: this.$el
            } );
        }
        else {
            this.newsPageDetailView.render( this.newsCollectionView.newsStoryCollection.get( this.routedStory ) );
        }
    },

    renderStoryList: function() {
        this.newsCollectionView.render();
        this.newsCollectionView.on( 'renderStoriesFinished', this.responsiveImages, this );
    },

    responsiveImages: function() {
        this.$el.find( '.newsStory' ).each( function( index, story ) {
            var $story = $( story ).find( '.storyImage' ),
                imageUrl = $story.data( 'image' ),
                imageUrl_mobile = $story.data( 'imageMobile' ),
                imageUrl_max = $story.data( 'imageMax' );

            if( G5.breakpoint.value == 'mini' ) {
                $story.attr( 'src', imageUrl_mobile );
            }
            else if( G5.breakpoint.value == 'desktopLarge' || G5.breakpoint.value == 'siteMax' ) {
                $story.attr( 'src', imageUrl_max );
            }
            else {
                $story.attr( 'src', imageUrl );
            }
        } );
    }

} );
