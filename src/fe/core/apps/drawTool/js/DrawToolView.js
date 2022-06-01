/* DrawToolView - draw tool to write on cards for recognitions */
/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported DrawToolView*/
/*global
$,
Modernizr,
_,
Backbone,
G5,
TemplateManager,
DrawCanvasView,
DrawToolView:true
*/
DrawToolView = Backbone.View.extend( {

    TOUCH_EVENTS: 'touchstart touchmove touchend',
    POINTER_EVENTS: 'MSPointerDown MSPointerMove MSPointerUp',
    MOUSE_EVENTS: 'mousedown mouseup mousemove',
    DEFAULT_MEME_FONT_SIZES: [ 12, 14, 16, 18, 20, 24 ],
    DEFAULT_MEME_FONT_SIZE: 12,
    STICKER_ADD_LIMIT: 5,
    STICKER_COUNT: 28, // harcoding this since there are 28 stickers in the image folder
    PATH_CONTEXT_ASSETS: window.location.origin + '/' + window.location.pathname.split( '/' )[ 1 ] + '/assets/',

    el: '#drawToolShell',

    // static card type strings
    CARD_TYPES: {
        NONE: 'none', // no card
        CARD: 'card', // cards are presets from the DB/BE system
        CERT: 'cert', // certs are like cards but slightly different
        UPLOAD: 'upload', // card is upload
        DRAWING: 'drawing' // card is drawing
    },

    //override super-class initialize function
    initialize: function ( opts ) {

        this.tplName = 'drawToolTemplate';
        this.cardsTplName = 'drawToolCardList';
        this.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'drawTool/tpl/';
        
        this.parentView = opts.parentView;
        this.isCanvasSupport = Modernizr.canvas;
        this.isTouch = Modernizr.touch || ( window.navigator && window.navigator.msPointerEnabled );
        this.isMobile = $( window ).width() <= 360; // mobile width
        this.isMobileTouch = this.isMobile && this.isTouch;
        this.stickerData = [];
        this.stickerSizes = {};
        this.stickerMoveData = {};
        this.ecardsBackup;
        this.memesRendered = null;
        this.activeMemeTabBean = null;
        this.drawToolCardId = opts.drawingToolSettings.drawToolCardId || '';

        // model
        this.model = new Backbone.Model( _.extend( { isVisible: true }, opts.drawingToolSettings ) );

        // canvas
        this.drawCanvasView = false; // initialized in render

        // controller
        this.eventsSetup();

        // render
        this.render();
    },

    events: {
        'click .drawTools button': 'doToolClick',

        'click #lineWidthSelect li': 'doSizeClick',
        'click #colorSelectMenu li': 'doColorClick',

        'click #clearImage': 'doClearClick',
        'click #clearImageConfirm': 'doClearClick',
        'click #clearImageCancel': 'doClearClick',

        'click #eCardThumbnailSelect li:not(.drawToolUploadClick)': 'doThumbClick',

        'change #addAnECard': 'doVisibleChange',

        'click .drawToolUploadContainer': 'doUploadClick',

        'click #drawToolStatusContainer .editMode': 'doEditClick',
        'click #drawToolStatusContainer .doneEditing': 'doContinueClick',
        'click #drawingComplete': 'doEditDoneClick',

        //card cycle
        'click #eCardThumbnailPager a': 'cardCyclePager',

        //Sticker events
        'click .memeDrawTools button.isAction': 'doToolClick',
        'click #clearMemeDrawing': 'doMemeClearClick',
        'touchstart #colorSelectMenu li': 'doColorClick',
        'click .stickersToggleBtn': 'handleStickerToggle',
        'click .ecardStickerItem': 'createStickersInDrawing',

        'click .deleteSticker': 'deleteSticker',
        'touchend .deleteSticker': 'deleteSticker',
        'click .deleteSticker i': 'deleteSticker',
        'touchend .deleteSticker i': 'deleteSticker',

        'click #fontSizeSelect li': 'handleFontSizeChange',

        'mousedown .createdCommonSticker': 'doStickerDown',
        'touchstart .createdCommonSticker': 'doStickerDown',
        'MSPointerDown .createdCommonSticker': 'doStickerDown',

        'mousemove .createdCommonSticker.canMove': 'doStickerMove',
        'mousemove .deleteSticker': 'doStickerMove',
        'touchmove .createdCommonSticker.canMove': 'doStickerMove',
        'touchmove .deleteSticker': 'doStickerMove',
        'MSPointerMove .createdCommonSticker.canMove': 'doStickerMove',
        'MSPointerMove .deleteSticker': 'doStickerMove',
        
        'mousemove .drawCanvas': 'doStickerMove',
        'touchmove .drawCanvas': 'doStickerMove',
        'MSPointerMove .drawCanvas': 'doStickerMove',
        
        'mousemove #drawToolStatusContainer': 'doStickerMove',
        'touchmove #drawToolStatusContainer': 'doStickerMove',
        'MSPointerMove #drawToolStatusContainer': 'doStickerMove',

        'mouseup .drawCanvas': 'doStickerUp',
        'touchend .drawCanvas': 'doStickerUp',
        'MSPointerUp .drawCanvas': 'doStickerUp',

        'mouseup .createdCommonSticker': 'doStickerUp',
        'mouseup .deleteSticker': 'doStickerUp',
        'mouseup #wPaint': 'doStickerUp',
        'touchend .createdCommonSticker': 'doStickerUp',
        'touchend .deleteSticker': 'doStickerUp',
        'touchend #wPaint': 'doStickerUp',
        'MSPointerUp .createdCommonSticker': 'doStickerUp',
        'MSPointerUp .deleteSticker': 'doStickerUp',
        'MSPointerUp #wPaint': 'doStickerUp',

    },

    // non-dom events (wire it up)
    eventsSetup: function() {
        var self = this;

        // triggered after initial render(s)
        this.on( 'renderedCards', self.cardCycleInit );
        this.on( 'renderedAll', self.updateCanvas );
        this.on( 'renderedAll', self.updateInputs );
        this.on( 'renderedAll', self.updateTools );
        this.on( 'renderedAll', self.updateOwnUpload );
        this.on( 'bgImageRemovedOnTabSwitch', self.setNoneCard );

        // 'cardChanged' triggered whenever the active card changes
        this.on( 'cardChanged', self.updateCardThumb );
        this.on( 'cardChanged', self.updateTools );
        this.on( 'cardChanged', self.updateCanvas );
        this.on( 'cardChanged', self.updateInputs );

        // 'drawingChanged' triggered when drawing changes
        this.on( 'drawingChanged', self.updateInputs );
        this.on( 'stickersDrawn', self.updateDrawingValue );

        // model changes
        this.model.on( 'change:isEditing', self.updateTools, this );
        this.model.on( 'change:isEditing', self.updateCanvas, this );

        this.model.on( 'change:activeTool', self.updateTools, this );
        this.model.on( 'change:activeColor', self.updateTools, this );
        this.model.on( 'change:activeSize', self.updateTools, this );

        this.model.on( 'change:isVisible', self.updateVisible, this );
        this.model.on( 'change:isVisible', self.updateInputs, this );        

        $( window ).keyup( function( e ) { // easter
            var s = 'TREETREETREE';
            if ( !self._easterBuffer ) {self._easterBuffer = ''; }
            self._easterBuffer += String.fromCharCode( e.keyCode );
            if ( self._easterBuffer.length > s.length ) {self._easterBuffer = self._easterBuffer.substring( 1 ); }

            if ( self._easterBuffer === s ) {
                self.$el.find( '#treeButton,#starButton' ).show();
            }
        } );
    },


    /* RENDER FUNCTIONS - populate the DOM with goodies */

    render: function() {
        var self = this,
            memes = this.model.get( 'memes' ) || [],
            activeMemeTab = _.some( memes, function( meme ) {
                return meme.id == self.drawToolCardId;
            } );

        this.$el.empty();

        if ( this.isMobileTouch ) {this.$el.addClass( 'touchenabled' ); }
        
        if( !this.model.get( 'memeFontSizes' ) ) {
            this.model.set( { 'memeFontSizes': this.DEFAULT_MEME_FONT_SIZES } );
        } else {
            this.model.set( { 'memeFontSizes': this.model.get( 'memeFontSizes' ).split( ',' ) } );            
        }

        if( !this.model.get( 'memeFontSizeDefault' ) ) {
            this.model.set( { 'memeFontSizeDefault': this.DEFAULT_MEME_FONT_SIZE, 'activeMemeFontSize': this.DEFAULT_MEME_FONT_SIZE }, { silent: true } );
        } else {
            this.model.set( { 'activeMemeFontSize': this.model.get( 'memeFontSizeDefault' ) } );
        }

        if( !this.model.get( 'stickerAddLimit' ) ) {
            this.model.set( { 'stickerAddLimit': this.STICKER_ADD_LIMIT } );
        }

        if( activeMemeTab ) {
            this.activeMemeTabBean = true;
            this.model.set( 'activeEcardTab', 'memes' );
        }
        
        TemplateManager.get( this.tplName,
            function ( tpl, vars, subTpls ) {
                self.subTpls = subTpls;
                self.$el.append( tpl( self.model.toJSON() ) );
                self.renderCards();
                self.renderTools();
                self.renderCanvas();
                if( self.model.get( 'stickersActive' ) ) {
                    self.renderStickers();
                }
                self.trigger( 'renderedAll' ); // except cards may not yet be rendered
            }, this.tplUrl );
    },
    renderCards: function() {
        var self = this,
            $cl = this.$el.find( '#eCardThumbnailSelect' ).empty(),
            activeCard = this.getActiveCard(),
            currentTabName = this.model.get( 'activeEcardTab' );

        // make sure the active card in the list isSelected
        // -- the template + card cycler (pager) will use this
        if ( activeCard ) {activeCard.isSelected = true;}

        if( ( currentTabName === 'memes' ) && this.memesRendered && this.activeMemeTabBean ) {
            return;
        }

        TemplateManager.get( this.cardsTplName,
            function ( tpl ) {
				//Grab model and append data needed for template render
                var renderedCards = self.model.toJSON();
                renderedCards.isRenderDrawIcons = self.isCanvasSupport && renderedCards.canDraw;
                $cl.append( tpl( renderedCards ) );
                self.trigger( 'renderedCards' );

                self.updateOwnUpload();
            },
        this.tplUrl );
    },
    renderTools: function() {
        // style brushes
        this.$el.find( '#lineWidthSelect li' ).each( function() {
            var $t = $( this ), w = $t.data( 'brush-width' );

            $t.css( { 'font-size': w } ).find( 'span' ).css( {
                width: w,
                height: w,
                lineHeight: w + 'px',
                marginLeft: -0.5 * w,
                marginTop: -0.5 * w
            } );
        } );

        // if touch, no tooltips. Otherwise, tooltips:
        if ( !this.isTouch ) {
            this.$el.find( '#drawToolMenu button' ).tooltip();// bootstrap tt (title attr)
            this.$el.find( '#eCardThumbnailPager a.btn' ).tooltip();// bootstrap tt (title attr)
        }

        // no canvas, no tools
        if ( !this.isCanvasSupport ) { // hide but keep dimensions?
            this.$el.find( '#drawToolMenu' ).css( 'visibility', 'hidden' );
        }
        
        this.$el.find( '#fontSizeSelect li[data-font-size="' + this.model.get( 'memeFontSizeDefault' ) + '"]' ).addClass( 'active' );
        this.$el.find( '#drawToolFontPickSize button .size' ).text( this.model.get( 'memeFontSizeDefault' ) );

        this.trigger( 'renderedTools' );
    },
    renderCanvas: function() {
        var self = this,
            $drawingContainer = this.$el.find( '#wPaint' );

        this.drawCanvasView = new DrawCanvasView( {
            el: $drawingContainer,
            drawingData: this.model.get( 'drawingData' ) || '',
            parentView: self
        } );

        // this forwards a childview event to this view
        // - we just forward the trigger, instead of listening directly to the child view
        this.drawCanvasView.on( 'drawingChanged', function() {self.trigger( 'drawingChanged' );} );
        this.drawCanvasView.on( 'bgImageRemovedOnTabSwitch', function() {self.trigger( 'bgImageRemovedOnTabSwitch' );} );
        this.drawCanvasView.on( 'stickersDrawn', function() {self.trigger( 'stickersDrawn' );} );

        this.trigger( 'renderedCanvas' );

        if( this.activeMemeTabBean ) {
            this.handleTabsClick( null, true ); // Passing true since it is edit mode if a form bean is set
        }
    },

    // WIP #62895 Changes start
    // Please note that there also other changes inside the file related to this feature. Search the repo for commit with message "WIP #62895 - Meme & Sticker tool changes" to see all the changes
    renderStickers: function() {
        var $stickersContainer = this.$el.find( '#ecardStickers' ),
            $stickersHolder = $stickersContainer.find( '.ecardStickerList' ),
            stickersActive = this.model.get( 'stickersActive' ),
            // stickers = this.model.get( 'stickers' ), // Commenting the stickers from model since it is now kept in local
            localStickers = [];

            this.$el.find( '#ecardStickers' ).parents( '.ecardsSection' ).css( { 'margin-bottom': '50px' } );

        for ( var i = 1; i <= this.STICKER_COUNT; i++ ) {
            var stickerObj = {
                id: 100 + i,
                name: 'Sticker ' + i,
                imgPath: this.PATH_CONTEXT_ASSETS + 'img/stickers/sticker' + i + '.png'
                // imgPath: 'img/stickers/sticker' + i + '.png' // This is for local sandbox environment
            };
            localStickers.push( stickerObj );
        }

        if( stickersActive && !this.activeMemeTabBean ) {
            $stickersContainer.show();
        }

        _.each( localStickers, function( sticker ) {
            var $li = '<li class="ecardStickerItem" data-sticker-id="' + sticker.id + '"><img src="' + sticker.imgPath + '"/></li>';
            $stickersHolder.append( $li );
        } );
    },

    handleStickerToggle: function( e ) {
        e.preventDefault();
        e.stopPropagation();
        var $tar = $( e.target ).closest( '.stickersToggleBtn' ),
            $ecardStickersHolder = this.$el.find( '.ecardStickersHolder' );

        $tar.toggleClass( 'open' );
        $( 'body' ).addClass( 'stickersToggleOpen' );
        

        $( '.stickersToggleOpen' ).unbind( 'click' ).bind( 'click', function() {
            $( '.stickersToggleBtn' ).removeClass( 'open' );
            $ecardStickersHolder.hide();
        } );
        $ecardStickersHolder.toggle();
    },

    createStickersInDrawing: function( e ) {
        e.stopPropagation();
        // xxxxxx
        var $tar = $( e.target ),
            stickerId = $tar.parent().data( 'stickerId' ),
            // stickerImgPath = 'img/badges/1year_lg.png',
            stickerImgPath = $tar.closest( 'img' ).attr( 'src' ),
            // stickerImgAbsolutePath = this.PATH_CONTEXT_ASSETS + stickerImgPath,
            $stickerContainer = this.$el.find( '#wPaint' ),
            stickerLength = this.$el.find( '.createdCommonSticker' ).length,
            stickerRandId = this.generateRandomId( 5 ),
            $sticker = $( '<div class="createdCommonSticker canMove createdSticker-' + stickerRandId + '" data-item-id="' + stickerRandId + '" data-sticker-id="' + stickerId + '" id="' + stickerRandId + '"><img src="' + stickerImgPath + '"/><a class="btn btn-danger deleteSticker" href="javascript:void(0)"><i class="icon-trash"></i></a></div>' );

        if( stickerLength >= this.model.get( 'stickerAddLimit' ) ) {
            console.log( 'Maximum stickers added!!', this.model.get( 'stickerAddLimit' ) );
            return;
        }
        var defaults = {
                sW: 48, // Sticker Width
                sH: 48 // Sticker Height
            },
            calcData = {
                cW: $stickerContainer.outerWidth(), // Container Width
                cH: $stickerContainer.outerHeight(), // Container height
                cT: $stickerContainer.offset().top, // Container Top
                cL: $stickerContainer.offset().left // Container Left
            },
            styleOffset = {
                top: Math.floor( Math.random() * ( calcData.cH - defaults.sH ) ) + 'px',
                left: Math.floor( Math.random() * ( calcData.cW - defaults.sW ) ) + 'px'
            };

        $sticker.css( styleOffset );
        $stickerContainer.append( $sticker );

        this.stickerData.push( {
            id: stickerRandId,
            stickerId: stickerId,
            'x-pos': parseInt( styleOffset.left.replace( 'px', '' ) ),
            'y-pos': parseInt( styleOffset.top.replace( 'px', '' ) ),
            class: 'createdSticker-' + stickerRandId
        } );
        this.stickerSizes = $.extend( {}, this.stickerSizes, defaults );

        this.checkStickerMaxError();

        console.log( 'this.stickerData : ', this.stickerData );
    },

    doStickerDown: function( e ) {
        this.normalizeEvent( e );
        console.log( 'Sticker Down!!' );
        var moveDiv =  $( e.target ).closest( '.createdCommonSticker' ),
            $stickerContainer = this.$el.find( '#wPaint' ),
            currentStickerDiv =  $( e.target ).closest( '.createdCommonSticker' ),
            currentStickerItemId = currentStickerDiv.data( 'itemId' );

        this.stickerMoveData.isDown = true;
        this.stickerMoveData.currentStickerItemId = currentStickerItemId;
        this.stickerMoveData.offsetC = [
            moveDiv.offset().left - e.pageX - $stickerContainer.offset().left, 
            moveDiv.offset().top - e.pageY - $stickerContainer.offset().top
        ];

        $stickerContainer.find( '.commonStickerActive' ).removeClass( 'commonStickerActive' );
        moveDiv.addClass( 'commonStickerActive' );

        console.log( 'SMD : ', this.stickerMoveData );
    },

    doStickerUp: function( e ) {
        this.normalizeEvent( e );
        var $stickerContainer = this.$el.find( '#wPaint' );
        this.stickerMoveData.isDown = false;
        this.stickerMoveData.currentStickerItemId = null;
        $stickerContainer.find( '.createdCommonSticker' ).addClass( 'canMove' );
        e.preventDefault();
        console.log( 'Sticker Up!!' );
    },

    doStickerMove: function( e ) {
        this.normalizeEvent( e );

        if( !this.stickerMoveData.isDown ) {
            return;
        }

        console.log( 'In Move : ', this.stickerMoveData );

        var that = this,
            $stickerContainer = this.$el.find( '#wPaint' ),
            // currentStickerDiv =  $( e.target ).closest( '.createdCommonSticker' ),
            moveDiv =  this.$el.find( '#' + this.stickerMoveData.currentStickerItemId ),
            itemId = this.stickerMoveData.currentStickerItemId,
            // itemId = moveDiv.data( 'itemId' ),
            currentActiveSticker = _.find( that.stickerData, function( sticker ) {
                return itemId == sticker.id;
            } );

            $stickerContainer.find( '.createdCommonSticker' ).not( '#' + itemId ).removeClass( 'canMove' );
        if ( that.stickerMoveData.isDown ) {

          e.preventDefault();
          var fixPositions = {
            x: e.pageX + that.stickerMoveData.offsetC[ 0 ],
            y: e.pageY + that.stickerMoveData.offsetC[ 1 ]
          };
          console.log( 'Sticker Move!!', fixPositions );
          moveDiv.css( { 'left': findMax( 'x', fixPositions.x ) } );
          moveDiv.css( { 'top': findMax( 'y', fixPositions.y ) } );
        }
        
        function findMax( axis, value ) {

            var dragDivWidth = moveDiv.outerWidth(),
                wrapperWidth =  $stickerContainer.outerWidth(),
                dragDivHeight = moveDiv.outerHeight(),
                wrapperHeight =  $stickerContainer.outerHeight(), 
                maxValue = ( axis === 'x' ) ? ( wrapperWidth - dragDivWidth ) : ( wrapperHeight - dragDivHeight ),
                returnVal;

            if( value <= 10 ) {
                returnVal = 10; // Adjusting 10px to avoid input ending up in the edge  of container
            } else if( value > maxValue ) {
                returnVal = maxValue - 10; // Adjusting 10px to avoid input ending up in the edge  of container
            } else {
                returnVal = value - 10; // Adjusting 10px to avoid input ending up in the edge  of container
            }

            currentActiveSticker[ axis + '-pos' ] = returnVal;
   
            return returnVal;
        }

    },

    deleteSticker: function( e ) {
        e.preventDefault();
        e.stopPropagation();
        var that = this,
            currentSticker =  $( e.target ).closest( '.createdCommonSticker' ),
            currentStickerItemId = currentSticker.data( 'itemId' ),
            filteredStickers = _.filter( that.stickerData, function( sticker ) {
                return currentStickerItemId != sticker.id;
            } );

        currentSticker.remove();
        that.stickerData = filteredStickers;

        this.checkStickerMaxError();

        console.log( 'CSD : ', that.stickerData );
    },

    checkStickerMaxError: function() {

        var stickersAfterAddLength = this.$el.find( '.createdCommonSticker' ).length;
        var $stickersHolder = this.$el.find( '.ecardStickersHolder' );

        if( stickersAfterAddLength >= this.model.get( 'stickerAddLimit' ) ) {
            $stickersHolder.addClass( 'stickerMax' );
        } else {
            $stickersHolder.removeClass( 'stickerMax' );
        }
    },

    clearStickers: function() {
        var $stickerContainer = this.$el.find( '#wPaint' ),
            stickers = $stickerContainer.find( '.createdCommonSticker' );

        stickers.remove();
        this.stickerData = [];
        this.stickerMoveData = {};
    },

    doMemeClearClick: function() {
        this.clearStickers();
        this.drawCanvasView.clearTextData();

        if( this.model.get( 'activeEcardTab' ) === 'memes' ) {
            this.handleTabChangeNeeds();
        }
    },

    handleTabsClick: function( e, isMemeEditMode ) {
        var $stickerContainer = this.$el.find( '#wPaint' ),
            $ecardStickers = this.$el.find( '#ecardStickers' ),
            $menu = this.$el.find( '#drawToolMenu' ),
            currentTabName = ( isMemeEditMode ) ? 'memes' : this.model.get( 'activeEcardTab' ),
            memes = this.model.get( 'memes' ),
            ecardTabs = this.parentView.$el.find( '#recognitionPageEcardSectionTabSelectedTabs' );    
            
        if( isMemeEditMode ) {
            ecardTabs.find( 'li.active' ).removeClass( 'active' );
            ecardTabs.find( 'li' ).last().addClass( 'active' );
        }

        if( currentTabName === 'memes' && !this.memesRendered ) {
            this.memesRendered = true;
            $menu.show();
            this.$el.find( '#drawToolMenu .btn-group' ).hide();
            this.$el.find( '.memeDrawTools' ).show();
            this.$el.find( '.memeDrawTools div, .memeDrawTools btn' ).show();
            $ecardStickers.hide();

            this.ecardsBackup = this.model.get( 'eCards' );
            this.model.set( { eCards: memes } );
            this.handleTabChangeNeeds();
            this.renderCards();

            this.$el.find( '#drawToolCardType' ).val( 'meme' );
        } else {
            this.memesRendered = false;
            this.activeMemeTabBean = false;
            if( this.model.get( 'canDraw' ) ) {
                $menu.show();
                this.$el.find( '#drawToolMenu .btn-group' ).not( '#drawingCompleteContainer' ).show();
                this.$el.find( '.memeDrawTools' ).hide();
            }
            if( this.model.get( 'stickersActive' ) ) {
                $ecardStickers.show();
                this.$el.find( '.ecardStickersHolder' ).hide();
                this.$el.find( '.stickersToggleBtn' ).removeClass( 'open' );
            }
            this.model.set( { eCards: this.ecardsBackup } );
            this.model.set( { 'activeTool': 'pencil' }, { silent: true } );
            this.model.set( { 'activeSize': 8 }, { silent: true } );

            this.drawCanvasView.clearTextData();
            $stickerContainer.css( { cursor: 'crosshair' } );
            this.renderCards();
            this.checkStickerMaxError();
        }
        if( isMemeEditMode ) { 
            return;
        }
        this.drawCanvasView.clearDrawing();
        this.drawCanvasView.removeBgImage();
        this.setActiveCardId( null );
        this.updateCanvas();
        this.clearStickers();       
    },

    handleTabChangeNeeds: function() {
        var $stickerContainer = this.$el.find( '#wPaint' ),
            $textToolButton = this.$el.find( '#textToolToggle' );

        this.model.set( { 'activeTool': 'texttype' } );
        this.model.set( { 'activeColor': '000000' } );
        $stickerContainer.css( { cursor: 'text' } );
        $textToolButton.addClass( 'active' );
    },

    handleFontSizeChange: function( e ) {
        var $tar = $( e.target ).closest( 'li.fontSizeSelectList' ),
            $allLi = this.$el.find( '#fontSizeSelect li' ),
            currentFontSize = $tar.data( 'fontSize' );

        $allLi.removeClass( 'active' );
        this.$el.find( '#drawToolFontPickSize button .size' ).text( currentFontSize );
        this.model.set( { activeMemeFontSize: currentFontSize }, { silent: true } );
        this.model.set( { 'activeTool': 'texttype' }, { silent: true } );
        this.model.set( { 'activeSize': currentFontSize }, { silent: true } );
        this.drawCanvasView.setSizeOrColorChange( 'Size' );
        $tar.addClass( 'active' );
    },

    generateRandomId: function( length ) {
        var result           = '';
        var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        var charactersLength = characters.length;
        for ( var i = 0; i < length; i++ ) {
           result += characters.charAt( Math.floor( Math.random() * charactersLength ) );
        }
        return result;
     },
     
     // massage event object for certain devices
     normalizeEvent: function( e ) {
         var //that = this,
             touches;
 
         // touch devices
         if ( this.TOUCH_EVENTS.indexOf( e.type ) >= 0 ) {
             touches = e.originalEvent.touches;
 
             // make sure we have a touch, else use last vals
             e.pageX = touches.length ? touches[ 0 ].pageX : this._lastTouchX;
             e.pageY = touches.length ? touches[ 0 ].pageY : this._lastTouchY;
 
             // for touchend we want to save these since it has no 'touches'
             this._lastTouchX = e.pageX;
             this._lastTouchY = e.pageY;
 
             // be lazy about killing mouse events
             // (galaxy shoots both mouse and touch at same time)
             this._mouseTrap = true;
         } else if ( this.POINTER_EVENTS.indexOf( e.type ) >= 0 ) {
              // pointer devices (M$)
             e.pageX = e.originalEvent.pageX;
             e.pageY = e.originalEvent.pageY;
 
             // lazy kill mouse events
             this._mouseTrap = true;
         } else if ( this._mouseTrap && this.MOUSE_EVENTS.indexOf( e.type ) >= 0 ) {
             // mouse killer (for galaxy)
             e._deadMouse = true;
         }
 
 
         // setTimeout(function(){
         //     var $e = that.$el.closest('.span12');
         //     $e.append(e.type+' '+e.originalEvent.pageY+' ');
         // },0)
 
     },
     // WIP #62895 Changes end
    // Please note that there also other changes inside the file related to this feature. Search the repo for commit with message "WIP #62895 - Meme & Sticker tool changes" to see all the changes

    /* UPDATE FUNCTIONS - synchronize the DOM with the DATA */

    updateCardThumb: function( c ) {
        if ( !c ) {return;}
        var $cl = this.$el.find( '#eCardThumbnailSelect' );

        // thumbnail selected state
        $cl.find( 'li' ).removeClass( 'selected' );
        $cl.find( 'li img#card-' + c.id ).closest( 'li' ).addClass( 'selected' );
    },

    updateTools: function() {
        var tool, color, size, canDraw,
            $menu = this.$el.find( '#drawToolMenu' ),
            $memeToolMenu = this.$el.find( '.memeDrawTools' ),
            $sizes = $menu.find( '#lineWidthSelect li' ),
            $colors = $menu.find( '#colorSelectMenu li' ),
            $stickerContainer = this.$el.find( '#wPaint' ),
            card = this.getActiveCard();

        // defaults (make sure these are silent, or else)
        if ( !this.model.get( 'activeTool' ) ) { this.model.set( { 'activeTool': 'pencil' }, { silent: true } ); }
        if ( !this.model.get( 'activeColor' ) ) { this.model.set( { 'activeColor': '000000' }, { silent: true } ); }
        if ( !this.model.get( 'activeSize' ) ) { this.model.set( { 'activeSize': 8 }, { silent: true } ); }

        tool = this.model.get( 'activeTool' );
        color = this.model.get( 'activeColor' );
        size = ( this.model.get( 'activeTool' ) == 'texttype' ) ? this.model.get( 'activeMemeFontSize' ) : this.model.get( 'activeSize' );
        canDraw = this.model.get( 'canDraw' );

        // set canvas tools
        this.drawCanvasView.setTool( tool, size, color );

        // canDraw menu show/hide
        $menu[ canDraw ? 'show' : ( this.model.get( 'activeEcardTab' ) === 'memes' ) ? 'show' : 'hide' ]();
        $menu[ canDraw ? 'removeClass' : ( this.model.get( 'activeEcardTab' ) === 'memes' ) ? 'removeClass' : 'addClass' ]( 'cannotDraw' );

        if( !canDraw ) {
            $stickerContainer.css( { cursor: 'default' } );
        }        

        // set tool mode
        this.$el.find( '#drawToolMenu' ).removeClass( function( i, c ) {
            c = c.match( /mode-[a-z]+/i );
            return c && c.length ? c[ 0 ] : false;
        } ).addClass( tool === 'eraser' ? 'mode-erase' : 'mode-draw' );

        // tool
        this.$el.find( '.drawTools button' ).removeClass( 'active' );
        this.$el.find( '#' + this.model.get( 'activeTool' ) + 'Button' ).addClass( 'active' );

        // set tool color
        this.$el.find( '#lineWidthSelect,#drawToolPickColor .btn' ).css( 'color', '#' + color );

        // set size text
        this.$el.find( '#drawToolPickSize .size' ).text( size );

        // set selected color/size
        $sizes.removeClass( 'selected' ).filter( '[data-brush-width=' + size + ']' ).addClass( 'selected' );
        $colors.removeClass( 'selected' ).find( 'div[data-hex-color-code=' + color + ']' ).closest( 'li' ).addClass( 'selected' );

        // enabled buttons
        if (
            !card // no card
          || ( card && !card.canEdit && canDraw ) //can draw but card is uneditable
          || ( card && card.canEdit && this.isMobileTouch && !this.model.get( 'isEditing' ) ) //can edit, touch mode
        ) {
            $menu.find( 'button:not(".alwaysEnable")' ).addClass( 'disabled' ).attr( 'disabled', true );
            if( !this.model.get( 'canAddStickerOnUpload' ) ) {
                this.$el.find( '.stickersToggleBtn' ).addClass( 'disabled' ).attr( 'disabled', true );
            } else {
                this.$el.find( '.stickersToggleBtn' ).removeClass( 'disabled' ).removeAttr( 'disabled' );
            }
            if( ( this.model.get( 'activeEcardTab' ) === 'memes' ) && ( this.model.get( 'drawToolCardType' ) === this.CARD_TYPES.UPLOAD ) ) {
                $memeToolMenu.find( '.btn' ).removeClass( 'disabled' ).removeAttr( 'disabled' );
            }
        } else {
            $menu.find( '.btn' ).removeClass( 'disabled' ).removeAttr( 'disabled' );
            this.$el.find( '.stickersToggleBtn' ).removeClass( 'disabled' ).removeAttr( 'disabled' );
        }

    },

    updateVisible: function() {
        var $vis = this.$el.find( '#addAnECard' ),
            isVis = this.model.get( 'isVisible' ),
            $tool = this.$el.find( '#drawingTool' );

        // BE careful with scrolling and sliding, be sure to test on all devices/os

        if ( this.isMobile ) { // mobile
                $tool[ isVis ? 'show' : 'slideUp' ]( G5.props.ANIMATION_DURATION );
                if ( isVis && this.isMobile ) {
                    $.scrollTo( this.$el, G5.props.ANIMATION_DURATION, { axis: 'y', offset: { top: -24, left: 0 } } );
                }
        }
        else { // not mobile
            $vis.attr( 'disabled', true );
            $tool[ isVis ? 'slideDown' : 'slideUp' ]( G5.props.ANIMATION_DURATION, function() {
                $vis.removeAttr( 'disabled' );
            } );
        }

    },

    updateCanvas: function() {
        var cv = this.drawCanvasView,
            card = this.getActiveCard(),
            wasMsg = false,
            wasHid = false,
            $dtc = this.$el.find( '#drawToolContainer' );

        if ( !cv ) { return; }

        // CARD
        if ( card ) {

            // on mobile devices, the container is hidden at first
            if ( !$dtc.is( ':visible' ) ) {
                $dtc.show();
                if ( this.isMobile ) {
                    setTimeout( function() { // win 8 phone wants this deferred
                        $.scrollTo( $dtc, G5.props.ANIMATION_DURATION, { axis: 'y', offset: { top: -24, left: 0 } } );
                    }, 0 );
                }
            }

            cv.setBgImage( card.largeImage );

            if ( !card.canEdit && this.isCanvasSupport && !this.model.get( 'stickersActive' )) {
                cv.showMsg( 'editingDisabledToolTip' );
                wasMsg = true;

                cv.hideCanvas();
                wasHid = true;
            }

            if ( card.canEdit && this.isMobileTouch && !this.model.get( 'isEditing' ) ) {
                // show "begin" msg once, after that "pause"
                cv.showMsg( ( this.showedBeginEditMsg ? 'pause' : 'begin' ) + 'ToolTipTouch' );
                this.showedBeginEditMsg = true; // next time we will show paused msg
                wasMsg = true;
            }
        }

        // NO CARD
        if ( !card ) {
            // UPLOAD
            if ( this.model.get( 'drawToolCardType' ) === this.CARD_TYPES.UPLOAD ) {

                if ( this.isCanvasSupport ) {
                    if( this.model.get( 'activeEcardTab' ) !== 'memes' ) {
                        cv.showMsg( 'editingDisabledToolTip' );
                        wasMsg = true;
                    }
                }

                cv.setBgImage( this.model.get( 'drawToolCardUrl' ) );

                if( ( this.model.get( 'activeEcardTab' ) !== 'memes' ) && !this.model.get( 'canAddStickerOnUpload' ) ) {
                    cv.hideCanvas();
                    wasHid = true;
                }
            }
            // NOTHING YET
            else {
                cv.showMsg( 'beginToolTip' );
                wasMsg = true;

                cv.setBgImage( '' );

                cv.hideCanvas();
                wasHid = true;
            }
        }

        if ( !wasMsg ) {cv.hideMsg();} // hide all msgs if none set
        if ( !wasHid ) {cv.showCanvas();} // show canvas
    },

    // update form inputs
    updateInputs: function() {
        var $url = this.$el.find( '#drawToolCardUrl' ),
            $type = this.$el.find( '#drawToolCardType' ),
            $drawing = this.$el.find( '#drawToolDrawingData' ),
            $data = this.$el.find( '#drawToolCardData' ),
            $id = this.$el.find( '#drawToolCardId' ),
            $videoUrl = this.$el.find( '#drawToolVideoUrl' ),
            card = this.getActiveCard(),
            isVis = this.model.get( 'isVisible' ),
            isDrawing = this.drawCanvasView.hasDrawing()
                && ( this.model.get( 'canDraw' ) || this.model.get( 'stickersActive' ) )
                && card 
                && ( card.canEdit || this.model.get( 'stickersActive' ) );

        // NOT ACTIVE
        if ( !isVis ) {
            $url.val( '' );
            $type.val( this.CARD_TYPES.NONE );
            $drawing.val( '' );
            $data.val( '' );
            $id.val( '' );
            $videoUrl.val( '' );
        }
        // CARD or CERT
        else if ( card && !isDrawing ) {
            $url.val( card.largeImage );
            $type.val( card.cardType );
            $drawing.val( '' );
            $data.val( '' );
            $id.val( card.id );
            $videoUrl.val( '' );

            //TODO remove this if it tests out ok on server
            // back end needs?? this disabled (not sent)
            // $data.attr('disabled','disabled');
        }
        // DRAWING
        else if ( isDrawing ) {
            $url.val( card.largeImage );
            if( this.model.get( 'activeEcardTab' ) == 'memes' ) {
                $type.val( 'meme' );  
            } else {
                $type.val( this.CARD_TYPES.DRAWING );
            }
            $drawing.val( this.drawCanvasView.getDrawingAsPng() );
            this.drawCanvasView.getCompositeAsPng( function( url ) {
                $data.val( url );
            } );

            $id.val( card.id );
            $videoUrl.val( '' );

            //TODO remove this if it tests out ok on server
            // back end needs?? this disabled (not sent)
            //$data.removeAttr('disabled');
        }
        // UPLOAD
        else if ( this.model.get( 'drawToolCardType' ) === this.CARD_TYPES.UPLOAD ) {
            $url.val( this.model.get( 'drawToolCardUrl' ) );
            $type.val( this.model.get( 'drawToolCardType' ) );
            $videoUrl.val( this.model.get( 'drawToolVideoUrl' ) );
            $id.val( '' );
            if( this.model.get( 'activeEcardTab' ) !== 'memes' ) {
                $drawing.val( '' );
                $data.val( '' );
            } else {
                $drawing.val( this.drawCanvasView.getDrawingAsPng() );
                this.drawCanvasView.getCompositeAsPng( function( url ) {
                    $data.val( url );
                } );
            }

            //TODO remove this if it tests out ok on server
            // back end needs?? this disabled (not sent)
            // $data.attr('disabled','disabled');
        }
        // NOTHING
        else {
            $url.val( '' );
            $type.val( this.CARD_TYPES.NONE );
            $drawing.val( '' );
            $data.val( '' );
            $id.val( '' );
            $videoUrl.val( '' );
        }
    },

    updateDrawingValue: function() {
        var $drawing = this.$el.find( '#drawToolDrawingData' ),
            $data = this.$el.find( '#drawToolCardData' );

        $drawing.val( this.drawCanvasView.getDrawingAsPng() );
        this.drawCanvasView.getCompositeAsPng( function( url ) {
            $data.val( url );
         } );
    },


    /* ACTIONS - interpret user actions, usually change some data */

    doVisibleChange: function( e ) {
        var $t = $( e.currentTarget ),
            isVis = $t.is( ':not(:checked)' );
        this.model.set( 'isVisible', isVis );
    },
    doToolClick: function( e ) {
        var $t = $( e.currentTarget ),
            toolName = $t.data( 'toolName' );
        e.preventDefault();

        if( toolName !== 'fontSizePick' ) {
            this.model.set( 'activeTool', toolName );
        }
    },
    doSizeClick: function( e ) {
        var $t = $( e.currentTarget ),
            size = $t.data( 'brushWidth' );
        e.preventDefault();
        this.model.set( 'activeSize', size );
    },
    doColorClick: function( e ) {
        e && this.normalizeEvent( e );
        var $t = $( e.currentTarget ),
            color = $t.find( '.colorSelect' ).data( 'hexColorCode' );
        e.preventDefault();
        this.model.set( 'activeColor', color );
        this.drawCanvasView.setSizeOrColorChange( 'Color' );
    },
    doClearClick: function( e ) {
        var $t = $( e.currentTarget ),
            id = $t.attr( 'id' ),
            cv = this.drawCanvasView;
        e.preventDefault();

        if ( this.drawCanvasView ) {
            if ( id === 'clearImageConfirm' ) {cv.clearDrawing(); cv.hideMsg(); this.doMemeClearClick();}
            if ( id === 'clearImageCancel' ) {cv.hideMsg();}
            if ( id === 'clearImage' ) {cv.showMsg( 'clearToolTip' );}
        }
    },
    doThumbClick: function( e ) {
        var $tar = $( e.currentTarget ),
            id = $tar.find( 'img' ).attr( 'id' ).replace( /card-/, '' );
        this.setActiveCardId( id );
        if( this.model.get( 'activeEcardTab' ) == 'memes' ) {
            this.$el.find( '#drawToolCardType' ).val( 'meme' );            
            this.drawCanvasView.triggerTextEdit();  
        }      
    },
    doUploadClick: function( e ) {
        e.preventDefault();

        var $upForm = this.$el.find( '#drawToolFormUploadImage' ),
            self = this;

            $upForm.fileupload( {
                url: G5.props.URL_JSON_DRAW_TOOL_IMAGE_UPLOAD,
                dataType: 'g5json',
                beforeSend: function() {
                    G5.util.showSpin( self.$el, {
                        cover: true,
                        elPosition: true
                    } );
                },
                done: function ( e, data ) {
                    var props = data.result.data.properties;
                    var message = data.result.data.messages;
                    console.log( message );
                    console.log( message[ 0 ].type );
                    if( message[ 0 ].type == 'error' ) {
                        $( '#uploadErrorModal' ).modal();
                    }
                    if ( props.isSuccess === true ) {
                        self.setActiveCardUpload( props.imageUrl, props.videoUrl );
                    }

                    G5.util.hideSpin( self.$el );

                    // $t.spin(false);
                }
            } );
        $( '#drawToolButtonUploadImage' ).trigger( 'click' );

    },
    doEditClick: function( e ) {
        e.preventDefault();
        this.model.set( 'isEditing', true );
    },
    doEditDoneClick: function( e ) {
        e.preventDefault();
        this.model.set( 'isEditing', false );
    },
    doContinueClick: function( e ) {
        e.preventDefault();
        $.scrollTo( this.$el, G5.props.ANIMATION_DURATION, {
            axis: 'y',
            offset: {
                top: this.$el.outerHeight() - 24,
                left: 0
            }
        } );
    },




    /* ****************************************************
        Card Cycle (Card Pager)
       **************************************************** */

    cardCycleInit: function() {
        var self = this,
            cc, calculate, bindToWindow;

        // create an object on the base view
            self.$el = $( '#drawToolShell' );

        _.delay( function() {

            self.cardCycle = {
            // start with a bunch of handy-dandy references
            $parent: self.$el.find( '#eCardThumbnailContainerParent' ),
            $container: self.$el.find( '#eCardThumbnailContainer' ),
            $pager: self.$el.find( '#eCardThumbnailPager' ),
            $list: self.$el.find( '#eCardThumbnailSelect' ),
            $cards: self.$el.find( '#eCardThumbnailSelect li' )
        };

        // this calculation is broken out so we can redo it every time the window resizes
        calculate = function() {
            // create a reference to the object that's easier to type and read
            cc = self.cardCycle;
            // how many fit horizontally in the window?
            cc.numX = Math.floor( cc.$container.width() / cc.$cards.first().outerWidth( true ) );
            // how many fit vertically in the window?
            cc.numY = Math.floor( cc.$container.height() / cc.$cards.first().outerHeight() );
            // alright then, how many fit in the window altogether?
            cc.numFit = cc.numX * cc.numY;
            // divide the total number of cards by the number that fit per window
            cc.numSteps = Math.ceil( cc.$cards.length / cc.numFit );
            // use the full height of a step and the number that fit vertically to figure out how far we slide each time
            cc.stepSize = cc.numY * cc.$cards.first().outerHeight( true );
            // find the selected card to make sure we show it in the view, or default to the first one
            cc.showThumb = cc.showThumb || Math.max( cc.$cards.filter( '.selected' ).index(), 0 );
            // figure out which step holds the currently selected one
            cc.showStep = Math.floor( cc.showThumb / cc.numFit );

            // if there is one or fewer (ha) steps to show, hide the pager controls
            if ( cc.numSteps <= 1 ) {
                cc.$pager.find( 'a' ).hide();
            }
            else {
                cc.$pager.find( 'a' ).show();
            }

            // set an explicit height on the full list, just in case
            cc.$list.css( {
                height: cc.stepSize * cc.numSteps
            } );

            //Update Scroll Information
            self.calculateCardCyclePagerMeta();

            // scroll into position instantly
            self.cardCycleScroll( 0 );
        };
        // run that calculation
        calculate();

        // we use _'s handy once() method to make sure we only bind to the window resize one time
        bindToWindow = _.once( function() {
            // on the resize, we bind an throttled version of calculate that will only fire every half second at most
            $( window ).on( 'resize', _.throttle( calculate, 500 ) );
        } );
        // and bind it
        bindToWindow();
    }, 200 );
    },
    calculateCardCyclePagerMeta: function() {
        var cc = this.cardCycle,
            that = this,
            json = {};

            if( this.subTpls ) {
                var tpl = this.subTpls.eCardThumbnailPagerMeta;
            } else {
                return;
            }
        cc.$pager.find( '#eCardThumbnailPagerMeta' ).remove();
        if( cc.$container.length ) {
            json.total = cc.$cards.length;
            json.actualStep = cc.showStep + 1;

            if( isNaN( json.actualStep ) ) {
                //setTimeout( function() {that.calculateCardCyclePagerMeta();}, 300 );
            } else {
                cc.$pager.append( tpl( json ) );
            }
        }
        else {
            json = {};
            return;
        }
        $( window ).trigger( 'resize' );
    },

    cardCyclePager: function( e ) {
        e.preventDefault();

        // first check to see if the target is the previous link
        var goToStep = $( e.target ).data( 'pager' ) == 'prev' || $( e.target ).closest( 'a' ).data( 'pager' ) == 'prev'
                    // if so, check to see if we're going below zero
                    ? this.cardCycle.showStep - 1 < 0
                        // if so, restart at the end by using the total number of steps and subtracting 1 to get to our zero-based index
                        ? this.cardCycle.numSteps - 1
                        // if not, go backwards one
                        : this.cardCycle.showStep - 1
                    // if we're not previous, we're next
                    : this.cardCycle.showStep + 1;

        // divide the step by the number of steps and take the remainder
        this.cardCycle.showStep = goToStep % this.cardCycle.numSteps;

        // log the top-left visible card in the card cycle as the one to keep visible if/when resizing
        this.cardCycle.showThumb = this.cardCycle.numFit * this.cardCycle.showStep + 1;

        // scroll it
        this.cardCycleScroll();

        //Update Scroll Information
        this.calculateCardCyclePagerMeta();
    },

    cardCycleScroll: function( duration ) {
        var coords = {
                // calculate the top by measuring the size of the step and multiplying by the number of step we're on
                top: this.cardCycle.stepSize * this.cardCycle.showStep,
                // we never scroll left/right
                left: 0
            };

        // scroll it
        this.cardCycle.$container.scrollTo( coords, {
            // use the default duration or the one passed to the function
            duration: duration === undefined ? G5.props.ANIMATION_DURATION : duration
        } );
    },



    /* *************************************************************
        Model - keeping this in the view unless shizz gets nasty
       ************************************************************* */

    getActiveCard: function() {
        var cardId = this.model.get( 'drawToolCardId' ),
            card;

        if ( !cardId ) { return null; }

        // enforce number type
        cardId = typeof cardId === 'string' ? parseInt( cardId, 10 ) : cardId;

        card = _.where( this.model.get( 'eCards' ), { id: cardId } );
        card = card.length ? card[ 0 ] : null;

        return card;
    },
    setActiveCardId: function( id, uploadUrl ) {
        var card;
        // enforce number type
        id = typeof cardId === 'string' ? parseInt( id, 10 ) : id;

        this.model.set( 'drawToolCardId', id );

        if( id === null ) {
            return;
        }
        card = this.getActiveCard(); // once the id is set, we can use this function

        this.model.set( 'drawToolCardUrl', card.largeImage );
        this.model.set( 'drawToolCardType', card.cardType );

        this.trigger( 'cardChanged', card );// trigger change with goodies
    },
    setActiveCardUpload: function( imageUrl, videoUrl ) {
        this.model.set( 'drawToolCardId', null );
        this.model.set( 'drawToolCardUrl', imageUrl );
        this.model.set( 'drawToolCardType', this.CARD_TYPES.UPLOAD );
        this.model.set( 'drawToolVideoUrl', videoUrl );

        this.trigger( 'cardChanged', null );
    },



    /* ****************************************************
        Misc. and Helpers
       **************************************************** */
    addConfirmTip: function( $trig, cont ) {
        $trig.qtip( {
            content: { text: cont },
            position: {
                my: 'top center',
                at: 'bottom center',
                container: this.$el,
                viewport: $( '#drawToolView' ),
                adjust: { method: 'shift none' },
                effect: false
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
                classes: 'ui-tooltip-shadow ui-tooltip-light PURLCommentAttachLinkTip PURLCommentAttachLinkTipPhoto',
                padding: 0,
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            }
        } );
    },

    updateOwnUpload: function() {
        var currentTabName = this.model.get( 'activeEcardTab' ),
            drawToolUploadClick = this.parentView.$el.find( '.drawToolUploadClick' );        

        if( currentTabName === 'memes' ) {
            if( this.model.get( 'canUpload' ) && this.model.get( 'allowOwnMemeUpload' ) ) {
                drawToolUploadClick.show();
            } else {
                drawToolUploadClick.hide();
            }
        } else {
            if( this.model.get( 'canUpload' ) && this.model.get( 'allowYourOwnCard' ) ) {
                drawToolUploadClick.show();
            } else {
                drawToolUploadClick.hide();
            }
        }
    },

    setNoneCard: function() {
        this.model.set( 'drawToolCardType', this.CARD_TYPES.NONE );
    },

    checkSubmit: function() {
        var canSubmit = false;

        if( this.stickerData.length > 0 ) {
            canSubmit = false;
        } else {
            canSubmit = true;
        }

        return canSubmit;
    }

} );
