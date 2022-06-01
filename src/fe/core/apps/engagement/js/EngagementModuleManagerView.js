/*exported EngagementModuleManagerView */
/*global
LaunchModuleView,
EngagementSummaryCollectionView,
EngagementModuleManagerView:true
*/
EngagementModuleManagerView = LaunchModuleView.extend( {
    initialize: function() {

        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        // the default mode of a module is 'user'
        this.mode = 'team';

        // pass the mode to the module model so it can be used in rendering
        this.model.set( 'mode', this.mode );

        // on template loaded and attached
        this.on( 'templateLoaded', function() {
            this.buildSummaryView();
         } );
    },

    events: {
        'click .engagement-link': 'handleVisitAppBtn'
    },

    buildSummaryView: function() {
        this.summaryView = new EngagementSummaryCollectionView( {
            el: this.$el.find( '.engagementSummaryCollectionView' ),
            mode: this.mode,
            moduleView: this
        } );

        // we sneakily hide the spinner inside the collection so we can use .dataLoad on the ModuleView instead
        G5.util.hideSpin( this.summaryView.$el );

        // start the module dataLoading spinner
        this.dataLoad( true );

        // listen for the collectionView's collection to finish loading so we can cancel the ModuleView .dataLoad
        this.summaryView.collection.on( 'loadDataDone', function() {
            this.$el.addClass( 'dataLoaded' );
            this.dataLoad( false );
        }, this );

        this.summaryView.on( 'tabActivated', this.handleTabActivation, this );
    },

    handleVisitAppBtn: function( e ) {
        var $tar = $( e.target ).closest( '.engagement-link' ),
            href = $tar.attr( 'href' ),
            tabNameId = this.summaryView.getTabName();

        // we can safely assume that if there is already a hash in the url, it's pointing to the user profile dashboard (profileUrl#profile/Dashboard)
        // we can therefore assume that if there is no hash in the url, it's pointing to the team dashboard page
        $tar.attr( 'href', href + ( href.indexOf( '#' ) >= 0 ? '/' : '#' ) + tabNameId );
    }
} );
