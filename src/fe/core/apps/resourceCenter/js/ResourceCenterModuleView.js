/*exported ResourceCenterModuleView*/
/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*global
console,
$,
_,
G5,
LaunchModuleView,
ResourceCenterModuleView:true
*/
ResourceCenterModuleView = LaunchModuleView.extend( {

    initialize: function() {

        //this is how we call the super-class initialize function
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //merge events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        this.displayQTip = false;

        this.on( 'templateLoaded', function() {
            //console.log("[INFO] resourceCenterModuleView: attaching tooltip to this module: ", this.$el);
            // resize the text to fit
            // the delay is to wait for custom fonts to load
            G5.util.textShrink( this.$el.find( '.wide-view h3' ) );
            _.delay( G5.util.textShrink, 100, this.$el.find( '.wide-view h3' ) );
        }, this );


        // resize the text to fit
        this.moduleCollection.on( 'filterChanged', function() {
            G5.util.textShrink( this.$el.find( '.wide-view h3' ) );
        }, this );
    },
} );
