/*exported CheersPopoverView */
/*global
TemplateManager,
Backbone,
RecognitionEzView,
CheersPopoverView:true
*/

// Participant popover view
// - display a popover of a single or multiple participants
// - if single, then optionally display public information (based on supplied JSON)
// - includes plugin method for easy creation
CheersPopoverView = Backbone.View.extend( {

    tagName: 'div',
    className: 'cheersPopoverView',

    //init function
    initialize: function( opts ) {
        var that = this, extraClasses = '';
        
        this.tplName = 'cheersPopoverView';
        this.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_BASE_ROOT + 'tpl/';

        this.$trigger = $( opts.triggerEl );
        this.$container = opts.containerEl ? $( opts.containerEl ) : $( 'body' );
        
        // increase the z-index when we are popping over a sheet
        this.isElevate = opts.isSheet ? true : false;
        
        this.canReload = opts.canReload ? true : false;

        //from the triggers 'data-participant-ids' attr, JQuery .data() method parses JSON
        this.participantIds = this.$trigger.data( 'participantIds' );
        this.recognitionId = this.$trigger.data( 'cheersPromotionId' );
        this.participantInfoType = this.$trigger.data( 'participantInfoType' );
        

        //optional classes for qtip
        extraClasses += this.isElevate ? ' elevate' : '';

        //setup the tooltip plugin
        this.$trigger.qtip( {
            content: { text: '<div class="spinnerBox"></div>' },
            position: {
                my: 'top center',
                at: 'center center',
                container: that.canReload ? $( 'body' ) : that.$trigger.parent(),
                effect: false,
                viewport: $( 'body' ),
                adjust: {
                    method: 'shift shift'
                }
            },
            show: {
                event: 'click',
                ready: false
            },
            hide: {
                event: 'unfocus',
                fixed: true,
                delay: 200
            },
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-light cheersPopoverQtip' + extraClasses,
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            },
            events: {
                // show == before anim show done
                // visible == after anim show complete
                visible: function( event, api ) { that.doShown();}
            }
            //prevent default (for anchor tags)
        } ).click( function( e ) {e.preventDefault();return false;} );



    },

    // assure template is loaded and grab data from server
    render: function( isMore ) {
        var that = this,
            params = {};

        // if rendered and not requesting more (isMore), then leave it be
        // if( this._isRendered && isMore !== true ) { return this; }

        // params
        if( this.participantIds ) {params.participantIds = this.participantIds;}
        if( this.recognitionId ) {params.recognitionId = this.recognitionId;}
        if( this.participantInfoType ) {params.participantInfoType = this.participantInfoType;}
        

        // get tpl
        TemplateManager.get( this.tplName, function( tpl, vars, subTpls ) {
            that.tpl = tpl;            
            $.ajax( {
                dataType: 'g5json', //must set this so SeverResponse can parse
                type: 'POST',
                url: G5.props.URL_JSON_CHEERS_INFO,
                data: params,
                success: function( serverResp ) {
                    var dat = serverResp.data;
                    that.initialRenderInner( dat );
                }
            } );
        }, this.tplUrl );

        return this;
    },


    initialRenderInner: function( data ) {
        this.$el.find( '.spinnerBox' ).spin( false );

        //set the contents of qtip
        this.$trigger.qtip( 'option', 'content.text', this.tpl( data ) );
        
        //refresh position and dimensions for new content
        this.$trigger.qtip( 'reposition' );

        this._isRendered = true;
    },
    

    doShown: function() {              
        //set our view element
        this.setElement( this.$trigger.data( 'qtip' ).elements.tooltip );
        this.$el.find( '.spinnerBox' ).spin();
        this.render();
    },

    

    events: {
        'click .closePopoverBtn': 'hide',        
        'click .cheers-popover-list a' : 'submitPointsRecognition'                     
    },

    submitPointsRecognition: function ( event ) {
        event.preventDefault();

        var self     = this,
            $tar     = $( event.currentTarget ),
            $points = $tar.data( 'pointsValue' ),
            params   = {
                recipientId: self.participantIds,                
                promotionId: self.recognitionId,
                points: $points,                
                responseType: 'html'
           };

        dataSent = $.ajax( {
            type: 'POST',
            url: G5.props.URL_JSON_CHEERS_SEND_RECOGNITION,
            data: params,
            dataType: 'g5html',
            beforeSend: function() {
                $tar.button( 'loading' ); // [2]
                self.destroyPopups();
            },
            success: function( serverResp ) {
                // console.log('[INFO] Cheers REcognition: postEzRecognition ajax post successfully posted this JSON object: ", serverResp);
                console.log( '[INFO] Cheers REcognition: server returned html' );

                var $serverResp = $( '<div />', {
                                    'class': 'modal fade autoModal recognitionResponseModal',
                                    'html': serverResp
                                  } );

                $serverResp.modal( 'show' );
                $serverResp.on( 'shown.bs.modal' , function () {
                  $( '.cofettiPop' ).show();
                  $( 'body' ).addClass('cofettiPopBody');
                });
                $serverResp.on( 'hide' , function () { 
                  $( '.cofettiPop' ).hide();
                  $( 'body' ).removeClass('cofettiPopBody');
                  G5._globalEvents.trigger( 'updatePRWall', self.canReload );				  				  
                });
                
            },
            error: function( jqXHR, status ) {
                console.log('Error..');
            },
            complete: function() {
                $tar.button( 'reset' );
            }
        } );

        dataSent.fail( function( jqXHR, textStatus ) {
            console.log( '[INFO] CheersRecognition: postCheersRecognition ajax post failed: ' + textStatus, jqXHR );
        } );
    },    

    hide: function() {
        this.$trigger.qtip( 'hide' );
    },

    destroyPopups: function() {
        $('.qtip').each(function(){
          $(this).data('qtip').hide();
        });        
    }
} );


//plugin
!function ( $ ) {

 /* PLUGIN DEFINITION */

  $.fn.cheersPopover = function ( option ) {
    return this.each( function () {

        var $this = $( this ),
            data = $this.data( 'participantPopover' ),
            options = typeof option == 'object' && option,
            ppv;

        if ( !data ) {
            ppv = new CheersPopoverView( _.extend( options || {}, { triggerEl: this } ) );
            $this.data( 'participantPopover', ( data = ppv ) );
        }

        if ( typeof option == 'string' ) {data[ option ]();}
    } );
  };

}( window.jQuery );
