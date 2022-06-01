/*exported NewsModuleView */
/*global
TemplateManager,
LaunchModuleView,
NewsStoryCollection,
NewsModuleView:true
*/
NewsModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function() {
        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        //doing the product model in the view (do not use the 'model' prop, this has a module model)
        this.newsStoryCollection = new NewsStoryCollection();

        //when products model gets reset, then render this
        this.newsStoryCollection.on( 'reset', function() {
            // stop the loading state and spinner
            this.dataLoad( false );

            this.renderStories();
        }, this );

        this.on( 'templateLoaded', function() {
            G5._globalEvents.on( 'breakpointChanged', this.responsiveImages, this );
        }, this );

        this.on( 'storiesRendered', function() {
            this.responsiveImages();
            this.renderCarousel();
        }, this );

        this.on( 'layoutChange', function( type ) {
            switch( type ) {
                case 'renderDone':
                    this.dataLoad( true );
                    this.newsStoryCollection.loadStories();
                    break;

                case 'woke':
                    this.playSlick();
                    break;

                case 'slept':
                    this.pauseSlick();
                    break;
            }
        }, this );
    },

    renderStories: function() {
        var that = this,
            tplName = 'newsModuleItem',
            tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'news/tpl/',
            $container = this.$el.find( '.carousel' ),
            $pager = this.$el.find( '.newsPager ul' );

        //empty the carousel items
        $container.empty();

        //for each story item
        TemplateManager.get( tplName, function( tpl ) {
            var storyString = '',
                pagerString = '';

            that.newsStoryCollection.each( function( story, index ) {
                // stop at seven items
                // should probably turn the max number into a variable at some point
                if( index > 6 ) {
                    return false;
                }

                var storyHtml = tpl( story.toJSON() );
                var storyPagerImg = story.get( 'storyImageUrl_mobile' );
                var storyPagerItem = '<li><img src="' + storyPagerImg + '" /></li>';

                story.$el = $( storyHtml );

                storyString += storyHtml;
                pagerString += storyPagerItem;
            } );

            $container.append( storyString );
            $pager.append( pagerString );

            that.$stories = that.$el.find( '.item' );

            that.trigger( 'storiesRendered' );

        }, tplUrl );

        return this;
    },

    responsiveImages: function() {
        this.$stories.each( function( index, story ) {
            var $story = $( story ).find( '.item-link' ),
                imageUrl = $story.data( 'image' ),
                imageUrl_mobile = $story.data( 'imageMobile' ),
                imageUrl_max = $story.data( 'imageMax' );

            if( G5.breakpoint.value == 'mini' ) {
                $story.css( 'background-image', 'url(' + imageUrl_mobile + ')' );
            }
            else if( G5.breakpoint.value == 'desktopLarge' || G5.breakpoint.value == 'siteMax' ) {
                $story.css( 'background-image', 'url(' + imageUrl_max + ')' );
            }
            else {
                $story.css( 'background-image', 'url(' + imageUrl + ')' );
            }
        } );
    },

    renderCarousel: function() {
        if( this.newsStoryCollection.length > 1 ) {
            this.startSlick();
        }
        else {
            this.$el.addClass( 'singleStory' );
        }
    },

    startSlick: function() {
        this.$slickEl = this.$el.find( '#newsCarousel' ); //find all modules with a carousel

        this.$slickEl.slick( {
            autoplay: true,
            dots: true,
            infinite: true,
            speed: G5.props.ANIMATION_DURATION,
            autoplaySpeed: 10000,
            slidesToShow: 1,
            slidesToScroll: 1,
            arrows: true,
            prevArrow: '<i class="icon-arrow-1-left slick-arrow slick-prev"></i>',
            nextArrow: '<i class="icon-arrow-1-right slick-arrow slick-next"></i>',
            customPaging: function ( slider, i ) {
                return $( '.newsPager li:nth-child(' + ( i + 1 ) + ')' ).html();
            }

        } );
    },
    pauseSlick: function() {
        if( !this.$slickEl ) {
            return false;
        }
        this.$slickEl.slick( 'slickPause' );
    },
    playSlick: function() {
        if( !this.$slickEl ) {
            return false;
        }
        this.$slickEl.slick( 'slickGoTo', this.$slickEl.slick( 'slickCurrentSlide' ) );
        this.$slickEl.slick( 'slickPlay' );
    }
} );
