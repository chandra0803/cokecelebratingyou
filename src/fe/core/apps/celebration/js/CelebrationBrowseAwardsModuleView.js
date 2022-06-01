/*exported CelebrationBrowseAwardsModuleView */
/*global
LaunchModuleView,
CelebrationBrowseAwardsModuleView:true
*/
CelebrationBrowseAwardsModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function ( opts ) {
        'use strict';
        //var that = this;

        LaunchModuleView.prototype.initialize.apply( this, arguments ); // this is how we call the super-class initialize function (inherit its magic)
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events ); // inherit events from the superclass ModuleView

        this.on( 'templateLoaded', function() {
            this.startSlick( { } );
        } );

    },

    startSlick: function() {
        var $slickEl = this.$el.find( '#awardsCarousel .cycle' ); //find all modules with a carousel
        $slickEl.slick( {
            autoplay: false,
            dots: true,
            arrows: true,
            infinite: false,
            speed: G5.props.ANIMATION_DURATION,
            slidesToShow: 4,
            slidesToScroll: 4,
            prevArrow: '<i class="icon-arrow-1-left slick-arrow slick-prev"></i>',
            nextArrow: '<i class="icon-arrow-1-right slick-arrow slick-next"></i>',
            responsive: [
            {
              breakpoint: 1279,
              settings: {
                slidesToShow: 3,
                slidesToScroll: 3
              }
            },
            {
              breakpoint: 1023,
              settings: {
                slidesToShow: 2,
                slidesToScroll: 2
              }
            },
            {
              breakpoint: 640,
              settings: {
                slidesToShow: 1,
                slidesToScroll: 1
              }
            }
            ]
        } );
    }
} );