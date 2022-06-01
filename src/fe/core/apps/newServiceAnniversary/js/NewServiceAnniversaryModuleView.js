/*exported NewServiceAnniversaryModuleView*/
/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*global
console,
$,
_,
G5,
LaunchModuleView,
NewServiceAnniversaryModuleView:true
*/
NewServiceAnniversaryModuleView = LaunchModuleView.extend( {

    initialize: function() {

        //this is how we call the super-class initialize function
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //merge events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        this.displayQTip = true;

        this.on( 'templateLoaded', function() {
            //console.log("[INFO] NewServiceAnniversaryModuleView: attaching tooltip to this module: ", this.$el);
            // resize the text to fit
            // the delay is to wait for custom fonts to load
            G5.util.textShrink( this.$el.find( '.wide-view h3' ) );
            _.delay( G5.util.textShrink, 100, this.$el.find( '.wide-view h3' ) );
        }, this );


    },

    events: {
        'click .personName': 'attachParticipantPopover'
    },

    attachParticipantPopover: function( e ) {
        var $tar = $( e.target );
        window.checkS = ' initialized1 ';
        //attach participant popovers
        if ( !$tar.data( 'participantPopover' ) ) {
            $( e.target ).participantPopover( { containerEl: this.$el } ).qtip( 'show' );
        }
        e.preventDefault();
    }
} );












