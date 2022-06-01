/*exported CelebrationAnniversaryFactsModuleView */
/*global
LaunchModuleView,
CelebrationAnniversaryFactsModuleView:true
*/
CelebrationAnniversaryFactsModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function ( opts ) {
        'use strict';
        var that = this;

        LaunchModuleView.prototype.initialize.apply( this, arguments ); // this is how we call the super-class initialize function (inherit its magic)
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events ); // inherit events from the superclass ModuleView

        this.on( 'templateLoaded', function() {
            that.startSlick();
        } );

    },

    startSlick: function() {

        var $slickEl = this.$el.find( '#yearThatWasCarousel .cycle' ); //find all modules with a carousel
        $slickEl.slick( {
            dots: false,
            infinite: true,
            speed: G5.props.ANIMATION_DURATION,
            slidesToShow: 1,
            slidesToScroll: 1,
            centerMode: false,
            variableWidth: false,
            prevArrow: '<i class="icon-arrow-1-left slick-arrow withbg slick-prev"></i>',
            nextArrow: '<i class="icon-arrow-1-right slick-arrow withbg slick-next"></i>',

        } );
    }
} );