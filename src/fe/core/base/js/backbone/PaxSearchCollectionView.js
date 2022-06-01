/*eslint no-underscore-dangle: ["error", { "allow": ["_globalEvents"] }]*/
/*exported PaxSearchCollectionView */
/*global

TemplateManager,
RecognitionEzView,
PaxSearchCollection,
PaxSearchCollectionView:true
*/

/**

display search results
handle verbs on each result → alternatively, create a separate CardView to handle the behavior of each card, further simplifying the view itthat


this holds the cards etc.

 **/
PaxSearchCollectionView = Backbone.View.extend( {
    //el: '#search-results',
    events: {
        'click .results-container .card-front': 'paxCardClicked',
        'click .back-arrow': 'flipCard',
        'click .back-arrow-alt': 'flipCardAlt',
        'click .card-front .celebrationInfo': 'celebrationInfoClicked',
        'click .btnRecognize': 'recognitionBtnClicked',
        // Cheers Customization - Celebrating you wip-62128 added cheers related events
        'click .btnCheers': 'recognitionCheersClicked',
        'click .btnCheers i': 'recognitionCheersIconClicked',
        'click .participant-name a': 'participantNameClicked',
        'click .follow': 'followBtnClicked',
        'click .down-arrow, .up-arrow': 'scrollPromotions',
        'click .promotion button:not(.btnCheers)': 'backRecBtnClicked',
        'click .left-to-load': 'loadMoreClicked'
    },


    initialize: function( opts ) {
        this.selectUrl = opts.selectUrl;
        this.deselectUrl = opts.deselectUrl;
        this.addOnSelect = opts.addOnSelect;
        this.addOnFollow = opts.addOnFollow;
        // this is set later if we have one
        this.collection = new PaxSearchCollection();
        this.collection.bind( 'followResponse', this.followResponse, this );
        this.collection.bind( 'updateItemSelectionView', this.updateItemSelectionView, this );
        this.collection.setSelectUrl( this.selectUrl );
        this.collection.setDeselectUrl( this.deSelectUrl );
        this.collection.addOnSelect = this.addOnSelect;
        this.totalRecordsFound = 0;
        this.collection.addOnFollow = this.addOnFollow;
        // can recognize etc
        this.recognition = opts.recognition;
        this.follow = opts.follow;
        this.multiSelect = opts.multiSelect;
        this.disableSelect = opts.disableSelect;
        // clean up
        this.overlayTpl = 'paxSearchView';

        this.render();
    },
    render: function() {
        var that = this;
        TemplateManager.get( that.overlayTpl, function( tpl, vars, subTpls ) {
            that.searchResultsViewTpl = subTpls.paxCardTpl;
        }, G5.props.URL_TPL_ROOT || G5.props.URL_BASE_ROOT + 'tpl/' );

        // global event listeners
        this.resizeListener();
        G5._globalEvents.on( 'windowResized', this.resizeListener, this );
    },
    postRender: function() {
         _.bindAll( this, 'lazyLoad' );
         if ( this.disableSelect ) {
            this.$el.addClass( 'disable-card-select' );
         }
        // bind to window
        $( '.PaxSearchView' ).scroll( this.lazyLoad );
    },

    setCollectionReference: function( collectionReference ) {
        this.collection.setSyncSelectedCollectionRef( collectionReference );
    },

    /***
     *                                             888             888
     *                                             888             888
     *                                             888             888
     *     8888b.  88888b.  88888b.       .d8888b  888888  8888b.  888888 .d88b.  .d8888b
     *        "88b 888 "88b 888 "88b      88K      888        "88b 888   d8P  Y8b 88K
     *    .d888888 888  888 888  888      "Y8888b. 888    .d888888 888   88888888 "Y8888b.
     *    888  888 888 d88P 888 d88P           X88 Y88b.  888  888 Y88b. Y8b.          X88
     *    "Y888888 88888P"  88888P"        88888P'  "Y888 "Y888888  "Y888 "Y8888   88888P'
     *             888      888
     *             888      888
     *             888      888
     */
    setEmptyResults: function() {
        this.showEmtpyResults();
        this.$el.find( '.card-profile' ).remove();
    },
    showEmtpyResults: function() {
        this.$el.find( '.results-container' ).hide();
        this.$el.find( '.no-results' ).hide();
        this.$el.find( '.empty-search-message' ).fadeIn();
    },
    hideEmtpyResults: function() {
        this.$el.find( '.empty-search-message' ).fadeOut();
    },

    // set all search cards not selected
    deSelectCard: function( pID ) {
        this.$el.find( '.card-profile[data-id="' + pID + '"]' ).removeClass( 'selected' );
    },
    //  select single seach card based on id
    selectCard: function( pID ) {
        this.$el.find( '.card-profile[data-id="' + pID + '"]' ).addClass( 'selected' );
    },
    selectAllCardsFollowed: function() {
        var that = this;
        _.each( this.collection.models, function( model ) {
            if ( model.get( 'follow' ) ) {
                this.followResponse( model );
                model.set( 'selected', true, { silent: true } );
                //model.set('selected', true);
                that.selectCard( model.get( 'id' ) );
            }

        } );
         this.collection.addAllToSynced();
    },
    selectAllCards: function() {
        var that = this;
        //this.$el.find('.card-profile').addClass('selected');
        _.each( this.collection.models, function( model ) {
            model.set( 'selected', true, { silent: true } );
            that.selectCard( model.get( 'id' ) );
        } );
        this.collection.addAllToSynced();
    },
    deSelectAllCards: function() {
        console.log( 'deselectAllCards' );
        //this.$el.find('.card-profile').removeClass('selected');
        _.each( this.collection.models, function( model ) {
            model.set( 'selected', false );
        } );
        // remove all from synced collction
        this.collection.removeAll();
    },

    setNoResultsFound: function( errorText ) {
        this.$el.find( '.empty-search-message' ).hide();
        this.$el.find( '.results-container' ).hide();
        this.$el.find( '.no-results' ).fadeIn();
        this.collection.reset();
        //this.disableSelectAll(); do will still need to call
       if ( errorText ) {
            this.$el.find( '.no-results' ).addClass( 'dataerror' );
            //this.$el.find('.no-data').text('An Error Has Occurred.');
            //this.$el.find('.no-results .suggestion').text(event.errorText);
            //this.$el.find('.no-results .caption').text('Error!').css('color', 'red');
            this.$el.find( '.icon-ghost' ).removeClass( 'icon-ghost' ).addClass( 'icon-refresh-warning' );
       } else {
            this.$el.find( '.no-results' ).removeClass( 'dataerror' );
            this.$el.find( '.icon-ghost' ).addClass( 'icon-ghost' ).removeClass( 'icon-refresh-warning' );
       }
    },


    /***
    *     .d888                            888    d8b
    *    d88P"                             888    Y8P
    *    888                               888
    *    888888 888  888 88888b.   .d8888b 888888 888  .d88b.  88888b.  .d8888b
    *    888    888  888 888 "88b d88P"    888    888 d88""88b 888 "88b 88K
    *    888    888  888 888  888 888      888    888 888  888 888  888 "Y8888b.
    *    888    Y88b 888 888  888 Y88b.    Y88b.  888 Y88..88P 888  888      X88
    *    888     "Y88888 888  888  "Y8888P  "Y888 888  "Y88P"  888  888  88888P'
    *
    *
    *
    */
    removeAnimateClass: function() {
        this.$el.find( '.card-profile' ).removeClass( 'animateUp' );
    },
    totalAdded: function() {
        return this.collection.size();
    },

     // flip card over
    flipCard: function( e, alt ) {
        var c = this.$el.find( e.target ).closest( '.card' ),
            $content = c.find( '.card-back-content' ),
            $contentAlt = c.find( '.card-back-content-alt' ),
            participantID = c.data( 'id' ),
            participant = _.where( this.collection.toJSON(), { id: participantID } );

        e.stopPropagation();
        e.preventDefault();

        if( !c.hasClass( alt === true ? 'flipped-alt' : 'flipped' ) ) {
            c.addClass( alt === true ? 'flipped-alt' : 'flipped' );

            // we have to do this manual show/hide and class add/remove to preserve the back side of the card when it flips to the front
            // note that we don't do this work when we flip to the front, only when we flip to the back
            if( alt === true ) {
                c.find( '.card-back' ).addClass( 'card-purlrec' ).removeClass( 'card-ezrec' );
                $content.hide();
                $contentAlt.show();
                this.doPurlRecognition( e, c, participant );
            } else {
                c.find( '.card-back' ).removeClass( 'card-purlrec' ).addClass( 'card-ezrec' );
                $content.show();
                $contentAlt.hide();
                // if recognizing on the back of the card:
                // this.doEzRecognition( e, c, participant );

                // if recognizing in a modal via buttons on the back of the card
                this.scrollPromotionsListener( { target: c.find( '.promotion' ) } );
            }
        }
        else {
            c.removeClass( alt === true ? 'flipped-alt' : 'flipped' );

            // we then wait a short period of time after flipping back to complete the reset
            setTimeout( function() {
                if( !c.hasClass( 'flipped' ) && !c.hasClass( 'flipped-alt' ) ) {
                    c.find( '.card-back' ).removeClass( 'card-purlrec card-ezrec' );
                    $content.hide();
                    $contentAlt.hide();
                }
            }, 1000 );
        }

        // this.scrollPromotionsListener({target:c.find('.promotion')});
        //console.log(c.find('.promotion'));
        return false;
    },

    flipCardAlt: function( e ) {
        // proxies the flipCard method, but with an extra parameter
        this.flipCard( e, true );
    },

    flipAllCardsNormal: function() {
       this.$el.find( '.card-profile' ).removeClass( 'flipped flipped-alt' );
    },

    /*
     * This version of doEzRecognition is for the back of the card
     *
    doEzRecognition: function( event, $card, participant ) {
        var that = this;

        if ( $card.data( 'ezView' ) ) {
            return false;
        }

        $card.data( 'ezView', new RecognitionEzView( {
            recipient: participant[ 0 ],
            // if we update the data that comes back from the pax search
            // to include more of the items in the response for G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO,
            // we don't have to make a second request within the EZ rec view and can just pass this information along

            // recipientInfo : participant, <-- needs more details, such as countryCode
            // promotions    : participant[0].promotions, <-- this one is mostly OK
            // nodes         : participant[0].allNodeIds, <-- need more node data (names) instead of just IDs
            el: $card.find( '.card-back .ezrec-container' ),
            close: function() {
                that.flipCard( event );
                $card.removeData( 'ezView' );
            }
        } ) );

        $card.data( 'ezView' ).on( 'templateReady', function() {
            // pull the form container out of the mess it's in
            $card.data( 'ezView' ).$el.find( '.formContainer' ).appendTo( $card.data( 'ezView' ).$el.find( '.ezRecLiner' ) );
            // get rid of the unneeded elements
            $card.data( 'ezView' ).$el.find( '.modal-body' ).remove();
            // manipulate the button cloasses
            $card.data( 'ezView' ).$el.find( '.btn' ).addClass( 'btn-block btn-small' );
            // the View hides itself. we need to reshow it
            $card.data( 'ezView' ).$el.find( '.ezRecLiner' ).show();
        } );
    },
    */

    /*
     * This version of doEzRecognition is for the modal
     */
    doEzRecognition: function( event, participant, promotionID, promotionType ) {
        event.preventDefault ? event.preventDefault() : event.returnValue = false;

        var that = this,
            paxID = participant[ 0 ].id,
            $theModal = this.$el.find( '.ezRecognizeMiniProfileModal' ).clone().appendTo( 'body' ).addClass( 'ezModuleModalClone' ).on( 'hidden', function() {
                $( this ).remove();
            } ),
            close = function () {
                $theModal.modal( 'hide' );
            },
            init = function() {
                that.eZrecognizeView = new RecognitionEzView( {
                    recipient: participant[ 0 ],
                    el: $theModal,
                    promotionID: promotionID,
                    close: close
                } );

                that.eZrecognizeView.on( 'templateReady', function() {
                    that.eZrecognizeView.$el.find( '.ezRecLiner' ).show(); // the View hides itself. we need to reshow it
                    that.eZrecognizeView.$el.find( '#ezRecModalCloseBtn' ).show();
                } );

                that.eZrecognizeView.on( 'ezRecognitionSent', function() {
                    that.flipAllCardsNormal();
                    that.trigger( 'exitSearch' );
                } );
            };
        paxID;
        $( this.el ).qtip( 'hide' );

        if ( $.support.transition ) { // ie is slow
            $theModal.on( 'shown', function() {
                init();
            } );

            $theModal.modal( 'show' );
        } else {
            $theModal.modal( 'show' );
            init();
        }

    },

    // Cheers Customization - Celebrating you wip-62128 starts
    doCheersRecognition: function( event, participant, promotionID, promotionType ) {
        event.preventDefault ? event.preventDefault() : event.returnValue = false;

        var that = this,
            paxID = participant,
            $theModal = this.$el.find( '.ezRecognizeMiniProfileModal' ).clone().appendTo( 'body' ).addClass( 'ezModuleModalClone' ).on( 'hidden', function() {
                $( this ).remove();
            } ),
            close = function () {
                $theModal.modal( 'hide' );
            },
            init = function() {
                that.eZrecognizeView = new CheersRecognitionEzView( {
                    recipient: participant,
                    el: $theModal,
                    promotionId: promotionID,
                    close: close
                } );

                that.eZrecognizeView.on( 'templateReady', function() {
                    that.eZrecognizeView.$el.find( '.ezCheersModal' ).show(); // the View hides itself. we need to reshow it
                    that.eZrecognizeView.$el.find( '#ezRecModalCloseBtn' ).show();
                } );

                that.eZrecognizeView.on( 'cheersRecognitionSent', function() {                    
                    that.trigger( 'exitSearch' );
                } );
            };
        paxID;
        $( this.el ).qtip( 'hide' );

        if ( $.support.transition ) { // ie is slow
            $theModal.on( 'shown', function() {
                init();
            } );

            $theModal.modal( 'show' );
        } else {
            $theModal.modal( 'show' );
            init();
        }

    },
    // Cheers Customization - Celebrating you wip-62128 ends


    doPurlRecognition: function( event, $card, participant ) {
        // console.log( event, $card, participant );
        // nothing to do, I guess
    },

    //updateItemSelectionView
    updateItemSelectionView: function( id, selected ) {
        if ( this.collection ) {
            if ( selected ) {
                this.selectCard( id );
                if ( this.collection.numberItemsSelected() === this.collection.size() ) {
                    this.trigger( 'setCheckAll', true );
                }
            } else {
                this.deSelectCard( id );
                this.trigger( 'setCheckAll', false );
            }
        }
    },

    numberItemsSelected: function() {
        return this.collection.numberItemsSelected();
    },

    checkLoadNum: function() {
        var ta = this.collection.size(),
            tf = this.totalRecordsFound,
            numLeft = tf - ta;

        //console.log('check left to load',tf, ta, numLeft);
        if ( numLeft > 0 ) {
            // show num left to load
            this.$el.find( '.left-to-load' ).css( 'display', 'inline-block' );
            this.$el.find( '.left-to-load-num' ).text( '+' + numLeft );
        } else {
            // hide num left to load
            this.$el.find( '.left-to-load' ).css( 'display', 'none' );
        }
        this.$el.find( '.left-to-load .spinner' ).hide();
        this.setLoadNumPos();

    },

    getMaxBlocksAndBlockWidth: function() {
        var windowWidth = $( '#overlay' ).width(),
            blockWidth = this.$el.find( '.card' ).outerWidth( true ),
            maxBoxPerRow = Math.floor( windowWidth / blockWidth );

        if ( maxBoxPerRow > 4 ) { maxBoxPerRow = 4; }
        return [ maxBoxPerRow, blockWidth ];
    },

    setLoadNumPos: function( numPerRow ) {
        var totalCards = this.collection.length,
            num,
            loadNum;
        if ( totalCards ) {
            if ( !numPerRow ) {
               numPerRow = this.getMaxBlocksAndBlockWidth()[ 0 ];
            }
            num = totalCards / numPerRow;
            loadNum = this.$el.find( '.left-to-load' );

            if ( parseInt( num ) === num ) {
                loadNum.addClass( 'own-row' );
            } else {
                loadNum.removeClass( 'own-row' );
            }
        }


    },

    /***
    *    888 d8b          888
    *    888 Y8P          888
    *    888              888
    *    888 888 .d8888b  888888 .d88b.  88888b.   .d88b.  888d888 .d8888b
    *    888 888 88K      888   d8P  Y8b 888 "88b d8P  Y8b 888P"   88K
    *    888 888 "Y8888b. 888   88888888 888  888 88888888 888     "Y8888b.
    *    888 888      X88 Y88b. Y8b.     888  888 Y8b.     888          X88
    *    888 888  88888P'  "Y888 "Y8888  888  888  "Y8888  888      88888P'
    *
    *
    *
    */

    resizeListener: function() {
        var getMaxBlocksAndBlockWidth;
        // var breakpoint = G5.breakpoint.value;
        // if (breakpoint === 'mobile' || breakpoint =='mini'){
        //     // flip all cards over as they cant be flipped on mobile
        //    this.flipAllCardsNormal();
        // } else {
        //
        // }

        $( '.results-container' ).css( 'width', 'auto' ); //reset
        getMaxBlocksAndBlockWidth = this.getMaxBlocksAndBlockWidth();
        this.setLoadNumPos( getMaxBlocksAndBlockWidth[ 0 ] );
        this.$el.find( '.results-container' ).width( getMaxBlocksAndBlockWidth[ 0 ] * getMaxBlocksAndBlockWidth[ 1 ] );

    },


    // called when a pax card is clicked
    paxCardClicked: function( e ) {
        var id = this.$el.find( e.target ).closest( '.card-profile' ).data( 'id' ),
            pax,
            isFollowed,
            parameters;

        // if add on follow and follow is lock - just stop the click of the card
        if( this.addOnFollow  && this.$el.find( e.target ).closest( '.card-profile' ).hasClass( 'follow-lock' ) ) {
            return false;
        }

        if ( !this.disableSelect ) {
            if ( !this.multiSelect ) {
                this.deSelectAllCards();
            }
            // tell the collection to update
            this.collection.updateItemSelection( id );
            //G5._globalEvents.trigger('updatePaxSelectedItem',{id:id, participant:participant, selected:card.hasClass('selected')});
        } else if ( this.follow ) {
            pax = this.collection.get( id );
            isFollowed = pax.get( 'follow' );

            parameters = {
                isFollowed: isFollowed,
                participantIds: id
            };
            // call to follow
            if ( !this.addOnFollow ) { this.collection.follow( parameters ); }
        }

    },

    // TODO: link this up correctly also do we need to listen for changes made in the popover such as follow and update
    participantNameClicked: function( e ) {
        var url = e.target.href,
            pageTitle = this.$el.find( e.target ).data( 'title-page-view' );

        event.preventDefault();
        event.stopPropagation();

        // G5.util.doSheetOverlay(false, targetUrl, targetPageTitle);
        G5.util.doSheetOverlay( false, url, pageTitle );
        return false;
    },

    // FOLLOW btn clicked
    followBtnClicked: function( event ) {
        var $target,
            id,
            pax,
            isFollowed,
            parameters;

        // check if we are else we will get a double call
        if ( !this.addOnFollow ) {
            event.preventDefault();
            event.stopPropagation();
            $target = this.$el.find( event.target );
            id = $target.data( 'id' );
            if( $target.hasClass( 'follow-lock' ) ) {
                // nope
            }else{
                pax = this.collection.get( id );
                isFollowed = pax.get( 'follow' );
                parameters = {
                    isFollowed: isFollowed,
                    participantIds: id
                };
                // call to follow
                this.collection.follow( parameters );
            }

        }

    },

    // called when we have response from the data the parameters are sent back so we know what do do with tem
    followResponse: function( participant ) {
        //console.log(participant,participant.get('follow'));
        // this should loop through if we have multiple
        var $target = this.$el.find( '.results-container [data-id="' + participant.get( 'id' ) + '"] .follow' );

        $target.tooltip( 'destroy' );

        if ( !participant.get( 'follow' ) ) {
            $target.removeClass( 'icon-user-check' );
            $target.addClass( 'pulse' );
            $target.removeClass( 'following followReady unfollowReady' );
            $target.data( 'follow', false );

            $target.one( 'mouseout', function() {
                    $target.addClass( 'followReady' );
                } )
                .tooltip( {
                    title: $target.data( 'titleFollow' )
                } );
        } else {
            $target.addClass( 'icon-user-check' );
            $target.addClass( 'pulse' );
            $target.addClass( 'following' );
            $target.data( 'follow', true );

            $target.removeClass( 'followReady unfollowReady' )
                .one( 'mouseout', function() {
                    $target.addClass( 'unfollowReady' );
                } )
                .tooltip( {
                    title: $target.data( 'titleUnfollow' )
                } );
        }

       $target.one(
            'transitionend MSTransitionEnd webkitTransitionEnd oTransitionEnd webkitAnimationEnd oanimationend msAnimationEnd animationend',
            function() {
                // Animation complete.
                //that.$carousel.slick('slickGoTo',0);
                $( this ).removeClass( 'pulse' );
            }
           );

    },

    celebrationInfoClicked: function ( e ) {
        $( e.target ).closest( '.celebrationInfo' ).tooltip( 'hide' );

        this.flipCardAlt( e );
    },

    // Cheers Customization - Celebrating you wip-62128 starts
    recognitionCheersIconClicked: function( e ) {
        e.preventDefault();
        e.stopPropagation();
        $( e.currentTarget ).parent().trigger( 'click' );        
    },

    recognitionCheersClicked: function( e ) {
        var $src = this.$el.find( e.target ),
            participantID = $src.data( 'participant-id' ),
            promotionID = $src.data( 'cheers-promotion-id' ),            
            promotionIsEasy = $src.data( 'iseasy' );           
            

        //console.log('participant',participant, participantID);
        if ( promotionIsEasy ) {
            e.preventDefault();
            e.stopPropagation();
            
            // this.openEZrecognition( participant, promotionID, promotionType );
            this.doCheersRecognition( e, participantID, promotionID, 'recognition' );
        }

    },
    // Cheers Customization - Celebrating you wip-62128 ends

    recognitionBtnClicked: function( e ) {
        this.flipCard( e );
    },

    // TODO: add all different places a partipant may go
    backRecBtnClicked: function( e ) {
        // this.trigger( 'pageLoadFull' );
        //console.log(e.target);
        var $src = this.$el.find( e.target ),
            participantID = $src.data( 'participant-id' ),
            promotionID = $src.data( 'promotion-id' ),
            promotionType = $src.data( 'promotion-type' ),
            promotionIsEasy = $src.data( 'iseasy' ),
            href = G5.props.URL_START_RECOGNITION,
            participant = _.where( this.collection.toJSON( ), { id: participantID } );

        //console.log('participant',participant, participantID);
        if ( promotionType == 'recognition' && promotionIsEasy ) {
            e.preventDefault();

            // this.openEZrecognition( participant, promotionID, promotionType );
            this.doEzRecognition( e, participant, promotionID, promotionType );
        } else {
             e.preventDefault();
             //console.log(e.target)
            // figure out better endpoint?
            // TODO: remove unecassary parms on the post of this
            var form = $( '<form action="' + href + '" method="post"><input type="hidden" name="claimRecipientFormBeans[0].userId" value="' + participantID + '" /><input type="hidden" name="promotionId" value="' + promotionID + '" /><input type="hidden" name="participant" value="' + participant + '" /></form>' );
            $( 'body' ).append( form );
            $( form ).submit();
            // send POST to href with pax obj sent
            // global url to post to instead of href
            // window.location.assign(href);
        }
    },

    scrollPromotionsListener: function( e ) {
        //console.log('scrollPromotionsListener'+e.target);
        var $promotionContainer = this.$el.find( e.target ).closest( '.promotions-container' ),
            $promotion = $promotionContainer.find( '.promotion' ),
            st = $promotion.scrollTop(),
            max = $promotion[ 0 ].scrollHeight - $promotion.innerHeight();

        if ( st === 0 ) {
            $promotionContainer.find( '.up-arrow' ).hide();
        } else {
            $promotionContainer.find( '.up-arrow' ).show();
        }
        if ( st === max ) {
            $promotionContainer.find( '.down-arrow' ).hide();
        } else {
            $promotionContainer.find( '.down-arrow' ).show();
        }
    },

    scrollPromotions: function( e ) {
        var that = this,
            $promotionContainer = this.$el.find( e.target ).closest( '.promotions-container' ),
            $promotion = $promotionContainer.find( '.promotion' ),
            dir = $( event.target ).attr( 'alt' ),
            distance = $promotionContainer.innerHeight() * 0.8; // 80% of height to accommodate arrows

        distance = dir == 'up' ? '-=' + distance : '+=' + distance;

        $promotion.scrollTo( distance + 'px', {
            duration: G5.props.ANIMATION_DURATION,
            axis: 'y',
            onAfter: function() {
                that.scrollPromotionsListener( e );
            }
        } );
    },

    loadMoreClicked: function() {
        var ta = this.collection.size();
        if ( !this.isLoading  && ta && ( ta < this.totalRecordsFound ) ) {
            this.setLazyLoadSpinner( true );
            this.trigger( 'callToFetchRecipientList', null, true );
        }
    },

    lazyLoad: function( event ) {
        var ta = this.collection.size();
        if ( !this.isLoading  && ta && ( ta < this.totalRecordsFound ) ) {
            this.scrollTop = $( '.PaxSearchView' )[ 0 ].scrollHeight - $( '.PaxSearchView' ).height() - 1;
            if ( this.scrollTop < event.target.scrollTop ) {
                this.scrollTop = event.target.scrollTop;
                this.setLazyLoadSpinner( true );
                this.trigger( 'callToFetchRecipientList', null, true );
            }
        }
    },

    //SET LAZYLOAD SPINNER -
    setLazyLoadSpinner: function( show ) {
        if( show ) {
            var spinProps = { };
            spinProps.classes = 'more';


            G5.util.showSpin( this.$el.find( '.left-to-load-spinner' ), {}  );
            this.$el.find( '.left-to-load-spinner' ).fadeIn();
            this.$el.find( '.left-to-load-num' ).fadeOut();
        }else{
            //console.log( 'end spinner????' );
            G5.util.hideSpin( this.$el.find( '.left-to-load-spinner' ) );
            this.$el.find( '.left-to-load-spinner' ).fadeOut();
            this.$el.find( '.left-to-load-num' ).fadeIn();
        }

    },

    // Fired when we have new search results - need to know if they are addded to selected or now
    renderRecipientList: function( participants, lazyLoad, preselected ) {
        var $list  = this.$el.find( '.results-container' ),
            that = this,
            animate = false,
            startNum = $list.find( '.card-profile' ).length,
            newParticipants = null,
            data,
            docElemStyle,
            ps,
            els,
            transitionProp;

        if ( participants.length ) {
            this.hideEmtpyResults();
            this.$el.find( '.no-results' ).hide();
            this.$el.find( '.results-container' ).fadeIn();
        } else {
            this.setNoResultsFound();
        }

        // quick loop through participants to manipulate data for PURLs
        _.each( participants, function( participant ) {
            if( participant.purlData && participant.purlData.length ) {
                _.each( participant.purlData, function( data ) {
                    data.anniversaryInt = parseInt( data.anniversary );
                } );
            }
            if( participant.departmentName ) {
                participant.departmentNameTooltip = participant.departmentName;
                if( participant.departmentName.length > 80 ) {
                    if( window.outerWidth < 321 ) {
                        participant.departmentName = G5.util.trimCharacters( participant.departmentName, 0, 36, true );
                    } else if( window.outerWidth > 320 && window.outerWidth < 381 ) {
                        participant.departmentName = G5.util.trimCharacters( participant.departmentName, 0, 51, true );
                    } else {
                        participant.departmentName = G5.util.trimCharacters( participant.departmentName, 0, 80, true );
                    }
                }
            }
        } );

        // check if it was a lazy load and add or new search and replace all
        if ( lazyLoad ) {
            newParticipants = [];
            animate = true;
            // check if we already have a partipant'
            _.each( participants, function( participant ) {
                //newParticipants.push(participant);
                if ( !that.collection.get( participant.id ) ) { newParticipants.push( participant ); }
            } );
            //console.log('newParticipants',newParticipants);
            if ( newParticipants.length ) {
                data = this.searchResultsViewTpl( { participants: newParticipants, canFollow: this.follow, recognition: this.recognition, 'addanimation': animate } );
                $( data ).insertBefore( $( '.left-to-load' ) );
                //$list.append(this.searchResultsViewTpl({participants:newParticipants,canFollow:this.follow,recognition:this.recognition,'addanimation':animate}));

                // updated the collection
                this.collection.add( newParticipants );
            }
        } else {
            this.scrollTop = 0;
            animate = !this.$el.find( '.card-profile' ).length;
            // clear the cards
            this.$el.find( '.card-profile' ).remove();
            data = this.searchResultsViewTpl( { participants: participants, canFollow: this.follow, recognition: this.recognition, 'addanimation': animate } );

            $( data ).insertBefore( $( '.left-to-load' ) );
            //$list.html(this.searchResultsViewTpl({participants:participants,canFollow:this.follow,recognition:this.recognition,'addanimation':animate}));

            // updated the collection
            this.collection.reset( participants );
            this.resizeListener();
        }

        this.$el.find( '.follow' ).each( function() {
            $( this ).tooltip( {
                title: $( this ).data( 'follow' ) ? $( this ).data( 'titleUnfollow' ) : $( this ).data( 'titleFollow' )
            } );
        } );

        if( window.outerWidth > 1024 ) {
            this.$el.find( '.pi-dept' ).each( function() {
                $( this ).tooltip( {
                    title: $( this ).data( 'departmentNameTooltip' )
                } );
            } );
        }

        this.$el.find( '.card-front .celebrationInfo' ).each( function() {
            if( $( this ).data( 'tooltip' ) ) {
                return;
            }

            $( this ).tooltip( {
                container: '.results-container'
            } );
        } );

        if ( animate ) {
            docElemStyle = document.documentElement.style;
            transitionProp = typeof docElemStyle.transition === 'string' ?  'animation' : 'WebkitAnimation';
            ps = newParticipants || participants;
            els = $list.children();

            var j, el;
            _.times( ps.length, function( i ) {
                j = i + startNum;
                el = $( els[ j ] );
                //console.log(els[i+startNum]);
                //el.addClass('animateUp');
                el.css( transitionProp + 'Delay', ( i * 30 ) + 'ms' );
            } );
            // CREATE A TIMEOUT TO REMOVE THE THE ANIMATE UP CLASS
            setTimeout( function() {
                that.removeAnimateClass();
            }, ( j * 30 ) + 700 );
            /*$list.children('.card-profile').each(function (index) {
                $(this).addClass('animate');
                this.style[ transitionProp + 'Delay' ] = (index * 50) + 'ms';
            });*/
        } else {
           //$list.find('.card-profile').addClass('animateOpacity');
        }


        this.$el.find( '.results-container .promotions-container .promotion' ).on( 'scroll', function ( e ) {
            that.scrollPromotionsListener( e );
        } );


        if ( preselected ) {
            // set all check'
            this.selectAllCards();
        }
        /*if (this.addOnFollow){
            this.selectAllCardsFollowed();
        }*/

    },

    /*
    // single recognition change name of this
    openEZrecognition: function( participant, promotionID, promotionType ) {
        participant.promotion = {
            'promotionID': promotionID,
            'promotionType': promotionType
        };
        //console.log(participantID, promotionID, promotionType, href, participant);
        G5._globalEvents.trigger( 'openEZrecognition', [ participant ] );
        this.trigger( 'exitSearch' );
    },
    */


    /***
     *         888                   888
     *         888                   888
     *         888                   888
     *     .d88888  .d88b.  .d8888b  888888 888d888 .d88b.  888  888
     *    d88" 888 d8P  Y8b 88K      888    888P"  d88""88b 888  888
     *    888  888 88888888 "Y8888b. 888    888    888  888 888  888
     *    Y88b 888 Y8b.          X88 Y88b.  888    Y88..88P Y88b 888
     *     "Y88888  "Y8888   88888P'  "Y888 888     "Y88P"   "Y88888
     *                                                           888
     *                                                      Y8b d88P
     *                                                       "Y88P"
     */
    destroy: function() {
        G5._globalEvents.off( 'windowResized', this.resizeListener, this );
        this.collection.off( 'followResponse', this.followResponse, this );
        this.collection.off( 'updateItemSelectionView', this.updateItemSelectionView, this );
        this.collection.destroy();
        this.collection = null;
        this.undelegateEvents();
    }
} );
