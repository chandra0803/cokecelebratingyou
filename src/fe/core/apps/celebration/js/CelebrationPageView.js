/*exported CelebrationPageView */
/*global
LaunchPageView,
CelebrationPageView:true
*/
CelebrationPageView = LaunchPageView.extend( {

    //override super-class initialize function
    initialize: function () {
        'use strict';

        //set the appname (getTpl() method uses this)
        this.appName = 'celebration';

        //this is how we call the super-class initialize function (inherit its magic)
        LaunchPageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, LaunchPageView.prototype.events, this.events );

        $( 'body' ).addClass( 'celebrationPage' );

    }

} );