/*exported EngagementModuleView */
/*global
SidebarModuleView,
EngagementSummaryCollectionView,
EngagementModuleView:true
*/
EngagementModuleView = SidebarModuleView.extend( {
    initialize: function() {

        //this is how we call the super-class initialize function (inherit its magic)
        SidebarModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass SidebarModuleView
        this.events = _.extend( {}, SidebarModuleView.prototype.events, this.events );

        // the default mode of a module is 'user'
        this.mode = 'user';

        // pass the mode to the module model so it can be used in rendering
        this.model.set( 'mode', this.mode );

        // on template loaded and attached
        this.on( 'templateLoaded', function() {
            this.buildSummaryView();
            // _.delay(G5.util.textShrink, 100, this.$el.find('.titleLink'), {minFontSize:20});
            // G5.util.textShrink( this.$el.find('.titleLink'), {minFontSize:20});
        } );
    },

    events: {
        'click .engagement-view-details': 'handleViewDetails'
    },

    buildSummaryView: function() {
        var route = G5.props.CURRENT_ROUTE.length && G5.props.CURRENT_ROUTE;

        this.dataLoad( true );

        this.summaryView = new EngagementSummaryCollectionView( {
            el: this.$el.find( '.engagementSummaryCollectionView' ),
            mode: this.mode,
            moduleView: this,
            startTab: route && route[ 0 ] === 'profile' && route[ 1 ] === 'Dashboard' && route[ 2 ] ? route[ 2 ] : null
        } );

        this.summaryView.collection.on( 'loadDataDone', function() {
            this.dataLoad( false );
        }, this );

        this.summaryView.on( 'tabActivated', this.handleTabActivation, this );

        G5._globalEvents.on( 'route', function( event, route ) {
            if ( route && route[ 0 ] === 'profile' && route[ 1 ] === 'Dashboard' && route[ 2 ] ) {
                this.summaryView.showTabByName( route[ 2 ] );
            }
        }, this );
    },

    handleViewDetails: function( e ) {
        var $tar = $( e.target ).closest( '.engagement-view-details' ),
            href = $tar.attr( 'href' ),
            tabNameId = this.summaryView.getTabName();

        $tar.attr( 'href', href.replace( /#.+/, '#profile/Dashboard/' ) + tabNameId );
    },

    handleTabActivation: function( $tab, view ) {
        G5._globalEvents.trigger( 'EngagementModuleViewChanged', {}, $tab, view );
    },

    updateView: function() {
        if ( this.summaryView ) {
            this.summaryView.activateTab();
        }
    }
} );
