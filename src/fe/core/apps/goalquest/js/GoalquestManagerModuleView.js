/*exported GoalquestManagerModuleView */
/*global
LaunchModuleView,
GoalquestManagerModuleView:true
*/
GoalquestManagerModuleView = LaunchModuleView.extend( {

    initialize: function(  ) {

        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

    }

} );