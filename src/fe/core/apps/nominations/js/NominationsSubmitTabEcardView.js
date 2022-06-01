/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported NominationsSubmitTabEcardView, $activeCard */
/*global
console,
$,
_,
G5,
PageView,
DrawToolView,
TemplateManager,
NominationsSubmitTabEcardView:true
*/
NominationsSubmitTabEcardView = PageView.extend( {
    initialize: function ( opts ) {

        // NominationsSubmitPageView parent container for this tab
        this.containerView = opts.containerView;

        this.selectedPromoModel = this.containerView.model.get( 'promotion' );

         //template names
        this.tpl = 'nominationsSubmitEcardTpl';
        this.tplPath = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/';

        this.render();

    },

    events: {
        //eCards        
        'click #recognitionPageEcardSectionTabSelectedTabs a': 'handleEcardsTabsClick',
    },

    render: function() {
        var $container = this.$el,
            that = this;

        TemplateManager.get( this.tpl, function( tpl ) {

            $container.empty().append( tpl( that.selectedPromoModel ) );

            that.initEcards();

        }, this.tplPath );
    },

    initEcards: function() {
        var that = this,
            $dtEl;

            if ( this.drawToolView ) { // remove
                this.drawToolView.undelegateEvents();
                this.drawToolView.$el.removeData().unbind();
                this.drawToolView.remove();
                this.drawToolView = null; // deref
            }

            $dtEl = this.$el.find( '#drawToolShell' );

            if ( !$dtEl.length ) {
                this.$el.append( '<div id="drawToolShell"/>' );
                $dtEl = this.$el.find( '#drawToolShell' );
            }

            this.drawToolView = new DrawToolView( {
                el: $dtEl,
                parentView: that,
                drawingToolSettings: {
                    eCards: that.selectedPromoModel.eCards,
                    // WIP #62895 Changes start
                    // Please note that there also other changes inside the file related to this feature. Search the repo for commit with message "WIP #62895 - Meme & Sticker tool changes" to see all the changes
                    memes: that.selectedPromoModel.memes,
                    memesActive: that.selectedPromoModel.memesActive,
                    stickers: that.selectedPromoModel.stickers,
                    stickersActive: that.selectedPromoModel.stickersActive,
                    // WIP #62895 Changes end
                    // Please note that there also other changes inside the file related to this feature. Search the repo for commit with message "WIP #62895 - Meme & Sticker tool changes" to see all the changes
                    drawToolCardType: that.selectedPromoModel.cardType || 'none',
                    drawToolVideoUrl: that.selectedPromoModel.videoUrl || '',
                    drawToolCardId: that.selectedPromoModel.cardId || '',
                    drawToolCardUrl: that.selectedPromoModel.cardUrl || '',
                    drawingData: that.selectedPromoModel.drawingData || '',
                    canDraw: that.selectedPromoModel.drawToolSettings.canDraw,
                    canUpload: that.selectedPromoModel.drawToolSettings.canUpload,
                    sizes: that.selectedPromoModel.drawToolSettings.sizes,
                    colors: that.selectedPromoModel.drawToolSettings.colors,
                    memeFontSizes: that.selectedPromoModel.drawToolSettings.memeFontSizes,
                    memeFontSizeDefault: that.selectedPromoModel.drawToolSettings.memeFontSizeDefault,
                    stickerAddLimit: that.selectedPromoModel.drawToolSettings.stickerAddLimit,
                    canAddStickerOnUpload: that.selectedPromoModel.drawToolSettings.canAddStickerOnUpload,
                    allowOwnMemeUpload: that.selectedPromoModel.allowOwnMemeUpload,
                    allowYourOwnCard: that.selectedPromoModel.allowYourOwnCard
                }
            } );

            if( !this.selectedPromoModel.memesActive ) {
                this.$el.find( '#recognitionPageEcardSectionTabSelectedTabs' ).hide();
            } else {
                this.$el.find( '#recognitionPageEcardSectionTabSelectedTabs' ).show();
            }  

            this.drawToolView.on( 'cardChanged', this.changeEcard, this );

            this.drawToolView.on( 'renderedCards', function() {
                if ( that.selectedPromoModel.isEditMode ) {
                    that.updateTabName( 'stepEcard' );
                }

            } );
            this.containerView.model.on( 'saveEnded', this.drawToolView.cardCycleInit, this.drawToolView );
    },

    changeEcard: function() {
        var drawing = this.$el.find( '#drawToolDrawingData' ).val(),
            card = this.$el.find( '#drawToolCardData' ).val();

        this.containerView.model.setEcard( this.drawToolView.model, drawing, card );
    },

    updateTabName: function( tab ) {
        var $fromTab = this.containerView.$el.find( ".wtTabVert[data-tab-name='" + tab + "']" ),
            $selectedText = this.$el.find( '.cardSelected' ).text(),
            $notSelectedText = this.$el.find( '.cardNotSelected' ).text(),
            $activeCard = this.$el.find( '#bgContainer img:visible' );

        $fromTab.find( '.wtvTabName' ).show();

        if ( this.selectedPromoModel.cardUrl === null || this.$el.find( '#addAnECard' ).is( ':checked' ) ) {
            $fromTab.find( '.wtvDisplay' ).text( $notSelectedText );
            $fromTab.find( '.wtvTabName' ).hide();
        } else {
            $fromTab.find( '.wtvDisplay' ).text( $selectedText );
            $fromTab.find( '.wtvTabName' ).hide();
        }

        console.log( $activeCard );
    },

    save: function( fromName, toName, ids, draft ) {
        var drawing = this.$el.find( '#drawToolDrawingData' ).val(),
            card = this.$el.find( '#drawToolCardData' ).val();

        this.containerView.model.setEcard( this.drawToolView.model, drawing, card );

        this.containerView.model.save( fromName, toName, ids,  draft /*isDraft*/ );
    },

    saveAsDraft: function( fromName, toName, ids, draft ) {
        var drawing = this.$el.find( '#drawToolDrawingData' ).val(),
            card = this.$el.find( '#drawToolCardData' ).val();

        this.containerView.model.setEcard( this.drawToolView.model, drawing, card );

        this.containerView.model.save( fromName, toName, ids,  draft /*isDraft*/ );
    },
    checkCards: function() {
            var $activeCard = this.$el.find( '#bgContainer img' );
            var fail = false;

            if ( $activeCard.attr( 'src' ).length > 0 ) {
                return fail;
            } else if ( this.$el.find( '#addAnECard' ).is( ':checked' ) === true ) {
                return fail;
            } else {
                fail = true;
            }

            return fail;
    },
    validate: function() {
        var $validate = this.$el.find( '.validateme:visible' ),
            isValid = G5.util.formValidate( $validate );

        // failed generic validation tests (requireds mostly)
        if ( !isValid ) {
            return { msgClass: 'msgGenericError', valid: false };
        }

        if ( this.checkCards() === true ) {
            return { msgClass: 'msgNoECard', valid: false };
        }

        return { valid: true };
    },

    handleEcardsTabsClick: function( e ) {
        e.preventDefault();
        var $tar = $( e.target ),
            currentTabName = $tar.closest( 'a' ).data( 'tabName' );
        
        if( currentTabName === 'memes' ) {
            this.drawToolView.model.set( { 'activeTool': 'texttype' }, { silent: true } );
            this.drawToolView.model.set( { 'activeEcardTab': 'memes' }, { silent: true } );
        } else {
            this.drawToolView.model.set( { 'activeEcardTab': 'ecard' }, { silent: true } );
        }
        this.drawToolView.handleTabsClick( e );
    },

} );
