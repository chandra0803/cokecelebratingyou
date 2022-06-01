/*exported GoalquestModuleView */
/*global
LaunchModuleView,
GoalquestCollectionView,
GoalquestModuleView:true
*/
GoalquestModuleView = LaunchModuleView.extend( {

    initialize: function( opts ) {

        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        this.jsonKey = this.jsonKey || opts.jsonKey || 'URL_JSON_GOALQUEST_COLLECTION';

        this.json = opts.model.get( 'json' ) || null;

        this.on( 'templateLoaded', function() {
            this.loadData();
        } );

        this.on( 'layoutChange', this.handleLayoutChange, this );
    },

    loadData: function() {
        // start the loading state and spinner
        this.dataLoad( true );

        this.collection = new GoalquestCollectionView( {
            el: this.$el.find( '.goalquestItemsWrapper' ),
            json: this.json,
            jsonUrl: G5.props[ this.jsonKey ],
            moduleView: this
        } );

        this.collection.on( 'renderPromotionsFinished', function() {
            // stop the loading state and spinner
            this.dataLoad( false );
        }, this );

        this.collection.loadPromotions();
    },

    handleLayoutChange: function() {
        if ( !this.collection ) {
            return false;
        }
        this.collection.adjustBudgetQtips();
    }

} );
