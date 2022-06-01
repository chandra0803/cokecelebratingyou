/*exported BannerModuleModuleView */
/*global
LaunchModuleView,
TemplateManager,
BannerModuleCollection,
BannerModuleModuleView:true
*/
BannerModuleModuleView = LaunchModuleView.extend( {

    initialize: function() {
        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass ModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        //create the data model
        this.dataModel = new BannerModuleCollection();

        // on the completion of the module template load and render
        this.on( 'templateLoaded', function() {
            G5._globalEvents.on( 'breakpointChanged', this.responsiveImages, this );

            //retrieve the data
            this.dataLoad( true );
            this.dataModel.loadData();
        }, this );

        this.dataModel.on( 'loadDataFinished', function() {
           // console.log("[INFO] BannerModuleModuleView: loadDataFinished triggered");
           this.dataLoad( false );
           this.renderSlides( {} );
        }, this );

        this.on( 'finishedRendering', function() {
            this.responsiveImages();
            this.renderCarousel();
        } );

        this.on( 'layoutChange', function( type ) {
            switch ( type ) {
                case 'woke':
                    this.playSlick();
                    break;

                case 'slept':
                    this.pauseSlick();
                    break;
            }
        }, this );
    },

    renderSlides: function() {
        //console.log("[INFO] BannerModuleCollection: renderSlides called using these slides: ", this.dataModel.models);

        var that = this,
            tplName = 'bannerSlideTpl',
            tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'bannerModule/tpl/',
            $container = this.$el.find( '#BannerCarousel' ),
            $pager = this.$el.find( '.bannerPager ul' ),
            bannerHtml,
            storyPagerImg,
            storyPagerItem;

        //empty the carousel items
        $container.empty();

        TemplateManager.get( tplName, function( tpl ) {
            var bannerString = '',
                pagerString = '';

            that.dataModel.each( function( banner, index ) {
                // stop at seven items
                // should probably turn the max number into a variable at some point
                // if ( index > 6 ) {
                //     return false;
                // }

                bannerHtml = tpl( banner.toJSON() );
                storyPagerImg = banner.get( 'bannerImageUrl_mobile' );
                storyPagerItem = '<li><img src="' + storyPagerImg + '" /></li>';

                banner.$el = $( bannerHtml );

                bannerString += bannerHtml;
                pagerString += storyPagerItem;
            } );

            $container.append( bannerString );
            $pager.append( pagerString );

            that.$items = that.$el.find( '.item' );

            that.trigger( 'finishedRendering' );
        }, tplUrl );

    },

    responsiveImages: function() {
        this.$items.each( function( index, item ) {
            var $item = $( item ).find( '.item-link' ),
                imageUrl = $item.data( 'image' ),
                imageUrlMobile = $item.data( 'imageMobile' ),
                imageUrlMax = $item.data( 'imageMax' );

            if ( G5.breakpoint.value === 'mini' ) {
                $item.css( 'background-image', 'url(' + imageUrlMobile + ')' );
            }            else if ( G5.breakpoint.value === 'desktopLarge' || G5.breakpoint.value === 'siteMax' ) {
                $item.css( 'background-image', 'url(' + imageUrlMax + ')' );
            }            else {
                $item.css( 'background-image', 'url(' + imageUrl + ')' );
            }
        } );
    },

    renderCarousel: function() {
        var banners = this.dataModel.length;
        if ( ( banners > 1 ) && ( banners < 8 ) ) {
            this.startSlick();
        }
        else if ( banners > 7 ) {
            this.startSlickWithPager();
        }
        else {
            console.log( 'no carousel with ' + banners );
            this.$el.addClass( 'singleBanner' );
        }
    },

    startSlick: function() {
        this.$slickEl = this.$el.find( '#BannerCarousel' ); //find all modules with a carousel

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
                return $( '.bannerPager li:nth-child(' + ( i + 1 ) + ')' ).html();
            }
        } );
    },
    startSlickWithPager: function() {
        this.$slickEl = this.$el.find( '#BannerCarousel' ); //find all modules with a carousel
        var countElement = this.$el.find( '.slick-counter' );
        countElement.show();

        this.$slickEl.on( 'init reInit afterChange', function ( event, slick, currentSlide, nextSlide ) {
            var i = ( currentSlide ? currentSlide : 0 ) + 1;
            countElement.text( i + ' / ' + slick.slideCount );
        } );

        this.$slickEl.slick( {
            autoplay: true,
            dots: false,
            infinite: true,
            speed: G5.props.ANIMATION_DURATION,
            autoplaySpeed: 10000,
            slidesToShow: 1,
            slidesToScroll: 1,
            arrows: true,
            prevArrow: '<i class="icon-arrow-1-left slick-arrow slick-prev"></i>',
            nextArrow: '<i class="icon-arrow-1-right slick-arrow slick-next"></i>'
        } );
    },
    pauseSlick: function() {
        if ( !this.$slickEl ) {
            return false;
        }
        this.$slickEl.slick( 'slickPause' );
    },
    playSlick: function() {
        if ( !this.$slickEl ) {
            return false;
        }
        this.$slickEl.slick( 'slickGoTo', this.$slickEl.slick( 'slickCurrentSlide' ) );
        this.$slickEl.slick( 'slickPlay' );
    }

} );
