/*exported LoginFormView */
/*global
LoginFormView:true
*/
LoginFormView = Backbone.View.extend( {

    //override super-class initialize function
    initialize: function () {
        'use strict';

        this.$el = this.options.$el;
    },

    events: {
        'submit': 'formHandler',
        // intercept any clicks on form buttons
        'click .form-action-btn': 'formActions',
        'click .show-pw': 'showPW',

        'click .sheetOverlay': 'doSheetOverlay'
    },

    formActions: function( e ) {
        this.$el.data( 'trigger', $( e.target ) );
    },

    formHandler: function( e ) {
        var $cont;
        if( this.$el.find( '.modal-body' ).length ) {
            $cont = this.$el.find( '.modal-body' );
        }else{
            $cont = this.$el;
        }

        var $form = this.$el,
            $trigger = $form.data( 'trigger' ),
            method = $form.attr( 'method' ),
            action = $trigger.attr( 'formaction' ) || $form.attr( 'action' ) || $form.data( 'default-action' ),
            $validate = $form.find( '.validateme' ),
            data = $form.serializeArray(),
            extraParams = { responseType: 'html' },
            request,
            qtipOpts = {
                position: {
                    my: 'bottom center',  // Position my top left...
                    at: 'top center', // at the bottom right of...
                    container: $cont,
                    viewport: $cont,
                    adjust: {
                         method: 'shift none'
                    }
                }
            };

        data.push( { name: 'trigger', value: $trigger.val() } );

        $trigger.attr( 'disabled', 'disabled' );

        // create a spinner next to the buttons to indicate that the server is doing something
        G5.util.showSpin( $trigger );

        // if the entire form fails to validate prevent it from continuing

        if( !G5.util.formValidate( $validate, null, { qtipOpts: qtipOpts } ) ) {
            // kill the spinner
            G5.util.hideSpin( $trigger );
            $trigger.removeAttr( 'disabled' );

            return false;
        }

        // assume all forms submit via ajax
        e.preventDefault();

        // otherwise, continue with the ajax submit
        request = $.ajax( {
            url: action,
            type: method,
            data: data,
            dataType: 'g5json'
        } );

        data = _.object( _.pluck( data, 'name' ), _.pluck( data, 'value' ) );
        extraParams = $.extend( {}, extraParams, data );

        request.done( function( data, textStatus, jqXHR ) {
            $trigger.removeAttr( 'disabled' );

            // if the form fails validation on the server, prevent it from doing anything else
            if( !G5.util.formValidateHandleJsonErrors( $form, data.data.messages ) ) {
                // kill the spinner
                G5.util.hideSpin( $trigger );
                return false;
            }
            // if the response contains a serverCommand, let it run and prevent the form from doing anything else
            else if( _.any( data.data.messages, function( message ) { return message.type == 'serverCommand'; } ) ) {
                // kill the spinner
                G5.util.hideSpin( $trigger );

                // if we get a modal back, we hide the current modal and kill the form
                if( _.any( data.data.messages, function( message ) { return message.command == 'modal'; } ) ) {
                    $form.closest( '.modal' ).modal( 'hide' ).on( 'hidden', function() {
                        $form.remove();
                    } );
                    return;
                }
                return false;
            }
            // if the response comes back empty, all is good. Load the next form.
            else if( data.data.messages.length <= 0 ) {
                $.ajax( {
                    url: $form.attr( 'action' ),
                    type: 'POST',
                    data: extraParams,
                    dataType: 'g5html',
                    success: function( data ) {
                        var $newForm = $( data );

                        $form
                            .html( $newForm.html() )
                            .attr( {
                                id: $newForm.attr( 'id' ),
                                action: $newForm.attr( 'action' ),
                                method: $newForm.attr( 'method' ),
                                'class': $newForm.attr( 'class' )
                            } );
                    }
                } );
            }
        } );

        request.fail( function( jqXHR, textStatus, errorThrown ) {
            console.log( '[ERROR] LoginPageView: form submission .ajax failed', jqXHR, textStatus, errorThrown );
        } );

    },

    showPW: function ( e ) {
        var $tar = $( e.target ),
            input = $tar.siblings( 'input' );

        if( input.prop( 'type' ) == 'password' ) {
            input.prop( 'type', 'text' );
            $tar.removeClass( 'icon-eye' ).addClass( 'icon-eye-off' );
        }
        else {
            input.prop( 'type', 'password' );
            $tar.addClass( 'icon-eye' ).removeClass( 'icon-eye-off' );
        }
    },

    doSheetOverlay: function( e ) {
        var $tar = $( e.target ),
            href = $tar.attr( 'href' );

        e.preventDefault();

        G5.util.doSheetOverlay( false, href, $tar.text() );
    }
} );