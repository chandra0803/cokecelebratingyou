/*exported RecognitionAdvisorModuleView*/
/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*global
console,
$,
_,
G5,
LaunchModuleView,
RecognitionAdvisorPageView:true
*/
RecognitionAdvisorPageView = PageView.extend( {

    initialize: function() {

        //this is how we call the super-class initialize function
    	PageView.prototype.initialize.apply( this, arguments );

        //merge events from the superclass LaunchModuleView
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        this.displayQTip = true;

        this.on( 'templateLoaded', function() {
            //console.log("[INFO] RecognitionAdvisorModuleView: attaching tooltip to this module: ", this.$el);
            // resize the text to fit
            // the delay is to wait for custom fonts to load
            G5.util.textShrink( this.$el.find( '.wide-view h3' ) );
            _.delay( G5.util.textShrink, 100, this.$el.find( '.wide-view h3' ) );
        }, this );


    },

    events: {
        'click .itemName': 'attachParticipantPopover',
		    'click .myteamTable .regDetailView': 'tableClickHandler'
    },
	
    attachParticipantPopover: function( e ) {
        var $tar = $( e.target );
        //attach participant popovers
        if ( !$tar.data( 'participantPopover' ) ) {
            $( e.target ).participantPopover( { containerEl: this.$el } ).qtip( 'show' );
        }
        e.preventDefault();
    },
	
	tableClickHandler: function( e ) {
        e.preventDefault();

        var $tar = $( e.target ).closest( 'a' );

        // for table cells
        if( $tar.closest( 'tbody, tfoot' ).length ) {
            var linkTarget = $tar.attr( 'target' );

            if( linkTarget ) {
                switch( linkTarget ) {
                    case '_blank' :
                    case '_parent' :
                    case '_self' :
                    case '_top' :
                        break;
                    case '_sheet' :
                        G5.util.doSheetOverlay( false, $tar.attr( 'href' ), $tar.data( 'urlName' ) );
                        break;
                    default :
                        break;
                }
                return;
            }


        }
    }
	
} );