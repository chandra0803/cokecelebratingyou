/*exported ManagerToolkitPageHistoryView */
/*global
PageView,
ManagerToolkitPageHistoryView:true
*/
ManagerToolkitPageHistoryView = PageView.extend( {

    //override super-class initialize function
    initialize: function( opts ) {

        console.log( '[INFO] ManagerToolkitPageHistoryView: Manager Toolkit Page History View initialized', this );

        //var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'managerToolkit';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        this.checkForServerErrors();

        this.render();

    },

    events: {
    }, // events

    render: function() {
        'use strict';
        //var that = this;

        // initialize any date pickers
        this.$el.find( '.datepickerTrigger' ).datepicker( { container: this.$el } );

    }, // render

    checkForServerErrors: function() {
        if ( $( '#serverReturnedErrored' ).val() === 'true' ) {
            $( '#managerBudgetHistoryErrorBlock' ).slideDown( 'fast' ); //show error block
        }
    } //checkForServerErrors

} );