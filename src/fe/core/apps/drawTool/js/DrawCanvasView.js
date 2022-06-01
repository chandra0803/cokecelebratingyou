/* DrawToolView - draw tool to write on cards for recognitions */
/*exported DrawCanvasView */
/*global
Modernizr,
DrawCanvasView:true
*/

/* DrawCanvasView - manage a canvas for drawing */
DrawCanvasView = Backbone.View.extend( {

    TOUCH_EVENTS: 'touchstart touchmove touchend',
    POINTER_EVENTS: 'MSPointerDown MSPointerMove MSPointerUp',
    MOUSE_EVENTS: 'mousedown mouseup mousemove',
    LINE_HEIGHT_MAPPING: { 4: 8, 8: 12, 16: 20, 32: 28 },

    //override super-class initialize function
    initialize: function ( opts ) {
        this.$bg = this.$el.find( '#bgContainer' );
        this.$canvas = null;
        this.$buffer = null;
        this.ctx = null;
        this.buffCtx = null;
        this.tool = { name: 'pencil', size: '8', color: '000000' };// default tool
        this.isToolDown = false;
        this.hasCanvas = Modernizr.canvas;
        this.parentView = opts.parentView;

        this.textTypeData = [];
        this.textMoveData = {};
        this.textMoveDataResize = {};
        this.currentEdit = {};
        this.inputPadding = 0;
        this.inputAddCounter = 0;

        this.paintedOn = false;

        this.currentSizeChanged = null;
        this.currentColorChanged = null;

        //eew, this is not inside the drawtool canvas $el, confusing
        this.$status = this.$el.closest( '#drawToolContainer' ).find( '#drawToolStatusContainer' );

        this.renderCanvas();

        this.eventsSetup();

        if ( opts.drawingData ) {
            this.setDrawingFromPng( opts.drawingData );
        }

    },

    events: {
        'mousedown .drawCanvas': 'doToolDown',
        'mousemove .drawCanvas': 'doToolMove',
        'mouseup .drawCanvas': 'doToolUp',

        'touchstart .drawCanvas': 'doToolDown',
        'touchmove .drawCanvas': 'doToolMove',
        'touchend .drawCanvas': 'doToolUp',

        'MSPointerDown .drawCanvas': 'doToolDown',
        'MSPointerMove .drawCanvas': 'doToolMove',
        'MSPointerUp .drawCanvas': 'doToolUp',

        'click #wPaint': 'doToolDown',
        'click #completeEdit': 'textEditDone',
        'input #typeTextInput': 'typeTextEdited',
        'click #clearTypeText': 'textTypeClear',
        'click #deleteTypeTool': 'textTypeDelete',
        'click .editCompleteBase': 'textTypeReEdited',

        'touchend #wPaint': 'doToolDown',
        'touchend #completeEdit': 'textEditDone',
        'touchend #typeTextInput': 'typeTextEdited',
        'touchend #clearTypeText': 'textTypeClear',
        'touchend #deleteTypeTool': 'textTypeDelete',
        'touchend .editCompleteBase': 'textTypeReEdited',

        'mousedown #moveTypeTool': 'doTypeTextDown',
        'mousedown #inputResizeHolder': 'doTypeTextResizeDown',
        'mousemove .moveWrapper': 'doTypeTextMove',
        'mousemove #typeTextInput': 'doTypeTextResize',
        'mouseup #moveTypeTool': 'doTypeTextUp',
        'mouseup #typeTextInput': 'doTypeTextUp',
        'mouseup .actionButtons': 'doTypeTextUp',
        'mouseup .moveWrapper': 'doTypeTextUp',
        'mouseup #inputResizeHolder': 'doTypeTextResizeUp',

        'touchstart #moveTypeTool': 'doTypeTextDown',
        'touchstart #inputResizeHolder': 'doTypeTextResizeDown',
        'touchmove #moveTypeTool': 'doTypeTextMove',
        'touchmove .moveWrapper': 'doTypeTextMove',
        'touchmove #typeTextInput': 'doTypeTextResize',
        'touchend #moveTypeTool': 'doTypeTextUp',
        'touchend #typeTextInput': 'doTypeTextUp',
        'touchend .actionButtons': 'doTypeTextUp',
        'touchend .moveWrapper': 'doTypeTextUp',
        'touchend #inputResizeHolder': 'doTypeTextResizeUp',
        
        'MSPointerDown #moveTypeTool': 'doTypeTextDown',
        'MSPointerDown #inputResizeHolder': 'doTypeTextResizeDown',
        'MSPointerMove .moveWrapper': 'doTypeTextMove',
        'MSPointerMove #typeTextInput': 'doTypeTextResize',
        'MSPointerUp #moveTypeTool': 'doTypeTextUp',
        'MSPointerUp #typeTextInput': 'doTypeTextUp',
        'MSPointerUp .actionButtons': 'doTypeTextUp',
        'MSPointerUp .moveWrapper': 'doTypeTextUp',
        'MSPointerUp #inputResizeHolder': 'doTypeTextResizeUp',

        'click #moveTypeTool': function( e ) { e.preventDefault(); },

        'click .drawCanvas': function( e ) { e.preventDefault(); } // no clicks
    },

    eventsSetup: function() {
        var that = this;

        // when there is a drag that ends outside the canvas, terminate draw
        $( document ).on( 'mouseup', function( e ) {
            if ( that.isToolDown ) {
                that.doToolUp( e );
            }
        } );

        G5._globalEvents.on( 'writeEcardText', this.writeTextToCanvas, this );
        G5._globalEvents.on( 'drawEcardStickers', this.drawStickersToCanvas, this );

    },

    renderCanvas: function() {
        var w, h;
        if ( !this.hasCanvas ) { return; }
        w = this.$el.outerWidth();
        h = this.$el.outerHeight();

        this.$canvas = $( '<canvas class="drawCanvas" />' );
        this.$canvas.attr( { width: w + 'px', height: h + 'px' } );
        this.$canvas.css( { position: 'absolute', left: 0, top: 0, '-ms-touch-action': 'none' } );
        this.$el.append( this.$canvas );

        this.ctx = this.$canvas[ 0 ].getContext( '2d' );
        this.ctx.lineJoin = 'round';
        this.ctx.lineCap = 'round';

        this.$buffer = $( '<canvas class="bufferCanvas" />' );
        this.$buffer.attr( { width: w + 'px', height: h + 'px' } );
        this.$buffer.css( { position: 'absolute', left: 0, top: 0, display: 'none' } );
        this.$el.append( this.$buffer );

        this.buffCtx = this.$buffer[ 0 ].getContext( '2d' );
    },
    showCanvas: function() {
        if ( !this.hasCanvas ) { return; }
        this.$canvas.show();
    },
    hideCanvas: function() {
        if ( !this.hasCanvas ) { return; }
        this.$canvas.hide();
    },

    setBgImage: function( url ) {
        var that = this,
            $img;

        // same image? return
        if ( url === this.$bg.find( 'img' ).attr( 'src' ) ) { return; }

        // trigger drawing change event after image loads
        $img = $( '<img/>' )
            .load(
                { responseType: 'html' },
                function( responseText ) {
                    that.trigger( 'drawingChanged' );
                    G5.serverTimeout( responseText );
                }
            )
            .attr( 'src', url )
            .attr('crossOrigin', "Anonymous");

        this.$bg.empty().append( $img );

        // if the image has no src attribute, we hide it (certain browsers show the broken image icon when src is empty)
        $img[ !$img.attr( 'src' ) ? 'hide' : 'show' ]();
    },

    removeBgImage: function() {
        this.$bg.empty();
        this.trigger( 'bgImageRemovedOnTabSwitch' );
        this.trigger( 'drawingChanged' );
    },

    setDrawingFromPng: function( data ) {
        var that = this,
            img;
        if ( data ) {
            img = new Image();
            img.onload = function() {
                that.ctx.drawImage( img, 0, 0 );

                // flag painted and trigger event
                that.paintedOn = true;
                that.trigger( 'drawingChanged' );
            };
            img.src = data;
        }
    },
    setPaintedOn: function() {
        this.paintedOn = true;
    },
    getDrawingAsPng: function() {
        var dat = '';
        if ( this.paintedOn ) {
            dat = this.$canvas[ 0 ].toDataURL();
        }
        return dat;
    },    
    toBase64DataURL: function( url ) {        
        return fetch( url )
        .then( function( response )
          {
             return response.blob();
          } )
        .then( function( blob ) {
          return new Promise( function( resolve, reject ) {

                var reader = new FileReader();
                reader.onloadend = function() {                
                   resolve( reader.result );
                };
                reader.onerror = reject;
                reader.readAsDataURL( blob ); 
          } );                          
        } );  
            
    },
    getCompositeAsPng: function( fnUrl ) {
        var that = this,            
            image = '',
            dat = '',
            orig,
            src,            
            $img,
            w = this.$buffer.width(),
            h = this.$buffer.height();

        if ( this.hasDrawing() ) {
            // NOTE: the images must come from same domain, otherwise we get an error
            // Hence image is converted to base64 before it is draw to canvas to avoid the cross origin issue
        	
            return this.toBase64DataURL( this.$bg.find( 'img' )[ 0 ].src )
            .then( function( data ) { 

                try {
                    // in certain cases the bg image may not yet be loaded, so we use an img element           
                    image = new Image();
                    image.crossOrigin = 'Anonymous';
                    image.onload = function() {
                        that.buffCtx.drawImage( image, 0, 0, w, h ); // bg
                        that.buffCtx.drawImage( that.$canvas[ 0 ], 0, 0, w, h ); // drawing
                        dat = that.$buffer[ 0 ].toDataURL();
                        fnUrl( dat );
                        console.log( dat );
                        G5._globalEvents.trigger( 'CanvasBgDataSet' );
                    };
                    image.src = data;
                } catch ( error ) {
                    orig = that.$bg.find( 'img' ).attr( 'src' );
                    src = orig;
                    orig = orig.match( /\/\/([^\/]+)/ );
                    orig = orig && orig.length === 2 ? orig[ 1 ] : src;
                    console.log( '[ERROR] DrawCanvasView - cannot save card background image, it is from a different origin [' + orig + ']' +  error );
                    that.drawStringToCanvas( 'could not save\nbackground Image\nfrom different origin\n[' + orig + ']', 20, 20 );
                    dat = that.$canvas[ 0 ].toDataURL(); // give just the drawing without bg
                } 
            } );
        }
    },

    isBgImgLoaded: function() {
        var i = this.$bg.find( 'img' )[ 0 ];
        return i.complete || ( typeof i.naturalWidth !== 'undefined' && i.naturalWidth > 0 );
    },

    hasDrawing: function() { return this.paintedOn; },

    showMsg: function( msgClass ) {

        this.$status.show()
            .find( '.drawToolQTip' ).hide().end()
            .find( '.' + msgClass ).show();
    },
    hideMsg: function( msgClass ) {
        if ( msgClass ) {
            this.$status.find( '.' + msgClass ).hide();
        } else {
            this.$status.find( '.drawToolQTip' ).hide();
        }
        this.$status.hide();
    },

    setTool: function( name, sizePx, colorHex ) {
        this.tool = {
            name: name,
            size: sizePx, // do not include "px"
            color: colorHex // do not include "#"
        };

        if( this.tool.name === 'texttype' ) {
            this.$el.css( { cursor: 'text' } );
            this.tools[ this.tool.name ].setInputFontSize( this );
        } else {
            this.textTypeData.length && this.textTypeDelete();
            this.$el.css( { cursor: 'crosshair' } );
        }
    },

    clearDrawing: function() {
        var $c = this.$canvas;
        this.ctx.clearRect( 0, 0, $c.width(), $c.height() );
        this.paintedOn = false;

        // trigger event
        this.trigger( 'drawingChanged' );
    },


    // DRAWING
    doToolDown: function( e ) {
        e && this.normalizeEvent( e );
        var oSet = this.$canvas.offset(),
            x = ( ( e && e.pageX ) || ( oSet.left + 100 ) ) - oSet.left,
            y = ( ( e && e.pageY ) || ( oSet.top + 100 ) ) - oSet.top;

        e && e.preventDefault();

        // assume the offset is static during one 'drag' of tool, cache offsets
        this.oX = oSet.left;
        this.oY = oSet.top;

        if ( e && e._deadMouse ) { return; }

        if( !e ) {
            this.tool.name = 'texttype';
        }

        // forward to tool behavior
        if( this.tool.name === 'texttype' ) {
            this.tools[ this.tool.name ].down( e, this );
        } else {
            this.tools[ this.tool.name ].down( x, y, this );
        }

        // flag the tool as down (moving, dragging, etc)
        this.isToolDown = true;
    },
    doToolMove: function( e ) {
        this.normalizeEvent( e );
        var x = e.pageX - this.oX,
            y = e.pageY - this.oY;
        if ( e._deadMouse ) { return; }

        if( !this.parentView.model.get( 'canDraw' ) &&  this.tool.name !== 'texttype' ) {
            return;
        }

        if ( this.isToolDown ) {
            e.preventDefault();
            // forward to tool behavior
            this.tools[ this.tool.name ].move( x, y, this );
        }
    },
    doToolUp: function( e ) {
        this.normalizeEvent( e );
        var x = e.pageX - this.oX,
            y = e.pageY - this.oY;
        if ( e._deadMouse ) { return; }

        // forward to tool behavior
        this.tools[ this.tool.name ].up( x, y, this );

        // flag tool up
        this.isToolDown = false;

        this.paintedOn = true;

        // trigger event
        this.trigger( 'drawingChanged' );
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

    drawStringToCanvas: function( s, x, y, paintText ) {
        var lines = s.split( '\n' ),
            that = this;
        this.ctx.globalCompositeOperation = 'source-over';
        this.ctx.font = 'bold 16px Arial';
        if( paintText ) {
            this.ctx.fillStyle = '#' + this.tool.color;
        }
        _.each( lines, function( line, idx ) {
            that.ctx.fillText( line, x, ( idx ) * 20 + y + 14 );
            if( paintText ) {
                that.paintedOn = true;
                that.trigger( 'drawingChanged' );
            }
        } );

    },
    
    drawResponsiveStringToCanvas: function( text, x, y, lineHeight, fitWidth, color, fontSize ) {

        if( !text || ( text == '' ) ) {
            return;
        }
        var that = this,        
        words = text.split( ' ' ),
        currentLine = 0,
        idx = 1;
        fitWidth = fitWidth || 0;
        that.ctx.globalCompositeOperation = 'source-over';
        that.ctx.font = fontSize + 'px TCCCUnityTextBold';
        that.ctx.fillStyle = '#' + color;
        
        if ( fitWidth <= 0 )
        {
            that.ctx.fillText( text, x, y );
            return;
        }    

        while ( words.length > 0 && idx <= words.length )
        {
            var str = words.slice( 0, idx ).join( ' ' );
            var w = that.ctx.measureText( str ).width;
            if ( w > fitWidth ) {
                if ( idx == 1 )
                {
                    idx = 2;
                }
                that.ctx.fillText( words.slice( 0, idx - 1 ).join( ' ' ), x, y + ( lineHeight * currentLine ) );
                currentLine++;
                words = words.splice( idx - 1 );
                idx = 1;
            } else { 
                idx++; 
            }
        }
        if  ( idx > 0 ) {
            that.ctx.fillText( words.join( ' ' ), x, y + ( lineHeight * currentLine ) );
        }

        that.paintedOn = true;
        that.trigger( 'drawingChanged' );
    },

    /* ****************************************************
        Tools - create tool behaviors here
       *************************************************** */
    tools: {
        pencil: {
            down: function( x, y, dcv ) { // start line
                dcv.ctx.globalCompositeOperation = 'source-over';

                dcv.ctx.lineWidth = dcv.tool.size;
                dcv.ctx.strokeStyle = '#' + dcv.tool.color;
                dcv.ctx.fillStyle = '#' + dcv.tool.color;

                dcv.ctx.beginPath();
                dcv.ctx.moveTo( x, y );
                this.oldX = x;
                this.oldY = y;
            },
            move: function( x, y, dcv ) { // create line segment
                dcv.ctx.lineTo( x, y );
                dcv.ctx.stroke();
            },
            up: function( x, y, dcv ) { // stop line
                if ( this.oldX === x && this.oldY === y ) { // detect a click
                    dcv.ctx.beginPath();
                    dcv.ctx.arc( x, y, dcv.tool.size / 2, 0, Math.PI * 2, true );
                    dcv.ctx.closePath();
                    dcv.ctx.fill();
                } else { // end of a line
                    dcv.ctx.closePath();
                }
            }
        }, // pencil tool

        eraser: {
            down: function( x, y, dcv ) { // start line
                dcv.ctx.save();
                dcv.ctx.globalCompositeOperation = 'destination-out';

                dcv.ctx.lineWidth = dcv.tool.size;

                dcv.ctx.beginPath();
                dcv.ctx.moveTo( x, y );
                this.oldX = x;
                this.oldY = y;
            },
            move: function( x, y, dcv ) { // create line segment
                dcv.ctx.lineTo( x, y );
                dcv.ctx.stroke();
            },
            up: function( x, y, dcv ) { // stop line
                if ( this.oldX === x && this.oldY === y ) { // detect a click
                    dcv.ctx.beginPath();
                    dcv.ctx.arc( x, y, dcv.tool.size / 2, 0, Math.PI * 2, true );
                    dcv.ctx.closePath();
                    dcv.ctx.fill();
                } else { // end of a line
                    dcv.ctx.closePath();
                }
            }
        }, // eraser tool

        tree: { // tree tool
            maxDepth: 12,
            down: function( x, y, dcv ) { // start line
                dcv.ctx.globalCompositeOperation = 'source-over';

                dcv.ctx.strokeStyle = '#' + dcv.tool.color;

                this.treeRecurse( x, y, Math.PI / 2, 14, 60, dcv, 0 );
            },
            move: function(  ) {},
            up: function( ) {},
            treeRecurse: function( x, y, a, w, h, dcv, dep ) {
                var nx = x + h * Math.cos( a ),
                    ny = y - h * Math.sin( a );

                if ( dep >= this.maxDepth ) { return; }

                dcv.ctx.beginPath();
                dcv.ctx.moveTo( x, y );
                dcv.ctx.lineTo( nx, ny );

                dcv.ctx.closePath();
                dcv.ctx.lineWidth = w;
                dcv.ctx.stroke();

                this.treeRecurse( nx, ny, a + Math.PI / ( 3 + Math.random() * 5 ), w * 0.7, h * 0.7 + Math.random() / 4, dcv, dep + 1 );
                this.treeRecurse( nx, ny, a - Math.PI / ( 3 + Math.random() * 5 ), w * 0.7, h * 0.7 + Math.random() / 4, dcv, dep + 1 );
                // this.treeRecurse(nx,ny, a+Math.PI/2, w*0.7, h*0.7, dcv, dep+1);
                // this.treeRecurse(nx,ny, a-Math.PI/2, w*0.7, h*0.7, dcv, dep+1);
            }
        }, // tree tool

        star: { // star tool
            maxDepth: 12,
            down: function( x, y, dcv ) { // start line
                dcv.ctx.globalCompositeOperation = 'source-over';
                dcv.ctx.fillStyle = '#' + dcv.tool.color;
                this.drawStar( x, y, 2 + Math.random() * 30, dcv.ctx );
            },
            move: function( x, y, dcv ) {
                var xm = Math.random() * 60 - 30,
                    ym = Math.random() * 60 - 30,
                    sm = Math.random() * 15;
                this.drawStar( x + xm, y + ym, 2 + sm, dcv.ctx );
            },
            up: function(  ) {},
            drawStar: function( x, y, s, ctx ) {
                var a = Math.PI * Math.random(), // rand angle
                    nx, ny, i;
                ctx.beginPath();
                for ( i = 0; i < 10; i++ ) {
                    a += Math.PI / 5;
                    nx = x + s * ( i % 2 === 0 ? 0.5 : 1 ) * Math.cos( a );
                    ny = y + s * ( i % 2 === 0 ? 0.5 : 1 ) * Math.sin( a );
                    ctx[ i ? 'lineTo' : 'moveTo' ]( nx, ny );
                }
                ctx.closePath();
                ctx.fill();
            }
        }, // star tool
        // WIP #62895 Changes start
        texttype: {
            formData: {},
            mainHolder: $( '#wPaint' ),
            textToolToggleBtn: $( '#textToolToggle' ),
            up: function( ) {
                // Future home of something awesome
            },
            move: function( ) {
                // Future home of something awesome
            },
            down: function( event, dcv ) {
                this.mainHolder = dcv.$el;
                if ( $( '.textTypeTool' ) ) {
                    $( '.textTypeTool' ).remove();
                }
                console.log( 'DCV : ', dcv );
                console.log( 'DCV Tool: ', dcv.tool.name );
                if ( dcv.tool.name !== 'texttype' ) {
                    return;
                }

                this.createTypeTool( 'create', event, dcv );
            },
            createTypeTool: function( mode, event, dcv ) {
                var that = dcv,
                    inputId = ( mode === 'edit' ) ? ( $( event.target ).parent().attr( 'class' ).split( ' ' )[ 0 ].split( '-' )[ 1 ] ) : ( that.textTypeData.length + 1 ),
                    defaults = {
                        typetoolcontainer:
                            '<div class="textTypeTool" data-input-id="' + inputId + '"><div class="actionButtons"><button id="completeEdit"><i class="icon-check"></i></button><button id="deleteTypeTool"><i class="icon-trash"></i></button><button id="clearTypeText"><i class="icon-eraser"></i></button><button id="moveTypeTool"><i class="icon-touch"></i></button></div></div>',
                        typetoolinput:
                            '<div id="inputResizeHolder"><textarea placeholder="Type Here" id="typeTextInput" ></textarea></div>',
                        moveWrapper: '<div class="moveWrapper"></div>',
                        parentWidth: $( '#wPaint' ).outerWidth(),
                        parentHeight: this.mainHolder.outerHeight(),
                        inputWidth: this.calcWidth( this.mainHolder ),
                        inputMaxWidth: this.calcMaxWidth( this.mainHolder ),
                        inputHeight: 25,
                        inputPadding: 6
                    };

                that.inputPadding = defaults.inputPadding;
        
                var inputProps = {
                        top: this.calcInputTop( event, that, defaults.inputHeight, defaults.inputPadding ),
                        left: this.calcInputLeft( event, that ),
                        width: defaults.inputWidth,
                        'max-width': defaults.inputMaxWidth,
                        // height: defaults.inputHeight
                    },
                    moveWrapperProps = {
                        top: 0,
                        left: 0,
                        position: 'absolute',
                        'background-color': 'transparent',
                        width: that.$el.outerWidth(),
                        height: that.$el.outerHeight(),
                        'z-index': 3
                    },
                    typeToolContainer = $( defaults.typetoolcontainer ),
                    typeToolInput = $( defaults.typetoolinput ),
                    moveToolWrapper = $( defaults.moveWrapper ),
                    currentToolSize = that.tool.size;

                
                if ( mode === 'edit' ) {
                    var editedBoxClass = $( event.target ).parent().attr( 'class' ).split( ' ' )[ 0 ],
                        editedBoxValue = _.find( that.textTypeData, function( eachData ) {
                            return eachData.class === editedBoxClass;
                        } );

                    currentToolSize = ( !this.currentSizeChanged ) ? editedBoxValue[ 'fSize' ] : this.tool.size;
                    // currentToolSize = ( that.tool.size === editedBoxValue[ 'fSize' ] ) ? editedBoxValue[ 'fSize' ] : that.tool.size;
                    that.currentEdit.mode = 'edit';
                    that.currentEdit.index = $( event.target ).parent().attr( 'class' ).split( ' ' )[ 0 ].split( '-' )[ 1 ];

                    defaults.typetoolinput = '<div id="inputResizeHolder"><textarea placeholder="Type Here" id="typeTextInput" value="' + editedBoxValue[ 'textValue' ] + '"></textarea></div>';

                    defaults.typetoolcontainer = '<div class="textTypeTool" data-input-id="' + inputId + '"><div class="actionButtons"><button id="completeEdit"><i class="icon-check"></i></button><button id="deleteTypeTool"><i class="icon-trash"></i></button><button id="clearTypeText"><i class="icon-eraser"></i></button><button id="moveTypeTool"><i class="icon-touch"></i></button></div></div>';

                    inputProps = _.extend( inputProps, { top: editedBoxValue[ 'y-pos' ], left: editedBoxValue[ 'x-pos' ] } );
                    $( '.completeEdit-' + that.currentEdit.index ).remove();

                } else {

                    that.currentEdit.mode = 'create';
                    that.textTypeData.push( { 'id': that.textTypeData.length + 1, 'x-pos': inputProps.left, 'y-pos': inputProps.top, 'fSize': currentToolSize } );
                    console.log( 'TTD Start ;', that.textTypeData );
                }

                typeToolContainer.css( inputProps );
                moveToolWrapper.css( moveWrapperProps );
                typeToolInput.find( 'input' ).css( { padding: defaults.inputPadding + 'px' } );
                typeToolContainer.prepend( typeToolInput );
                this.mainHolder.append( typeToolContainer );
                this.mainHolder.append( moveToolWrapper );

                if( mode === 'edit' ) {                    
                    $( '#typeTextInput' ).val( editedBoxValue[ 'textValue' ] );
                }

                var activeTypeToolContainer = $( '.textTypeTool' );
                
                activeTypeToolContainer.find( 'textarea' ).css( { 'font-size': currentToolSize, 'padding': defaults.inputPadding, 'line-height': ( currentToolSize + 4 ) + 'px' } ).focus();
                // activeTypeToolContainer.find( 'textarea' ).css( { 'font-size': currentToolSize, 'padding': defaults.inputPadding, 'line-height': this.LINE_HEIGHT_MAPPING[ currentToolSize ] + 'px' } ).focus();
                that.textMoveDataResize[ 'x' ] = this.mainHolder.find( '#typeTextInput' ).outerWidth();
                that.textMoveDataResize[ 'y' ] = this.mainHolder.find( '#typeTextInput' ).outerHeight();
            },

            setInputFontSize: function( thisPassed ) {
                var that = thisPassed,
                    textTypeToolId = that.$el.find( '.textTypeTool' ).data( 'inputId' ),
                    currentItem = _.find( that.textTypeData, function( eachData ) {
                        return eachData.id === textTypeToolId;
                    } ),
                currentToolSize = ( !that.currentSizeChanged && currentItem ) ? currentItem[ 'fSize' ] : that.tool.size;
                that.$el.find( '.textTypeTool textarea' ).css( { 'font-size': currentToolSize, 'line-height': that.LINE_HEIGHT_MAPPING[ currentToolSize ] + 'px' } );
                if( currentItem ) {
                    currentItem[ 'fSize' ] = currentToolSize;
                }
                that.currentSizeChanged = null;
            },

            calcWidth: function ( thisPassed ) {
                var that = thisPassed;
                return that.outerWidth() - that.outerWidth() / 2;
            },

            calcMaxWidth: function ( thisPassed ) {
                var that = thisPassed;
                return that.outerWidth() - that.outerWidth() / 10;
            },
    
            calcInputLeft: function ( event, that ) {
                var containerWidth = that.$el.outerWidth(),
                    inputBoxWidth = this.calcWidth( that.$el ),
                    containerLeft = that.$el.offset().left,
                    mouseClickPositionLeft = ( ( event && event.pageX ) || ( containerLeft + 100 ) ) - containerLeft,
                    maxLeft = containerWidth - inputBoxWidth;

                return mouseClickPositionLeft > maxLeft
                    ? maxLeft - 10 // Adjusting 10px to avoid input ending up in the edge  of container
                    : mouseClickPositionLeft - 10; // Adjusting 10px to avoid input ending up in the edge  of container
            },
    
            calcInputTop: function ( event, that, inputHeight, inputPadding ) {
                var containerHeight = that.$el.outerHeight(),
                    containerTop = that.$el.offset().top,
                    mouseClickPositionTop = ( ( event && event.pageY ) || ( containerTop + 100 ) ) - containerTop,
                    maxTop = containerHeight - ( ( inputHeight * 2 ) + inputPadding );
                    
                console.log( containerHeight, inputHeight, containerTop, mouseClickPositionTop, maxTop );
    
                return mouseClickPositionTop > maxTop
                    ? maxTop - 10 // Adjusting 10px to avoid input ending up in the edge  of container
                    : mouseClickPositionTop - 10; // Adjusting 5px to avoid input ending up in the edge  of container
            }
        }
    },

    drawStickersToCanvas: function( e ) {
        e && e.preventDefault();
        var that = this,
            stickerHolder = this.parentView.$el.find( '#wPaint' ),
            stickerSizes = this.parentView.stickerSizes,
            stickers = stickerHolder.find( '.createdCommonSticker' );

        if( stickers.length ) {

            // Finding all the stickers and drawing them to the canvas 
            _.each( stickers, function( sticker, index ) {
                var $sticker = $( sticker ),
                    itemId = $sticker.data( 'itemId' ),
                    itemData = _.find( that.parentView.stickerData, function( sticker ) {
                        return itemId == sticker.id;
                    } );
                
                that.ctx.drawImage( $sticker.find( 'img' )[ 0 ], itemData[ 'x-pos' ], itemData[ 'y-pos' ], stickerSizes.sW, stickerSizes.sH ); // Stickers
                if( stickers.length === ( index + 1 ) ) {
                    that.paintedOn = true;
                    that.trigger( 'stickersDrawn' );
                }
            } );
        } else {
        	that.trigger( 'stickersDrawn' );
        }
    },

    textEditDone: function ( e ) {
        e.preventDefault();
        console.log( 'TTD : ', this.textTypeData );
        var activeInput = document.querySelector( '#typeTextInput' ),
        mainHolder = this.$el,
        computedStyles = window.getComputedStyle( activeInput ),
        xPadding = parseInt( computedStyles.getPropertyValue( 'padding-left' ).replace( 'px', '' ) ),
        yPadding = parseInt( computedStyles.getPropertyValue( 'padding-top' ).replace( 'px', '' ) ),
        textAreaWidth = mainHolder.find( '.textTypeTool' ).outerWidth(),
        currentTextTypeData = ( this.currentEdit.mode === 'edit' ) ? this.textTypeData[ this.currentEdit.index - 1 ] : this.textTypeData[ this.textTypeData.length - 1 ],
        textValue = currentTextTypeData[ 'textValue' ],
        currentToolColor = this.tool.color,
        currentToolSize = this.tool.size,
        textDoneProps = {
            top: this.textTypeData[ this.textTypeData.length - 1 ][ 'y-pos' ] + yPadding,
            left: this.textTypeData[ this.textTypeData.length - 1 ][ 'x-pos' ] + xPadding,
            width: textAreaWidth,
            color: '#' + currentToolColor,
            'font-size': currentToolSize,
            'line-height': ( currentToolSize + 4 ) + 'px'
            // 'line-height': this.LINE_HEIGHT_MAPPING[ currentToolSize ] + 'px'
        };

        if( this.currentEdit.mode === 'edit' ) {
            currentToolColor = ( !this.currentColorChanged ) ? currentTextTypeData[ 'color' ] : this.tool.color;
            // currentToolColor = ( this.tool.color === currentTextTypeData[ 'color' ] ) ? currentTextTypeData[ 'color' ] : this.tool.color;
            currentToolSize = ( !this.currentSizeChanged ) ? currentTextTypeData[ 'fSize' ] : this.tool.size;
            // currentToolSize = ( this.tool.size === currentTextTypeData[ 'fSize' ] ) ? currentTextTypeData[ 'fSize' ] : this.tool.size;

            textDoneProps =  _.extend( textDoneProps, 
                { 
                    top: this.textTypeData[ this.currentEdit.index - 1 ][ 'y-pos' ] + yPadding, 
                    left: this.textTypeData[ this.currentEdit.index - 1 ][ 'x-pos' ] + xPadding, 
                    color: '#' + currentToolColor, 
                    'font-size': currentToolSize, 
                    'line-height': ( currentToolSize + 4 ) + 'px'
                    // 'line-height': this.LINE_HEIGHT_MAPPING[ currentToolSize ] + 'px' 
                } 
            );
            textValue = currentTextTypeData[ 'textValue' ];
        }

        if( activeInput.value !== '' ) {
            // this.drawStringToCanvas( this.textTypeData[ this.textTypeData.length - 1 ][ 'textValue' ], textDoneProps.left, textDoneProps.top, true );

            // this.drawResponsiveStringToCanvas( this.textTypeData[ this.textTypeData.length - 1 ][ 'textValue' ], textDoneProps.left, textDoneProps.top, textLineHeight, textAreaWidth );
            var inputCounter = ( this.currentEdit.mode === 'edit' ) ? this.currentEdit.index : $( 'div[class^="completeEdit"]' ).length + 1,
            editedText = $(
                '<div class="completeEdit-' + inputCounter + ' editCompleteBase"><span>' + textValue + '</span></div>'
            );

            currentTextTypeData.color = currentToolColor;
            currentTextTypeData.class = 'completeEdit-' + inputCounter;
            currentTextTypeData.fSize = currentToolSize;
            currentTextTypeData.textAreaWidth = textAreaWidth;
            currentTextTypeData.lineHeight = ( currentToolSize + 4 );
            // currentTextTypeData.lineHeight = this.LINE_HEIGHT_MAPPING[ currentToolSize ];

            editedText.css( textDoneProps );
            mainHolder.append( $( editedText ) );

            mainHolder.find( '.textTypeTool' ).remove();
            mainHolder.find ( '.moveWrapper' ).remove();
        }

        if( this.currentColorChanged ) { 
            this.currentColorChanged = null;
        }

        if( this.currentSizeChanged ) { 
            this.currentSizeChanged = null;
        }
    },

    textTypeReEdited: function ( e ) {
        e && this.normalizeEvent( e );
        e.stopPropagation();
        console.log( $( e.target ) );
        this.tools.texttype.createTypeTool( 'edit', e, this );
    },

    typeTextEdited: function( e ) {
        e && this.normalizeEvent( e );
        if( this.textTypeData.length ) {
            if ( this.currentEdit.mode === 'edit' ) {
                this.textTypeData[ this.currentEdit.index - 1 ].textValue = event.target.value;
            } else {
                this.textTypeData[ this.textTypeData.length - 1 ].textValue = event.target.value;
            }
        }
    },

    textTypeClear: function( e ) {
        e.preventDefault();
        this.$el.find( '#typeTextInput' ).val( '' );
    },

    textTypeDelete: function( e ) {
        e && e.preventDefault();
        var indexToRemove = parseInt( this.$el.find( '.textTypeTool' ).attr( 'data-input-id' ) ) - 1;
        this.textTypeData.splice( indexToRemove, 1 );
        console.log( 'TTD Rem : ', this.textTypeData );
        this.$el.find( '.textTypeTool' ).remove();
        this.$el.find ( '.moveWrapper' ).remove();
    },

    doTypeTextDown: function( e ) {
        e && this.normalizeEvent( e );
        var that = this,
            moveDiv =  that.$el.find( '.textTypeTool' );
        that.textMoveData.isDown = true;
        that.textMoveData.offsetC = [
            moveDiv.offset().left - e.pageX - that.$el.offset().left, 
            moveDiv.offset().top - e.pageY - that.$el.offset().top
        ];
    },

    doTypeTextUp: function( e ) {
        e && this.normalizeEvent( e );
        var that = this;
        that.textMoveData.isDown = false;
        e.preventDefault();
    },

    doTypeTextMove: function( e ) {
        e && this.normalizeEvent( e );
        var that = this,
            moveDiv =  that.$el.find( '.textTypeTool' ),
            currentActiveTextType = ( that.currentEdit.mode === 'edit' ) ? that.textTypeData[ that.currentEdit.index - 1 ] : that.textTypeData[ that.textTypeData.length - 1 ];

        if ( that.textMoveData.isDown ) {

          e.preventDefault();
          var fixPositions = {
            x: e.pageX + that.textMoveData.offsetC[ 0 ],
            y: e.pageY + that.textMoveData.offsetC[ 1 ]
          };
          moveDiv.css( { 'left': findMax( 'x', fixPositions.x ) } );
          moveDiv.css( { 'top': findMax( 'y', fixPositions.y ) } );
        }
        
        function findMax( axis, value ) {

            var moveDivInputHeight = moveDiv.find( 'input' ).outerHeight(),
                actionButtonsHeight = moveDiv.find( '.actionButtons' ).outerHeight(),
                dragDivWidth = moveDiv.outerWidth(),
                wrapperWidth =  that.$el.outerWidth(),
                dragDivHeight = moveDivInputHeight + actionButtonsHeight,
                wrapperHeight =  that.$el.outerHeight(), 
                maxValue = ( axis === 'x' ) ? ( wrapperWidth - dragDivWidth ) : ( wrapperHeight - dragDivHeight ),
                returnVal;

            if( value <= 10 ) {
                returnVal = 10; // Adjusting 10px to avoid input ending up in the edge  of container
            } else if( value > maxValue ) {
                returnVal = maxValue - 10; // Adjusting 10px to avoid input ending up in the edge  of container
            } else {
                returnVal = value - 10; // Adjusting 10px to avoid input ending up in the edge  of container
            }

            currentActiveTextType[ axis + '-pos' ] = returnVal;
   
            return returnVal;
        }

    },

    doTypeTextResizeDown: function( ) {
        var that = this;
        that.textMoveDataResize.isDown = true;
    },

    doTypeTextResizeUp: function( ) {
        var that = this;
        that.textMoveDataResize.isDown = false;
    },

    doTypeTextResize: function ( e ) {
        var that = this,
            textarea = that.$el.find( '#typeTextInput' ),
            textTypeTool = that.$el.find( '.textTypeTool' );

        if (  that.textMoveDataResize.isDown && ( textarea.outerWidth()  != that.textMoveDataResize[ 'x' ] || textarea.outerHeight() != that.textMoveDataResize[ 'y' ] ) ) {
            textTypeTool.css( { width: textarea.outerWidth() } );
        }

        // store new height/width
        that.textMoveDataResize[ 'x' ] = textarea.outerWidth();
        that.textMoveDataResize[ 'y' ] = textarea.outerHeight();

    },
    writeTextToCanvas: function ( e ) {
       e && e.preventDefault();
        var that = this;

        _.each( that.textTypeData, function( eachData ) {
            var xPos = eachData[ 'x-pos' ] + that.inputPadding,
                yPos = eachData[ 'y-pos' ] + that.inputPadding,
                textValue = eachData[ 'textValue' ],
                textAreaWidth = eachData[ 'textAreaWidth' ],
                color = eachData[ 'color' ],
                fontSize = eachData[ 'fSize' ],
                textLineHeight = eachData[ 'lineHeight' ];
            yPos = yPos + ( textLineHeight - 4 );
            that.drawResponsiveStringToCanvas( textValue, xPos, yPos, textLineHeight, textAreaWidth, color, fontSize );
        } );

        that.$el.find( '.editCompleteBase' ).remove();
        if( this.parentView.model.get( 'activeEcardTab' ) == 'memes' ) {
            this.parentView.$el.find( '#drawToolCardType' ).val( 'meme' );
        } 
    },

    setSizeOrColorChange: function( type ) {
        this[ 'current' + type + 'Changed' ] = true;
        if( type === 'Size' ) {
            this.tool.size = this.parentView.model.get( 'activeSize' );
            this.tools[ this.tool.name ].setInputFontSize( this );
        }
    },

    clearTextData: function() {
        this.$el.find( '.editCompleteBase' ).remove();
        this.$el.find( '.textTypeTool' ).remove();

        this.textTypeData = [];
        this.textMoveData = {};
        this.textMoveDataResize = {};
        this.currentEdit = {};
        this.inputPadding = 0;
        this.inputAddCounter = 0;
        this.currentSizeChanged = null;
        this.currentColorChanged = null;
    },

    triggerTextEdit: function() {
        this.doToolDown( null );
    }
    // WIP #62895 Changes end
    // Please note that there also other changes inside the file related to this feature. Search the repo for commit with message "WIP #62895 - Meme & Sticker tool changes" to see all the changes

} );
