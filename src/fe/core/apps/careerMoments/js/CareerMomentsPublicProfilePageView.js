/*exported CareerMomentsPublicProfilePageView */
/*global
PageView,
PublicRecognitionSetCollectionView,
ProfilePageBadgesTabCollection,
ProfilePageBadgesTabView,
RecognitionEzView,
CareerMomentsPublicProfilePageView:true
*/
CareerMomentsPublicProfilePageView = PageView.extend( {

    initialize: function( opts ) {

        var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'careerMoments';
        
        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        // we need to wrap all the Router/History stuff in this conditional
        // when the public profile opens in a sheet, this will conflict with any existing Router/History that's already been initialized
        if ( !Backbone.History.started ) {
            this.tabRouter = new Backbone.Router( {
                routes: {
                    'tab/:name': 'loadTab',
                    'tab/:name/:params': 'loadTab',
                    '*other': 'loadTab'
                }
            } );

            this.tabRouter.on( 'route:loadTab', function ( name ) {
                //Render views if required
                if ( name === 'Badges' ) {
                    that.activateBadges();
                }                else if ( name === 'Recognition' ) {
                    that.activateRecognition();
                }
                //load tab by emulated click
                that.$el.find( 'li.tab' + name + ' a' ).trigger( 'click' );
            } );

            Backbone.history.start();
        }


        this.commentTplName = opts.commentTplName || 'careerMomentsSubComments';
        this.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'careerMoments/tpl/';

		
        this.tdPlayerStatsTable();

        //is the text being translated?
        this.translatedText = opts.translatedText || false;

        this.CareerMomentsProfileModel = new CareerMomentsProfileModel( opts.json );
        //this.CareerMomentsProfileModel.newEmployeeAbout(); 
        
        this.setupEvents();

        this.initVideoJs();

        this.updateImInfo();
        
        this.updateContribComment();

        //this.renderSpellcheck();

        this.initCommentImageUploader();
            
        this.$el.find('[placeholder]').placeholder();
        
        this.CareerMomentsProfileModel.fetchComments( );     
        
        //this.showInitialModal();
        
        //this.initEmpLikeInfo();
     },

    events: {
        'click .tabBadges': 'activateBadges',
        'click .tabRecognition': 'activateRecognition',
        'click .miniProfFollowLink': 'followParticipant',
        'click .regonizeFromPubProfile': 'doEzRecognize',
        'click .profile-popover': 'attachParticipantPopover',
        'click .publicRecognition .sa-button': 'invokesaContribute',
        'change #promotionSelect': 'changeTdStatsPromo',

        // contribute comment section updates
        'keyup .contribCommentInp': 'updateContribComment',
        'blur .contribCommentInp': 'updateContribComment',
        'paste .contribCommentInp': 'updateContribComment',

        // attach video/image
        'click .attachPhotoBtn,.attachVideoBtn': 'doAttachMode',
        'click .attachVideoUrlBtn': 'doAttachVideoUrl',
        'click .closeAttModeBtn': 'doCloseAttachMode',
        'click .attachedMediaDisplayWrapper .rmBtn': 'doRemoveCommentMedia',

        // events in video url
        'keyup .attachVideoInput': 'updateAttachMedia_video',
        'blur .attachVideoInput': 'updateAttachMedia_video',
        'paste .attachVideoInput': 'updateAttachMedia_video',


        // submit comment
        'click .commentsLike': 'doLike',
        'click .levelOneCommentsLike': 'doLike',
        'click .iamCommentsLike': 'doIamLike',        
        'click .userComment': 'doContributeComment',
        'click .submitContribCommentBtn': 'doSubmitContribComment',
        'keydown .comment-input': 'doCommentKeydown',
        //area.maxlength shim for opera and ie
        'keyup .comment-input': 'enforceMaxLength',
        'blur .comment-input': 'enforceMaxLength',
        'mouseup .comment-input': 'enforceMaxLength',

    },

    // wire model events
    setupEvents: function(){
        
        // fetch comment list
        this.CareerMomentsProfileModel.on('success:fetchnewEmployeeAbout', this.fetchnewEmployeeAbout, this);
      
        // fetch comment list
        this.CareerMomentsProfileModel.on('start:fetchComments', this.startFetchComments, this);
        this.CareerMomentsProfileModel.on('end:fetchComments', this.endFetchComments, this);
        this.CareerMomentsProfileModel.on('success:fetchComments', this.successFetchComments, this);
       
        // attachments
        this.CareerMomentsProfileModel.on('change:attachMode', this.updateContribComment, this);
        this.CareerMomentsProfileModel.on('change:attachMode', this.updateAttachMedia, this);
        this.on('start:commentImageUpload', this.startCommmentImageUpload, this);
        this.on('end:commentImageUpload', this.endCommentImageUpload, this);

        // save comment
        this.CareerMomentsProfileModel.on('start:saveComment', this.startSaveComment, this);
        this.CareerMomentsProfileModel.on('end:saveComment', this.endSaveComment, this);
        this.CareerMomentsProfileModel.on('success:saveComment', this.successSaveComment, this);

        //translate text
        this.CareerMomentsProfileModel.on('translated', function(){this.updateTranslatedText();},this);   

        //when model is liked
        this.CareerMomentsProfileModel.on('liked', function(numLikes, id) {this.changeDomToLiked(numLikes, id); }, this);

        //when Iam containers is liked
        this.CareerMomentsProfileModel.on('iamliked', function(numLikes, id) {this.changeIamDomToLiked(numLikes, id); }, this);
    

        //when model has a comment added
        this.CareerMomentsProfileModel.on( 'levelOneCommentAdded', function ( comment ) {
            this.addLevelOneCommentToDom( comment, 'single' );
        }, this );
        //this.model.on( 'showNewComment', this.doShowComments, this );        
    },

    invokesaContribute: function( event ) {
        G5.util.saContribute( event );
    },

    activateBadges: function() {
        var that = this;

        if ( !this.badgesLoaded ) {
            // utterly ridiculous, but I have to slow down the tab initialization until after the switch has happened
            setTimeout( function() {
                that.loadBadges();
            }, 0 );
        }
    },

    activateRecognition: function() {
        var that = this;

        if ( !this.pubRecSetCollectionView ) {
            // utterly ridiculous, but I have to slow down the tab initialization until after the switch has happened
            setTimeout( function() {
                that.loadRecognitions();
            }, 0 );
        }
    },

    loadRecognitions: function() {
        this.pubRecSetCollectionView = new PublicRecognitionSetCollectionView( {
            el: $( '#Recognition' ),
            '$tabsParent': $(),
            '$recognitionsParent': this.$el.find( '.pubRecItemsCont' ),
            recogSetId: 'global',
            participantId: parseInt( $( '#participantId' ).val() )
        } );
    },

    loadBadges: function() {
        var that = this;

        var params = {
            participantId: parseInt( $( '#participantId' ).val() )
        };

        this.BadgeTabCollection = new ProfilePageBadgesTabCollection( null, {
            dataUrl: G5.props.URL_JSON_PUBLIC_PROFILE_LOAD_BADGES,
            dataParams: params
        } );

        this.BadgeTabView = new ProfilePageBadgesTabView( {
            el: this.$el.find( '#Badges .span12' ),
            tplName: 'profilePageBadgesTab',
            model: this.BadgeTabCollection
        } );

        this.BadgeTabView.activate();

        this.BadgeTabView.on( 'renderDone', function() {
            that.badgesLoaded = true;
        } );
    },
    changeTdStatsPromo: function() {
        var that = this;

        //submit the throwdown tab page data
        that.$el.find( '#profilePagePlayerStatsTab' ).submit();

        //empty the page
        that.$el.find( '#tabPlayerStatsCont' ).empty();

        //re-render the page
        that.$el.find( '#tabPlayerStatsCont' )
            .load(
                G5.props.URL_HTML_THROWDOWN_PUBLIC_PROFILE,
                { responseType: 'html' },
                function( responseText ) {
                    G5.serverTimeout( responseText );
                }
            );

    },
    tdPlayerStatsTable: function() {
        this.$el.find( '.td-matches-schedule tbody tr:even' ).addClass( 'stripe' );
    },
    followParticipant: function( event ) {
        var $target = $( event.target ).closest( '.btn' );
        var parameters = {
            isFollowed: $target.hasClass( 'unfollow' ),
            participantIds: $target.data( 'participantId' )
        };

        event.preventDefault ? event.preventDefault() : event.returnValue = false;

        $.ajax( {
            dataType: 'g5json', //must set this so SeverResponse can parse
            type: 'POST',
            url: G5.props.URL_JSON_PARTICIPANT_FOLLOW,
            data: parameters,
            success: function( serverResp ) {
                if ( serverResp.data.messages[ 0 ].isSuccess ) {
                    // hide one, show the other
                    $target.closest( 'div' ).find( '.miniProfFollowLink' ).toggle();
                } else {
                    console.error( '[ERROR] ParticipantPopoverView.followParticipant() - Server Error : ' + serverResp.data.messages[ 0 ].text );
                }
            }
        } );
    },

    doEzRecognize: function( event ) {
        var that = this,
            $theModal = this.$el.find( '#ezRecognizeMiniProfileModal' ).clone().appendTo( 'body' ).addClass( 'ezModuleModalClone' ).on( 'hidden', function() {
                $( this ).remove();
            } ),
            paxId = $( event.target ).closest( '.btn' ).data( 'participantId' ),
            nodeId = $( event.target ).closest( '.btn' ).data( 'nodeId' ),
            close = function () {
                $theModal.modal( 'hide' );
            },
            init = function() {
                that.eZrecognizeView = new RecognitionEzView( {
                    recipient: { id: paxId, nodes: [ { id: nodeId } ] },
                    el: $theModal,
                    close: close
                } );

                that.eZrecognizeView.on( 'templateReady', function() {
                    that.eZrecognizeView.$el.find( '.ezRecLiner' ).show(); // the View hides itself. we need to reshow it
                    that.eZrecognizeView.$el.find( '#ezRecModalCloseBtn' ).show();

                    if ( that.eZrecognizeView.$el.position().top < $( window ).scrollTop() ) {
                        $.scrollTo( that.eZrecognizeView.$el, G5.props.ANIMATION_DURATION, { axis: 'y', offset: -20 } );
                    }
                } );
            };
        event.preventDefault();
        // console.log("$.support.transition", $.support.transition);

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
    attachParticipantPopover: function( e ) {
        var $tar = $( e.target ),
            isSheet = ( $tar.closest( '.modal-stack' ).length > 0 ) ? { isSheet: true } : { isSheet: false };
        if ( $tar.is( 'img' ) ) {
            $tar = $tar.closest( 'a' );
        }
        isSheet.containerEl = this.$el;
        //attach participant popovers
        if ( !$tar.data( 'participantPopover' ) ) {
            $tar.participantPopover( isSheet ).qtip( 'show' );
        }
        e.preventDefault();
    },

    doAttachMode: function( e ) {
        var $tar = $( e.currentTarget ),
            mode = $tar.data( 'attachMode' );
        e.preventDefault();
        this.CareerMomentsProfileModel.set( 'attachMode', mode );
    },
    doCloseAttachMode: function( e ) {
        var $videoInput = this.$el.find( '.attachVideoInput' );
        // clear this in case it has text
        $videoInput.val( '' );
        // triggers, and view listens
        this.CareerMomentsProfileModel.set( 'attachMode', 'select' );
    },
    doRemoveCommentMedia: function( e ) {
        var $cont = this.$el.find( '.attachedMediaContent' ),
            $upBtn = this.$el.find( '.uploadBtn' );

        // clear current elements in media content wrapping el
        $cont.empty();
        $upBtn.show();
        // clear any data assoc. with media
        this.CareerMomentsProfileModel.set( 'commentImage', null );
        this.CareerMomentsProfileModel.set( 'commentVideo', null );
        // set to select mode, view will listen for this and update
        this.CareerMomentsProfileModel.set( 'attachMode', 'select' );
        // this.toggleMobileUploadBtn();
    },
    doAttachVideoUrl: function( e ) {
        var $url = this.$el.find( '.contributeToPurlSection .attachVideoInput' );

        // set video in model
        this.CareerMomentsProfileModel.set( 'commentVideo', $url.val() );

        // clear input
        $url.val( '' );

        // this will trigger, and the view will listen and update
        this.CareerMomentsProfileModel.set( 'attachMode', 'display' );
    },

    // update the attach photo, video view
    updateAttachMedia: function() {
        var mode = this.CareerMomentsProfileModel.get( 'attachMode' ),
            $upBtn = this.$el.find( '.uploadBtn' ),
            $attSec = this.$el.find( '.attachmentSection' ),
            $tools = $attSec.find( '.attachTools' ),            
            $disp = this.$el.find( '.attachedMediaDisplayWrapper' );



        // show/hide states
        $tools[ mode === 'display' ? 'hide' : 'show' ](); // show tools on all but display mode
        // $photo[mode==='photo'?'slideDown':'slideUp'](G5.props.ANIMATION_DURATION); // attach photo mode
        // $video[mode==='video'?'slideDown':'slideUp'](G5.props.ANIMATION_DURATION); // attach video mode
        $disp[ mode === 'display' ? 'slideDown' : 'slideUp' ]( G5.props.ANIMATION_DURATION ); // display attachment mode
        $upBtn[ mode === 'display' ? 'hide' : 'show' ](); // display upload button mode

        // set tools state class for css styling
        $tools.removeClass( function( i, c ) {
            c = c.match( /[a-z]+Mode/i );
            return c && c.length ? c[ 0 ] : false;
        } ).addClass( mode + 'Mode' );

        // if(mode==='video'){ this.updateAttachMedia_video(); }
        if( mode === 'display' ) { this.updateAttachMedia_display(); }
    },
    // events - comment image upload
    startCommmentImageUpload: function() {
        var // $clsBtn = this.$el.find('.attachPhotoWrapper .closeAttModeBtn'),
            $spin = this.$el.find( '.uploadWrapper .upSpin' ),
            $submit = this.$el.find( '.submitContribCommentBtn' );

        $spin.show().spin();
        $submit.attr( 'disabled', 'disabled' );
        // $clsBtn.hide();
        // this.toggleMobileUploadBtn();
    },
    endCommentImageUpload: function() {
        var // $clsBtn = this.$el.find('.attachPhotoWrapper .closeAttModeBtn'),
            $spin = this.$el.find( '.uploadWrapper .upSpin' );

        $spin.spin( false ).hide();
        this.updateContribComment();
        // $clsBtn.show();
    },
    updateAttachMedia_video: function() {
        var $inp = this.$el.find( '.attachVideoInput' ),
            $attBtn = this.$el.find( '.attachVideoUrlBtn' ),
            urlRe = /^(https?:\/\/)?[\da-z\.-]+\.[a-z\.]{2,6}.*$/;

        $attBtn[ urlRe.test( $inp.val() ) ? 'removeAttr' : 'attr' ]( 'disabled', 'disabled' );
    },
    updateAttachMedia_display: function() {
        var //$attSec = this.$el.find( '.attachmentSection' ),
            $disp = this.$el.find( '.attachedMediaDisplayWrapper' ),
            $cont = $disp.find( '.attachedMediaContent' ),
            $upBtn = this.$el.find( '.uploadBtn' ),
            imgObj = this.CareerMomentsProfileModel.get( 'commentImage' ),
            vidObj = this.CareerMomentsProfileModel.get( 'commentVideo' ),
            randId = Math.round( Math.random() * 1000000000 )/*,
            videoHtml*/;

        $cont.empty();
        $upBtn.hide();

        if( imgObj ) {
            $cont.append( '<div class="cmtImgWrap"><i class="btn btn-mini btn-icon btn-danger icon-trash rmBtn"></i>' +
                '<img src="' + imgObj.thumbUrl + '"></div>' );
        }
        if( vidObj ) {
            $cont.append( '<div class="cmtImgWrap responsiveVideoContainer"><i class="btn btn-mini btn-icon btn-danger icon-trash rmBtn"></i>' +
                '<video id="careerMomentContribVideo' + randId + '" class="video-js vjs-default-skin vjs-16-9 vjs-big-play-centered" controls preload="auto" data-setup="{}" poster="' + vidObj.thumbUrl + '">' +
                    '<source src="' + vidObj.url + '" type="video/' + vidObj.fileType + '">' +
                '</video></div>' );

            this.initVideoJs( 'careerMomentContribVideo' + randId );
        }        
    },

    doSubmitContribComment: function( e ) {
        var cmt = this.$el.find( '.contribCommentInp' ).val();
        e.preventDefault();

        this.CareerMomentsProfileModel.saveComment( cmt );
    },

    doLike: function ( e ) {
        var $tar = $( e.target ).closest( 'a' ),
        conId = $tar.closest( '.likeAndCommentsWrapper' ).data( 'commentId' );
        $mask = $tar.prev();

        e.preventDefault();

        $mask.show();
        G5.util.showSpin( $mask );

        $tar.toggleClass( 'liked' );

        $tar.toggleClass( 'mylike' );

        if ( $tar.hasClass( 'liked' ) ) {
            this.CareerMomentsProfileModel.saveLike( conId );
        } else {
            this.CareerMomentsProfileModel.saveUnlike( conId );
        }
    },

    doIamLike: function ( e ) {
        var $tar = $( e.target ).closest( 'a' ),
        iamId = $tar.data('iamId');
        
        $mask = $tar.closest( '.likeAndCommentsWrapper' ).find( '.mask' );

        e.preventDefault();
        $mask.show();
        G5.util.showSpin( $mask );

        $tar.toggleClass( 'liked' );

        $tar.toggleClass( 'mylike' );

        if ( $tar.hasClass( 'liked' ) ) {
            this.CareerMomentsProfileModel.saveIamLike( iamId );
        } else {
            this.CareerMomentsProfileModel.unsaveIamUnlike( iamId );
        }
    },

    addLevelOneCommentToDom: function ( comment, type ) {
        var that = this;
        TemplateManager.get( 'levelOneCommentItem', function ( tpl ) {
            if ( type == 'single' ) {
                that.$el.find( '.commentsItemWrapper.comment_'+comment.commentId+ ' .levelOneComments' ).append( tpl( comment ) );
            }
            else {
                var commentId = comment.commentId;
                _.each( comment.levelOneComments, function ( com ) {
                    that.$el.find( '.commentsItemWrapper.comment_'+commentId+ ' .levelOneComments'  ).append( tpl( com ) );
                } );
            }            

        } );
    },

    changeIamDomToLiked: function ( numLikes, id ) {
        var $likeBtn = this.$el.find( '#iamCommentsLike_'+id ),            
            unlike = $likeBtn.data( 'unlike' ),
            like = $likeBtn.data( 'like' ),
            $numLikers = this.$el.find( '#iamLikeCounts_'+id );
            $mask = $likeBtn.parent().find('.mask'),            
            m = this.CareerMomentsProfileModel.toJSON();
        
        if ( m.isLiked ) {
            $likeBtn.contents()[ 1 ].textContent = unlike;
        } else {
            $likeBtn.contents()[ 1 ].textContent = like;
        }

        $mask.hide();
        G5.util.hideSpin( $mask );
        
        // this should update the text as well like vs likes? for likeCountStatus
        if( numLikes === 1 ) { 
            // set to like
            $numLikers.text( '('+numLikes+')' );
        }else{ 
            // set to likes
            $numLikers.text( '('+numLikes+')' );
        }

        $numLikers.text( '('+numLikes+')' );
    },

    changeDomToLiked: function ( numLikes, id ) {
        var $likeBtn = this.$el.find( '#commentsLike_'+id ),            
            unlike = $likeBtn.data( 'unlike' ),
            like = $likeBtn.data( 'like' ),
            $numLikers = this.$el.find( '#likeCounts_'+id );
            $mask = $likeBtn.closest( '.likeAndCommentsWrapper' ).find( '.mask' ),            
            m = this.CareerMomentsProfileModel.toJSON();
        
        if ( m.isLiked ) {
            $likeBtn.contents()[ 1 ].textContent = unlike;
        } else {
            $likeBtn.contents()[ 1 ].textContent = like;
        }

        $mask.hide();
        G5.util.hideSpin( $mask );
        
        // this should update the text as well like vs likes? for likeCountStatus
        if( numLikes === 1 ) { 
            // set to like
            $numLikers.text( '('+numLikes+')' );
        }else{ 
            // set to likes
            $numLikers.text( '('+numLikes+')' );
        }

        $numLikers.text( '('+numLikes+')' );
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

    doContributeComment: function( e ) {
        e.preventDefault();
        var that = this,
        $tar = $( e.target ).next( '.comment-block' );
        
        if ( $tar ) {
            $tar.show();
        }

        //slide down the input
        $tar.find('.commentInputWrapper').slideDown(
            function () {
                var $this = $( this ); // .commentInputWrapper
                var $container = $this.closest( '.likeAndCommentsWrapper' );

                
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

    doCommentKeydown: function ( e ) {
        var $inp,
            $cmtForm = this.$el.find( '.publicProfileCommentForm' ),
            jsonUrl = $cmtForm.attr( 'action' ),            
            params = {},
            $commentId = $( e.target ).closest('.likeAndCommentsWrapper').data('commentId');

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

                this.CareerMomentsProfileModel.saveLevelOneComment( params, $inp.val(), $commentId, jsonUrl, function ( cmt ) {
                    // console.log(e);

                    $inp.val( '' ).parent().spin( false );

                } ); // saveComment
            } // if it's not empty

        } // if keydown is enter

    }, // doCommentKeydown    

    // events - save comment
    startSaveComment: function() {
        var $cont = this.$el.find( '.contribCommentWrapper' ),
            $comInp = $cont.find( '.contribCommentInp' ),
            $comFauxInp = $cont.find( '.commentFauxInput' ),
            $subBtn = $cont.find( '.submitContribCommentBtn' ),
            $mask = $cont.find( '.mask' );

        $comInp.attr( 'disabled', 'disabled' );
        $comFauxInp.addClass( 'disabled' );
        $subBtn.attr( 'disabled', 'disabled' );
        $mask.show();
        G5.util.showSpin( $mask );
    },
    endSaveComment: function() {
        var $cont = this.$el.find( '.contribCommentWrapper' ),
            $comInp = $cont.find( '.contribCommentInp' ),
            $comFauxInp = $cont.find( '.commentFauxInput' ),
            $subBtn = $cont.find( '.submitContribCommentBtn' ),
            $mask = $cont.find( '.mask' );

        $comInp.removeAttr( 'disabled' );
        $comFauxInp.removeClass( 'disabled' );
        $subBtn.removeAttr( 'disabled' );
        $mask.hide();
        G5.util.hideSpin( $mask );

        // make sure the state is good after our manipulation above
        this.updateContribComment();
    },
    successSaveComment: function() {
        var that = this,
            $cont = this.$el.find( '.contribCommentWrapper' ),
            $comInp = $cont.find( '.contribCommentInp' );//,
            //$subBtn = $cont.find( '.submitContribCommentBtn' ),
            //$mask = $cont.find( '.mask' );

        $comInp.val( '' );
        this.updateContribCommentsList( true );

        // scroll to comment input container top
        $.scrollTo( $cont, {
            axis: 'y',
            offset: { top: -10 },
            duration: G5.props.ANIMATION_DURATION,
            onAfter: function() {
                // focus the text input
                $comInp.focus();
                // background fade the newest comment
                G5.util.animBg( that.$el.find( '.commentItemWrapper:eq(0) .innerCommentWrapper' ), 'flashBg' );
            }
        } );
    },

    //renderIAmInfo Wrapper
    updateImInfo: function() {        
        var that = this,
        $iamWrapper = this.$el.find ('#iAmWrapper' ),
        immodel = this.CareerMomentsProfileModel.get('iaminfo');

        TemplateManager.get( 'iAmItem', function ( tpl ) {  
             _.each( immodel, function ( iam ) {
                $iamWrapper.append( tpl( iam ) );                
             });          
            
        } );
    },

    // basically render comments
    updateContribCommentsList: function( isLatestOnly ) {
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
            comments = this.CareerMomentsProfileModel.get( 'comments' );//,
            //toAddAt;

        // if(!comments || !comments.length) { return; } // need comments

        TemplateManager.get( 'commentItem', function( tpl ) {
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

            

            // single item
            if( isLatestOnly ) {
                single = $.extend( true, {}, comments[ 0 ] ); // clone

                // get embed HTML - will return null if no support for link
                // template will output just the link if no html
                captureVideoEmbedUrl(
                    single.commentText, // pass in comment
                    function( validEmbedUrl ) {
                        // callback receives valid URL back as argument
                        single.videoWebLink ? single.videoWebLink.push( validEmbedUrl ) : single.videoWebLink = [ validEmbedUrl ];
                    }
                );

                // get embed HTML
                if( single.videoWebLink ) {
                    single.videoHtml = [];

                    _.each( single.videoWebLink, function( url ) {
                        single.videoHtml.push( G5.util.oEmbed( url ) );
                    } );
                }

                // prepend the template
                $cont.prepend( tpl( single ) );

                // if there is HTML5 video, init VideoJS
                if( $cont.find( '#careerMomentContribVideo' + single.commentId ) ) { that.initVideoJs( 'careerMomentContribVideo' + single.commentId ); }
            }
            // full list
            else {
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

                    that.addLevelOneCommentToDom( c, 'multiple' )

                } );

            }

            // embed videos -- this *could* be slow
            $cont.find( '.videoLink' ).each( function() {
                var $vl = $( this ),
                    vUrl = $vl.attr( 'href' ),
                    vHtml = G5.util.oEmbed( vUrl );
                $vl.replaceWith( vHtml );
            } );
        } );
    },

    updateContribComment: function() {
        
        var $inp = this.$el.find( '.contribCommentInp' ),
            maxChars = parseInt( $inp.attr( 'maxlength' ), 10 ),
            $remChars = this.$el.find( '.commentTools .remChars' ),
            $submit = this.$el.find( '.submitContribCommentBtn' ),
            hasInput = $.trim( $inp.val() ).length ? true : false,
            //hasMedia = this.CareerMomentsProfileModel.get( 'commentImage' ) || this.CareerMomentsProfileModel.get( 'commentVideo' ),
            allowSubmit = hasInput;

        // enforce maxlength (ie only)
        if( $.browser.msie && $inp.val().length > maxChars ) {
            $inp.val( $inp.val().slice( 0, maxChars ) );
        }

        // remaining chars
        $remChars.text( $.format.number( maxChars - $inp.val().length ) );

        // enable/disable submit button
        $submit[ allowSubmit ? 'removeAttr' : 'attr' ]( 'disabled', 'disabled' );

    },

    // responsive videojs
    // from: http://daverupert.com/2012/05/making-video-js-fluid-for-rwd
    initVideoJs: function( id ) {
        var idOfVideoElement = id || 'purlDetailVideo';
        // if the video element doesn't exist, we kick out of this method
        if( !$( '#' + idOfVideoElement ).length ) {
            return;
        }
        videojs( '#' + idOfVideoElement );        
    }, 

    initCommentImageUploader: function() {
        var that = this,
            $upInp = this.$el.find( '.uploadInput' ),
            $upBtn = this.$el.find( '.uploadBtn' );

        // proxy the hover on the input to the button
        $upInp.on( 'mouseover mouseout', function( e ) {
            $upBtn[ e.type == 'mouseover' ? 'addClass' : 'removeClass' ]( 'active' );
        } );

        // upload pluggy
        $upInp.fileupload( {
            paramName: 'fileAsset',
            url: G5.props.URL_JSON_CAREERMOMENTS_UPLOAD_PHOTO,
            dataType: 'g5json',
            beforeSend: function() {
                that.trigger( 'start:commentImageUpload' );
            },
            done: function( e, res ) {//console.log(res);
                var data = res.result.data, // abnormal JSON
                    msg = data.messages[ 0 ];

                if( msg.isSuccess ) {
                    if( msg.vidURL ) {
                        // set the video data on the model
                        that.CareerMomentsProfileModel.set( 'commentVideo', {
                            url: msg.vidURL,
                            thumbUrl: msg.thumbNailURL,
                            fileType: msg.fileType
                        } );
                    }
                    else {
                        // set the image data on the model
                        that.CareerMomentsProfileModel.set( 'commentImage', {
                            url: msg.picURL,
                            thumbUrl: msg.thumbNailURL
                        } );
                    }
                    // we know we are in display mode now b/c this was a success
                    // view listens to changes on attachMode to update itself
                    that.CareerMomentsProfileModel.set( 'attachMode', 'display' );
                } else {
                    // ho ho - lazy, but ok I guess, passed QA before
                    that.trigger( 'end:commentImageUpload' );
                    setTimeout( function() {alert( msg.text );}, 100 );
                }
            },
            error: function( xhr, status, error ) {
                // didn't even catch this before
                alert( status + ' - ' + error );
            },
            complete: function( xhr, status ) {
                that.trigger( 'end:commentImageUpload' );
            }
        } );
    },

    // events - fetch comments
    startFetchComments: function() {
        // maybe a spinner someday if for some reason the users
        // are often sitting waiting for the list of comments
        // but really, they will prolly be reading the recog. text
        // and watching the video, when, *poof*, the comments appear

        // since we're dialoging in comments (with someone who has left),
        // I did decided to add a spinner
        var $cont = this.$el.find( '.commentsListWrapper' );
        G5.util.showSpin( $cont );
    },
    endFetchComments: function() {
        // maybe a spinner someday if for some reason the users
        // are often sitting waiting for the list of comments
        // but really, they will prolly be reading the recog. text
        // and watching the video, when, *poof*, the comments appear
    },
    successFetchComments: function() {
        this.updateContribCommentsList();
    },


} );
