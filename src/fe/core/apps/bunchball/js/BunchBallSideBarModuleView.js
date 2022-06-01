/*exported BudgetTrackerModuleView */
/*global
SidebarModuleView,
BunchBallModuleView,
BudgetTrackerModuleView:true
*/
BunchBallSideBarModuleView = SidebarModuleView.extend( {

    //override super-class initialize function
    initialize: function() {
        'use strict';

        //this is how we call the super-class initialize function (inherit its magic)
        SidebarModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass SidebarModuleView
        this.events = _.extend( {}, SidebarModuleView.prototype.events, this.events );

    }

} );
