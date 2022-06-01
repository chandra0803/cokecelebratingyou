/*exported ReportsDashboardModuleView*/
/*global
LaunchModuleView,
FavoriteReportsModuleView,
AllReportsModuleView,
ReportsDashboardModuleView:true
*/
ReportsDashboardModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function() {
        'use strict';

        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        // handy storage for later
        this.favoritesView = null;
        this.allView = null;

        this.on( 'templateLoaded', this.postRender, this );

    }, // initialize

    events: {
    },

    postRender: function() {
        var that = this;

        // more handy storage for later
        this.$favoritesTab = this.$el.find( '.dashboard-favorites' );
        this.$allTab = this.$el.find( '.dashboard-all' );
        this.$favoritesCont = this.$el.find( '.favoritesContainer' );
        this.$allCont = this.$el.find( '.allContainer' );

        // set up tabs
        this.$el.find( '.nav-tabs' ).tab();

        // make the two columns match heights
        this.fitHeights();
        this.fitHeightsPoll = setInterval( function() {
            that.fitHeights();
        }, 1000 );

        this.favoritesView = new FavoriteReportsModuleView( {
            $el: this.$favoritesCont,
            favTpl: this.subTpls.favoriteTpl
        } );

        this.allView = new AllReportsModuleView( {
            $el: this.$allCont
        } );
    },

    fitHeights: function() {
        this.$allTab.css( 'min-height', this.$favoritesTab.height() );
    }
} ); // extend
