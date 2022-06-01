/*exported CelebrationCompanyTimelineModuleView */
/*global
LaunchModuleView,
CelebrationCompanyTimelineModuleView:true
*/
CelebrationCompanyTimelineModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function ( opts ) {
        'use strict';
        // var that = this;

        LaunchModuleView.prototype.initialize.apply( this, arguments ); // this is how we call the super-class initialize function (inherit its magic)
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events ); // inherit events from the superclass ModuleView

    }
} );