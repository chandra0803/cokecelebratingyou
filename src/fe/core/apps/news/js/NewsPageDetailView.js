/*exported NewsPageDetailView */
/*global
TemplateManager,
PageView,
NewsStoryCollection,
NewsPageDetailView:true
*/
NewsPageDetailView = PageView.extend( {
    el: $( '#newsPageDetailView' ), // el attaches to existing element

    initialize: function( opts ) {
        var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'news';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        if( opts.singleStory ) {
            this.newsStoryCollection = opts.singleStoryView.newsStoryCollection;
            this.options.messageUniqueId = opts.singleStory;
            this.$el = opts.$singleStoryCont;

            if( this.newsStoryCollection.length ) {
                this.render( this.newsStoryCollection.get( this.options.messageUniqueId ) );
            }
        }
        // otherwise, go get the data
        else {
            this.newsStoryCollection = new NewsStoryCollection();
            this.newsStoryCollection.loadStories( {
                messageUniqueId: this.options.messageUniqueId
            } );
        }

        G5.util.showSpin( this.$el );

        this.newsStoryCollection.on( 'dataLoaded', function() {
            that.render( that.newsStoryCollection.get( that.options.messageUniqueId ) );
        } );

        G5._globalEvents.on( 'breakpointChanged', this.responsiveImages, this );
    },

    render: function( singleStory ) {
        var self = this,
            tplName = 'newsPageDetailItem',
            tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'news/tpl/',
            $cont = this.options.$singleStoryCont || this.$el;

        $cont.empty();

        // singleStory.set('storyImageUrl', singleStory.get('storyImageUrl4x4'));

        TemplateManager.get( tplName, function( tpl ) {
            G5.util.hideSpin( self.$el );
            $cont.append( tpl( singleStory.toJSON() ) );
            self.sizeTopper();
        }, tplUrl );

        G5._globalEvents.on( 'windowResized', this.sizeTopper, this );
    },

    sizeTopper: function() {
        // console.log(e);

        var $mod = this.$el.find( '.page-topper' ),
            data = $mod.data().size;

        // ratio sizing
        G5.util.sizeByRatio( $mod, data.ratio, [ 'min-height', 'line-height' ] );
    },

    responsiveImages: function() {
        var $story = this.$el.find( '.storyImage' ),
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
    }

  } );