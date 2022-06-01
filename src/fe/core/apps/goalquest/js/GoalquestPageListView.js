/*exported GoalquestPageListView */
/*global
PageView,
GoalquestCollectionView,
GoalquestPageListView:true
*/
GoalquestPageListView = PageView.extend( {

    initialize: function(  ) {

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        if ( this.options.mode === 'manager' ) {
            return;
        }

        this.gqCollView = new GoalquestCollectionView( {
            el: this.$el.find( '.goalquestItemsWrapper' ),
            jsonUrl: G5.props.URL_JSON_GOALQUEST_COLLECTION
        } );

        this.gqCollView.loadPromotions();
    },

    events: {
        'click .rulesLink': 'doViewRules'
    },

    doViewRules: function( e ) {
        var $tar = $( e.target ),
            href = $tar.attr( 'href' );

        e.preventDefault();

        G5.util.doSheetOverlay( false, href, $tar.text(), null );
    }

} );