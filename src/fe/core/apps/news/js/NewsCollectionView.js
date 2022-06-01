/*exported NewsCollectionView */
/*global
TemplateManager,
NewsStoryCollection,
NewsCollectionView:true
*/
NewsCollectionView = Backbone.View.extend( {
    initialize: function( opts ) {
        var that = this;

        this.newsStoryCollection = new NewsStoryCollection();
        this.newsStoryCollection.loadStories();
        this.newsStoryCollection.on( 'dataLoaded', function() {
            // check to see if the view is being told not to render
            if( !that.options.loadOnly ) {
                that.render();
            }
        } );

        this.on( 'renderPageFinished', function() {
            that.renderStories();
        } );
    },

    render: function() {
        var that = this,
            delay = ( function() { //create a delay between keystrokes when this is called
                var timer = 0;
                return function( callback, ms ) {
                    clearTimeout ( timer );
                    timer = setTimeout( callback, ms );
                };
            } )();

        if( this.newsStoryCollection._dataLoaded === false ) {
            console.log( '[INFO] NewsCollectionView: render. Checking if loaded...' );

            delay( function() { //delay this event
                console.log( '[INFO] NewsCollectionView: render. Is loaded. Rendering' );
                that.render();
            }, 300 );

            return false;
        }
        this.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'news/tpl/';
        this.tplName = 'newsCollectionView';
        this.itemTplName = 'newsPageItem';

        if( !this.newsStoryCollection.length ) {
            return false;
        }

        // we can safely assume that if the #collectionOfStories element doesn't exist, the page hasn't yet been rendered
        if( !this.$el.find( '#collectionOfStories' ).length ) {
            this.renderPage();
        }
        // if the #collectionOfStories element does exist, trigger the custom event
        else {
            this.trigger( 'renderPageFinished' );
        }

    },

    renderPage: function() {
        var that = this;

        TemplateManager.get( this.tplName, function( tpl ) {
            that.$el.empty().append( tpl( {
                sortedByDate: that.newsStoryCollection.sortedBy() == 'sortDate' ? true : false,
                sortedByName: that.newsStoryCollection.sortedBy() == 'storyName' ? true : false
            } ) );

            that.trigger( 'renderPageFinished' );
        }, this.tplUrl );
    },

    renderStories: function() {
        var that = this,
            $cont = this.$el.find( '#collectionOfStories' );

        //empty the carousel items
        $cont.empty();

        TemplateManager.get( this.itemTplName, function( tpl ) {
            that.newsStoryCollection.each( function( story ) {
                // story.set('storyImageUrl', story.get('storyImageUrlListPage'));
                $cont.append( tpl( story.toJSON() ) );
            } );
            that.trigger( 'renderStoriesFinished' );
        }, this.tplUrl );
    },

    events: {
        'change select#sortBy': 'sortStories'
    },

    sortStories: function() {
        var howToSort = this.$el.find( '#sortBy' ).val();
        var sortedCollection;
        if ( howToSort === 'date' ) {
            sortedCollection = this.newsStoryCollection.sortBySortDate();
        } else {
            sortedCollection = this.newsStoryCollection.sortByName();
        }
        this.render( sortedCollection.toJSON() );
    }

} );
