/*jslint browser: true, nomen: true, unparam: true*/
/*exported PublicRecognitionCertificateInterstitialPageView */
/*global
 $,
 _,
 G5,
 TemplateManager,
 console,
 PageView,
 PublicRecognitionModel,
 PublicRecognitionCertificateInterstitialPageView:true
 */

PublicRecognitionCertificateInterstitialPageView = PageView.extend( {

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

    $spinner: this.$el.find( '.spinner-wrapper' ),

    initialize: function ( opts ) {
        var recognitionId = opts.recognitionId || G5.props.urlParams.recognitionId ? G5.props.urlParams.recognitionId : false;
        this.recipientId = opts.recipientId || G5.props.urlParams.recipientId ? G5.props.urlParams.recipientId : false;

        G5.util.showSpin( this.$spinner );

        // if either one of these is false, do an early return and serve up some sort of error/404 visual rather than attempt to generate a PDF cert
        if ( !recognitionId || !this.recipientId ) {
            return this.renderError();
        }

        this.model = new PublicRecognitionModel( {
            pubRecId: recognitionId
        } );

        this.model.on( 'dataLoaded', _.bind( function () {
            this.createCertificate();
        }, this ) );
    },

    createCertificate: function () {
        var certificateId = this.model.get( 'viewCertificateId' );
        var certificateTpl = this.certificateTemplates[ String( certificateId ) ];
        var certificateForm = $( '#pdfForm' );

        TemplateManager.get(
            certificateTpl.name,
            _.bind( function ( tpl ) {
                var htmlString = tpl(
                    // this.model.attributes.certificates is an object containing key/value pairs where keyNames correspond
                    // to recipient IDs
                    this.model.get( 'certificates' )[ this.recipientId ]
                );
                certificateForm.find( '.html' ).val( htmlString );
                certificateForm.find( '.orientation' ).val( certificateTpl.orientation );
                certificateForm.attr( 'action', G5.props.URL_PDF_SERVICE || 'http://127.0.0.1:3032/pdf' );
                certificateForm.submit();
            }, this ),
            ( G5.props.URL_CERT_TPL_ROOT || certificateTpl.path )
        );
    },

    renderError: function () {
        G5.util.hideSpin( this.$spinner );
        this.$el.find( '.errorMsg' ).removeClass( 'hide' );
        return false;
    }
} );
