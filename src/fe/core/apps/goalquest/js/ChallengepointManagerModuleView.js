/*exported ChallengepointManagerModuleView */
/*global
GoalquestManagerModuleView,
ChallengepointManagerModuleView:true
*/
// NOTE: the ChallengepointManagerModuleView is being treated as a clone of the GoalquestManagerModuleView.
ChallengepointManagerModuleView = GoalquestManagerModuleView.extend( {

    initialize: function( ) {

        //this is how we call the super-class initialize function (inherit its magic)
        GoalquestManagerModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass GoalquestManagerModuleView
        this.events = _.extend( {}, GoalquestManagerModuleView.prototype.events, this.events );

        this.$el.addClass( 'challengepoint' );

    },
	 events: {
        'click .promotionRules': 'doViewRules'
    },

    doViewRules: function( e ) {
        var $tar = $( e.target ),
            href = $tar.attr( 'href' );

        e.preventDefault();

        G5.util.doSheetOverlay( false, href, $tar.text(), null );
    }



} );
