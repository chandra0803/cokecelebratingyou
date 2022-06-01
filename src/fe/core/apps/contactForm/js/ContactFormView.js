/*exported ContactFormView */
/*global
PageView,
LoginFormView,
ContactFormView:true
*/
ContactFormView = PageView.extend( {

    //override super-class initialize function
    initialize: function (  ) {
        'use strict';
        //var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'contactForm';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        this.render();
    },

    events: {
    },

    render: function() {
        this.formView = new LoginFormView( {
            $el: this.$el.find( 'form' )
        } );
    }
} );