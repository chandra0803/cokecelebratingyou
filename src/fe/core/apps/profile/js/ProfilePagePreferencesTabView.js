/*exported ProfilePagePreferencesTabView */
/*global
TemplateManager,
ProfilePagePreferencesTabView:true
*/
ProfilePagePreferencesTabView = Backbone.View.extend( {
    initialize: function ( opts ) {
        'use strict';
        var that = this;

        this.profilePageView    = opts.profilePageView;
        this.tplName            = opts.tplName || 'profilePagePreferencesTab';
        this.tplUrl             = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/';
        this.on( 'templateloadedfirsttime', function() { that.saveInitialData(); } );
        //ARNxyzzy// //console.log("[INFO]11 ARNlogging ProfilePagePreferencesTabView initialized");
        //
        G5._globalEvents.on( 'partProfileUpdate', this.render, this );
    },

    activate: function () {
        'use strict';

        // this.render();
    },

    events: {
        'click .profile-popover': 'attachParticipantPopover',
        'click #profilePagePreferencesTabButtonSave': 'formHandler',
        // "click .form-actions button":   "formActions",  // intercept any clicks on form buttons
        'click .select-all a': 'selectTextAlerts',
        'click #profilePagePreferencesTabButtonCancel': 'resetFields',
        'click .ua-disconnect': 'uaDisconnect'

        // TEMPORARY for debugging/testing - validation only done with Save button formActions
        // "blur .validateme input, .validateme select, .validateme textarea" : function (e) {
        //     'use strict';
        //     G5.util.formValidate($(e.target));
        // }
    },

    saveInitialData: function() {
        //save form data the first time around
        var $form       = $( '#profilePagePreferencesTabForm' ),
            dataToSendA = $form.serializeArray();
        $form.data( 'savedState', dataToSendA );
    },

    attachParticipantPopover: function ( evnt ) {
        'use strict';
        var $tar = $( evnt.target );

        //attach participant popovers
        if ( !$tar.data( 'participantPopover' ) ) {
            $tar.participantPopover( { containerEl: this.$el } ).qtip( 'show' );
        }
        evnt.preventDefault();
    },

    initTextAlerts: function() {
        var that = this,
            $textAlertWrapper = this.$el.find( '#profilePagePreferencesTabFieldsetTextMessages' ),
            $conditionalFields = this.$el.find( '#profilePagePreferencesTabFieldsetCountryPhoneCode, #profilePagePreferencesTabFieldsetPhoneNumber, #profilePagePreferencesTabFieldsetTermsAccepted' ),
            somethingChecked = $textAlertWrapper.find( 'input:checked' ).length ? true : false;

        $conditionalFields
            .prop( 'disabled', !somethingChecked )
            .closest( '.error' ).each( function() {
                G5.util.formValidateMarkField( 'valid', $( this ) );
                // $(this).removeClass('validateme');
            } );

        $textAlertWrapper.find( 'input' ).one( 'change', function() {
            that.initTextAlerts();
        } );
    },

    selectTextAlerts: function( e ) {

        var $textAlertWrapper = this.$el.find( '#profilePagePreferencesTabFieldsetTextMessages' );
        e.preventDefault();
        $textAlertWrapper.find( 'input' ).prop( 'checked', $( e.target ).attr( 'id' ) === 'all' );
        this.initTextAlerts();
    },

    formHandler: function ( evnt ) {
        'use strict';
        //ARNxyzzy// //console.log("[INFO] ARNlogging ProfilePagePREFERENCESTabView formHandler...");


        var $form       = $( '#profilePagePreferencesTabForm' ),
            actionURL   = G5.props.URL_JSON_PROFILE_PAGE_PREFERENCES_TAB,
            method      = $form.attr( 'method' ),
            //dataToSend  = $form.serialize() + '&' + $form.data( 'triggeredByName' ) + '=' + $form.data( 'triggeredByValue' ),
            dataToSendA = $form.serializeArray(),
            i           = 0,
            $inputs;
        evnt.preventDefault();
        //the legal thing only needs to be validated if there is a phone number attached.
        // if (!$('#profilePagePreferencesTabFieldsetPhoneNumber').val() ==""){
        //     $('#preferencesTabLegalCheckBox').addClass('validateme');
        // } else {
        //     $('#preferencesTabLegalCheckBox').removeClass('validateme');
        //     $('#profilePagePreferencesTabFieldsetPhoneNumber').qtip('hide');
        // }

        //moving this logic to validateTextAlerts()

        this.validateTextAlerts();

        this.validateEmailAddress();

        // if the entire form fails to validate prevent it from continuing
        if ( !G5.util.formValidate( $form.find( '.validateme' ) ) ) {
            return false;
        }

        $.ajax( {
            dataType: 'g5json',
            url: actionURL,
            action: actionURL,
            type: method,
            data: dataToSendA,
            success: function ( data ) {
                $form.data( 'savedState', dataToSendA );

                //check for errors
                if ( !G5.util.formValidateHandleJsonErrors( $form, data.data.messages ) ) {
                    return false;
                }

                for ( i = 0; i < dataToSendA.length; i++ ) {
                    // //console.log("[INFO] [" + dataToSendA[i].name + "] = [" + dataToSendA[i].value + "]");
                }
                //load in updated values to form (for 'reset' purposes)

                $inputs = $( '#profilePagePreferencesTabForm :input' );
                i = 0;
                //for each input, give it a data attribute of what it should be resetting to if
                //the 'reset button is pressed.'
                $inputs.each( function() {
                    $( this ).data( 'ResetValue', $( this ).val() );
                } );

            },
            error: function (  ) {
                //console.log("[INFO] ProfilePagePREFERENCESTabView save error");
            }
        } );

        //ARNxyzzy// //console.log("[INFO] ARNlogging ProfilePagePREFERENCESTabView ...formHandler");
    },

    validateTextAlerts: function() {
        //if a text alert option is checked and the phone number input is empty, do not validate
        //also, if a phone number is entered, but no text alerts checked, do not validate
        // if something is checked, and the terms are not accepted, do not validate
        var validAlertSettings = true,
            somethingChecked = false,
            $phoneNumbeElm = $( '#profilePagePreferencesTabFieldsetPhoneNumber' ),
            $textAlertElm = $( '#profilePagePreferencesTabFieldsetTextMessages' ).find( '.control-group' ),
            $txtAlertTermsElm = $( '#profilePagePreferencesTabFieldsetTermsAccepted' ),
            $checkedAlerts = $textAlertElm.find( 'input' ).each( function() {
                if ( $( this ).attr( 'checked' ) !== undefined ) {
                    somethingChecked = true;
                    if ( $phoneNumbeElm.val() === '' ) {
                        $phoneNumbeElm.closest( '.control-group' ).addClass( 'validateme' );
                        G5.util.formValidate( $phoneNumbeElm );
                        validAlertSettings = false;
                    }
                }

            } );
            console.log( $checkedAlerts );

        if ( !somethingChecked ) {
                $phoneNumbeElm.closest( '.control-group' ).removeClass( 'validateme error' );
                $phoneNumbeElm.closest( '.control-group' ).find( '.qtip' ).qtip( 'hide' );
            }

        if ( !somethingChecked ) {
            if ( $phoneNumbeElm.val() !== '' ) {
                $textAlertElm.addClass( 'validateme' );
                G5.util.formValidate( $textAlertElm );
                validAlertSettings = false;
            } else {
                $textAlertElm.removeClass( 'validateme' );
                $textAlertElm.find( '.qtip' ).qtip( 'hide' );
            }
        } else {
            $textAlertElm.removeClass( 'validateme' );
            $textAlertElm.find( '.qtip' ).qtip( 'hide' );
        }

        if ( somethingChecked && $txtAlertTermsElm.attr( 'checked' ) !== 'checked' ) {
            $txtAlertTermsElm.closest( '.control-group' ).addClass( 'validateme' );
            G5.util.formValidate( $txtAlertTermsElm );
            validAlertSettings = false;
        } else {
            $txtAlertTermsElm.closest( '.control-group' ).removeClass( 'validateme' );
            $txtAlertTermsElm.closest( '.control-group' ).find( '.qtip' ).qtip( 'hide' );
        }

        return validAlertSettings;
    },

    validateEmailAddress: function() {
        var $estateInp = this.$el.find( '.estatements' ),
            $emailInpUpdate = this.$el.find( '.participantEmailUpdate' ),
            $emailInpCont = this.$el.find( '.participantEmailUpdateCont' ),
            $estateCheck = this.$el.find( '.estatementCont' ),
            $emailInp = this.$el.find( '.estatementEmail' );

        if ( $estateInp.prop( 'checked' ) ) {
            $emailInpUpdate.addClass( 'validateme' );
        } else if ( !$estateInp.prop( 'checked' ) && $emailInp.val() !== '' && $emailInp.is( ':visible' ) ) {
            $emailInpUpdate.addClass( 'validateme' );

            if ( G5.util.formValidate( $emailInpCont.find( '.validateme' ) ) ) {
                $estateCheck.addClass( 'validateme' );
            }
        } else {
            $emailInpUpdate.removeClass( 'validateme' ).find( '.qtip' ).qtip( 'hide' );
        }
    },

    render: function () {
        'use strict';
        var that = this;

        G5._globalEvents.off( 'partProfileUpdate', this.render, this );

        // if there is no html in the tab content element, go get the remote contents
        if ( this.$el.html().length === 0 ) {
            this.$el
                .append( '<span class="spin" />' )
                .find( '.spin' ).spin();

            TemplateManager.get( this.tplName,
                function ( tpl ) {
                    // that.$el.empty().append(tpl({}));
                    that.$el.empty().append( tpl( { id: that.profilePageView.getParticipantProfile( 'id' ) } ) );
                    that.initTextAlerts();
                    that.estatementEmailCheck();
                    that.trigger( 'templateloaded' );
                    that.trigger( 'templateloadedfirsttime' );
                    that.initUA();
                },
                this.tplUrl );
        } else {
            this.trigger( 'templateloaded' );
        }

        //clear out old form values
        $( '#profilePagePreferencesTabForm' ).each( function() {
          this.reset();
        } );

        $( '#profilePagePreferencesTabButtonCancel' ).trigger( 'click' );
        return this;

    },

    estatementEmailCheck: function() {
        var $estateCont = this.$el.find( '#profilePagePreferencesTabFieldsetEStatements' ),
            $emailAddress = $estateCont.find( '.participantEmail' ),
            $emailInpCont = $estateCont.find( '.participantEmailUpdateCont' );

        if ( !$emailAddress.text().length ) {
            $emailInpCont.show();
        }
    },

    initUA: function() {
        var uaConnectButton = this.$el.find( '.ua-login' ),
            uaDisconnectButton = this.$el.find( '.ua-disconnect' ),
            uaAuthState = this.$el.find( '#uaAuthorized' ).attr( 'value' ),
            uaEnabled = this.$el.find( '#uaEnabled' ).attr( 'value' )
        ;
        if( uaEnabled === 'false' ) {
            this.$el.find( '#profilePagePreferencesTabFieldsetUnderarmour' ).hide();
        } else {
            this.$el.find( '#profilePagePreferencesTabFieldsetUnderarmour' ).show();
        }
        if ( uaAuthState === 'true' ) {
            uaDisconnectButton.css( 'display', 'inline-block' );
        }
        if ( uaAuthState === 'false' ) {
            uaConnectButton.css( 'display', 'inline-block' );
        }
    },

    uaDisconnect: function( e ) {
        var dataBackFromUA = 'data',
            method = 'POST',
            uaConnectButton = this.$el.find( '.ua-login' ),
            uaDisconnectButton = this.$el.find( '.ua-disconnect' ),
            disconnectURL = uaDisconnectButton.data( 'disconnecturl' ),
            uaError = this.$el.find( '.ua-error' )
            ;

        $.ajax( {
            dataType: 'g5json',
            url: disconnectURL,
            action: disconnectURL,
            type: method,
            data: dataBackFromUA,
            success: function ( data ) {
                uaConnectButton.css( 'display', 'inline-block' );
                uaDisconnectButton.hide();
                uaError.hide();
            },
            error: function (  ) {
                uaError.show();
            }

        } );

        e.preventDefault();
    },

    resetFields: function( e ) {

        // Reset form
        var $form = $( e.target ).closest( 'form' ),
            savedState = $form.data( 'savedState' );
        e.preventDefault();
        $form.find( ':checked' ).removeAttr( 'checked' );

        // remove error message(s)
        $( '.error' ).removeClass( 'error' );

        // hide any qtips visible from an error message
        $( '.qtip.ui-tooltip' ).qtip( 'hide' );

        _.each( savedState, function( v ) {
            var $elems = $form.find( '[name="' + v.name + '"]' );
            if ( $elems.length === 1 ) {
                if ( v.name === 'contactMethodTypes' ) {
                    $elems.filter( '[value="' + v.value + '"]' ).attr( 'checked', 'checked' );
                } else {
                    $elems.val( v.value );
                }
            }            else if ( $elems.length > 1 ) {
                $elems.filter( '[value="' + v.value + '"]' ).attr( 'checked', 'checked' );
            }
        } );

        this.initTextAlerts();

    }

} );
