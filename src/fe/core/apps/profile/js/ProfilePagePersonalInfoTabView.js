/*exported ProfilePagePersonalInfoTabView */
/*global
TemplateManager,
ProfilePagePersonalInfoTabView:true
*/
ProfilePagePersonalInfoTabView = Backbone.View.extend( {

    initialize: function (  ) {
        'use strict';
    },

    activate: function () {
        'use strict';

        this.render();
    },

    render: function () {
        'use strict';

        var that    = this,
            tplName = 'profilePagePersonalInfoTab',
            tplUrl  = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/';
        this.$cont = this.$el;

        // if there is no html in the tab content element, go get the remote contents
        if ( this.$cont.html().length === 0 ) {
            this.$el
                .append( '<span class="spin" />' )
                .find( '.spin' ).spin();

            TemplateManager.get( tplName,
                function ( tpl ) {
                    that.$cont.empty().append( tpl( 0 ) );  //loads a template without any args i.e. a static view
                    that.trigger( 'templateloaded' );
                    that.triggerFetchComments(); // Custom for Coke
                },
                tplUrl );
        } else {
            // otherwise, just trigger the completion event
            this.trigger( 'templateloaded' );
        }

        //trigger the 'reset button'
        $( '#personalInfoTabResetButton' ).trigger( 'click' );
    },

    events: {
        'click #personalInformationButtonSaveAnswers': 'saveAnswers',
        'click #personalInformationUploadImageLink': 'uploadPopUp',
        'click #personalInformationFormUpdateAnswers .resetButton': 'resetValues',
        'click .profile-popover': 'attachParticipantPopover' // Custom for Coke
    },

    uploadPopUp: function( event ) {

        var $tar = $( event.target );
        event.preventDefault();
        // show a qtip
        if ( !$tar.data( 'qtip' ) ) {
            this.addFileUploadTip(
                $tar,
                this.$el.find( '#personalInformationUploadImageForm' )
            );
        }

        // initialize the fileupload widget
        this.uploadPicture();
    },

    //add confirm tooltip
    addFileUploadTip: function( $trig, cont ) {
        //attach qtip and show
        $trig.qtip( {
            content: { text: cont },
            position: {
                my: 'bottom center',
                at: 'top center',
                container: this.$el,
                viewport: $( 'body' ),
                adjust: {
                    method: 'shift none',
                    effect: false
                }
            },
            show: {
                event: 'click',
                ready: true
            },
            hide: {
                event: 'unfocus',
                fixed: true,
                delay: 200
            },
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-light personalInfoUploadImageTip',
                padding: 0,

                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            }
        } );
    },

    uploadPicture: function () {
        'use strict';

        var that = this,
            $form = this.$el.find( '#personalInformationFormUploadImage' );
        /*
            makes use of this plugin: https://github.com/blueimp/jQuery-File-Upload/wiki/Basic-plugin
        */
        $form.fileupload( {
            url: G5.props.URL_JSON_PROFILE_PAGE_PERSONAL_INFORMATION_IMAGE_UPLOAD,
            dataType: 'g5json',
            beforeSend: function() {
                G5.util.showSpin( that.$el.find( '.avatarwrap' ), { cover: true } ); //start the spinner
                that.$el.find( '.ui-tooltip' ).qtip( 'hide' );
            },
            done: function ( e, data ) {
                //error validation
                if ( !G5.util.formValidateHandleJsonErrors( $form, data.result.data.messages[ 0 ] ) ) {
                    return false;
                }


                G5.util.hideSpin( that.$el.find( '.avatarwrap' ), { cover: false } ); //stop the spinner (after the new avatar loads)


                $( '#personalInformationAvatar' ).attr( 'src', data.result.data.properties.avatarUrl );

                that.trigger( 'updateAvatar', data.result.data.properties.avatarUrl );

                $( '#personalInformationUploadImageLink' ).trigger( 'upload' );

            }
        } );
    },

    saveAnswers: function ( e ) {
        'use strict';

        var //that=this,
            $form = $( '#personalInformationFormUpdateAnswers' ),
            $data = $( '#personalInformationFormUpdateAnswers' ).serializeArray();

        // if the entire form fails to validate prevent it from continuing
        if ( !G5.util.formValidate( $form.find( '.validateme' ) ) ) {
            return false;
        }

        e.preventDefault();
            $.ajax( {
                dataType: 'g5json',
                type: 'POST',
                url: G5.props.URL_JSON_PROFILE_PAGE_PERSONAL_INFORMATION_ABOUT_ME,
                action: G5.props.URL_JSON_PROFILE_PAGE_PERSONAL_INFORMATION_ABOUT_ME,
                data: $data,
                success: function ( response ) {
                $form.data( 'savedState', $data );
                    //check for errors
                    if ( !G5.util.formValidateHandleJsonErrors( $form, response.data.messages ) ) {
                        return false;
                    }

                    // //load in updated values to form (for 'reset' purposes)
                    // var $inputs = $('#personalInformationFieldsetAnswers :input.answerField'),
                    //     i = 0;
                    // //for each input, give it a data attribute of what it should be resetting to if
                    // //the 'reset button is pressed.'
                    // $inputs.each(function() {
                    //     $(this).data("ResetValue", $(this).val());
                    // });
                },
                error: function (  ) {
                                            //console.log(a, b, c);
                }
            } );

    },

    resetValues: function( e ) {

        // // Reset form
        // if($('#personalInformationFieldsetAnswers input:first').data('ResetValue')!=undefined) {
        //     // Have saved values, reset to last saved values
        //     $('#personalInformationFieldsetAnswers :input.answerField').each(function() {
        //         $(this).val($(this).data("ResetValue"));
        //     });
        // }
        // else {
        //     // No saved values, reset form
        //     $('#personalInformationFormUpdateAnswers').each (function(){
        //       this.reset();
        //     });
        // }

        // Reset form

        var $form = $( e.target ).closest( 'form' ),
            savedState = $form.data( 'savedState' );
        e.preventDefault();

        $form.find( ':checked' ).removeAttr( 'checked' );


            $form.each( function() {
                this.reset();
            } );

        // if (savedState === undefined){
        //     //console.log('no values exist.');
        //     $form.each(function(){
        //         this.reset();
        //     });
        // } else {
        //     //console.log('values exist.');
        //     //console.log(savedState);
        // }

        _.each( savedState, function( v ) {
            var $elems = $form.find( '[name="' + v.name + '"]' );

            if ( $elems.length === 1 ) {
                $elems.val( v.value );
            }            else if ( $elems.length > 1 ) {
                $elems.filter( '[value="' + v.value + '"]' ).attr( 'checked', 'checked' );
            }
        } );

    },

    // Custom for Coke - Added comments and likesections Starts
    attachParticipantPopover: function ( e ) {
        var $tar = $( e.target ).closest( '.profile-popover' ),
            isSheet = ( $tar.closest( '.modal-stack' ).length > 0 ) ? { isSheet: true } : { isSheet: false };

        isSheet.containerEl = this.$el;

        //attach participant popovers
        if ( !$tar.data( 'participantPopover' ) ) {
            $tar.participantPopover( isSheet ).qtip( 'show' );
        }
        e.preventDefault();
    },

    initVideoJs: function( id ) {
        var idOfVideoElement = id || 'purlDetailVideo';
        // if the video element doesn't exist, we kick out of this method
        if( !$( '#' + idOfVideoElement ).length ) {
            return;
        }
        videojs( '#' + idOfVideoElement );        
    },

    
    renderComments: function( comments ) {
        // this function will capture a valid embeddable youtube/vimeo video URL from your passed-in comment string
        // and call your callback function with that embed URL as an argument
        function captureVideoEmbedUrl( comment, callback ) {
            var urlRegex = new RegExp( /((?:(?:https?):(?:\/\/)(?:www\.)?|www\.)[\w\d:#@%/;$()~_?\+,\-=\\.&]+)/gi ),
                urls = comment.match( urlRegex );

            _.each( G5.util.oEmbedProviders, function( provider ) {
                _.each( provider.detectRe, function( regexStr ) {
                    _.each( urls, function( url ) {
                        ////console.log((url.match(RegExp(regexStr)) && url.match(RegExp(regexStr))[0] ? url.match(RegExp(regexStr))[0] : 'no URL match'));
                        if( url.match( RegExp( regexStr ) ) && url.match( RegExp( regexStr ) )[ 0 ] ) {
                            callback( url.match( RegExp( regexStr ) )[ 0 ] );
                        }
                    } );
                } );
            } );
        }
        var that = this,
            $cont = this.$el.find( '.commentsListWrapper' ),
            tplPath  = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/';

        TemplateManager.get( 'profilePagePersonalInfoComments', function( tpl ) {
            var single;
            // no comments
            if( !comments || !comments.length ) {
                single = {
                    _isEmpty: true
                };
                $cont.empty();
                $cont.append( tpl( single ) );
                return;
            }
            else {
                $cont.find( '.isEmptyMessage' ).remove();
            }

            // clear out containing element
            $cont.empty();
            _.each( comments, function( c ) {
                c = $.extend( true, {}, c ); // clone

                // if videoWebLink already exists, we turn a string into an array
                // this should only come into play if old G5 PURLs with attached video URLs is ported to G6
                if( c.videoWebLink ) {
                    if( typeof c.videoWebLink === 'string' ) {
                        c.videoWebLink = [ c.videoWebLink ];
                    }
                }

                // get embed HTML - will return null if no support for link
                // template will output just the link if no html
                captureVideoEmbedUrl(
                    c.commentText, // pass in comment
                    function( validEmbedUrl ) {
                        // callback receives valid URL back as argument
                        c.videoWebLink ? c.videoWebLink.push( validEmbedUrl ) : c.videoWebLink = [ validEmbedUrl ];
                    }
                );

                // get embed HTML
                if( c.videoWebLink ) {
                    c.videoHtml = [];

                    _.each( c.videoWebLink, function( url ) {
                        c.videoHtml.push( G5.util.oEmbed( url ) );
                    } );
                }

                // append the template
                $cont.append( tpl( c ) );

                // if there is HTML5 video, init VideoJS
                if( $cont.find( '#careerMomentContribVideo' + c.commentId ) ) { that.initVideoJs( 'careerMomentContribVideo' + c.commentId ); }

            } );

            // embed videos -- this *could* be slow
            $cont.find( '.videoLink' ).each( function() {
                var $vl = $( this ),
                    vUrl = $vl.attr( 'href' ),
                    vHtml = G5.util.oEmbed( vUrl );
                $vl.replaceWith( vHtml );
            } );
        }, tplPath );
    },

    triggerFetchComments: function() {
        var that = this,
            params = {
            recipientId: that.$el.find( '#currentPaxId' ).val(),
            // not sure why we are asking the server for this since we are only loading once
            // we can easily sort this on FE
            commentOrderDescending: 'true'
        };

        //this.trigger( 'start:fetchComments' );

        $.ajax( {
            type: 'POST',
            dataType: 'g5json',
            url: G5.props.URL_JSON_CAREERMOMENTS_ACTIVITY_FEED,
            data: { data: JSON.stringify( params ) },
            success: function( serverResp ) {
                var comments = serverResp.data.activityPods;                                
                // trigger the success
                that.renderComments ( comments );
            }
        } ).fail( function( jqXHR, textStatus ) {
            console.log( '[INFO] PersonalInfo.fetchComments - failed: ' + textStatus);
        } );
    }
    // Custom for Coke - Added comments and likesections Ends
} );
