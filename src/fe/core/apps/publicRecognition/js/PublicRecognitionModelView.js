/*jslint browser :  true, nomen :  true, devel :  false, unparam :  true*/
/*exported PublicRecognitionModelView*/
/*global
 $,
 _,
 G5,
 TemplateManager,
 Backbone,
videojs,
 PublicRecognitionModelView:true
 */

//Public Recognition MODEL VIEW

// MODES
// 1) compact style (module and pub rec page)
// 2) details style (detail page - shows ecard etc.)
PublicRecognitionModelView = Backbone.View.extend( {

    tag: 'div',

    className: 'publicRecognitionItem public-recognition-item',

    certificateTemplates: {
        100: {
            name: 'youreAwesome',
            path: './../base/tpl/certificates/',
            orientation: 'portrait'
        },

        101: {
            name: 'youreAmazing',
            path: './../base/tpl/certificates/',
            orientation: 'portrait'
        },

        102: {
            name: 'keyToOurSuccess_1',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        103: {
            name: 'excellence_1',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        104: {
            name: 'peakNomination',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        105: {
            name: 'rockStar_1',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        106: {
            name: 'beBold',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        107: {
            name: 'frontline',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        108: {
            name: 'playToWin',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        109: {
            name: 'resultsMatter',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        110: {
            name: 'rightWay',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        111: {
            name: 'welcomeToTheTeam',
            path: './../base/tpl/certificates/',
            orientation: 'portrait'
        },

        112: {
            name: 'accelerateMyPerformance',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        113: {
            name: 'courageousInspirationalDynamic',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        114: {
            name: 'badAssLeader',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        115: {
            name: 'thankYou',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        116: {
            name: 'wereSoInSync',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        117: {
            name: 'definitionOfLeader',
            path: './../base/tpl/certificates/',
            orientation: 'portrait'
        },

        118: {
            name: 'wayToGo',
            path: './../base/tpl/certificates/',
            orientation: 'portrait'
        },

        151: {
            name: 'keyToOurSuccess_2',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        152: {
            name: 'excellence_2',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        153: {
            name: 'rockStar_2',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        154: {
            name: 'magentaPlaymaker',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        180: {
            name: 'csWeek2015',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        201: {
            name: 'oneTeam',
            path: './../base/tpl/certificates/',
            orientation: 'portrait'
        },

        211: {
            name: 'diversityInclusionAllStar',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        }
    },

    initialize: function ( opts ) {
        this.tplName = opts.tplName || 'publicRecognitionItem';
        this.commentTplName = opts.commentTplName || 'publicRecognitionComment';
        this.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'publicRecognition/tpl/';

        //are we hiding all but the first comment?
        this.isHideComments = opts.isHideComments || false;

        this.isKeepElementOnHide = opts.isKeepElementOnHide || false;

        //is the text being translated?
        this.translatedText = opts.translatedText || false;

        //when model has a comment added
        this.model.on( 'commentAdded', function ( comment ) {
            this.addCommentToDom( comment );
        }, this );
        this.model.on( 'showNewComment', this.doShowComments, this );
        //when model is liked
        this.model.on( 'liked', function ( numLikes ) {
            this.changeDomToLiked( numLikes );
        }, this );

        //when model is hidden
        this.model.on( 'hidden', function ( commentId ) {
            this.updateHidden( commentId );
        }, this );

        this.model.on( 'translated', function ( commentId ) {
            this.updateTranslatedText( commentId );
        }, this );

        //when model has data loaded, render
        this.model.on( 'dataLoaded', function () {
            this.render();
        }, this );

        this.model.on( 'activeBudgetRemainingChanged', this.updateBudgetRemaining, this );

        G5._globalEvents.on( 'windowResized', this.groupAllRecipients, this );


    },

    render: function () {
        var that = this,
            comments = this.model.get( 'comments' ),
            $comCont;

        //did we already render?
        if ( this.rendered ) {
            that.trigger( 'recItemRendered', that.$el );
            return this;
        }

        //RECOGNITION :  render
        TemplateManager.get( this.tplName, function ( tpl, vars, subTpls ) {
            var myModel = that.model.toJSON();

            //extra variables added for the template
            myModel = _.extend( {}, myModel, {
                isMine: myModel.isMine || myModel.recognitionSetNameId === 'mine',
                isHideComments: that.isHideComments,
                recipientsShown: _.first( myModel.recipients, 10 ),
                recipientIdsHidden: _.pluck( _.rest( myModel.recipients, 10 ), 'id' ),
                recognizerShown: _.first( myModel.recognizer, 10 ),
                isPromoTypeNomination: myModel.promotionType === 'nomination', // nomination
                isPromoTypePurl: myModel.promotionType === 'purl', // purl
                isPromoTypeRecognition: !myModel.promotionType || myModel.promotionType === 'recognition' // recognition/normal
            } );

            // adjust the number of likers if this user likes this
            // if (myModel.isLiked) {
            //     myModel.numLikers -= 1;
            // }

            //append the recognition template
            that.$el.append( tpl( myModel ) );

            // attach event listeners to View Certificate links
            that.initCertificateLinks( that.model.get( 'viewCertificateId' ) );

            // hide hidden recipients
            that.initRecipientsList();

            //that.initLikeInfo();

            that.$el.find( 'input,textarea' ).placeholder();

            //get the comment container element
            $comCont = that.$el.find( '.pubRecComments' );

            that.renderBudgets();

            //VIDEO JS
            if( myModel.isDetail ) {
                that.initVideoJs();
            }


            //Grouping
            that.groupMultiRecipients();

            that.groupAllRecipients();

            that.$el.find( '.badgeImg' ).tooltip();


            TemplateManager.get( that.commentTplName, function ( tpl ) {

                _.each( comments, function ( com ) {
                    $comCont.append( tpl( com ) );
                } );

                //if comments hidden, show first, else show all
                if ( that.isHideComments ) {
                    $comCont.find( '.pubRecCommentsComment' ).last().css( 'display', '' );
                } else {
                    $comCont.find( '.pubRecCommentsComment' ).show();
                }

                that.rendered = true;

                // call after render (might not be attached to DOM yet)
                that.initReadMore();


            }, that.tplUrl );//COMMENTS :  end tpl get

        that.trigger( 'recItemRendered', that.$el, myModel.id, myModel.videoUrl );

        }, this.tplUrl );//RECOGNITION :  end tpl get


        G5.util.hideSpin( this.$el.parents( '.publicRecognition' ) );
        return this;//chaining
    },

    renderBudgets: function () {
        var that = this,
            $bs = this.$el.find( 'select.budgetSelect' ),
            budgs = that.model.get( 'budgets' );

        _.each( budgs, function ( b ) {
            $bs.append( that.make( 'option', { value: b.id }, b.name ) );
        } );

        if ( !budgs || budgs.length === 0 ) {
            // hide it -- no budget
            $bs.closest( '.budgetSelectWrapper' ).hide();
            this.$el.find( '.budgetInfo' ).hide();// no budget -- hide budg info
        } else if ( budgs.length === 1 ) {
            // select first & hide
            $bs.val( budgs[ 0 ].id ).change();
            $bs.closest( '.budgetSelectWrapper' ).hide();
        } else {
            // let user select a budget
            this.$el.find( '.budgetInfo' ).hide();// no selection -- hide budg info
        }
    },

    // Cheers Customization - Celebrating you wip-62128 - Added additional cheers 
    // recognition display check to group recipients // Addition 1 and Addition 2
    groupMultiRecipients: function() {
        var $avatar = this.$el.find( '.pubRecAvatarWrap .multiRecipientAvatar' ),
            $recpName = this.$el.find( '.comment-top .pubRecRecipient' ),            
            $cheersRecpName = this.$el.find( '.cheersRecognition .pubRecRecipient' ),  // Addition 1          
            that = this;

        _.each( this.$el, function( item ) {

            //avatars
            if( $( item ).find( $avatar ).length > 6 ) {
                var oldAvatarCount = $( item ).find( $avatar ).length,
                    newAvatarCount;

                //slice after 5 if more than 6 and hide the rest to use later
                $( item ).find( $avatar ).slice( 5 ).addClass( 'hide' );

                newAvatarCount = oldAvatarCount - 5;

                $( item )
                    .find( $avatar ).not( '.hide' ).last()
                    .after( '<div class="avatarwrap multiRecipientAvatar multiRecipientAvatarCount"><a class="viewOtherRecipients"><span class="avatar-initials">+' + newAvatarCount + '</span></a></div>' );

                //find hidden and create popover list
                _.each( $( item ).find( $avatar ).slice( 5 ), function( ha ) {

                    $( ha ).addClass( 'hiddenAvatar' );
                    var $li = $( '<li />' ).text( $( ha ).find( '[data-name]' ).data( 'name' ) );
                    //var $li = $('<li />').text($(ha).find('span').data('name'));

                    that.$el.find( '.viewOthersPopover ul' ).append( $li );
                } );

            }

            //names
            if( $( item ).find( $recpName ).length > 3 ) {
                var oldRecpCount = $( item ).find( $recpName ).length,
                    newRecpCount;

                $( item ).find( $recpName ).slice( 3 ).remove();

                newRecpCount = oldRecpCount - $( item ).find( $recpName ).length;

                $( item ).find( '.othersWrap' ).show();
                $( item ).find( '.othersCount' ).text( newRecpCount );
            }

            //cheers names check  
            /* Addition 2 */
            if( $( item ).find( $cheersRecpName ).length > 3 ) {
                var oldRecpCount = $( item ).find( $cheersRecpName ).length,
                    newRecpCount;

                $( item ).find( $cheersRecpName ).slice( 3 ).appendTo($( item ).find( '.othersWrap' ));

                newRecpCount = oldRecpCount - $( item ).find( '.othersWrap .pubRecRecipient' ).length;

                $( item ).find( '.othersCountWrap' ).show();
                $( item ).find( '.othersCount' ).text( newRecpCount ).show();
            }

        } );
        console.log( 'groupMultiRecipients' );
    },

    groupAllRecipients: function() {
        console.log( 'groupAllRecipients' );
        var $avatar = this.$el.find( '.pubRecAvatarWrap .multiRecipientAvatar' );

        if( G5.breakpoint.value == 'mini' || G5.breakpoint.value == 'mobile' || G5.breakpoint.value == 'tablet' ) {
            _.each( this.$el, function( item ) {
                var $allRecip = $( item ).find( '.allRecipAvatar' );

                if( $( item ).find( '.viewOtherRecipients' ).length ) {
                    $allRecip.find( 'span' ).text( $avatar.length - 1 );
                } else {
                    $allRecip.find( 'span' ).text( $avatar.length );
                }

                $( item ).find( $avatar ).addClass( 'hide' );
                    //.parent( '.profile-popover' )
                    //.hide();

            } );
        } else {
            _.each( this.$el, function( item ) {

                $( item ).find( $avatar ).not( '.hiddenAvatar' ).removeClass( 'hide' );

                //$( item ).find( '.hiddenAvatar' ).addClass( 'hide' );
            } );
        }
    },

    initCertificateLinks: function ( certificateId ) {
        // mocking out for now
        certificateId = certificateId ? certificateId : 114;

        var targetTpl = this.certificateTemplates[ String( certificateId ) ];

        // delegation
        this.$el.on( 'click', '.certificateUrl', _.bind( function ( e ) {
            e.preventDefault();

            // If IE8, use createCertificate() formPost method as fallback and do an early return
            if( $( 'html' ).hasClass( 'lt-ie9' ) ) {
                this.createCertificate(
                    targetTpl,
                    e.target.getAttribute( 'data-recipientId' ),
                    'formPost',
                    $( '#pdfForm' )
                );
                return; // early return for early exit
            }

            var certModal = $( '.certificateModal' );
            var modalBody = certModal.find( '.modal-body' );
            var progress = certModal.find( '.progress-indicator' );

            certModal.modal( 'show' );

            certModal.on( 'hidden', function() {
                if( !modalBody.hasClass( 'loading' ) ) {
                    modalBody.addClass( 'loading' );
                }
            } );

            G5.util.showSpin( progress );

            this.createCertificate(
                targetTpl,
                e.target.getAttribute( 'data-recipientId' ),
                'ajaxPost',
                certModal
            );

            // fallback for browsers without adobe reader (non-compatible w/ object/embed implementation)
            this.initPdfEmbedFallback( certModal, targetTpl );

        }, this ) );
    },

    createCertificate: function( certificateTpl, recipientId, method, $el ) {
        var originalSuffix = G5.props.TMPL_SFFX;

        if( originalSuffix !== '.html' ) {
            G5.props.TMPL_SFFX = '.html';
        }

        TemplateManager.get(
            certificateTpl.name,
            _.bind( function( tpl ) {
                G5.props.TMPL_SFFX = originalSuffix;

                var htmlString = tpl(
                    // this.model.attributes.certificates is an object containing key/value pairs where keyNames correspond
                    // to recipient IDs. Values are JS object representations of data intended for template interpolation
                    this.model.get( 'certificates' )[ recipientId ]
                );

                if( method === 'ajaxPost' ) {
                    this.requestPdfWithAjax( certificateTpl, $el, htmlString, recipientId );
                }

                if( method === 'formPost' ) {
                    this.requestPdfWithFormPost( certificateTpl, $el, htmlString );
                }
            }, this ),
            ( G5.props.URL_CERT_TPL_ROOT || certificateTpl.path )
        );
    },

    requestPdfWithAjax: function( certificateTpl, $el, htmlString, recipientId ) {
        $.ajax( G5.props.URL_PDF_SERVICE || 'http://10.0.2.2:3033/pdf', {
            contentType: 'application/json',
            dataType: 'text',
            type: 'POST',
            processData: false,
            data: JSON.stringify( {
                html: htmlString,
                orientation: certificateTpl.orientation,
                convertToBase64: true // updated pdf service to conditionally respond with base64 string of PDF if POST body includes this property set to true (bool)
            } ),
            success: function ( resp ) {
                TemplateManager.get(
                    'embed',
                    _.bind( function ( tpl ) {
                        if( certificateTpl.orientation === 'landscape' ) {
                            $el.addClass( 'landscape' );
                        }

                        $el.find( '.pdf-wrapper' ).html(
                            tpl( {
                                pdfSrc: resp, // resp is base64 encoded string, which we set to pdfSrc for a data URI solution
                                recipientId: recipientId // pass to embed tpl for embed-incompatible fallback
                            } )
                        );

                        $el.find( '.modal-body' ).removeClass( 'loading' );

                    }, this ),
                    ( G5.props.URL_CERT_TPL_ROOT || './../base/tpl/certificates/' )
                );
            }
        } );
    },

    requestPdfWithFormPost: function ( certificateTpl, $el, htmlString ) {
        $el.find( '.html' ).val( htmlString );
        $el.find( '.orientation' ).val( certificateTpl.orientation );
        $el.attr( 'action', G5.props.URL_PDF_SERVICE || 'http://10.0.2.2:3033/pdf' );
        $el.submit();
    },

    initPdfEmbedFallback: function ( $el, certificateTpl ) {
        $el.on( 'click', '.pdf-embed-fallback', _.bind( function( e ) {
            console.error( 'pdf fallback' );
            e.preventDefault();
            this.createCertificate(
                certificateTpl,
                e.target.getAttribute( 'data-recipientId' ),
                'formPost',
                $( '#pdfForm' )
            );
        }, this ) );
    },

    initRecipientsList: function () {
        var recipientsToShow = 10,
            $container = this.$el.find( '.recipientsContainer' ),
            $showHidden = this.$el.find( '.showHiddenWrap' ),
            $hiddenRecipients = this.$el.find( '.hiddenRecipients' );

        if ( recipientsToShow < this.model.get( 'recipients' ).length ) {
            $hiddenRecipients.hide();
            $container.find( '.recipientWrap:gt(' + ( recipientsToShow - 1 ) + ')' ).appendTo( $container.find( '.hiddenRecipients' ) );
        }
        else {
            $showHidden.hide();
        }
    },
    // smacktalk in throwdown is the only thing I see with .likesentence ? is this used?
    // initLikeInfo: function () {
    //     var that = this,
    //         $sen = this.$el.find( '.likeSentence' ),
    //         lo = '<a class="profile-popover" href="#" data-recognition-id="'
    //             + this.model.get( 'id' ) + '" data-participant-info-type="likers" >',
    //         lc = '</a>',
    //         no = '<span class="likeCount">',
    //         nc = '</span>',
    //         numLikers = that.model.get( 'numLikers' );

    //     numLikers = this.model.get( 'isLiked' ) ? numLikers - 1 : numLikers;

    //     //fill in sentence markup
    //     $sen.each( function () {
    //         var $t = $( this ), num;
    //         // number of likers
    //         $t.html( $t.html().replace( '{0}', numLikers ) );

    //         num = $t.html().match( /\d+/ ); // find the number
    //         num = num && num.length ? num[ 0 ] : null; // pull from array

    //         if ( num ) { // if we have a number
    //             $t.html( $t.html().replace( num, no + num + nc ) );
    //         }

    //         $t.html( $t.html().replace( '{link}', lo ) );
    //         $t.html( $t.html().replace( '{/link}', lc ) );
    //         console.log( $t );
    //     } );
    // },

    // this must be called after render AND attachment to account
    // for inconsistent data-load+render VS DOM attachment
    initReadMore: function () {
        var $rm = this.$el.find( '.readMore' ),
            newHeight;

        // must be attached, and rendered
        if ( !this.$el.closest( 'body' ) || !this.rendered ) {
            return;
        }

        $rm.each( function () {
            var $t = $( this ),
                numLines = $t.data( 'readMoreNumLines' ) || 2,
                rmTxt = $t.data( 'msgReadMore' ) || 'more';

            newHeight = numLines * parseInt( $t.css( 'line-height' ), 10 );
            // only give read more option when necessary
            if ( $t.height() > newHeight ) {
                $t.css( 'height', newHeight ).addClass( 'moreToRead' );

                if ( !$t.find( '.readMoreTrigger' ).length ) {
                    $t.append( $( '<a class="readMoreTrigger" />' ).html( '&hellip;' + rmTxt ) );
                }
            }

        } );
    },

    doReadMore: function ( e ) {
        var $tar = $( e.target ),
            $rm = $tar.closest( '.readMore' );

        e.preventDefault();

        $rm.css( 'height', '' ).removeClass( 'moreToRead' ).data( '_readMoreClicked', true );

        // ie8 JQ1.8 event.target.remove() fix (defer remove() call)
        setTimeout( function () {
            $tar.remove();
        }, 0 );
    },

    initVideoJs: function (videoId) {
    	var idOfVideoElement = ( videoId ) ? 'recognitionVideo_' + videoId : 'recognitionVideo',
                videoElement = $( 'body' ).find( '#' + idOfVideoElement );

            // if the video element doesn't exist, we kick out of this method
            if ( !videoElement.length ) {
                return;
            }

            videojs( '#' + idOfVideoElement, {}, function() {
                // Logic to be done after player is initialized
            } );
        // // otherwise, initialize the videojs
        // if ( _V_ ) { // global reference to videojs lib
        //     _V_( idOfVideoElement ).ready( function () {
        //         var player = this,
        //             aspRat = 9 / 16, // aspect ratio
        //         // resize callback
        //             onResize = function () {
        //                 var w = document.getElementById( player.id_ ).parentElement.offsetWidth;
        //                 player.width( w ).height( w * aspRat );
        //             };

        //         // initial call to resize function
        //         onResize();
        //         // bind to window resize event
        //         window.onresize = onResize;
        //     } );
        // }
    },

    //EVENTS
    events: {
        'click .viewAllCommentsBtn': 'doShowComments',
        'click .profile-popover': 'attachParticipantPopover',
        'click .showHidden': 'doShowAllRecipients',
        'click .flag': 'attachFlagPopover',
        'click .readMoreTrigger': 'doReadMore',
        'click .viewOtherRecipients': 'doViewOthers',
        'click .viewAllRecip': 'doViewAllRecip',

        //comment form
        'click .showCommentFormBtn': 'showCommentInput',
        'keydown .commentInputTxt': 'doCommentKeydown',
        //add points form
        'click .showAddPointsFormBtn': 'showAddPointsForm',
        'focus .pointsInputTxt': 'attachPointsInputPO',
        'keydown .pointsInputTxt': 'doPointsKeydown', //for enter intercept
        'keyup .pointsInputTxt': 'doPointsKeyup', //for filter and update budget
        'keydown .addPointsCommentInputTxt': 'doAddPointsCommentKeydown', //intercept for apropo
        'hover .budgetPopoverTrigger': 'showBudgetPopover',
        'click .budgetPopoverTrigger': 'showBudgetPopover', //Implements prevent default as expected on click of the budget popover.
        'click .addPointsBtn': 'submitAddPointsForm',
        'change .budgetSelect': 'doBudgetSelectChange',
        //translate text
        'click .translatePubRec': 'doTranslate',

        //area.maxlength shim for opera and ie
        'keyup .comment-input': 'enforceMaxLength',
        'blur .comment-input': 'enforceMaxLength',
        'mouseup .comment-input': 'enforceMaxLength',

        'click .likePubRecBtn': 'doLike',

        'click .sharePubRecBtn': function ( e ) {
            $( e.target ).sharePopover( {}, this.model.get( 'shareLinks' ) );
            e.preventDefault();
        },

        //detail view stuff
        'click .hidePublicRecognitionLnk': 'hidePublicRecognition',
        'click .publicRecognitionHideRecognitionConfirm': 'doHide',
        'click .publicRecognitionHideRecognitionCancel': 'hideHideConfirmTip',
        'click .deletePublicRecognitionLnk': 'deletePublicRecognition',
        'click .publicRecognitionDeleteRecognitionConfirm': 'doDelete',
        'click .publicRecognitionDeleteRecognitionCancel': 'hideDeleteConfirmTip',

        // Cheers Customization - Celebrating you wip-62128 - starts
        'click .cheers-popover': 'attachCheersPopover',
        'click .othersCountWrap': 'attachCheersPaxPopover'
        // Cheers Customization - Celebrating you wip-62128 - ends
    },

    enforceMaxLength: function ( e ) {
        var $tar = $( e.target ),
            ml = $tar.attr( 'maxlength' );
        // only for IE and textareas with maxlength attrs
        if ( !$.browser.msie || !ml ) {
            return;
        }

        if ( $tar.val().length > ml ) {
            $tar.val( $tar.val().slice( 0, ml ) );
        }
    },

    doShowComments: function ( e ) {
        var $comCont = this.$el.find( '.pubRecComments' ),
            i = 0;

        e.preventDefault();

        if( $comCont.children().length > 0 || e.showAnyway === true ) {

            $comCont.show();

            $comCont.find( '.pubRecCommentsComment' ).each( function () {
                var $cmt = $( this );
                //delayed fade in effect
                setTimeout( function () {
                    $cmt.fadeIn();
                }, i * 100 );
                i++;
            } );
        }
        //hide the view all link now that everything is visible
        // $tar.closest('.viewAllCommentsWrapper').hide();


    },

    doShowAllRecipients: function ( e ) {
        e.preventDefault();

        var $showHidden = $( e.target ).closest( '.showHiddenWrap' ),
            $hiddenRecipients = this.$el.find( '.hiddenRecipients' );

        $hiddenRecipients.slideDown( G5.props.ANIMATION_DURATION );
        $showHidden.slideUp( G5.props.ANIMATION_DURATION );
    },

    doCommentKeydown: function ( e ) {
        var $inp,
            $cmtForm = this.$el.find( '.publicRecognitionCommentForm' ),
            jsonUrl = $cmtForm.attr( 'action' ),
            params = {};

        //put the form elements in an object
        _.each( $cmtForm.serializeArray(), function ( input ) {
            params[ input.name ] = input.value;
        } );

        if ( e.which === 13 ) {
            e.preventDefault();
            params[ 'e' ] = e;
            $inp = $( e.target );
            // clear whitespace
            $inp.val( $inp.val().replace( /^\s+$/, '' ) );
            // enforce maxlength for ie
            this.enforceMaxLength( e );

            if ( $inp.val().length > 0 ) {

                $inp.parent().spin();

                this.model.saveComment( params, $inp.val(), jsonUrl, function ( cmt ) {
                    // console.log(e);

                    $inp.val( '' ).parent().spin( false );

                } ); // saveComment
            } // if it's not empty

        } // if keydown is enter

    }, // doCommentKeydown

    addCommentToDom: function ( comment ) {
        var that = this,
            $cmtCount = this.$el.find( '.commentCount' ),
            numComments = this.model.get( 'comments' ).length;
        //update comment count
        $cmtCount.text( numComments );

         // this should update the text as well like vs likes? for likeCountStatus
        if( numComments === 1 ) {
            // set to like
            this.$el.find( '.commentCountStatus .badge' ).text( numComments );
        }else{
            // set to likes
            this.$el.find( '.commentCountStatus .badge' ).text( numComments  );
        }

        TemplateManager.get( this.commentTplName, function ( tpl ) {
            var $cmt = $( tpl( comment ) ),
                $cc = $cmt.closest( '.pubRecCommentsComment' ),
                origBg, dkBg;

            //attach the comment template to the DOM
            that.$el.find( '.pubRecComments' ).append( $cmt );

            //get the bg colors from css
            origBg = $cc.css( 'background-color' );
            dkBg = $cc.addClass( 'darken' ).css( 'background-color' );
            $cc.removeClass( 'darken' );

            //effects to show user where new comment is
            $cc.css( 'background', dkBg ).show()
                .animate( { backgroundColor: origBg }, 1000 );

            //scroll to new message
            //targeting inner elements that's not a display : table
            //or if page then just call $.scrollTo()
            setTimeout( function() {
                ( that.isPageView() ? $ : $cmt.closest( '.pubRecItemsCont' ) ).scrollTo( $cmt.find( '.app-col' ).get( 0 ), 400 );
            }, 100 );



        }, that.tplUrl );
    },

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

    //attach flag popover
    attachFlagPopover: function ( e ) {
        var $tar = $( e.currentTarget ),
            that = this;

        // lazy attach -- much more efficient
        if ( !$tar.data( 'qtip' ) ) {
            // attach country tooltip
            $tar.qtip( {
                content: { text: $tar.attr( 'title' ) },
                position: {
                    my: 'bottom center',
                    at: 'top center',
                    container: that.$el
                },
                show: { event: 'click', ready: true },
                hide: {
                    event: 'unfocus',
                    fixed: true,
                    delay: 200
                },
                style: { classes: 'ui-tooltip-shadow ui-tooltip-light' }
            } );
        }
    },

    // Cheers Customization - Celebrating you wip-62128 - starts 
    attachCheersPaxPopover: function ( e ) {
        var $tar = $( e.currentTarget ),
            $content = $tar.prev(),
            that = this;

        // lazy attach -- much more efficient
        if ( !$tar.data( 'qtip' ) ) {
            // attach country tooltip
            $tar.qtip( {
                content: { text: $content },  
                position: {
                    my: 'top center',
                    at: 'center center',
                    container: $('body'),
                    effect: false,
                    viewport: $( 'body' ),
                    adjust: {
                        method: 'shift shift'
                    }
                },
                show: { event: 'click', ready: true },
                hide: {
                    event: 'unfocus',
                    fixed: true,
                    delay: 200
                },
                style: { classes: 'ui-tooltip-shadow ui-tooltip-light cheersPaxQtip' }
            } );
        }
    },

    attachCheersPopover: function( e ) {
        var $tar = $( e.target ),
            $paxId = $tar.data( 'participantIds' ),
            $promoId = $tar.data( 'cheersPromotionId' );

        //attach participant popovers
        if ( !$tar.data( 'cheersPopover' ) ) {
        	$tar.cheersPopover( { containerEl: this.$el, recipientId: $paxId, promotionId : $promoId, canReload : true } ).qtip( 'show' );
        }
        e.preventDefault();
    },
    // Cheers Customization - Celebrating you wip-62128 - ends

    showCommentInput: function ( e ) {
        var that = this;
        e.preventDefault();

        this.$el.find( '.addPointsWrapper' ).slideUp();

        //slide down the input
        this.$el.find( '.commentInputWrapper' ).slideDown(
            function () {
                var $this = $( this ); // .commentInputWrapper
                var $container = that.isPageView() ? $( window ) : $this.closest( '.pubRecItemsCont' );

                // elastic plugin lazy attach
                // if (!that.comInpElasticAttached) { // plugin is too stupid to keep track
                //     that.$el.find('.comment-input').elastic();
                //     that.comInpElasticAttached = true;
                // }

                // scroll to the input and focus
                setTimeout( function() {

                    $container.scrollTo(
                        $this,
                        {
                            offset: -( $container.outerHeight() - $this.outerHeight() - 8 ),
                            duration: 500,
                            onAfter: function () {
                                $this.find( '.commentInputTxt' ).focus();
                            }
                        }
                    );
                }, 100 );

            }
        );
    },

    doLike: function ( e ) {
        var $tar = $( e.target ).closest( 'a' );

        e.preventDefault();

        $tar.toggleClass( 'liked' );

        $tar.toggleClass( 'mylike' );

        if ( $tar.hasClass( 'liked' ) ) {
            this.model.saveLike();
        } else {
            this.model.saveUnlike();
        }


    },

    changeDomToLiked: function ( numLikes ) {
        var $likeBtn = this.$el.find( '.likePubRecBtn' ),
            unlike = $likeBtn.data( 'unlike' ),
            like = $likeBtn.data( 'like' ),
            m = this.model.toJSON();
        
        if ( m.isLiked ) {
            $likeBtn.contents().last()[ 0 ].textContent = unlike;
        } else {
            $likeBtn.contents().last()[ 0 ].textContent = like;
        }
        
        // this should update the text as well like vs likes? for likeCountStatus
        if( numLikes === 1 ) { 
            // set to like
            this.$el.find( '.likeCountStatus .badge' ).text( numLikes  );
        }else{ 
            // set to likes
            this.$el.find( '.likeCountStatus .badge' ).text( numLikes  );
        }
        this.$el.find( '.likeCount' ).text( numLikes );


    },


    doViewOthers: function( e ) {
        var that = this,
            $trig = $( e.target );

        if ( !$trig.data( 'qtip' ) ) {
            //point budget tooltip
            $trig.qtip( {
                content: that.$el.find( '.viewOthersPopover' ).clone(),
                position: {
                    my: 'bottom center',
                    at: 'top center',
                    container: that.$el
                },
                show: { event: 'click' },
                hide: { event: 'mouseleave', delay: 300 },
                style: { classes: 'ui-tooltip-shadow ui-tooltip-light' }
            } ).qtip( 'show' );
        }
        e.preventDefault();
    },

    doViewAllRecip: function( e ) {
        var that = this,
            $trig = $( e.target );

        if ( !$trig.data( 'qtip' ) ) {
            //point budget tooltip
            $trig.qtip( {
                content: that.$el.find( '.viewAllRecipPopover' ),
                position: {
                    my: 'bottom center',
                    at: 'top center',
                    container: that.$el
                },
                show: { event: 'click' },
                hide: { event: 'mouseleave', delay: 300 },
                style: { classes: 'ui-tooltip-shadow ui-tooltip-light' }
            } ).qtip( 'show' );
        }
        e.preventDefault();
    },

    //POINTS
    showAddPointsForm: function ( e ) {
        var that = this;
        e.preventDefault();

        this.updateBudgetInfo();

        this.$el.find( '.commentInputWrapper' ).slideUp();

        // make sure the add points form is visible and the success message is hidden
        this.$el.find( '.addPointsWrapper .publicRecognitionAddPointsForm' ).show();
        this.$el.find( '.addPointsWrapper .msgAddPointsSuccess' ).hide();

        //slide down the input
        this.$el.find( '.addPointsWrapper' ).slideDown(
            G5.props.ANIMATION_DURATION,
            function () {
                var $this = $( this );

                //scroll to the input and focus
                ( that.isPageView() ? $ : $this.closest( '.pubRecItemsCont' ) )
                    .scrollTo( $this, {
                        duration: G5.props.ANIMATION_DURATION,
                        offset: { top: -40, left: 0 }, // give some room on top
                        onAfter: function () {
                            var $s = $this.find( '.budgetSelect:visible' ),
                                $pit = $this.find( '.pointsInputTxt' ),
                                $cmt = $this.find( '.addPointsCommentInputTxt' );
                            if ( $s.length ) {
                                $s.focus();
                            }
                            else {
                                if ( $pit.attr( 'readonly' ) ) {
                                    $cmt.focus();
                                }
                                else {
                                    $pit.focus();
                                }
                            }
                        }
                    } );
            }
        );
    },

    doPointsKeyup: function ( e ) {
        //$tar.val($tar.val().replace(/[^0-9]/g,''));
        this.updateBudgetInfo();
    },

    doBudgetSelectChange: function ( e ) {
        var $tar = $( e.target );

        this.$el.find( '.budgetInfo' )[ $tar.val() == -1 ? 'hide' : 'show' ]();

        this.model.setActiveBudget( $tar.val() );
        this.updateBudgetInfo();
    },

    doPointsKeydown: function ( e ) {
        var $cmtInp = this.$el.find( '.addPointsCommentInputTxt' );

        //filter non-numerics
        this.filterNonNumericKeydown( e );

        //if enter press, focus on comment field
        if ( e.which === 13 ) {

            //kill enter event, otherwise a newline appears in comment
            //or it tries to do normal form submit
            e.preventDefault();

            //no comments? go to comments
            if ( $cmtInp.val().length === 0 ) {
                this.$el.find( '.addPointsCommentInputTxt' ).focus();
            }
            //i guess we can submit
            else {
                //this.submitAddPointsForm();
                this.$el.find( '.addPointsBtn' ).focus().click();
            }
        }
    },

    doAddPointsCommentKeydown: function ( e ) {
        if ( e.which === 13 ) {
            e.preventDefault();

            // enforce maxlength for ie
            this.enforceMaxLength( e );

            //this.submitAddPointsForm();
            this.$el.find( '.addPointsBtn' ).focus().click();
        }
    },

    showBudgetPopover: function ( e ) {
        var that = this,
            $trig = that.$el.find( '.budgetPopoverTrigger' );

        if ( !$trig.data( 'qtip' ) ) {
            //point budget tooltip
            $trig.qtip( {
                content: that.$el.find( '.budgetPopover' ),
                position: { my: 'bottom center', at: 'top center' },
                show: { event: 'mouseenter' },
                hide: { event: 'mouseleave', delay: 300 },
                style: { classes: 'ui-tooltip-shadow ui-tooltip-light' }
            } ).qtip( 'show' );
        }
        e.preventDefault();
    },

    updateBudgetInfo: function () {
        var $budgPOTrig = this.$el.find( '.budgetPopoverTrigger' ),
        //find the popover element, it might not yet be attached to trigger
            $budgPO = $budgPOTrig.data( 'qtip' ) ?
                $budgPOTrig.data( 'qtip' ).elements.tooltip : this.$el.find( '.budgetPopover' ),
            $POavail = $budgPO.find( '.budgetAvailable' ),
            $POded = $budgPO.find( '.budgetDeduction' ),
            $POrem = $budgPO.find( '.budgetRemaining' ),
            $pts = this.$el.find( '.pointsInputTxt' ),
            $budg = this.$el.find( '.budgetRemaining' ),
            p = parseInt( $pts.val(), 10 ), //points
            bObj = this.model.getActiveBudget(), //budget obj
            b = parseInt( bObj ? bObj.remaining : 0, 10 ), //budget
            r = parseFloat( this.model.get( 'countryRatio' ) ), //ratio
            calcDed = Math.floor( p * r ),
            rem = Math.floor( b - ( p * r ) ); //new remaining budget
        //if NaN, then nothing in input field
        rem = _.isNaN( rem ) ? b : rem;
        calcDed = _.isNaN( calcDed ) ? 0 : calcDed;

        $POavail.animateNumber( b, 500, { addCommas: false } );
        $POded.animateNumber( calcDed, 500, { addCommas: false } );
        $POrem.animateNumber( rem, 500, { addCommas: false } );
        $budg.animateNumber( rem, 500, { addCommas: false } );
    },

    submitAddPointsForm: function ( e ) {
        if ( e ) {
            e.preventDefault();
        }
        var $form = this.$el.find( '.publicRecognitionAddPointsForm' ),
            $btn = this.$el.find( '.addPointsBtn' ),
            $pts = this.$el.find( '.pointsInputTxt' ),
            $cmt = this.$el.find( '.addPointsCommentInputTxt' ),
            min = this.model.get( 'awardAmountMin' ),
            max = this.model.get( 'awardAmountMax' ),
            budg = this.model.getActiveBudget(),
            p = parseInt( $pts.val(), 10 ), //points
            b = parseInt( budg ? budg.remaining : 0, 10 ), //budget
            r = parseFloat( this.model.get( 'countryRatio' ) ), //ratio
            rem = Math.floor( b - ( p * r ) ), //remaining budget
            hasBudg = this.model.get( 'budgets' ) &&
                this.model.get( 'budgets' ).length > 0, //do we have a budget?

            that = this;
        //console.log(p,b,r,calcDed,rem,hasBudg,budg,$btn.data());
        //console.log('hasBudg',hasBudg);

        $cmt.val( $cmt.val().replace( /^\s+$/, '' ) );

        //ERROR CASES
        // if has budgets, but none selected
        if ( hasBudg && !budg ) {
            this.showSubmitPointsError( $btn.data( 'msgErrNoBudgSel' ) );
        }
        else if ( _.isNaN( p ) || p === 0 ) {
            this.showSubmitPointsError( $btn.data( 'msgErrNoPoints' ) );
        }
        else if ( $cmt.val().length < 2 ) {
            this.showSubmitPointsError( $btn.data( 'msgErrNoComment' ) );
        }
        else if ( p < min || p > max ) {
            this.showSubmitPointsError( $btn.data( 'msgErrOutOfRange' ) );
        }
        // if has budget and hard cap
        else if ( rem < 0 && hasBudg && !budg.isSoftCap ) {
            this.showSubmitPointsError( $btn.data( 'msgErrOverBudget' ) );
        }

        //success case
        else {//OK - submit!


            //Disable Submit button
            $btn.attr( 'disabled', 'disabled' ).spin();
            $.ajax( {
                dataType: 'g5json',
                type: 'POST',
                url: $form.attr( 'action' ) || G5.props.URL_JSON_PUBLIC_RECOGNITION_ADD_POINTS,
                data: $form.serialize(),
                success: function ( servResp ) {
                    var err = servResp.getFirstError(),
                        data = servResp.data,
                        $ptsWrap = that.$el.find( '.addPointsWrapper' );

                    $btn.removeAttr( 'disabled' ).spin( false );

                    if ( err ) {
                        that.showSubmitPointsError( err.text );
                    }
                    //ok, hide add points
                    else {
                        //if comment returned from server, then add to model
                        if ( data.comment ) {that.model.addComment( data.comment, e );}
                        // resetting points stuff
                        $ptsWrap.find( '.publicRecognitionAddPointsForm' ).hide();
                        $pts.val( $pts.attr( 'readonly' ) ? $pts.val() : '' );
                        $cmt.val( '' );
                        $ptsWrap.find( '.msgAddPointsSuccess' ).show();
                        //get All Page Views
                        that.model.setActiveBudgetRemaining( rem );
                    }
                }
            } );
        }
    },

    showSubmitPointsError: function ( msg ) {
        var $btn = this.$el.find( '.addPointsBtn' );

        //if old qtip still visible, obliterate!
        if ( $btn.data( 'qtip' ) ) {$btn.qtip( 'destroy' );}

        $btn.qtip( {
            content: '<i class="icon icon-warning-triangle"></i> ' + msg,
            position: { my: 'bottom center', at: 'top center' },
            show: { ready: true },
            hide: { event: 'mouseleave', delay: 500 },
            //only show the qtip once
            events: {
                hide: function ( evt, api ) {
                    $btn.qtip( 'destroy' );
                }
            },
            style: { classes: 'ui-tooltip-shadow ui-tooltip-red' }
        } );
    },

    attachPointsInputPO: function ( e ) {
        //point input tooltip
        $( e.target ).qtip( {
            content: function () {
                return $( this ).data( 'tooltip' );
            },
            position: {
                my: 'bottom center', at: 'top center',
                container: this.$el.closest( '.pubRecItemsCont' )
            },
            show: { event: 'focus', ready: true },
            hide: { event: 'blur', delay: 300 }
        } );
    },

    //the 'hide' link
    hidePublicRecognition: function ( e ) {
        e.preventDefault();
        var $tar = $( e.target ),
            commentId = $tar.parents( '.comment-block' ).data( 'commentId' );

        $tar.removeClass( 'clickedHide' );

        if ( !$tar.data( 'qtip' ) ) {
            this.addConfirmTip(
                $tar,
                this.$el.find( '.hidePublicRecognitionQTip' ).eq( 0 ).clone( true ),
                commentId
            );
        }

        $tar.addClass( 'clickedHide' );
    },

    //the 'delete' link
    deletePublicRecognition: function ( e ) {
        e.preventDefault();
        var $tar = $( e.target ),
            commentId = $tar.parents( '.comment-block' ).data( 'commentId' );

        $tar.removeClass( 'clickedHide' );

        if ( !$tar.data( 'qtip' ) ) {
            this.addConfirmTip(
                $tar,
                this.$el.find( '.deletePublicRecognitionQTip' ).eq( 0 ).clone( true ),
                commentId
            );
        }

        $tar.addClass( 'clickedHide' );
    },

    //add confirm tooltip
    addConfirmTip: function ( $trig, cont, commentId ) {

        // if a comment was removed, add the commentId to the qTip element so we can use it later
        if ( commentId ) {
            cont.data( 'commentId', commentId );
        }

        //attach qtip and show
        $trig.qtip( {
            content: { text: cont },
            position: {
                my: 'bottom right',
                at: 'top right',
                container: this.$el,
                viewport: $( 'body' ),
				adjust: {
					method: 'shift none'
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
            events: {
                show: function () {
                    // if (that.$el.closest('.launchModule').length) {
                    //     that.$el.closest('.launchModule').css('overflow', 'visible');
                    //     $(this).data('modelView', that);
                    // }
                },
                hide: function () {
                    // if (that.$el.closest('.launchModule').length) {
                    //     that.$el.closest('.launchModule').css('overflow', '');
                    // }
                }
            },
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-light',
                tip: {
                    corner: true,
                    width: 10,
                    height: 10
                }
            }
        } );
    },

    doHide: function ( e ) {
        // get the commentId if a comment was removed
        var commentId = $( '.hidePublicRecognitionQTip:visible' ).data( 'commentId' );

        // remove the commentId data so it does not interfere with future hide/remove requests
        $( '.hidePublicRecognitionQTip:visible' ).removeData( 'commentId' );

        e && e.preventDefault();
        this.hideHideConfirmTip();
        this.model.saveHide( commentId );
    },

    doDelete: function ( e ) {
        // get the commentId if a comment was removed
        var commentId = $( '.deletePublicRecognitionQTip:visible' ).data( 'commentId' );

        // remove the commentId data so it does not interfere with future hide/remove requests
        $( '.deletePublicRecognitionQTip:visible' ).removeData( 'commentId' );

        e && e.preventDefault();
        this.hideDeleteConfirmTip();
        this.model.saveHide( commentId );
    },

    hideHideConfirmTip: function ( e ) {
        e && e.preventDefault();
        var $lnk = this.$el.find( '.hidePublicRecognitionLnk' );
        $lnk.qtip( 'hide' );
    },

    hideDeleteConfirmTip: function ( e ) {
        e && e.preventDefault();
        var $lnk = this.$el.find( '.deletePublicRecognitionLnk' );
        $lnk.qtip( 'hide' );
    },

    updateHidden: function ( commentId ) {
        var $lnk = this.$el.find( '.hidePublicRecognitionLnk' ),
            $txt = this.$el.find( '.publicRecognitionHiddenLinkText' ),
            that = this,
            isHide = this.model.get( 'isHidden' ),
            isHideEl = isHide && !this.isKeepElementOnHide || isHide && commentId,
            isShowEl = !isHide && !this.isKeepElementOnHide,
            isShowMode = !isHide;

        if ( isShowMode ) {
            $lnk.show();
            $txt.hide();
        }

        if ( isHideEl ) {

            $lnk.data( 'qtip' ) && $lnk.qtip( 'hide' );
            if ( $( '.recognition-props a' ).hasClass( 'clickedHide' ) ) {
                $lnk.hide();
                $txt.show();

                this.$el.slideUp( function() {
                    that.updateCommentCount();
                } );

                $( '.recognition-props a' ).removeClass( 'clickedHide' );

            }

            else if ( $( '.hide-comment a' ).hasClass( 'clickedHide' ) ) {
                this.$el.find( "[data-comment-id='" + commentId + "']" ).slideUp( function () {
                    that.$el.find( "[data-comment-id='" + commentId + "']" ).remove();
                    that.updateCommentCount();
                } );

                $( '.hide-comment a' ).removeClass( 'clickedHide' );


            }
        }
        else {
            $lnk.hide();
            $txt.show();
        }

        if ( isShowEl ) {
            this.$el.slideDown();
        }

    },

    updateCommentCount: function() {
        var comments = this.$el.find( '.pubRecCommentsComment:visible' ).length;

        // this should update the text as well like vs likes? for likeCountStatus
        if( comments === 1 ) {
            // set to like
            this.$el.find( '.commentCountStatus .badge' ).text( comments  );
        }else{
            // set to likes
            this.$el.find( '.commentCountStatus .badge' ).text( comments  );
        }

    },

    doTranslate: function ( e ) {
        var $tar = $( e.target ),
            commentId = $tar.parents( '.comment-block' ).data( 'commentId' );

        this.model.translateData( commentId );
        $tar.replaceWith( '<span class="translateLinkDisabled">' + $tar.text() + '</span>' );

        e.preventDefault();
    },

    updateTranslatedText: function ( commentId ) {
        var isTranslated = this.model.get( 'translatedText' ),
            tText = this.model.get( 'newTransText' ),
            $recDetail = this.$el.closest( '#publicRecognitionPageView' ).hasClass( 'public-recognition-page-detail' );

        if ( isTranslated ) {
            if ( $recDetail ) {
                if ( commentId !== undefined ) {
                    this.$el.find( "[data-comment-id='" + commentId + "']" ).find( '.readMore' ).html( tText );
                } else {
                    this.$el.find( '.detail-comment' ).html( tText );
                }
            } else if ( commentId !== undefined ) {
                this.$el.find( "[data-comment-id='" + commentId + "']" ).find( '.readMore' ).html( tText );
            } else {
                this.$el.find( '.recognition-comment' ).html( tText );
            }
        }
    },

    //detect if we are in a page view
    isPageView: function () {
        return $( '#publicRecognitionPageView' ).length > 0;
    },

    //helper, filter non-numeric keydown
    filterNonNumericKeydown: function ( event ) {

        //http : //stackoverflow.com/questions/995183/how-to-allow-only-numeric-0-9-in-html-inputbox-using-jquery
        // Allow :  backspace, delete, tab, escape, and enter
        if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 ||
                // Allow :  Ctrl+A
            ( event.keyCode == 65 && event.ctrlKey === true ) ||
                // Allow :  home, end, left, right
            ( event.keyCode >= 35 && event.keyCode <= 39 ) ) {
            // let it happen, don't do anything
            return;
        }
        else {
            // Ensure that it is a number and stop the keypress
            if ( event.shiftKey || ( event.keyCode < 48 || event.keyCode > 57 ) && ( event.keyCode < 96 || event.keyCode > 105 ) ) {
                event.preventDefault();
            }
        }

    },

    updateBudgetRemaining: function () {
        this.updateBudgetInfo();
    }

} );
