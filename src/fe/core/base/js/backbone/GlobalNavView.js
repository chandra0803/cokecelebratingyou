/*exported GlobalNavView */
/*global
TemplateManager,
GlobalNavRouter,
GlobalNavView:true
*/
GlobalNavView = Backbone.View.extend( {

    el: '#globalNav',

    initialize: function( opts ) {
        'use strict';
        var that = this;
        console.log( '[INFO] GlobalNavView:', this, opts, this.options );

        // contain the model in the view if it stays simple
        // commented out for now; can see a use for a larger data model in the future but not at this point in time
        // this.model = new Backbone.Model();

        this.page = opts.page;
        this.options.globalNav = that.options.globalNav || G5.props.globalNav || {};
        this.router = new GlobalNavRouter();

        // listen for data change and update (could be used later?)
        // this.model.on('change', this.updateView, this);

        G5._globalEvents.on( 'route', this.handleRoute, this );
        G5._globalEvents.on( 'navigate', this.handleRoute, this );

        this.on( 'rendered', this.handleRoute, this );
        this.on( 'rendered', this.setUpCarousel, this );

        // only render if the user has logged in
        if( opts.loggedIn === true ) {
            this.render();
        }
        else {
            // otherwise remove the globalNav from the DOM
            this.$el.remove();
            $( 'body' ).addClass( 'no-globalNav' );
        }
    }, // initialize

    render: function( opts ) {
        'use strict';
        var that = this;

        opts = opts || this.options.globalNav;

        TemplateManager.get( 'globalNavView', function( tpl ) {
            that.$el.find( '#globalNavView' ).remove();
            that.$el.find( '.container' ).append( tpl( opts ) );

            that.$bar = that.$el.find( '.nav' );
            that.$items = that.$bar.find( '.item' );

            that.trigger( 'rendered' );

        }, G5.props.URL_TPL_ROOT || G5.props.URL_BASE_ROOT + 'tpl/' );

        return this;
    }, // render

    events: {

    },

    handleRoute: function( event, route ) {
        route = route || G5.props.CURRENT_ROUTE;

        if( route && route[ 0 ] == 'launch' ) {
            this.markActive( route[ 1 ] );
        }
    },

    markActive: function( name ) {
        this._active = name || this._active || G5.props.CURRENT_ROUTE[ 1 ];

        this.$el.find( '.item' ).removeClass( 'active' );
        this.$el.find( '.' + this._active + 'Filter' ).addClass( 'active' );
    },

    setUpCarousel: function() {
        var that = this;

        this.itemsW = 0;
        this.slickResponsive = [];

        this.$items.each( function() {
            that.itemsW += $( this ).outerWidth( true );
            that.slickResponsive.push( {
                breakpoint: that.itemsW,
                settings: {
                    initialSlide: Math.max( that.$items.index( that.$items.filter( '.active' ) ), 0 ),
                    slidesToShow: that.slickResponsive.length,
                    slidesToScroll: that.slickResponsive.length
                }
            } );
        } );

        this.$bar.slick( {
            draggable: true,
            focusOnSelect: true,
            infinite: false,
            // initialSlide: this.$items.index( this.$items.filter('.active') ) || 0,
            slidesToShow: this.$items.length,
            slidesToScroll: this.$items.length,
            speed: G5.props.ANIMATION_DURATION,
            swipeToSlide: true,
            variableWidth: true,
            prevArrow: '<i class="icon-arrow-1-circle-left slick-arrow slick-prev"></i>',
            nextArrow: '<i class="icon-arrow-1-circle-right slick-arrow slick-next"></i>',
            responsive: this.slickResponsive
        } );
    },

    getCurrentNavObj: function() {
        var obj = {
            code: this._active,
            name: $.trim( this.$items.filter( '.' + this._active + 'Filter' ).text() )
        };

        return obj;
    }
} );