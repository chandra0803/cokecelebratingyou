/*exported RecognitionPagePreviewView*/
/*global
$,
_,
Backbone,
PageView,
TemplateManager,
RecognitionPagePreviewView:true
*/
RecognitionPagePreviewView = PageView.extend( {
    certificateTemplates: {
        100: {
            name: 'youreAwesome',
            path: './../base/tpl/certificates/',
            orientation: 'portrait'
        },

        101: {
            name: 'youreAmazing',
            path: './../base/tpl/certificates/',
            orientation: 'portrait'
        },

        102: {
            name: 'keyToOurSuccess_1',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        103: {
            name: 'excellence_1',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        104: {
            name: 'peakNomination',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        105: {
            name: 'rockStar_1',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        106: {
            name: 'beBold',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        107: {
            name: 'frontline',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        108: {
            name: 'playToWin',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        109: {
            name: 'resultsMatter',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        110: {
            name: 'rightWay',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        111: {
            name: 'welcomeToTheTeam',
            path: './../base/tpl/certificates/',
            orientation: 'portrait'
        },

        112: {
            name: 'accelerateMyPerformance',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        113: {
            name: 'courageousInspirationalDynamic',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        114: {
            name: 'badAssLeader',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        115: {
            name: 'thankYou',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        116: {
            name: 'wereSoInSync',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        117: {
            name: 'definitionOfLeader',
            path: './../base/tpl/certificates/',
            orientation: 'portrait'
        },

        118: {
            name: 'wayToGo',
            path: './../base/tpl/certificates/',
            orientation: 'portrait'
        },

        151: {
            name: 'keyToOurSuccess_2',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        152: {
            name: 'excellence_2',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        153: {
            name: 'rockStar_2',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        154: {
            name: 'magentaPlaymaker',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        180: {
            name: 'csWeek2015',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        },

        201: {
            name: 'oneTeam',
            path: './../base/tpl/certificates/',
            orientation: 'portrait'
        },

        211: {
            name: 'diversityInclusionAllStar',
            path: './../base/tpl/certificates/',
            orientation: 'landscape'
        }
    },

    //override super-class initialize function
    initialize: function ( opts ) {
        //var self = this;
        this.model = new Backbone.Model();

        this.appName = 'recognition';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        this.$el.find( '.table' ).responsiveTable();

        this.previewCertData = opts.previewCertData;

        /*
        The recognition preview view is the same, regardless of whether someone is viewing a preview
        view with points, with a plateau award, or with the calculator.

        The pages are largely static, so they will be left for the backend to build (no loading JSON,
        no loading template, etc). As such, the main purpose of the view is to handle events such as
        profile popovers and send/edit/cancel buttons at the bottom of the page.
        */
    },

    events: {
        'click .profile-popover': 'attachParticipantPopover',
        'click button': 'buttonController',
        'click .testLink': 'previewCertificate'
    },

    previewCertificate: function( e ) {
        e.preventDefault();

        var certificateTpl = this.certificateTemplates[ this.previewCertData.certificateId ];

        TemplateManager.get(
            certificateTpl.name,
            _.bind( function( tpl ) {
                var htmlString = tpl( {
                    recipientFirstName: this.previewCertData.recipientFirstName,
                    recipientLastName: this.previewCertData.recipientLastName,
                    recognizer: this.previewCertData.senderName,
                    date: this.previewCertData.date,
                    message: this.previewCertData.comment,
                    imageServerBaseUrl: G5.props.URL_QA_IMG_SERVER_OVERRIDE || 'http://tmobilerecognition.performnet.com' //TODO: determine strategy for setting based on environment
                } );
                this.openPreviewModal( htmlString );
            }, this ),
            ( G5.props.URL_CERT_TPL_ROOT || certificateTpl.path )
        );
    },

    openPreviewModal: function( htmlString ) {
        var previewModal = $( '#previewCertModal' );

        previewModal.find( '.modal-body' ).html( htmlString );
        previewModal.modal( 'show' );
    },

    attachParticipantPopover: function( e ) {
        var $tar = $( e.target );
        //attach participant popovers
        if( !$tar.data( 'participantPopover' ) ) {
            $( e.target ).participantPopover( { containerEl: this.$el } ).qtip( 'show' );
        }
        e.preventDefault();
    },

    buttonController: function( e ) {

        var $button = $( e.target ),
            buttonId = e.target.id,
            //self = this,
            url = $button.data( 'url' );

        //aggregate all buttons here, to keep things tidy
        switch ( buttonId ) {
            case 'recognitionSendButtonCancel':
                this.cancelRecognition( e );
                break;

            //inside the 'cancel Recognition' modal:
            case 'recognitionSendCancelDialogCancel':
                this.recognitionSendCancelDialogClick( e, false );
                break;

            //inside the 'cancel Recognition' modal:
            case 'recognitionSendCancelDialogConfirm':
                this.recognitionSendCancelDialogClick( e, true );
                break;

            case 'recognitionSendButtonEdit':
                if( url ) {
                    e.preventDefault();
                    window.location = url;
                }
                break;

            case 'recognitionSendButtonSend':
                // disable and show busy (should make this a style class)
                $button.css( 'color', 'transparent' ).spin();
                $button.siblings( '.btn' ).attr( 'disabled', 'disabled' );
                // defer disable to end of stack so event may bubble
                _.defer( function() {$button.attr( 'disabled', 'disabled' );} );
                if( url ) {
                    e.preventDefault();
                    window.location = url;
                }
                break;

        }

    },

    /*
    These functions are for the send/edit/cancel buttons
    */
    cancelRecognition: function( e ) {

        e.preventDefault();
        var $tar = $( e.target );//,
            //self = this;
        // show a qtip
        if( !$tar.data( 'qtip' ) ) {

            this.addConfirmTip(
                $tar,
                $tar.closest( '.actionList' ).find( '.recognitionSendCancelDialog' )
            );
        }

    },

    /*
    These functions take care of the confirm tooltip
    */

    //add confirm tooltip
    addConfirmTip: function( $trig, cont ) {
        //attach qtip and show
        $trig.qtip( {
            content: { text: cont },
            position: {
                my: 'bottom center',
                at: 'top center',
                container: $trig
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
                classes: 'ui-tooltip-shadow ui-tooltip-light participantPopoverQtip',
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            }
        } );
    },

    recognitionSendCancelDialogClick: function( e, isConfirm ) {
        var $tar = $( e.target ),
            $cancel = this.$el.find( '#recognitionSendButtonCancel' );

        //just hide the qTip
        e.preventDefault();

        $tar.closest( '.ui-tooltip' ).qtip( 'hide' );

        // if cancel confirmed and if url is set, then let's go there
        if( isConfirm && $cancel.data( 'url' ) ) {
            window.location = $cancel.data( 'url' );
        }


    }

} );
