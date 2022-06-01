/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported PaginationView, NominationsWinnersDetailPageView */
/*global
$,
_,
Backbone,
G5,
TemplateManager,
PaginationView,
ArrayBuffer,
Uint8Array,
NominationsWinnersDetailPageModel,
NominationsWinnersDetailPageView:true
*/
NominationsWinnersDetailPageView = PageView.extend( {
    //init function
    initialize: function( opts ) {
        var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'nominations';

		//this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

		//template names
        this.nominationWinnersDetailTpl = 'nominationWinnersDetailTpl';

        this.tplPath = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/';

		this.model = new NominationsWinnersDetailPageModel( {} );

        this.opts = this.options.data;

		this.model.loadData();

		 //is the text being translated?
        this.translatedText = opts.translatedText || false;

		this.model.on( 'translated', function( commentId ) {
            this.updateTranslatedText( commentId );
        }, this );

		this.model.on( 'loadDataFinished', function( comments ) {
            that.renderWinnersDetail( comments );
        } );

		// this.on( 'finishedRendering', function() {
		// 	$( '.carousel' ).cycle( {
		// 			fit: true, //to change the width and height of the slide manually, set fit to true in your options first
		// 			width: null,
		// 			height: 'auto',
		// 			nowrap: 0,
		// 			slideResize: false, // force slide width/height to fixed size before every transition
		// 			containerResize: 1, // resize container to fit largest slide
		// 			fx: 'scrollHorz',
		// 			speedIn: G5.props.ANIMATION_DURATION * 3,
		// 			speedOut: G5.props.ANIMATION_DURATION * 3,
		// 		    random: false,
		// 			speed: 500
		// 	} );
		// 	_.delay( G5.util.textShrink, 100, that.$el.find( '.awardPoints' ), { minFontSize: 20, vertical: true, horizontal: true } );
  //       } );


    },
    startSlick: function ( ) {

        var $slickEl = this.$el.find( '.awardMain .carousel' ); //find all modules with a carousel
        $slickEl.slick( {
            dots: false,
            infinite: true,
            speed: G5.props.ANIMATION_DURATION * 3,
            slidesToShow: 1,
            slidesToScroll: 1,
            centerMode: false,
            variableWidth: false,
            adaptiveHeight: false,
            autoplay: true,
            autoplaySpeed: G5.props.ANIMATION_DURATION * 20,
            prevArrow: '<i class="icon-arrow-1-left slick-arrow slick-prev withbg"></i>',
            nextArrow: '<i class="icon-arrow-1-right slick-arrow slick-next withbg"></i>',
        } );
        _.delay( G5.util.textShrink, 100, this.$el.find( '.awardPoints' ), { minFontSize: 20, vertical: true, horizontal: true } );
    },

    events: {
		'click  .participant-popover': 'attachParticipantPopover',
		'click .viewTeamList': 'viewTeamList',
		//translate text
        'click .translateTextLink': 'doTranslate',
        'click .pdfExportIcon': 'submitPDFData',
        'click .generateCertPdf': 'certPDFData'
    },
    certPDFData: function( e ) {
        var self = this,
            target = $( e.target ),
            tpl = 'cert1',
            claimId = target.data( 'claimid' ),
            orientation = 'landscape';

        e.preventDefault();
        console.log( G5.props.URL_JSON_NOMINATIONS_CERTIFICATE_DATA );
        G5.props.TMPL_SFFX = '.html';

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NOMINATIONS_CERTIFICATE_DATA + '&claimId=' + claimId,
            data: claimId,
            success: function ( serverResp, textStatus, jqXHR ) {
                var originalSuffix = '.html';
                G5.props.TMPL_SFFX = originalSuffix;
                TemplateManager.get( tpl, function( tpl, vars, subTpls ) {
                    var originalSuffix = '.html';
                    G5.props.TMPL_SFFX = originalSuffix;
                    var textHtml = jQuery.parseHTML( serverResp.data.certificate.reason );

                    serverResp.data.certificate.reason = $( textHtml ).text().substr( 0, 650 );
                    var certData = serverResp.data.certificate;
                    console.log( certData );
                    var htmlString = tpl( certData );
                    console.log( htmlString );
                    var certModal = $( '.certificateModal' );
                    var modalBody = certModal.find( '.modal-body' );

                    certModal.modal( 'show' );
                    certModal.on( 'hidden', function() {
                        if( !modalBody.hasClass( 'loading' ) ) {
                            modalBody.addClass( 'loading' );
                        }
                    } );
                    self.requestPdfWithAjax( htmlString, certModal, orientation );
                }, G5.props.URL_CERT_TPL_ROOT );

            },

            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( '[ERROR] Certificate Generation Nomination: ', jqXHR, textStatus, errorThrown );
            },
        } );


    },
    submitPDFData: function( e ) {
        console.log( this.model.get( 'nominationWinnersDetail' ) );
        var tpl = 'nominationsWinnersDetailPagePDF',
            self = this;
        e.preventDefault();
        G5.props.TMPL_SFFX = '.html';
        var originalSuffix = '.html';
        TemplateManager.get( tpl, function( tpl, vars, subTpls ) {
            G5.props.TMPL_SFFX = originalSuffix;
            var htmlPDF = tpl( self.model.toJSON() );
            // var thePages = $(htmlPDF).find('html');


            var certModal = $( '.certificateModal' );
            var modalBody = certModal.find( '.modal-body' );

            certModal.modal( 'show' );
            certModal.on( 'hidden', function() {
                if( !modalBody.hasClass( 'loading' ) ) {
                    modalBody.addClass( 'loading' );
                }
            } );
            console.log( htmlPDF );
                self.requestPdfWithAjax( htmlPDF, certModal, 'landscape' );


        }, G5.props.URL_CERT_TPL_ROOT );
    },
    requestPdfWithAjax: function( htmlString, $el, orientation ) {

        $.ajax( {
            contentType: 'application/json',
            dataType: 'text',
            type: 'POST',
            url: G5.props.URL_PDF_SERVICE,
            processData: false,
            data: JSON.stringify( {
                html: htmlString,
                orientation: orientation,
                convertToBase64: true, // updated pdf service to conditionally respond with base64 string of PDF if POST body includes this property set to true (bool)
                inlineCss: true,
                hasInlineAsyncResources: true
            } ),
            success: function ( resp ) {
                G5.props.TMPL_SFFX = '.html';
                var originalSuffix = '.html';

                if ( window.navigator.userAgent.search( 'rv:11.0' ) < 0 ) {
                    TemplateManager.get(
                        'embed',
                        _.bind( function ( tpl ) {
                            G5.props.TMPL_SFFX = originalSuffix;

                            $el.find( '.pdf-wrapper' ).html(
                                tpl( {
                                    pdfSrc: resp, // resp is base64 encoded string, which we set to pdfSrc for a data URI solution

                                } )
                            );
                            //
                            var certModal = $( '.certificateModal' );
                            var progress = certModal.find( '.progress-indicator' );

                            $( progress ).text( '' );

                        }, this ),
                        ( G5.props.URL_CERT_TPL_ROOT )

                    );
                } else {
                    $( '#certificateModal .btn' ).click();
                    var raw = window.atob( resp );
                    var ab = new ArrayBuffer( raw.length );
                    var ia = new Uint8Array( ab );
                    for ( var i = 0; i < raw.length; i++ ) {
                        ia[ i ] = raw.charCodeAt( i );
                    }

                    window.navigator.msSaveBlob( new Blob( [ ab ], { type: 'application/pdf' } ), 'extract.pdf' );



                }

            }
        } );
    },

	viewTeamList: function( e ) {
		var $tar = $( e.currentTarget ),
            cont = $tar.closest( '.awardContent' ).find( '.teamNominationWinners' );
        if( $tar.data( 'export' ) ) {
            $tar.attr( 'target', '_blank' );
            return;
        }
        e.preventDefault();
        if( !$tar.data( 'qtip' ) ) {
            $tar.qtip( {
				content: { text: cont },
				position: {
					my: 'left center',
					at: 'right center',
					container: this.$el
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
        }
	},

	attachParticipantPopover: function( e ) {
        var $tar = $( e.target ),
            isSheet = ( $tar.closest( '.modal-stack' ).length > 0 ) ? { isSheet: true }  :  { isSheet: false };

        if ( $tar.is( '.avatarwrap' ) ) {
            $tar = $tar.closest( 'a' );
        }
        isSheet.containerEl = this.$el;
        //attach participant popovers
        if ( !$tar.data( 'participantPopover' ) ) {
            $tar.participantPopover( isSheet ).qtip( 'show' );
        }

        e.preventDefault();
    },

    //events- translate text
    doTranslate: function( e ) {
        var $tar = $( e.target ),
            claimId = $( e.target ).parents( '.nominatorInfoC' ).find( '.comment-text' ).data( 'claimid' );

        // console.log();

        e.preventDefault();

        this.model.translateData( claimId, e );
        $tar.hide();
        // $tar.replaceWith('<span class="translateLinkDisabled">'+$tar.text()+'</span>');
    },

    updateTranslatedText: function( data ) {
        var pageFields = this.$el.find( '.translate' );

            console.log( $( data.target.target ) );
            console.log( $( data.target.target ).parent() );
        $( data.translation.fields ).each( function( index, value ) {
            if( value.name == 'comment' ) {
                $( data.target.target ).parents( '.nominatorInfoC' ).find( '.comment-text' ).text( value.text );
            }
            pageFields.each( function( indexp, valuep ) {
                console.log( $( valuep ).data( 'fieldId' ) );
                if( $( valuep ).data( 'fieldid' ) > 0 && $( valuep ).data( 'fieldid' ) == value.fieldId ) {
                    $( valuep ).text( value.text );
                }
            } );

        } );

    },

    renderWinnersDetail: function( comments ) {
		var that = this,
    		tplName = 'nominationWinnersDetail',
            $container = that.$el.find( '.nominationsTableWrap' ),
    		commentsData = comments;
        console.log( comments );
        if( isNaN( parseFloat( commentsData.nominationWinnersDetail[ 0 ].award ) ) ) {
          commentsData.nominationWinnersDetail[ 0 ].other = true;
        } else {
          commentsData.nominationWinnersDetail[ 0 ].other = false;
        }
        console.log( commentsData );
		TemplateManager.get( tplName, function( tpl ) {
			$container.empty().append( tpl( commentsData ) );

			that.$el.find( '.nominationsTableWrap [data-toggle=tooltip]' ).tooltip();
			that.trigger( 'finishedRendering' );
            that.startSlick();
		}, that.tplUrl );
    }
} );
