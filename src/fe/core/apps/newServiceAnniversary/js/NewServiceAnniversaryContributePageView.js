/*exported NewServiceAnniversaryContributePageView*/
/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*global
$,
_,
G5,
PageView,
NewServiceAnniversaryContributePageView:true
*/
NewServiceAnniversaryContributePageView = PageView.extend( {

    //override super-class initialize function
    initialize: function () {
        'use strict';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );       

    },
    events: {
        'click .sa-action': 'invokesaContribute'
    },
    invokesaContribute: function( event ) {
        G5.util.saContribute( event );
    }    
} );
