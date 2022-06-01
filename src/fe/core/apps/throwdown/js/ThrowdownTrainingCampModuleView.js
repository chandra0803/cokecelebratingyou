/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported ThrowdownTrainingCampModuleView*/
/*global
console,
$,
_,
LaunchModuleView,
ThrowdownTrainingCampModuleView:true
*/
//ThrowdownTrainingCampModuleView = ResourceCenterModuleView.extend({});
ThrowdownTrainingCampModuleView = LaunchModuleView.extend( {
    initialize: function() {

        //this is how we call the super-class initialize function
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //merge events from the superclass ModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );


        //console.log("[INFO] ThrowdownTrainingCampModuleView initialized ", this);
        var self = this;
        this.displayQTip = false;

        var buildToolTip = function ( $trainingCampModule ) {

            $trainingCampModule.qtip( {
                content: {
                    text: $trainingCampModule.find( 'ul' ).clone().css( { 'list-style': 'none', 'margin': 0 } ) // taking the content from the module rather than writing another template
                },
                position: {
                    my: 'right rightMiddle',
                    at: 'left rightMiddle',
                    container: this.$container || $( 'body' ),
                    viewport: $( '.moduleContainerViewElement' )
                },
                show: false,
                hide: {
                    event: 'unfocus',
                    fixed: true,
                    delay: 200
                },
                style: {
                    padding: 0,
                    classes: 'ui-tooltip-shadow ui-tooltip-light participantPopoverQtip managerToolKitPop',
                    tip: {
                        corner: true,
                        width: 20,
                        height: 10
                    }
                },
                events: {
                    hide: function( event, api ) {
                        self.displayQTip = false;
                    },
                    show: function( event, api ) {
                        self.displayQTip = true;
                    }
                }
            } );
        };

        this.on( 'templateLoaded', function() {
            console.log( '[INFO] ThrowdownTrainingCampModuleView: attaching tooltip to this module: ', this.$el );

            buildToolTip( this.$el.find( '.module-liner' ) );

            _.delay( G5.util.textShrink, 100, this.$el.find( '.title-icon-view h3' ) );
            G5.util.textShrink( this.$el.find( '.title-icon-view h3' ) );

            this.bindToolTip();
        }, this );


        // resize the text to fit
        this.moduleCollection.on( 'filterChanged', function() {
            G5.util.textShrink( this.$el.find( '.title-icon-view h3' ) );
        }, this );
    },

    bindToolTip: function() {
        var $theModule = this.$el.find( '.module-liner' ),
            self = this;
        $theModule.off(); //remove any previous event bindings

        $theModule.on( 'click', function( e ) {
            // e.preventDefault ? e.preventDefault() : e.returnValue = false;
            e.preventDefault(); // I'm fairly certain that the proper jQ event handler takes care of the IE-specific mess on the above line
            if ( self.displayQTip === true ) {
                $theModule.qtip( 'hide' );
            } else {
                $theModule.qtip( 'show' );
            }
        } );
    }
} );
