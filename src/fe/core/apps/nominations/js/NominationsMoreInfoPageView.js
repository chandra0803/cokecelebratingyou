/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported NominationsMoreInfoPageView */
/*global
$,
_,
Backbone,
G5,
TemplateManager,
NominationsMoreInfoPageModel,
NominationsMoreInfoPageView:true
*/
NominationsMoreInfoPageView = PageView.extend( {
    //init function
    initialize: function( opts ) {
        var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'nominations';

		//this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

		//template names
        this.nominationsMoreInfoTpl = 'nominationsMoreInfoTpl';

        this.nominationsMoreInfoDocTpl = 'attachmentDocTpl';

        this.tplPath = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/';

        this.subTpls;

		this.model = new NominationsMoreInfoPageModel( {} );

        this.opts = this.options.data;

		this.model.loadData();

        //is the text being translated?
        this.translatedText = opts.translatedText || false;

		this.on( 'docUploaded', this.updateDocAttachment, this );
        this.model.on( 'docUploaded', this.updateDocAttachment, this );

        this.on( 'finishedRendering', this.renderCompleteed, this );

		this.model.on( 'translated', function( commentId ) {
            this.updateTranslatedText( commentId );
        }, this );

		this.model.on( 'loadDataFinished', function( info ) {
            that.renderNominationsMoreInfo( info );
        } );

    },
    events: {
         'click  .participant-popover': 'attachParticipantPopover',
         'click  #addAttachment': 'showAttachmentDrawer',
         'click .addUrl': 'addUrlPopover',
         'click .attachUrl': 'checkForRemove',
         'keyup #nominationUrl': 'validateUrl',
         'click .addDoc': 'addDocPopover',

         'click .removeLink': 'attachmentPopover',
         'click .cancelTip': 'cancelQtip',
         'click #removeAttachmentConfirmBtn': 'removeDoc',
         'click #removeAttachmentCancelBtn': 'cancelQtip',
         'click .nominationsMoreInfoSubmit': 'submitForm',
         'click  #nominationDoNotCancelBtn': 'cancelQtip',
         'click  #nominationNewDocDoNotCancelBtn': 'cancelQtip',
         'click  #nominationNewUrlDoNotCancelBtn': 'cancelQtip',
         'click #nominationCancelConfirmBtn': 'cancelForm',
         'click .teamMemberTrigger': 'teamMemberPopover',
          //translate text
         'click .translateTextLink': 'doTranslate',

         // Additional functionality added for Doc and URL upload section. Provide confirmation popups when they try to upload new.
         'click .addUrlNew': 'maxReached',
         'click #nominationNewUrlConfirmBtn': 'attachNewUrlPopover',
         'click .addDocNew': 'maxReached',
         'click #nominationNewDocConfirmBtn': 'attachUploadPopover',
         'click .cancelBtn': 'cancelNominationPopover'
    },

	showAttachmentDrawer: function( e ) {
        var $tar = $( e.currentTarget ),
            $tarChecked = $tar.is( ':checked' ),
            $attachmentDrawer = this.$el.find( '.attachmentDrawer' ),
			$nomLink = $attachmentDrawer.find( '#nominationLink' ),
            $nomUrl = $attachmentDrawer.find( '#nominationLinkUrl' );

			if ( $tarChecked ) {
				if ( this.model.attributes.attachmentName ) {
					$nomLink.parent( 'div' ).addClass( 'validateme' );
				} else {
					$nomUrl.parent( 'div' ).addClass( 'validateme' );
				}
			} else {
				$nomLink.parent( 'div' ).removeClass( 'validateme' );
				$nomUrl.parent( 'div' ).removeClass( 'validateme' );
			}

			$attachmentDrawer.slideToggle( G5.props.ANIMATION_DURATION );
    },

	renderNominationsMoreInfo: function( info ) {
		var that = this,
            tplName = 'nominationsMoreInfo',
            $container = that.$el.find( '.dataWrap' ),
            $commentsData = info;

            that.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/';

		TemplateManager.get( tplName, function( tpl, subTpls ) {
            that.subTpls = subTpls;
			$container.empty().append( tpl( $commentsData ) );

			that.$el.find( '.dataWrap [data-toggle=tooltip]' ).tooltip();
			that.trigger( 'finishedRendering' );
		}, that.tplUrl );
    },

    renderCompleteed: function() {
        var that = this,
        currentModel = this.model;
        // Trigger Doc Upload Checkbox to make default checked 
        if ( currentModel.get( 'minDocsAllowed' ) > 0 ) {
            that.$el.find( '#addAttachment' ).trigger( 'click' );
            that.$el.find( '#addAttachment' ).attr( 'disabled', true );
        }
        that.updateDocAttachment();
    },


	//Custom Added for Tooltip Doc

    maxReached: function( e ) {
        var $tar = $( e.currentTarget );
        e.preventDefault();
        this.destroyQtips();

        if( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, this.$el.find( '.maxUploadReached' ).eq( 0 ).clone( true ), this.$el );
        }
    },

	attachingNewDoc: function( e ) {

    var $tar = $( e.currentTarget ),
            $cont = this.$el.find( '.attachingNewDoc' );

		e.preventDefault();

		if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, this.$el );
        }

    },

	//Custom Added for Tooltip Doc
	attachUploadPopover: function( e ) {

		e.preventDefault();
		$( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );

		this.$el.find( '#nominationsMoreInfoForm .addDoc' ).show();
		this.$el.find( '#nominationsMoreInfoForm .addDocNew' ).hide();
		this.$el.find( '.addDoc' ).trigger( 'click' );
    },

	//Custom Added for Tooltip NewUrl
	attachingNewUrl: function( e ) {
    var $tar = $( e.currentTarget ),
        $cont = this.$el.find( '.attachingNewUrl' );

		e.preventDefault();

		if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, this.$el );
        }
    },

	//Custom Added for Tooltip NewUrl
	attachNewUrlPopover: function( e ) {

		e.preventDefault();
		$( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );

		this.$el.find( '#nominationsMoreInfoForm .addUrl' ).show();
		this.$el.find( '#nominationsMoreInfoForm .addUrlNew' ).hide();
		this.$el.find( '.addUrl' ).trigger( 'click' );

    },
	cancelNominationPopover: function( e ) {
        var $tar = $( e.currentTarget ),
            $cont = this.$el.find( '.cancelNominationPopover' );

		e.preventDefault();

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, this.$el );
        }
    },

    cancelForm: function( e ) {
        var $btn = this.$el.find( '.formSection .cancelBtn' );

        if ( $btn.data( 'url' ) ) {
            e.preventDefault();

            window.location = $btn.data( 'url' );
        }
    },

	cancelQtip: function( e ) {
        e.preventDefault();

        $( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );
    },

    destroyQtips:  function( e ) {
        this.$el.find('.removeLlink .qtip').each(function(){
          $(this).qtip('api').destroy();
        });
    },

	addUrlPopover: function( e ) {
        var $tar = $( e.currentTarget ),
            $popoverCont = this.$el.find( '.addUrlPopover' ),
            $attachBtn = this.$el.find( '.attachUrl' );

        e.preventDefault();

        $attachBtn.attr( 'disabled', '' );

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $popoverCont, this.$el );
        }

        this.validateUrl();
    },

	attachmentPopover: function( e ) {
        e.preventDefault();
        var $tar = $( e.target ),        
        $cont = this.$el.find( '.removeAttachmentPopover' );
        $tar.parent().attr( 'data-linkId' );
        $cont.find( '#removeAttachmentConfirmBtn' ).attr( 'data-linkid', $tar.parent().attr('data-linkid') );

        this.destroyQtips();

        if( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, this.$el.find( '.removeAttachmentPopover' ).eq( 0 ).clone( true ), $tar );
        }
    },

    removeDoc: function( e ) {
        e.preventDefault();
        var $tar = $(e.currentTarget);
        $linkId = $tar.closest('button').attr('data-linkid');
        $btn = this.$el.find( '.removeLink' );


        if( $btn.hasClass( 'docAdded' ) ) {
            this.model.removeUploadedDoc( this.claimId, $linkId );
        } else {
            //this.doUnattachUrlDoc();
            this.model.deleteAttachments( $linkId );
        }

        //$( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );
        $( '.addDoc' ).show();
        //$( '#nominationsTabWhyView .addDocNew' ).hide();
        $( '.addUrl' ).show();
        //$( '#nominationsTabWhyView .addUrlNew' ).hide();
    },

	doUnattachUrlDoc: function( e ) {
        var $attachCont = this.$el.find( '.nominationDisplayAttached' ),
            $attach = $attachCont.find( '.nominationDisplayLink' ),
            $hiddenInp = $attachCont.find( '#nominationLink' ),
			$attachmentLinkUrl = $attachCont.find( '#nominationLinkUrl' ),
            $removeLink = $attachCont.find( '.removeLink' );

        e.preventDefault();

        $attach.html( '' );
        $hiddenInp.val( '' );
        $removeLink.hide();
		$hiddenInp.attr( 'value', '' );
		$attachmentLinkUrl.attr( 'value', '' );

		$( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );
        $( '#nominationsMoreInfoForm .addDoc' ).show();
        $( '#nominationsMoreInfoForm .addDocNew' ).hide();
        $( '#nominationsMoreInfoForm .addUrl' ).show();
        $( '#nominationsMoreInfoForm .addUrlNew' ).hide();
    },

	addDocPopover: function( e ) {
        var that = this,
            $form = this.$el.find( '#nominationDocUpload' ),
            $tar = $( e.currentTarget ),
            $inp = $tar.parents().find( '#nominationDoc' ),
            $container = this.$el.find( '.nominationDisplayAttached' );

        e.preventDefault();

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, this.$el.find( '.addDocPopover' ) );

            $form.fileupload( {
                url: G5.props.URL_JSON_NOMINATIONS_MORE_INFO_UPLOAD,
                dataType: 'g5json',
                beforeSend: function() {
                    $tar.qtip( 'hide' );
                    G5.util.showSpin( $container );
                },
                done: function( e, data ) {
                    var msg = data.result.data.messages[ 0 ];
                    console.log( data );
                    if ( msg.isSuccess ) {
                        // set the document data on the model
                        that.model.setAttachmentLink( msg.nominationLink, null, msg.fileName, msg.linkId );
                        //that.model.set( 'attachedDoc', msg.nominationLink );
                        //that.model.set( 'fileName', msg.fileName );
                        console.log( that.model );
                        $inp.val( '' );
                        that.trigger( 'docUploaded' );

						//Custom Added for Tooltip Doc
						//$( '#nominationsMoreInfoForm .addDoc' ).hide();
						//$( '#nominationsMoreInfoForm .addDocNew' ).show();

                    } else {
                        console.log( msg.text );
                    }

                    G5.util.hideSpin( $container );
                }
            } );
        }
    },

	updateDocAttachment: function() {        
        var that = this,
            tplName = 'nominationsMoreInfoDocUpload',
            $container = that.$el.find( '.attachmentSection' );            
            $modelObj = that.model;

            that.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/';

        TemplateManager.get( tplName, function( tpl, subTpls ) {
            $container.empty().append(tpl ( that.model.toJSON() ));            
        }, that.tplUrl );

        if ( $modelObj.get('maxDocsAllowed') == $modelObj.get('updatedDocCount' ) ) {
            $( '#nominationsMoreInfoPageView .addDoc, #nominationsMoreInfoPageView .addUrl' ).hide();
            $( '#nominationsMoreInfoPageView .addDocNew,#nominationsMoreInfoPageView .addUrlNew' ).show();
        }
        else {             
           $( '#nominationsMoreInfoPageView .addDoc, #nominationsMoreInfoPageView .addUrl' ).show();
           $( '#nominationsMoreInfoPageView .addDocNew,#nominationsMoreInfoPageView .addUrlNew' ).hide(); 
        }

        /*var $attachCont = this.$el.find( '.nominationDisplayAttached' ),
            $attach = $attachCont.find( '.nominationDisplayLink' ),
            $hiddenInp = this.$el.find( '#nominationLink' ),
            $removeLink = $attachCont.find( '.removeLink' ),
            docUrl = this.model.get( 'attachedDoc' ),
            fileName = this.model.get( 'fileName' ),
            $urlInp = this.$el.find( '#nominationUrl' ),
			$urlLinkInp = this.$el.find( '#nominationLinkUrl' );

        G5.util.hideSpin( $attachCont );

        $attach.html( fileName );
        $hiddenInp.attr( 'value', fileName );
		$urlLinkInp.attr( 'value', docUrl );


        $removeLink.show().addClass( 'docAdded' );
        $urlInp.val( '' );
        $urlInp.parent( '.validateme' ).removeClass( 'validateme' );
        $urlInp.parent( 'div' ).removeClass( 'error' );
        $urlInp.closest( '.qtip' ).qtip( 'hide' );*/

    },

	submitForm: function( e ) {
		var $form = this.$el.find( '#nominationsMoreInfoForm' ),
            formfields = $form.serializeArray();

            e.preventDefault();
            console.log( formfields );
        if ( this.validateForm() ) {
            $form.submit();
        }
    },

	validateForm: function() {
        var $form = this.$el.find( 'form' );

        if ( !G5.util.formValidate( $form.find( '.validateme' ) ) ) {
            return false;
        } else {
            return true;
        }
    },

    checkForRemove: function( e ) {
        var $removeLink = this.$el.find( '.removeLink' ),
            event = e;

        if( $removeLink.hasClass( 'docAdded' ) ) {
            this.containerView.model.removeUploadedDoc( this.promoIds.claimId, null, true, event );
        }
        /*else if( $removeLink.hasClass( 'urlAdded' ) ) {
            this.containerView.model.deleteAttachments( $removeLink.attr('data-linkid') );
        }*/
        else {
            this.doAttachUrl( e );            
        }
    },

	validateUrl: function() {
        var $inp = this.$el.find( '#nominationUrl' ),
            $attachBtn = this.$el.find( '.attachUrl' ),
            urlRe = /^(https?:\/\/)?[\da-z\.-]+\.[a-z\.]{2,6}.*$/;

        $attachBtn[ urlRe.test( $inp.val() ) ? 'removeAttr' : 'attr' ]( 'disabled', 'disabled' );
    },

	doAttachUrl: function( e ) {

        var $tar = $( e.currentTarget ),
            $inp = $tar.parents().find( '.nominationUrlAdd' ),
            $attachCont = this.$el.find( '.nominationDisplayLink' ),
            $hiddenInp = this.$el.find( '#nominationUrl' ),
            $docInp = this.$el.find( '#nominationLink' ),
            $removeLink = this.$el.find( '.removeLink' );

        $attachCont.html( $inp.val() ).attr( 'title', $inp.val() );
        $hiddenInp.val( $inp.val() );
        if( $hiddenInp.parent( 'div' ).hasClass( 'validateme' ) === false ) {
            $hiddenInp.parent( 'div' ).addClass( 'validateme' );
        }
        this.model.setAttachmentLink( null, $inp.val(), null, null );
        $removeLink.show().addClass( 'urlAdded' );

        $tar.closest( '.qtip' ).qtip( 'hide' );
        $inp.val( '' );

        $docInp.val( '' );
        $docInp.parent( '.validateme' ).removeClass( 'validateme' );
        $docInp.parent( 'div' ).removeClass( 'error' );
        $docInp.closest( '.qtip' ).qtip( 'hide' );

        this.trigger( 'docUploaded' );

        /*var $tar = $( e.currentTarget ),
            $inp = $tar.parents().find( '#nominationUrl' ),
            $attachCont = this.$el.find( '.nominationDisplayAttached span' ),
            $hiddenInp = this.$el.find( '#nominationLinkUrl' ),
            $removeLink = this.$el.find( '.removeLink' );

        $attachCont.html( $inp.val() ).attr( 'title', $inp.val() );
        $hiddenInp.val( $inp.val() );
        $removeLink.show();

        $tar.closest( '.qtip' ).qtip( 'hide' );
        $inp.val( '' );
		//Custom Added for Tooltip NewUrl
		$( '#nominationsMoreInfoForm .addUrl' ).hide();
		$( '#nominationsMoreInfoForm .addUrlNew' ).show();*/
    },

	attachParticipantPopover: function( e ) {
        var $tar = $( e.target ),
            isSheet = ( $tar.closest( '.modal-stack' ).length > 0 ) ? { isSheet: true }  :  { isSheet: false };

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
    teamMemberPopover: function( e ) {
        var $tar = $( e.target ),
            $cont = $( $tar ).siblings( '.teamMembersTooltip' ).clone(); //special for team members list
            console.log( $cont );
            console.log( $tar );
        e.preventDefault();

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, this.$el, 'approvalsTeamTooltip' );
        }
    },
    //events- translate text
    doTranslate: function( e ) {
        var $tar = $( e.target ),
            $attr = $( '.reason-data' ).attr( 'data-reasonId' );

        this.model.translateData( $attr );

        e.preventDefault();
        $tar.hide();

        // $tar.replaceWith('<span class="translateLinkDisabled">'+$tar.text()+'</span>');
    },

    updateTranslatedText: function( data ) {
        var that = this,
            pageFields = this.$el.find( '.translate' );


        $( data.translation.fields ).each( function( index, value ) {
            if ( value.name === 'comment' ) {
                that.$el.find( '.reason-data' ).text( value.text );
            }
            if ( value.name === 'approverComment' ) {
                that.$el.find( '.approversMessage' ).text( value.text );
            }
            pageFields.each( function( indexp, valuep ) {
                console.log( $( valuep ).data( 'fieldId' ) );
                if ( $( valuep ).data( 'fieldid' ) > 0 && $( valuep ).data( 'fieldid' ) === value.fieldId ) {
                    $( valuep ).text( value.text );
                }
            } );

        } );
    },

    attachPopover: function( $trig, content, container, extraClass ) {
        $trig.qtip( {
            content: { text: content },
            position: {
                my: 'left center',
                at: 'right center',
                container: container,
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
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-light ' + extraClass,
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            }
        } );
    }
} );
