/*exported CelebrationManagerMessageModuleView */
/*global
PageView,
LaunchModuleView,
CelebrationManagerMessageModuleView:true
*/
CelebrationManagerMessageModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function ( opts ) {
        'use strict';
        var that = this;

        LaunchModuleView.prototype.initialize.apply( this, arguments ); // this is how we call the super-class initialize function (inherit its magic)
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events ); // inherit events from the superclass ModuleView

        this.on( 'templateLoaded', function() {
            that.resizeText();
            that.startSlick();
             that.resizeListener();
        G5._globalEvents.on( 'windowResized', that.resizeListener, that );
            //_.delay( G5.util.textShrink, 100, this.$el.find( '.comment' ), { minFontSize: 20 } );
            //G5.util.textShrink( this.$el.find( '.comment' ), { minFontSize: 20 } );
        } );


    },
    events: {
        'click .cycleDot': 'resizeText'
    },
    resizeText: function() {

        //Have to add separate textShrink for the items in the carousel to resize on click since they are hidden on page load
        //_.delay( G5.util.textShrink, 100, this.$el.find( '.comment' ), { minFontSize: 20 } );
        G5.util.textShrink( this.$el.find( '.comment' ), { minFontSize: 16 } );
    },
    resizeListener: function() {

        this.resizeText();
        },


    startSlick: function() {

        var $slickEl = this.$el.find( '#managerMessageCarousel .cycle' ); //find all modules with a carousel
        $slickEl.slick( {
            dots: true,
            infinite: true,
            speed: 300,
            slidesToShow: 1,
            slidesToScroll: 1,
            centerMode: false,
            variableWidth: false,
            adaptiveHeight: true,
            prevArrow: '<i class="icon-arrow-1-left slick-arrow slick-prev"></i>',
            nextArrow: '<i class="icon-arrow-1-right slick-arrow slick-next"></i>',
        } );
    }
} );