/*exported ProfilePageSecurityTabView */
/*global
TemplateManager,
ProfilePageSecurityTabView:true
*/
ProfilePageSecurityTabView = Backbone.View.extend( {
    initialize: function ( opts ) {
        'use strict';
        var that = this;
        this.tplName    = opts.tplName || 'profilePageSecurityTab';
        this.tplUrl     = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/';
        this.on( 'templateloadedfirsttime', function() { that.saveInitialData(); } );

        //ARNxyzzy// //console.log("[INFO] ARNlogging ProfilePageSECURITYTabView initialized");
    },

    activate: function () {
        'use strict';
        this.render();
        //ARNxyzzy// //console.log("[INFO] ARNlogging ProfilePageSECURITYTabView activated");
    },

    events: {
        'click #profilePageSecurityTabButtonSave': 'formHandler',   // intercept any clicks on form buttons
        'click #profilePageSecurityTabButtonCancel': 'resetFields',
        'click .show-pw': 'showPW',
		'change #countryPhoneCode': 'changeCountryCode',
        'change #emailAddress': 'changeEmailAddress',
        'change #phoneNumber': 'changePhoneNumber',
        'input #profilePageSecurityTabCurrentPassword': 'buttonEnableSecurity',
        'input #profilePageSecurityTabConfirmNewPassword': 'buttonEnableSecurity',
        'input #phoneNumber': 'validateAndEnable',
        'input #emailAddress': 'buttonEnableSecurity',
        'click .verifyEmail': 'showVerifyModal',
        'click .verifyPhone': 'showVerifyModal',
        'keypress #code': 'handleKeyPress',
        'click .submitCode': 'submitCode',
        'click .terms-open': 'showTnCModal',
        'keyup #phoneNumber': 'dataChanged',
        'input #phoneNumber': 'validateAndEnable',
        'paste #phoneNumber': 'stopPastingAlphabets',
        'drop #phoneNumber': 'stopPastingAlphabets',
        'keyup #emailAddress': 'dataChanged',
        'click .recovery-no-phone': 'showCarrierModal',
        'focus #emailAddress': 'removeFocus',
        'focus #phoneNumber': 'removeFocus'
    },
    validateAndEnable: function() {
        this.validateOnlyNumbers(),
        this.buttonEnableSecurity();
    },
    saveInitialData: function() {
        //save form data the first time around
        var $form       = $( '#profilePageSecurityTabForm' ),
            dataToSend = $form.serializeArray();
        $form.data( 'savedState', dataToSend );
        $( 'form' ).attr( 'autocomplete', 'false' );
        $( '#emailAddress' ).attr( 'autocomplete', 'off' );
        $( '#phoneNumber' ).attr( 'autocomplete', 'false' );
        $( '#profilePageSecurityTabCurrentPassword' ).attr( 'autocomplete', 'new-password' );
        $( '#profilePageSecurityTabConfirmNewPassword' ).attr( 'autocomplete', 'new-password' );
    },
    removeFocus: function( event ) {
        console.log( event );
        $( event.target ).attr( 'readonly', false );
    },
	buttonEnableSecurity: function() {
		this.$el.find( '#profilePageSecurityTabButtonSave' ).removeAttr( 'disabled' );
	},
    dataChanged: function( evnt ) {
        var button = $( evnt.target ).parents( '.row' ).siblings( '.row' ).find( '.btn' ),
            type = button.data( 'type' );
        if( type === 'phone' ) { 
            $( '.phoneVerified' ).addClass( 'hide' );
            $( '.phoneNotVerified' ).removeClass( 'hide' );
        } else {
            $( '.emailVerified' ).addClass( 'hide' );
            $( '.emailNotVerified' ).removeClass( 'hide' );
        }
        button.addClass( 'hide' );

    },
    validateOnlyNumbers: function( event ) {
        var event = event || window.event,
            inputVal = event.target.value,
            allowOnlyNumeric = /[^\d]/g; // Regex pattern to match any non numeric character

        if ( allowOnlyNumeric.test( inputVal ) ) {
            // Replacing any non numeric character with empty string during key input
            event.target.value = inputVal.replace( allowOnlyNumeric, '' );
        }
    },
    stopPastingAlphabets: function( event ) {
        var eventType = event.handleObj.type, 
            eventData;
        if( eventType === 'drop' ) {
            if( event.originalEvent.dataTransfer.items ) {
                event.originalEvent.dataTransfer.items[ 0 ].getAsString( function( str )
                {
                    eventData = parseInt( str ); // Getting the text that was dragged and dropped
                } );
            } else {
                eventData = 'null';
            }
        }
        if( eventType === 'paste' ) {
            if( event.originalEvent.clipboardData ) {
                eventData = event.originalEvent.clipboardData.getData( 'Text' ); // Getting text from clipboard
            } else {
                eventData = window.clipboardData.getData( 'text' ); // for IE
            }
        }
        if( isNaN( eventData ) ) { // Preventing the paste only for non numeric content
            event.preventDefault();
        }
    },
	changeCountryCode: function( evnt ) {
    	var selectVal = evnt.target.value;

    	$( "#countryPhoneCode option[value='" + selectVal + "']" ).attr( 'selected', 'selected' );
        if( selectVal.length ) {
            $( '.phoneNumberWrapper' ).addClass( 'validateme' );
        }
    },

	changeEmailAddress: function( evnt ) {
        var emailVal = evnt.target.value;
        $( '#emailAddress' ).attr( 'value', emailVal );
        if( emailVal.length ) {
          $( '.emailAddress' ).addClass( 'validateme' );
        }
    },

    changePhoneNumber: function( evnt ) {
        var phoneVal = evnt.target.value;
        $( '#phoneNumber' ).attr( 'value', phoneVal );
    },
    showCarrierModal: function( evnt ) {
        console.log( 'here' );
        $( '#verifyModal' ).modal( 'hide' );
        $( '#carrierModal' ).modal();
        $( '#carrierModal' ).on( 'hide', function() {
            $( '#verifyModal' ).modal();
        } );
    },
    showVerifyModal: function( evnt ) {
        var that = this,
            type = $( evnt.target ).data( 'type' ),
            value;

        G5.util.showSpin( this.$el );
        console.log( $( evnt.target ).attr( 'disabled' ) );
        if( $( evnt.target ).attr( 'disabled' ) === 'disabled' ) {
            return;
        }
        $( '#verifyModal' ).find( '.alert' ).addClass( 'hide' );
        $( '#verifyModal' ).data( 'type', type );
        if( type === 'phone' ) {
            $( '.phoneHeader' ).removeClass( 'hide' );
            $( '.emailHeader' ).addClass( 'hide' );
            $( '.recovery-no-phone' ).removeClass( 'hide' );
            $( '.icon-info' ).removeClass( 'hide' );
            value = $( '#phoneNumber' ).val();
        } else if( type === 'email' ) {
            $( '.phoneHeader' ).addClass( 'hide' );
            $( '.emailHeader' ).removeClass( 'hide' );
            $( '.recovery-no-phone' ).addClass( 'hide' );
            $( '.icon-info' ).addClass( 'hide' );
            value = $( '#emailAddress' ).val();
        }
        $.ajax( {
            dataType: 'g5json',
            contentType: 'application/json',
            url: 'recoveryverification/v1/sendRecoveryCode.action',
            type: 'POST',
            data: JSON.stringify( { contactType: type, emailOrPhone: value } ),
            success: function( data ) {
                if( data.data.responseCode === 200 ) {
                    $( '.alert-success' ).text( data.data.responseMessage );
                    $( '.alert-success' ).removeClass( 'hide' );
                }
                $( '#code' ).val( '' );
                $( '#code' ).show();
                $( 'label' ).removeClass( 'hide' );
                $( '.submitCode' ).removeClass( 'hide' );
                $( '#verifyModal' ).modal();
                G5.util.hideSpin( that.$el );
            },
            error: function ( error ) {
                //console.log("[INFO] ProfilePageSECURITYTabView save error");
                console.log( error );
                $( '#verifyModal' ).modal();
                $( '.alert-error' ).text( error.responseJSON.fieldErrors[ 0 ].message );
                $( '.alert-error' ).removeClass( 'hide' );
                $( '#code' ).hide();
                $( '#verifyModal' ).find( 'label' ).addClass( 'hide' );
                $( '.submitCode' ).addClass( 'hide' );
                $( '#verifyModal' ).find( 'h1' ).addClass( 'hide' );
                G5.util.hideSpin( that.$el );
            }
        } );



    },
    showTnCModal: function( evnt ) {
        $( '#tncModal' ).modal();
    },
    submitCode: function( evnt ) {
        var type = $( '#verifyModal' ).data( 'type' );
        var code = $( '#code' ).val();
        var url = 'recoveryverification/v1/verifyRecoveryCode.action' || G5.props.SOME_ENDPOINT;
        $.ajax( {
            dataType: 'g5json',
            contentType: 'application/json',
            url: url,
            type: 'POST',
            data: JSON.stringify( { contactType: type, token: code } ),
            success: function ( data ) {
                console.log( data );
                if( type === 'phone' ) {
                    $( '.verifyPhone' ).addClass( 'hide' );
                    $( '.verifyPhone' ).data( 'verified', true );
                    $( '.phoneVerified' ).removeClass( 'hide' );
                    $( '.phoneNotVerified' ).addClass( 'hide' );

                } else {
                    $( '.verifyEmail' ).addClass( 'hide' );
                    $( '.verifyEmail' ).data( 'verified', true );
                    $( '.emailVerified' ).removeClass( 'hide' );
                    $( '.emailNotVerified' ).addClass( 'hide' );
                }

                $( '#verifyModal' ).modal( 'hide' );
            },
            error: function ( error ) {
                //console.log("[INFO] ProfilePageSECURITYTabView save error");
                console.log( error );
                $( '.alert-error' ).text( error.responseJSON.fieldErrors[ 0 ].message );
                $( '.alert-error' ).removeClass( 'hide' );
                $( '.alert-success' ).addClass( 'hide' );
            }
        } );
    },
    handleKeyPress: function( evnt ) {
        console.log( evnt );
        if( evnt.keyCode === 13 ) {
            this.submitCode( evnt );
        }
    },
    formHandler: function ( evnt ) {
        'use strict';
        //ARNxyzzy// //console.log("[INFO] ARNlogging ProfilePageSECURITYTabView formHandler...");

        var $form        = $( '#profilePageSecurityTabForm' ),
            actionURL    = $form.attr( 'action' ) || G5.props.URL_JSON_PROFILE_PAGE_SECURITY_TAB,
            method       = $form.attr( 'method' ),
			dataToSendA  = $form.serializeArray(),
			crntPassword = $form.find( '#profilePageSecurityTabCurrentPassword' ).val(),
            newPassword  = $form.find( '#profilePageSecurityTabConfirmNewPassword' ).val(),
			phoneNumber  = $form.find( '#phoneNumber' ).val(),
			emailAddress = $form.find( '#emailAddress' ).val(),
            dataToSend   = $form.serialize();

        evnt.preventDefault();
		if ( ( crntPassword && crntPassword.length ) || ( newPassword && newPassword.length ) ) {
           $( '.passwordReset .control-group' ).addClass( 'validateme' );
        } else {
           $( '.passwordReset .control-group' ).removeClass( 'validateme' );
        }
		if ( phoneNumber && phoneNumber.length ) {
            $( '.countryPhoneCodeWrapper' ).addClass( 'validateme' );
            $( '.phoneNumberWrapper' ).addClass( 'validateme' );
        } else {
            $( '.countryPhoneCodeWrapper' ).removeClass( 'validateme' );
            $( '.phoneNumberWrapper' ).removeClass( 'validateme' );
        }

        if ( emailAddress && emailAddress.length ) {
            $( '.emailAddress' ).addClass( 'validateme' );
        } else {
            $( '.emailAddress' ).removeClass( 'validateme' );
        }

        // if the entire form fails to validate prevent it from continuing
        if ( !G5.util.formValidate( $form.find( '.validateme' ) ) ) {
            return false;
        }
		if ( ( !phoneNumber || ( phoneNumber && !phoneNumber.length ) )  && ( !emailAddress || ( emailAddress && !emailAddress.length ) ) ) {
			$form.find( '.help-block' ).show();
			return false;
		} else {
			$form.find( '.help-block' ).hide();
		}
        $.ajax( {
            dataType: 'g5json',
            url: actionURL,
            type: method,
            data: dataToSend,
            success: function ( data ) {
                //console.log("[INFO] ProfilePageSECURITYTabView save success");

                //check for errors
                if ( !G5.util.formValidateHandleJsonErrors( $form, data.data.messages ) ) {
                    $( '#profilePageSecurityTabConfirmNewPassword' ).focus();
                    return false;
                }

                $form.data( 'savedState', dataToSendA );
                //load in updated values to form (for 'reset' purposes)
                $( '#profilePageSecurityTabCurrentPassword' ).val( '' );
                $( '#profilePageSecurityTabConfirmNewPassword' ).val( '' );
                $( '#saveSuccessModal' ).find( 'p' ).text( data.data.messages[ 0 ].text );
                $( '#saveSuccessModal' ).modal();

            },
            error: function ( ) {
                //console.log("[INFO] ProfilePageSECURITYTabView save error");
            }
        } );

        // return false;
        //ARNxyzzy// //console.log("[INFO] ARNlogging ProfilePageSECURITYTabView ...formHandler");
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

    render: function () {
        'use strict';
        var that = this;

        // if there is no html in the tab content element, go get the remote contents
        if ( this.$el.html().length === 0 ) {
            this.$el
                .append( '<span class="spin" />' )
                .find( '.spin' ).spin();

            TemplateManager.get( this.tplName,
                function ( tpl ) {
                    that.$el.empty().append( tpl( {} ) );
                    that.trigger( 'templateloaded' );
                    that.trigger( 'templateloadedfirsttime' );
                },
                this.tplUrl );
        } else {
            // otherwise, just trigger the completion event
            this.trigger( 'templateloaded' );
        }


        this.on( 'templateloaded', function() {
            this.showHideVerified();
            this.processLink();
            $( '#profilePageSecurityTabNewPassword', that.$el ).focus();
            $( '#saveSuccessModal' ).on( 'hidden', function() {
                location.reload();
            } );
        } );

        //clear out old form values
        $( '#profilePageSecurityTabForm' ).each( function() {
                this.reset();
        } );


        //$( '#profilePageSecurityTabButtonCancel' ).trigger( 'click' );
        // return this;
    },
    processLink: function() {
        var linkText = $( '.terms-link' ).html();

        linkText = linkText.replace( '\${linkOpen}', '<a class="terms-open">' );
        linkText = linkText.replace( '\${linkClose}', '</a>' );

        $( '.terms-link' ).html( linkText );
    },
    showHideVerified: function() {
        if( $( '#phoneNumber' ).length === 0 ) {
            if( $( '.verifyPhone' ).data( 'verified' ) === true ) {
                $( '.phoneVerified' ).removeClass( 'hide' );
                $( '.phoneNotVerified' ).addClass( 'hide' );
            } else if( $( '.verifyPhone' ).data( 'verified' ) === false ) {
                $( '.phoneVerified' ).addClass( 'hide' );
                $( '.phoneNotVerified' ).removeClass( 'hide' );
            }
        }
        if( $( '#emailAddress' ).length === 0 ) {
            if( $( '.verifyEmail' ).data( 'verified' ) === true ) {
                $( '.emailVerified' ).removeClass( 'hide' );
                $( '.emailNotVerified' ).addClass( 'hide' );
            } else if( $( '.verifyEmail' ).data( 'verified' ) === false ) {
                $( '.emailVerified' ).addClass( 'hide' );
                $( '.emailNotVerified' ).removeClass( 'hide' );
            }
        }
        if( $( '.verifyPhone' ).data( 'verified' ) === true || ( $( '#phoneNumber' ).length && $( '#phoneNumber' ).val().length === 0 ) ) {
            $( '.verifyPhone' ).addClass( 'hide' );

        }

        if( $( '#phoneNumber' ).length && $( '#phoneNumber' ).val().length > 0 ) {
            if( $( '.verifyPhone' ).data( 'verified' ) === true ) {
                $( '.phoneVerified' ).removeClass( 'hide' );
                $( '.phoneNotVerified' ).addClass( 'hide' );
            } else if( $( '.verifyPhone' ).data( 'verified' ) === false ) {
                $( '.phoneVerified' ).addClass( 'hide' );
                $( '.phoneNotVerified' ).removeClass( 'hide' );
            }
        }
        if( $( '.verifyEmail' ).data( 'verified' ) === true || ( $( '#emailAddress' ).length && $( '#emailAddress' ).val().length === 0 ) ) {
            $( '.verifyEmail' ).addClass( 'hide' );
        }

        if( $( '#emailAddress' ).length && $( '#emailAddress' ).val().length > 0 ) {
            if( $( '.verifyEmail' ).data( 'verified' ) === true ) {
                $( '.emailVerified' ).removeClass( 'hide' );
                $( '.emailNotVerified' ).addClass( 'hide' );
            } else if( $( '.verifyEmail' ).data( 'verified' ) === false ) {
                $( '.emailVerified' ).addClass( 'hide' );
                $( '.emailNotVerified' ).removeClass( 'hide' );
            }
        }
    },
    resetFields: function( event ) {

        var $form = $( event.target ).closest( 'form' ),
            savedState = $form.data( 'savedState' );
        event.preventDefault();

        $form.find( ':checked' ).removeAttr( 'checked' );

        _.each( savedState, function( v ) {
            var $elems = $form.find( '[name="' + v.name + '"]' );

            if ( $elems.length === 1 ) {
                $elems.val( v.value );
            }            else if ( $elems.length > 1 ) {
                $elems.filter( '[value="' + v.value + '"]' ).attr( 'checked', 'checked' );
            }
        } );
        _.each( $form.find( ':input' ), function( current ) {
            var $current = $( current );
            if ( !$current.hasClass( 'saveField' ) ) {
                $current.val( '' );
            }
        } );

        //clear out errors
        $( '.validate-tooltip' ).qtip( 'hide' );
        $( '.validateme' ).removeClass( 'error' );

    }
} );
