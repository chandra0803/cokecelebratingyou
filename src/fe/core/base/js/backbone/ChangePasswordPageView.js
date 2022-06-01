/*exported ChangePasswordPageView */
/*global
PageView,
ChangePasswordPageView:true
*/
ChangePasswordPageView = PageView.extend( {

    initialize: function( opts ) {
        console.log( '[INFO] ChangePasswordPageView initialized' );
        //var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'changePasswordPageView';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        this.checkForServerErrors();
    },

    events: {
        'click #submitBtn': 'validateChanges',
        'click .show-pw': 'showPW',
    },

    validateChanges: function( event ) {
        event.preventDefault ? event.preventDefault() : event.returnValue = false;
        var $validateTargets = $( '.validateme' );

        if( G5.util.formValidate( $validateTargets ) ) {
            $( '#changePassForm' ).submit();
        }
    },
    
    showPW: function ( e ) {
        var $tar = $( e.target ),
            input = $tar.siblings( 'input' );

        if( input.prop( 'type' ) == 'text' ) {
            input.prop( 'type', 'password' );
            $tar.removeClass( 'icon-eye' ).addClass( 'icon-eye-off' );
        }
        else {
            input.prop( 'type', 'text' );
            $tar.addClass( 'icon-eye' ).removeClass( 'icon-eye-off' );
        }
    },

    checkForServerErrors: function() {
        if ( $( '#serverReturnedErrored' ).val() === 'true' ) {
            $( '#firstTimeLoginErrorBlock' ).slideDown( 'fast' ); //show error block
            $( '.firstTimeLoginFieldSet' ).show(); //show all fields
            $( '.nextSectionBtn' ).hide(); //hide all continue buttons
            $( '#submitBtn' ).show(); //show submit button
        }
    }

} );
