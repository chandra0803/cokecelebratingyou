/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported NominationsSubmitTabWhyView */
/*global
console,
$,
_,
G5,
PageView,
TemplateManager,
NominationsSubmitTabWhyView:true
*/
NominationsSubmitTabWhyView = PageView.extend( {
    initialize: function ( opts ) {
        var self = this;

        // NominationsSubmitPageView parent container for this tab
        this.containerView = opts.containerView;
        this.promoIds = opts.containerView.allIds;

        this.selectedPromoModel = this.containerView.model.get( 'promotion' );

         //template names
        this.tpl = 'nominationsSubmitWhyTpl';
        this.tplPath = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/';
        this.subTpls;
        this.on( 'docUploaded', this.updateDocAttachment, this );
        this.containerView.model.on( 'docUploaded', this.updateDocAttachment, this );

        //rendering this once tabs are initialized instead because of the rich text editor.
        //this.containerView.wizTabs.on('tabsInitialized', this.render, this);
        //
        this.containerView.model.loadCustomElements( this.promoIds );

        this.containerView.model.on( 'customElementsLoaded', this.render, this );

        this.containerView.model.on( 'docRemoved', this.doUnattachUrlDoc, this );

        this.containerView.model.on( 'docReplaced', function( $target ) {
            self.doAttachUrl( $target );
        } );

    },

    events: {
        'click #addAttachment': 'showAttachmentDrawer',

        'keyup .customWhyComment': 'updateContribComment',
        'blur .customWhyComment': 'updateContribComment',
        'paste .customWhyComment': 'updateContribComment',

    'click .addUrl': 'addUrlPopover',
    'click .attachUrl': 'checkForRemove',
    'keyup .nominationUrlAdd': 'validateUrl',
    //Custom Added for Tooltip addUrlNew
    'click .addUrlNew': 'maxReached',
    'click #nominationNewUrlConfirmBtn': 'attachNewUrlPopover',

    'keyup .numericInput': 'validateNumeric',

    'click .addDoc': 'addDocPopover',
    'click #attachDocConfirmSection': 'addDocPopover',
    //Custom Added for Tooltip Doc
    'click .addDocNew': 'maxReached',
    'click #nominationNewDocConfirmBtn': 'attachUploadPopover',    
    'click  .cancelTip': 'cancelQtip',
    'click  #nominationNewDocDoNotCancelBtn': 'cancelQtip',
    'click  #nominationNewUrlDoNotCancelBtn': 'cancelQtip',
    'click .removeLink': 'attachmentPopover',
    'click #removeAttachmentConfirmBtn': 'removeDoc',
    'click #removeAttachmentCancelBtn': 'cancelQtip',
    'click #removeAllDocsConfirmBtn': 'addUrlPopoverConfirmation',
    'click #removeAllDocsCancelBtn': 'cancelQtip',
    },

    render: function() {
        var $container = this.$el,
            customElModel = this.containerView.model.get( 'customElements' ),
            self = this;

        TemplateManager.get( this.tpl, function( tpl, vars, subTpls ) {

            self.subTpls = subTpls;

            $container.empty().append( tpl( self.containerView.model.toJSON() ) );

            self.orderFormFields();

            self.$el.find( '.richtext' ).htmlarea( G5.props.richTextEditor );

            if( customElModel && customElModel.customWhyId ) {
                self.updateContribComment();
            }

            self.containerView.initButtons();

            self.$el.find( '.datepickerTrigger' ).datepicker( 'setEndDate', '+0d' );

            if( self.selectedPromoModel.addAttachment ) {
                if ( self.selectedPromoModel.minDocsAllowed > 0 ) {
                    self.$el.find( '#addAttachment' ).trigger( 'click' );
                    self.$el.find( '#addAttachment' ).attr( 'disabled', true );
                    self.showEditAttachment();
                }                
            }

            if ( self.containerView.model.get('promotion').nominationLinks ) {
                self.updateDocAttachment();
            }

        }, this.tplPath );
    },

    orderFormFields: function() {
        var $defaultSection = this.$el.find( '.defaultWhySection' ),
            $customSection = this.$el.find( '.customElementsWrap' ),
            promotion = this.selectedPromoModel;

        if( promotion.customBeforeDefault ) {
            $customSection.insertBefore( $defaultSection );
        } else {
            $defaultSection.insertBefore( $customSection );
        }
    },

    showAttachmentDrawer: function( e ) {
        var $attachmentDrawer = this.$el.find( '.attachmentDrawer' ),
            $nomLink = $attachmentDrawer.find( '#nominationLink' ),
            $nomUrl = $attachmentDrawer.find( '#nominationUrl' );

        if( this.selectedPromoModel.nominationUrl ) {
            $nomUrl.parent( 'div' ).addClass( 'validateme' );
        } else {
            $nomLink.parent( 'div' ).addClass( 'validateme' );
        }

        $attachmentDrawer.slideToggle( G5.props.ANIMATION_DURATION );


    },

    showEditAttachment: function() {
        var $attachCont = this.$el.find( '.nominationDisplayAttached' ),
            $nomLink = $attachCont.find( '#nominationLink' ),
            $nomUrl = $attachCont.find( '#nominationUrl' ),
            $removeLink = $attachCont.find( '.removeLink' );

        if( this.selectedPromoModel.nominationUrl ) {
            $nomUrl.show();
            $removeLink.show();
        } else if( this.selectedPromoModel.nominationLink ) {
            $nomLink.show();
            $removeLink.show();
        }
    },


    //Custom Added for Tooltip Doc
    maxReached: function( e ) {
        var $tar = $( e.currentTarget );
        e.preventDefault();
        
        if( !$tar.data( 'qtip' ) ) {
            this.containerView.attachPopover( $tar, this.$el.find( '.maxUploadReached' ).eq( 0 ).clone( true ), this.$el );
        }
    },

    attachingNewDoc: function( e ) {

        var $tar = $( e.currentTarget ),
            $cont = this.$el.find( '.attachingNewDoc' );

        e.preventDefault();

        if( !$tar.data( 'qtip' ) ) {
            this.containerView.attachPopover( $tar, $cont, this.$el );
        }

    },

    //Custom Added for Tooltip Doc
    attachUploadPopover: function( e ) {

       var $tar = $( e.currentTarget );

        e.preventDefault();
        $( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );

        //this.containerView.model.removeAttachedDocuments();
        $tar.parents().find( '#nominationsTabWhyView .addDoc' ).show();
        //$tar.parents().find( '#nominationsTabWhyView .addDocNew' ).hide();
        //$tar.parents().find( '#nominationsTabWhyView .addDoc' ).trigger( 'click' );
    },

    //Custom Added for Tooltip NewUrl
    attachingNewUrl: function( e ) {
        var $tar = $( e.currentTarget ),
            $cont = this.$el.find( '.attachingNewUrl' );

        e.preventDefault();

        if( !$tar.data( 'qtip' ) ) {
            this.containerView.attachPopover( $tar, $cont, this.$el );
        }

    },

    //Custom Added for Tooltip NewUrl
    attachNewUrlPopover: function( e ) {
       this.containerView.model.removeAttachedDocuments(); 
       var $tar = $( e.currentTarget );

        e.preventDefault();
        $( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );


        $tar.parents().find( '#nominationsTabWhyView .addUrl' ).show();
        //$tar.parents().find( '#nominationsTabWhyView .addUrlNew' ).hide();
        //$tar.parents().find( '#nominationsTabWhyView .addUrl' ).trigger( 'click' );

    },

    cancelQtip: function( e ) {
        e.preventDefault();

        $( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );
    },

    addUrlPopoverConfirmation: function( e ) {
        var $tar = $( e.currentTarget ),
            $uploadLimitCheck = this.checkForUploadLimit();
            $urlCont = this.$el.find( '#attachUrlSection' );                        

        e.preventDefault();
        if( $uploadLimitCheck ) {
            $urlCont.show();     
        }
        else {
            alert('Maximum Limit Reached');
        }
        
    },

    addUrlPopover: function( e ) {        
        var $tar = $( e.currentTarget ),
            $popoverCont = this.$el.find( '.addUrlPopover' ),
            $maxPopCont = this.$el.find( '.maxDocPopover' ),
            $maxWrapper = this.$el.find( '.maxReachedWrapper' ),
            $uploadLimitCheck = this.checkForUploadLimit(),
            $attachBtn = this.$el.find( '.attachUrl' );

        e.preventDefault();


        if( $uploadLimitCheck ) {
            $attachBtn.attr( 'disabled', '' );

            if( !$tar.data( 'qtip' ) ) {
                this.containerView.attachPopover( $tar, $popoverCont, this.$el );
            }
            this.validateUrl();     
        }
        
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
        this.containerView.model.setAttachmentLink( null, $inp.val(), null, null );
        $removeLink.show().addClass( 'urlAdded' );

        $tar.closest( '.qtip' ).qtip( 'hide' );
        $inp.val( '' );

        $docInp.val( '' );
        $docInp.parent( '.validateme' ).removeClass( 'validateme' );
        $docInp.parent( 'div' ).removeClass( 'error' );
        $docInp.closest( '.qtip' ).qtip( 'hide' );

        this.trigger( 'docUploaded' );

        //$tar.parents().find( '#nominationsTabWhyView .addDoc' ).hide();
        //$tar.parents().find( '#nominationsTabWhyView .addDocNew' ).show();

        //Custom Added for Tooltip NewUrl
        //$tar.parents().find( '#nominationsTabWhyView .addUrl' ).hide();
        //$tar.parents().find( '#nominationsTabWhyView .addUrlNew' ).show();
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
        var $inp = this.$el.find( '.nominationUrlAdd' ),
            $attachBtn = this.$el.find( '.attachUrl' ),
            urlRe = /^(https?:\/\/)?[\da-z\.-]+\.[a-z\.]{2,6}.*$/;

        $attachBtn[ urlRe.test( $inp.val() ) ? 'removeAttr' : 'attr' ]( 'disabled', 'disabled' );
    },

    validateNumeric: function( e ) {
        var $tar = $( e.target ),
            v = $tar.val(),
            decPlaces = $tar.parents( '.numericInput' ).data( 'maxdecimal' ),
            reStr = '^-?(\\d{1,9})?(\\.\\d{0,' + ( decPlaces ) + '})?$',
            regEx = new RegExp( reStr );

        if( !regEx.test( v ) ) {
            // if not matching our regex, then set the value back to what it was
            // or empty string if no last val
            $tar.val( $tar.data( 'lastVal' ) || '' );
        } else {
            // store this passing value, we will use this to reset the field
            // if a new value doesn't match
            $tar.data( 'lastVal', v );
        }
    },

    checkForUploadLimit: function(  ) {
        if ( this.selectedPromoModel.updatedDocCount < this.selectedPromoModel.maxDocsAllowed ) {
            return true;
        }
        else {            
            return false;
        }
    },

    addDocPopover: function( e ) {
        var self = this,
            $form = this.$el.find( '#nominationDocUpload' ),
            $tar = $( e.currentTarget ),
            $btns = this.$el.find( '.stepContentControls button' ),
            $inp = $tar.parents().find( '.nominationLink' ),            
            $container = this.$el.find( '.nominationDisplayAttached' ),            
            $uploadLimitCheck = self.checkForUploadLimit();
            

        e.preventDefault();

        if ( $uploadLimitCheck ) {                        
            if( !$tar.data( 'qtip' ) ) {                
                this.containerView.attachPopover( $tar, this.$el.find( '.addDocPopover' ) );
                $form.fileupload( {
                    autoUpload:false,                                
                    url: G5.props.URL_JSON_NOMINATIONS_UPLOAD_DOC,
                    formData: {
                        claimId: this.promoIds.claimId
                    },
                    dataType: 'g5json',
                    beforeSend: function() {
                        $tar.qtip( 'hide' );
                        G5.util.showSpin( $container );
                        $btns.attr( 'disabled', true );
                    },

                    add: function ( e, data ) {
                        var f = data.files[ 0 ],
                            fileName = f.name;

                       data.submit();
                    },


                    done: function( e, data ) {
                        var msg = data.result.data.messages[ 0 ];

                        if( msg.isSuccess ) {
                            // set the document data on the model
                            self.containerView.model.setAttachmentLink( msg.nominationLink, null, msg.fileName, msg.linkId );
                            $inp.val( '' );

                            //self.trigger( 'docUploaded' );

                            //Custom Added for Tooltip Doc
                            //$tar.parents().find( '#nominationsTabWhyView .addUrl' ).hide();
                            //$tar.parents().find( '#nominationsTabWhyView .addUrlNew' ).show();

                        }
                        else {
                            alert( msg.text );
                        }

                        $btns.attr( 'disabled', false );

                        G5.util.hideSpin( $container );
                    },

                    complete: function( xhr, status ) {                    
                        self.trigger( 'docUploaded' );
                    }

                } );
            }
        }
        else {             
           console.log('Reached Maximum Limit');            
        }
        
    },

    // *******************
    // POPOVERS
    // *******************

    attachmentPopover: function( e ) {
        var $tar = $( e.target ),        
        $cont = this.$el.find( '.removeAttachmentPopover' );
        $tar.parent().attr( 'data-linkId' );
        $cont.find( '#removeAttachmentConfirmBtn' ).attr( 'data-linkid', $tar.parent().attr('data-linkid') );


        console.log($tar.data( 'qtip' ));

        if( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, this.$el.find( '.removeAttachmentPopover' ).eq( 0 ).clone( true ), $tar, true );
        }
    },

    attachPopover: function( $trig, content, container, canDestroy ) {
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
                delay: 100
            },
            events: {
                hidden: function(event, api) {
                  // Destroy it immediately
                  if ( canDestroy ) {
                    api.destroy(true);
                  }                  
                }
            },
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-light',
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            }
        } );
    },

    destroyQtips:  function( e ) {
        console.log('Destroying the Tooltip');
        this.$el.find('.removeLlink .qtip').each(function(){          
          $(this).qtip('destroy', true);
        });
    },

    cancelQtip: function( e ) {
        e.preventDefault();
        $( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );
    },

    updateDocAttachment: function() {
        var self = this,
        $container = this.$el.find( '.attachmentSection' ),
        $promoObj = self.containerView.model.get('promotion');
        $container.empty().append(self.subTpls.attachmentDocTpl ( self.containerView.model.toJSON() ));

        if ( $promoObj.maxDocsAllowed == $promoObj.updatedDocCount ) {
            $( '#nominationsTabWhyView .addDoc, #nominationsTabWhyView .addUrl' ).hide();
            $( '#nominationsTabWhyView .addDocNew,#nominationsTabWhyView .addUrlNew' ).show();
        }
        else {
           $( '#nominationsTabWhyView .addDoc, #nominationsTabWhyView .addUrl' ).show();
            $( '#nominationsTabWhyView .addDocNew,#nominationsTabWhyView .addUrlNew' ).hide(); 
        }

        /*var $attachCont = this.$el.find( '.nominationDisplayAttached' ),
            $attach = $attachCont.find( '.nominationDisplayLink' ),
            $hiddenInp = $attachCont.find( '#nominationLink' ),
            $removeLink = $attachCont.find( '.removeLink' ),
            docUrl = this.selectedPromoModel.nominationLink,
            docName = this.selectedPromoModel.fileName,
            $urlInp = $attachCont.find( '#nominationUrl' );

            console.log(this.uploadFileData);

        G5.util.hideSpin( $attachCont );
        if( $hiddenInp.parent( 'div' ).hasClass( 'validateme' ) === false ) {
            $hiddenInp.parent( 'div' ).addClass( 'validateme' );
        }

        

        $attach.html( docName );
        $hiddenInp.val( docUrl );

        $removeLink.show().addClass( 'docAdded' );
        $urlInp.val( '' );
        $urlInp.parent( '.validateme' ).removeClass( 'validateme' );
        $urlInp.parent( 'div' ).removeClass( 'error' );
        $urlInp.closest( '.qtip' ).qtip( 'hide' );
        console.log( $urlInp.closest( '.qtip' ) );*/
    },

    removeDoc: function( e ) {
        e.preventDefault();
        var $tar = $(e.currentTarget);
        $linkId = $tar.closest('button').attr('data-linkid');
        $btn = this.$el.find( '.removeLink' );


        if( $btn.hasClass( 'docAdded' ) ) {
            this.containerView.model.removeUploadedDoc( this.promoIds.claimId, $linkId );
        } else {
            //this.doUnattachUrlDoc();
            this.containerView.model.deleteAttachments( $linkId );
        }

        //$( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );
        $( '#nominationsTabWhyView .addDoc' ).show();
        //$( '#nominationsTabWhyView .addDocNew' ).hide();
        $( '#nominationsTabWhyView .addUrl' ).show();
        //$( '#nominationsTabWhyView .addUrlNew' ).hide();
    },

    doUnattachUrlDoc: function() {
        var $attachCont = this.$el.find( '.nominationDisplayAttached' ),
            $attach = $attachCont.find( '.nominationDisplayLink' ),
            $inpDoc = $attachCont.find( '#nominationLink' ),
            $inpUrl = $attachCont.find( '#nominationUrl' ),
            $removeLink = $attachCont.find( '.removeLink' );

        $attach.html( '' );
        $inpDoc.val( '' );
        $inpUrl.val( '' );

        $removeLink.hide().removeClass( 'docAdded urlAdded' );

        this.containerView.model.setAttachmentLink( null, null, null );


    },

    updateContribComment: function() {
        var $inp = this.$el.find( '.customWhyComment' ),
            maxChars = parseInt( $inp.data( 'maxChars' ), 10 ),
            $remChars = this.$el.find( '.commentTools .remChars' );

        // enforce maxlength (ie only)
        if( $.browser.msie && $inp.val().length > maxChars ) {
            $inp.val( $inp.val().slice( 0, maxChars ) );
        }

        // remaining chars
        $remChars.text( $.format.number( maxChars - $inp.val().length ) );
    },

    updateTabName: function( tab ) {
        var $fromTab = this.containerView.$el.find( ".wtTabVert[data-tab-name='" + tab + "']" );

        $fromTab.find( '.wtvTabName' ).show();
    },

    // validate the state of elements within this tab
    validate: function() {
        var $validate = this.$el.find( '.validateme:visible' ),
            isValid = G5.util.formValidate( $validate );

        // failed generic validation tests (requireds mostly)
        if( !isValid ) {
            return { msgClass: 'msgGenericError', valid: false };
        }
        else {
            return { valid: true };
        }

    }

} );
