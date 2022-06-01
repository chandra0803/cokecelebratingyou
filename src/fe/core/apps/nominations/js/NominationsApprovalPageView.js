/*jslint browser: true, nomen: true, unparam: true, unused: true*/
/*exported NominationsApprovalPageView, jqXHR, subTpls */
/*global
console,
$,
subTpls,
PageView,
TemplateManager,
NominationsApprovalPageModel,
NominationsApprovalPageView:true,
ArrayBuffer,
Uint8Array
*/
NominationsApprovalPageView = PageView.extend( {

    //override super-class initialize function
    initialize: function ( opts ) {

        //set the appname (getTpl() method uses this)
        this.appName = 'nominations';

        //template names
        this.tpl = 'nominationsApprovalPageTpl';
        this.tplPath = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        this.calcObj = null;
        this.savedCalcObj = null;
        this.savedCalcRows = [];
        this.calcData = null;
        this.didCalc = false;
        this.levelId = null;
        this.nodeId = opts.idJson.claimId;
        this.levelNumber = opts.idJson.levelNumber;
        this.statusSel = opts.idJson.status;
        this.model = new NominationsApprovalPageModel();
        this.statusCount = 0;
        this.promoChanged = false;
        this.promoModel = null;

        this.promotionId = opts.idJson.promotionId;

        this.ids = {
            promotionId: this.promotionId,
            levelNumber: this.levelNumber

        };

        this.model.loadApprovalData( this.ids );

        this.setupEvents();

        this.checkForServerErrors();
        this.model.on( 'translated', function( comment ) { this.updateTranslatedText( comment ); }, this );
        this.on( 'finishedTableLoaded', function() {
            this.$el.find('.button-nominationTable').show();
            this.setLevelData(); // Custom for coco cola wip#58122
        } );
    },

    events: {
        //promotion events
        'click #nominationChangePromoConfirmBtn': 'changePromotion',
        'click #nominationChangePromoCancelBtn': 'cancelQtip',
        'change #promotionId': 'updatePromotion',

        //UI
        'click .expandCollapseRow': 'expandCollapseDetails',
        'click .cumulativeExpand': 'getCumulativeDetails',
        'click .cumulativeCollapse': 'hideCumulativeDetails',
        'click .expandCollapseAll': 'expandCollapseAll',
        'click .viewMoreRows': 'viewMoreTableRows',

        'keyup .awardPointsInp': 'doPointsKeyup',
        'keydown .awardPointsInp': 'doPointsKeydown',
        'focus .awardPointsInp': 'doPointsFocus',
        'blur .awardPointsInp': 'decimalCheck',
        'click .translateTextLink': 'doTranslate',
        'change .notificationDate .datepickerTrigger': 'showSameForAllTip',
        'click .sameForAllTip a': 'doSameForAllClick',
        'click .compare': 'doComparison',
        'click .compareNomineeBlock .close-compare': 'removeComparison',
        'change #timePeriodFilter': 'filterStatusSelect',
        'change .timePeriod': 'calcMaxNominees',

        // modals
        'click .doViewRules': 'showRulesModal',
        'click .teamAddAward': 'teamAddAwardModal',

        //table events
        'click .sortable a': 'sortModal',
        'click .doTheSort': 'handleTableSort',
        'change .statusSelect': 'changeStatus',
        'change .levelPayoutSelect' : 'updateAward', // Custom for coco cola wip#58122
        'click .viewTableRow': 'viewCompareTableRow',
        'click .calcLink': 'doViewRecognitionCalculator',

        //popovers
        'click .popoverTrigger': 'showGenericPopover',
        'click .budgetIncreaseLink': 'budgetIncreasePopover',
        'click .viewApproverList': 'viewApproversPopover',
        'click .teamMemberTrigger': 'teamMemberPopover',
        'click .profile-popover': 'attachParticipantPopover',
        'click #nominationDoNotCancelBtn': 'cancelQtip',
        'mouseenter .optOut': 'showOptOutTip',
        'mouseleave .optOut': 'hideOptOutTip',

        //submits and validation
        'change .date': 'validateDates',
        'keyup .budgetIncrease': 'validateNumeric',

        'click .addAwardBtn': 'submitTeamAward',
        'click #nominationCancelConfirmBtn': 'doCancelApproval',
        'click .showActivityBtn': 'submitFilterForm',
        'click .sendBudgetRequest': 'submitBudgetRequest',
        'click .submitApproval': 'submitApproval',
        'click .updateBtn': 'reloadOrGoBack',
        //table pdf click
        'click .pdfExportIcon': 'submitPDFData',
        'click .generateCertPdf': 'certPDFData',

        //testing button
        'click .makeAllWinners': 'allWinners'
    },
    allWinners: function() {
        var statuses = $( '.statusSelect' );
        _.each( statuses, function( status ) {
            console.log( status );
            $( status ).val( 'winner' ).change();
        } );
    },
    setupEvents: function() {
        var that = this;

        this.model.on( 'approvalDataLoaded', this.render, this );
        this.model.on( 'approvalDataReloaded', this.rerender, this );
        this.model.on( 'nominationsLoaded', this.renderPromoDropdown, this );

        this.model.on( 'approvalTableLoaded', this.renderTable, this );
        this.model.on( 'loadMoreApprovals', this.appendTableRows, this );
        this.model.on( 'approvalTableLoaded', function() {

            if ( this.get( 'tabularData' ).isCumulativeNomination === true ) {
                that.hideExpCol();
            }
        } );
        this.model.on( 'cumulativeDataLoaded', function( details, id ) {
            that.renderCumulativeDetails( id );
        } );
        this.model.on( 'cumulativePDFDataLoaded', this.renderCumulativePDFDetails, this );
        this.on( 'pageRendered', this.checkForMobile, this );

        this.model.on( 'calcLoaded', function( calcObj ) {
            that.setCalcObj( calcObj );
        } );
    },
    hideExpCol: function() {
        this.$el.find( '.nominationTableControls' ).hide();
    },
    checkForMobile: function() {
        var $doneBtn = this.$el.find( '.submitApproval' );

        if ( screen.width <= 450 ) {

            this.$el.find( '.mobileDetectTooltip' ).show();
            $doneBtn.attr( 'disabled', 'disabled' );
        }
    },

    render: function() {
        var $container = this.$el.find( '.approvalSearchWrapper' ),
            $promotionListContainer = this.$el.find( '.promotionList' ),
            promo = this.model.get( 'promotion' ),
            levels,
            that = this;

        TemplateManager.get( this.tpl, function( tpl, vars, subTpls ) {
            var statusString = promo.status,
                statusArr = statusString.split( ',' ),
                data;

            statusArr = $.map( statusArr, $.trim );

            that.subTpls = subTpls;

            that.promoModel = that.model.get( 'promotion' );

            $container.empty().append( tpl( that.model.toJSON() ) );

            that.promotionId = promo.promoId;

            _.each( statusArr, function() {
                var $statusFilter = that.$el.find( '#statusFilter option' );

                _.each( $statusFilter, function( sfilter ) {
                    if ( $( sfilter ).val() === that.statusSel ) {
                        $( sfilter ).attr( 'selected', 'selected' );
                    }
                } );
            } );

            //prevent future dates from filters search
            that.$el.find( '.approvalFiltersWrap .datepickerTrigger' ).datepicker( 'setEndDate', '+0d' );

            if ( that.$el.find( '#levelsFilter' ).length ) {
                levels = that.$el.find( '#levelsFilter option:selected' );
            } else {
                levels = that.$el.find( '.levelsFilterInp' );
            }
            console.log( promo );
            that.levelId = promo.approvalLevels.length ? parseInt( levels.val() ) : null;
            console.log( that );
            that.nodeId = promo.nodeId;

            data = {
                promotionId: that.promotionId,
                levelId: that.levelId,
                nodeId: that.nodeId,
                statusFilter: that.statusSel
            };

            that.model.loadApprovalTable( data );

            that.trigger( 'pageRendered' );

        }, this.tplPath );

        TemplateManager.get( 'promotionName', function( tpl ) {
            // $promotionListContainer.find('.promoChangeWrapper').hide();
            $promotionListContainer.empty().append( tpl( promo ) );
        } );

        G5.util.hideSpin( this.$el );
    },

    rerender: function() {
        var $container = this.$el.find( '.approvalSearchWrapper' ),
            $promotionListContainer = this.$el.find( '.promotionList' ),
            promo = this.model.get( 'promotion' ),
            levels,
            data,
            that = this;

        TemplateManager.get( this.tpl, function( tpl, vars, subTpls ) {
            var statusString = promo.status,
                statusArr = statusString.split( ',' );

            statusArr = $.map( statusArr, $.trim );

            that.subTpls = subTpls;

            that.promoModel = that.model.get( 'promotion' );

            $container.empty().append( tpl( that.model.toJSON() ) );

            that.promotionId = promo.promoId;

            _.each( statusArr, function() {
                var $statusFilter = that.$el.find( '#statusFilter option' );

                _.each( $statusFilter, function( sfilter ) {
                    if ( $( sfilter ).val() === that.statusSel ) {
                        $( sfilter ).attr( 'selected', 'selected' );
                    }
                } );
            } );

            //prevent future dates from filters search
            that.$el.find( '.approvalFiltersWrap .datepickerTrigger' ).datepicker( 'setEndDate', '+0d' );

            if ( that.$el.find( '#levelsFilter' ).length ) {
                levels = that.$el.find( '#levelsFilter option:selected' );
            } else {
                levels = that.$el.find( '.levelsFilterInp' );
            }

            that.levelId = promo.approvalLevels.length ? parseInt( levels.val() ) : null;
            console.log( that );
            that.nodeId = promo.nodeId;

            data = {
                promotionId: that.promotionId,
                levelId: that.levelId,
                nodeId: that.nodeId,
                statusFilter: that.statusSel
            };

            that.model.loadApprovalTable( data );

            that.trigger( 'pageRendered' );

        }, this.tplPath );

        TemplateManager.get( 'promotionName', function( tpl ) {
            // $promotionListContainer.find('.promoChangeWrapper').hide();
            $promotionListContainer.empty().append( tpl( promo ) );
        } );

        G5.util.hideSpin( this.$el );
    },

    renderTable: function() {
        var $container = this.$el.find( '.approvalTableWrap' ),
            tableTpl = 'nominationsApprovalTableTpl',
            budgetTpl = 'nominationsApprovalBudgetTpl',
            $budgetCont = this.$el.find( '.approvalTopRightCont' ),
            promo = this.model.get( 'tabularData' ),
            data,
            that = this;
        G5.util.showSpin( this.$el.find('.approvalTableWrap') );        
        TemplateManager.get( tableTpl, function( tpl ) {

            $container.empty().append( tpl( that.model.toJSON() ) );

            data = {
                promotionId: that.promotionId,
                levelId: that.levelId,
                nodeId: that.nodeId
            };

            if ( promo.awardType === 'calculated' ) {
                that.model.getCalcSetup( data );
            }

            if ( promo.results.length === 0 ) {

                that.$el.find( '.export-tools' ).hide();
                that.$el.find( '.approvalsExportIconsWrapper' ).hide();
            } else {
                that.$el.find( '.export-tools' ).show();
                that.$el.find( '.approvalsExportIconsWrapper' ).show();
            }

            that.appendTableRows();

        }, this.tplPath );

        if ( !promo.budgetBalance && promo.nextApproverCount === 0 && promo.previousApproverCount === 0 ) {
            this.$el.find( '.approvalTopRightCont' ).hide();
            return false;
        }

        TemplateManager.get( budgetTpl, function( tpl ) {

            $budgetCont.empty().append( tpl( that.model.toJSON() ) );

        }, this.tplPath );

        G5.util.hideSpin( this.$el );

    },

    appendTableRows: function() {
        var tableData = this.model.get( 'tabularData' ),
            promo = this.model.get( 'promotion' ),
            $conatiner = this.$el.find( '#nominationApprovalTable tbody' ),
            $statusFilter = this.$el.find( '#statusFilter option' ),
            tpl = 'nominationsApprovalTableRowTpl',
            $viewMore = this.$el.find( '.viewMoreContainer' ),
            $awardTh = this.$el.find( 'th.award' ),
            currency = promo.currencyLabel,
            pointsTxt = $awardTh.data( 'msgPoints' ),
            statusWinner,
            that = this;

        _.each( $statusFilter, function( sf ) {
            if ( $( sf ).val() === 'winner' ) {
                statusWinner = true;
            }
        } );

        TemplateManager.get( tpl, function( tpl ) {
            $conatiner.append( tpl( that.model.toJSON() ) );

            that.responsiveTable();

            //prevent selecting past dates for notification date
            that.$el.find( 'table .datepickerTrigger' ).datepicker( 'setStartDate', '+0d' );

            //hiding table columns. Usually this is done by BE but the changes made to the JSON made it harder for them to do this.
            if ( !promo.timePeriodEnabled || promo.timePeriodEnabled && !statusWinner ) {
                that.$el.find( 'tr.timePeriod, th.timePeriod' ).hide();
            }

            if ( !promo.notificationDateEnabled ) {
                that.$el.find( 'tr.notificationDate, th.notificationDate' ).hide();
            }

            _.each( that.$el.find( '.statusSelect' ), function( $statusSel ) {
                if ( promo.timePeriodEnabled && promo.finalLevelApprover ) {
                    if ( $( $statusSel ).hasClass( 'noTP' ) ) {
                        $( $statusSel ).find( 'option[value="winner"]' ).attr( 'disabled', 'disabled' );
                    }
                }
            } );

            //change award headers based on award type
            if ( tableData.awardType === 'range' ) {
                if ( currency ) {
                    $awardTh.text( '(' + tableData.awardMin + '-' + tableData.awardMax + ')' + ' ' + currency );
                } else {
                    $awardTh.text( '(' + tableData.awardMin + '-' + tableData.awardMax + ') ' + pointsTxt );
                }
            } else if ( tableData.awardType === 'fixed' || tableData.awardType === 'calculated' ) {
                if ( currency ) {
                    $awardTh.text( currency );
                } else {
                    $awardTh.text( pointsTxt );
                }
            } else if ( tableData.awardType === 'none' && tableData.isCustomPayoutType === false) {
                    $awardTh.hide();
            }
            else if ( tableData.isCustomPayoutType === true) {
                    $awardTh.show();
            }

            // if(tableData.isCumulativeNomination){
            //     that.renderCycle();
            // }

            //Lazy load
            if ( tableData.totalPages > tableData.currentPage ) {
                $viewMore.show();
            } else {
               $viewMore.hide();
            }
            that.trigger( 'finishedTableLoaded' );
        }, this.tplPath );

        G5.util.hideSpin( this.$el );

    },
    decimalCheck: function( e ) {
        var that = this,
            amount = $( e.target ).val();

        console.log( that );
        console.log( e );
        if ( that.promoModel.payoutType === 'cash' && amount.length > 0 ) {
            amount = parseFloat( amount ).toFixed( 2 );
            $( e.target ).val( amount );
        }

    },
    //events- translate text
    doTranslate: function( e ) {
        var $tar = $( e.target ),
            $attr = $tar.attr( 'href' );

        this.model.translateData( $attr );
        e.preventDefault();
        $tar.replaceWith( '<dd class="translateLinkDisabled">' + $tar.text() + '</dd>' );
    },
    updateTranslatedText: function( comment ) {
        var tText = comment;
        this.$el.find( '.reason' ).html( tText );
    },
    certPDFData: function( e ) {
        var that = this,
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
            success: function ( serverResp ) {
                var originalSuffix = '.html';

                TemplateManager.get( tpl, function( tpl ) {
                    var textHtml = jQuery.parseHTML( serverResp.data.certificate.reason ),
                        certData = serverResp.data.certificate,
                        htmlString = tpl( certData ),
                        certModal = $( '.certificateModal' ),
                        modalBody = certModal.find( '.modal-body' );

                    G5.props.TMPL_SFFX = originalSuffix;


                    serverResp.data.certificate.reason = $( textHtml ).text().substr( 0, 650 );
                    // serverResp.data.certificate.reason = $(serverResp.data.certificate.reason).text();
                    console.log( certData );

                    console.log( htmlString );


                    certModal.modal( 'show' );

                    certModal.on( 'hidden', function() {
                        if ( !modalBody.hasClass( 'loading' ) ) {
                            modalBody.addClass( 'loading' );
                        }
                    } );

                    that.requestPdfWithAjax( htmlString, certModal, orientation );
                }, ( G5.props.URL_CERT_TPL_ROOT || './../base/tpl/certificates/' ) );
            },

            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( '[ERROR] Certificate Generation Nomination: ', jqXHR, textStatus, errorThrown );
            }
        } );

    },

    submitPDFData: function( e ) {
        var promo = this.model.get( 'promotion' ),
            tableData = this.model.get( 'tabularData' ),
            results = tableData.results,
            tpl = 'nominationsApprovalTablePDFExtract',
            $viewMore = this.$el.find( '.viewMoreContainer' ),
            that = this,
            data,
            thisData,
            allTabularData,
            originalSuffix = '.html';

            G5.props.TMPL_SFFX = '.html';
            e.preventDefault();

            console.log( tableData );
            if ( tableData.isCumulativeNomination === true ) {
                _.each( results, function( result ) {
                    console.log( result );
                    data = {
                        promotionId: that.promotionId,
                        nodeId: that.nodeId,
                        levelId: that.levelId,
                        paxId: result.paxId
                    };
                    that.model.loadCumulativePDFInfo( $.extend( {}, data ) );
                } );
                return false;
            }

            if ( that.model.get( 'allData' ) ) {
              thisData = that.model.get( 'allData' );

              _.each( that.model.get( 'tabularData' ).results, function( data ) {
                thisData.results.push( data );
              } );
            } else {
              thisData = that.model.get( 'tabularData' );
            }
            console.log( thisData );
            // thisData = thisData.toJSON();
            allTabularData = {
              'tabularData': thisData,
              'promotion': promo
            };


        TemplateManager.get( tpl, function( tpl ) {
            var htmlPDF = tpl( allTabularData ),
                certModal = $( '.certificateModal' ),
                modalBody = certModal.find( '.modal-body' );
            G5.props.TMPL_SFFX = originalSuffix;

            certModal.modal( 'show' );

            certModal.on( 'hidden', function() {
                if ( !modalBody.hasClass( 'loading' ) ) {
                    modalBody.addClass( 'loading' );
                }
            } );

            console.log( htmlPDF );
            that.requestPdfWithAjax( htmlPDF, certModal, 'portrait' );

            //hiding table columns. Usually this is done by BE but the changes made to the JSON made it harder for them to do this.
            if ( !promo.timePeriodEnabled && !promo.finalLevelApprover ) {
                that.$el.find( 'tr.timePeriod, th.timePeriod' ).hide();
            }

            if ( !promo.notificationDateEnabled ) {
                that.$el.find( 'tr.notificationDate, th.notificationDate' ).hide();
            }

            //Lazy load
            if ( tableData.totalPages > tableData.currentPage ) {
                $viewMore.show();
            } else {
               $viewMore.hide();
            }

        }, ( G5.props.URL_CERT_TPL_ROOT ) );
    },
    renderCumulativePDFDetails: function() {
        var promo = this.model.get( 'promotion' ),
            tableData = this.model.get( 'tabularData' ),
            tpl = 'nominationsApprovalTablePDFExtract',
            $viewMore = this.$el.find( '.viewMoreContainer' ),
            thisData,
            that = this,
            allTabularData,
            originalSuffix = '.html';

            G5.props.TMPL_SFFX = '.html';

            if ( that.model.get( 'allData' ) ) {
              thisData = that.model.get( 'allData' );
              console.log( that.model.get( 'tabularData.results' ) );
              _.each( that.model.get( 'tabularData' ).results, function( data ) {
                thisData.results.push( data );
                console.log( data );
                console.log( thisData.results );
              } );
              console.log( thisData );
            } else {
              thisData = that.model.get( 'tabularData' );
            }
            console.log( thisData );
            // thisData = thisData.toJSON();
            allTabularData = {
              'tabularData': thisData,
              'promotion': promo
            };

        TemplateManager.get( tpl, function( tpl ) {


            var htmlPDF = tpl( allTabularData );

            var certModal = $( '.certificateModal' );
            var modalBody = certModal.find( '.modal-body' );

            G5.props.TMPL_SFFX = originalSuffix;
            certModal.modal( 'show' );

            certModal.on( 'hidden', function() {
                if ( !modalBody.hasClass( 'loading' ) ) {
                    modalBody.addClass( 'loading' );
                }
            } );

            console.log( htmlPDF );
            that.requestPdfWithAjax( htmlPDF, certModal, 'portrait' );

            //hiding table columns. Usually this is done by BE but the changes made to the JSON made it harder for them to do this.
            if ( !promo.timePeriodEnabled && !promo.finalLevelApprover ) {
                that.$el.find( 'tr.timePeriod, th.timePeriod' ).hide();
            }

            if ( !promo.notificationDateEnabled ) {
                that.$el.find( 'tr.notificationDate, th.notificationDate' ).hide();
            }

            //render cycle if nominations are cumulative
            // if(tableData.isCumulativeNomination){
            //     that.renderCycle();
            // }

            //Lazy load
            if ( tableData.totalPages > tableData.currentPage ) {
                $viewMore.show();
            } else {
               $viewMore.hide();
            }

        }, ( G5.props.URL_CERT_TPL_ROOT ) );
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
                var originalSuffix = '.html',
                    raw = window.atob( resp ),
                    ab = new ArrayBuffer( raw.length ),
                    ia = new Uint8Array( ab ),
                    i;
                G5.props.TMPL_SFFX = '.html';

                if ( window.navigator.userAgent.search( 'rv:11.0' ) < 0 ) {
                    TemplateManager.get(
                        'embed',
                        _.bind( function ( tpl ) {
                            var certModal = $( '.certificateModal' );
                            var progress = certModal.find( '.progress-indicator' );
                            G5.props.TMPL_SFFX = originalSuffix;

                            $el.find( '.pdf-wrapper' ).html(
                                tpl( {
                                    pdfSrc: resp // resp is base64 encoded string, which we set to pdfSrc for a data URI solution

                                } )
                            );
                            //

                            $( progress ).text( '' );

                        }, this ),
                        ( G5.props.URL_CERT_TPL_ROOT )

                    );
                } else {

                    $( '#certificateModal .btn' ).click();
                    for ( i = 0; i < raw.length; i++ ) {
                        ia[ i ] = raw.charCodeAt( i );
                    }

                    window.navigator.msSaveBlob( new Blob( [ ab ], { type: 'application/pdf' } ), 'extract.pdf' );

                }

            }
        } );
    },

    renderTeamAward: function( id ) {
        var results = this.model.get( 'tabularData' ).results,
            selectedResult = _.where( results, { index: id } ),
            tableData = this.model.get( 'tabularData' ),
            promo = this.model.get( 'promotion' ),
            $awardHeader = this.$el.find( '.teamHeader .award' ),
            currency = promo.currencyLabel,
            pointsTxt = $awardHeader.data( 'msgPoints' );

        this.$el.find( '#addAwardModal .teamRecipientList' ).empty().append( this.subTpls.addAwardTpl( selectedResult[ 0 ] ) );

        //change award headers based on award type
        if ( tableData.awardType === 'range' ) {
            if ( currency ) {
                $awardHeader.text( '(' + tableData.awardMin + '-' + tableData.awardMax + ')' + ' ' + currency );
            } else {
                $awardHeader.text( '(' + tableData.awardMin + '-' + tableData.awardMax + ') ' + pointsTxt );
            }
        } else if ( tableData.awardType === 'fixed' || tableData.awardType === 'calculated' ) {
            if ( currency ) {
                $awardHeader.text( currency );
            } else {
                $awardHeader.text( pointsTxt );
            }
        }

        if ( !tableData.budgetBalance ) {
            this.$el.find( '.budgetContent' ).hide();
        } else {
            this.$el.find( '#addAwardModal .balance' ).text( tableData.budgetBalance );
        }
    },

    renderCumulativeDetails: function( claimData ) {
        var cumulatDat = this.model.get( 'cumulativeInfo' ),
            tableData = this.model.get( 'tabularData' ).results,
            row = _.where( tableData, { paxId: claimData.paxId, claimGroupId: claimData.claimGroupId } ),
            $tar = this.$el.find( '[data-paxid="' + claimData.paxId + '"][data-groupclaimid="' + claimData.claimGroupId + '"]' ),
            $detailRow = $tar.parents( 'tr' ).next( '.detailRow' ),
            ids = [];

            $detailRow.find( '.cumulativeCarousel .cycle' ).empty().append( this.subTpls.cumulativeCarouselDetails( cumulatDat ) );


        this.renderCycle( $detailRow, $tar );

        _.each( cumulatDat, function( cd ) {
            console.log( cd );
            ids.push( cd.claimId );
        } );
        console.log( row );
        $detailRow.append( '<input type="hidden" name="tabularData.results[' + row[ 0 ].index + '].id" value="' + ids + '" />' );
    },

    renderCycle: function( detailRow, tar ) {
        var $cycle = detailRow.find( '.cycle' ),
            $detailRow = detailRow,
            $tar = tar,
            $cycle = $( tar ).parent().next( '.detailRow' ).find( '.cycle' );//,
            //rowId = $( tar ).parent().next( '.detailRow' ).data( 'carousel-id' ),
            //prev = '#prev' + rowId,
            //next = '#next' + rowId;

            // $cycle.cycle( {
            //         fit: 1,
            //         containerResize: 0,
            //         slideResize: 0,
            //         timeout: 0,
            //         prev: prev,
            //         next: next,
            //         speed: 500,
            //         before: function( currSlideElement, nextSlideElement ) {
            //             var container = $( this ).parents( '.cumulativeCarousel' );
            //             container.css( 'height', Math.max( container.height(), $( nextSlideElement ).height() ) ).addClass( 'active' );
            //         },
            //         after: function() {
            //             $( this ).parents( '.cumulativeCarousel' ).css( 'height', $( this ).height() );
            //         }
            // } );




            $detailRow.slideDown();
            $tar.find( '.expandIcon' ).addClass( 'hide' );
            $tar.find( '.collapseIcon' ).removeClass( 'hide' );

            // if ( detailRow.find( '.cycle .item' ).first().height() ) {
            //     $cycle.parents( '.cumulativeCarousel.active' ).css( 'height', detailRow.find( '.cycle .item' ).first().height() );
            // } else {
            //     _.delay( function() {
            //         $cycle.parents( '.cumulativeCarousel.active' ).css( 'height', detailRow.find( '.cycle .item' ).first().height() );
            //     }, 500 );
            // }

            var $slickEl = $cycle; //find all modules with a carousel



            $slickEl.slick( {
                dots: true,
                infinite: true,
                speed: G5.props.ANIMATION_DURATION * 3,
                slidesToShow: 1,
                slidesToScroll: 1,
                centerMode: false,
                variableWidth: false,
                adaptiveHeight: true,
                //autoplay: false,
                //autoplaySpeed: G5.props.ANIMATION_DURATION * 20,
                prevArrow: '<i class="icon-arrow-1-left slick-arrow withbg slick-prev"></i>',
                nextArrow: '<i class="icon-arrow-1-right slick-arrow  withbg slick-next"></i>',
            } );


    },

    hideCumulativeDetails: function( e ) {
        var $tar = $( e.currentTarget ),
            $detailRow = $tar.parents( 'tr' ).next( '.detailRow' );



        $detailRow.slideUp();


       this.closeDetailRow( $tar );

        //$detailRow.find( '.item' ).hide();
    },

    closeDetailRow: function( $tar ) {
        var $detailRow = $tar.parents( 'tr' ).next( '.detailRow' );

        var $expandIcon = $tar.siblings( '.expandIcon' );

        $expandIcon.removeClass( 'hide' );
        $expandIcon.removeClass( 'disabled' );


        $tar.addClass( 'hide' );
        $tar.addClass( 'disabled' );

        _.delay( function() {
            console.log( 'test' );
            $detailRow.find( '.cycle' ).slick( 'unslick' );
            $detailRow.find( '.cycle' ).html( '' );
            $tar.removeClass( 'disabled' );
            $expandIcon.removeClass( 'disabled' );
        }, 500 );

        //$detailRow.find( '.item' ).hide();
    },

    // *******************
    // PROMOTION SETUP
    // *******************
    updatePromotion: function() {
        var $promoVal = this.$el.find( '#promotionId option:selected' ).val(),
            $promoName = this.$el.find( '#promotionId option:selected' ).text(),
            that = this,
            data;

            that.promoChanged = true;
        this.$el.find( '.promoChangeWrapper' ).show();
        _.delay( function() { $( '.nominationPromotionName' ).text( $promoName ); }, 100 );


        data = {
            promotionId: $promoVal,
            claimId: this.nodeId,
            levelNumber: 1,
            levelId: null
        };

        this.model.loadNewApprovalData( data );
    },

    changePromotion: function() {
        var $promotionPopover = this.$el.find( '.promoChangePopover' );

        $promotionPopover.closest( '.qtip' ).qtip( 'hide' );

        this.model.loadPromotionsList( this.ids );
    },

    renderPromoDropdown: function() {
        var $promotionListContainer = this.$el.find( '.promotionList' ),
            noms = this.model.get( 'nominations' ),
            that = this;

        TemplateManager.get( 'promotionsList', function( tpl ) {
            $promotionListContainer.find( '.promoChangeWrapper' ).hide();
            $promotionListContainer.append( tpl( noms ) );

            _.each( $promotionListContainer.find( '#promotionId option' ), function( promoIdOpt ) {
                if ( parseInt( $( promoIdOpt ).val() ) === that.promotionId ) {
                    $( promoIdOpt ).attr( 'selected', 'selected' );
                }
            } );
        } );
    },

    showRulesModal: function( e ) {
        var $rulesModal = $( 'body' ).find( '#rulesModal' ),
            $cont = $rulesModal.find( '.modal-body' ).empty();

        e.preventDefault();

        $cont.append( this.model.get( 'promotion' ).rulesText || 'ERROR, modal found no rulesText on promo' );

        $rulesModal.modal();
    },

    teamAddAwardModal: function( e ) {
        var $tar = $( e.target ),
            id = $tar.data( 'teamId' ),
            $awardModal = $( 'body' ).find( '#addAwardModal' );


        if ( $tar.hasClass( 'disabled' ) ) {
            return false;
        }

        this.renderTeamAward( id );

        $awardModal.modal();
    },

    // *******************
    // POPOVERS
    // *******************
    showGenericPopover: function( e ) {
        var $tar = $( e.target ),
            trigData = $tar.data( 'popoverContent' ),
            $cont = this.$el.find( '.' + trigData + 'Popover' ).first().clone();

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, this.$el );
        }
    },

    budgetIncreasePopover: function( e ) {
        var $tar = $( e.target ),
            $cont = _.clone( this.$el.find( '.budgetIncreasePopover' ) );

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, this.$el );
        }
    },

    attachParticipantPopover: function( e ) {
        var $tar = $( e.target ),
            isSheet = { isSheet: true }; //isSheet needs to be true for popover to show over team award modal.

        isSheet.containerEl = this.$el;

        if ( !$tar.data( 'participantPopover' ) ) {
            $tar.participantPopover( isSheet ).qtip( 'show' );
        }

        e.preventDefault();
    },

    viewApproversPopover: function( e ) {
        var $tar = $( e.target ),
            $cont = _.clone( $tar.siblings( '.approverListTooltip' ) ); //special for approver list

        e.preventDefault();
        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, $( '.approvalSearchWrapper' ) );
        }
    },

    teamMemberPopover: function( e ) {
        var $tar = $( e.target ),
            $cont = _.clone( $tar.parents( 'td' ).find( '.teamMembersTooltip' ) ); //special for team members list

        e.preventDefault();

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, $tar.parents( 'table' ), 'approvalsTeamTooltip' );
        }
    },

    attachPopover: function( $trig, content, container, extraClass ) {
        $trig.addClass( 'ignorePageOffset' );
        $trig.qtip( {
            content: { text: content },
            position: {
                my: 'left center',
                at: 'right center',
                adjust: {
                    method: 'shift none'
                },
                viewport: $( 'body' ),
                container: container
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
                classes: 'ui-tooltip-shadow ui-tooltip-light ' + extraClass,
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            }
        } );
    },

    // display a qtip for next buttons or tab clicks (uses class name to show proper error)
    onNavErrorTip: function( msgClass, $target, isDestroyOnly, errorMsg ) {
		//Bug id 77595: Removing class pin-body to make the popup error float near the field
		if(msgClass) {
			$( 'body' ).removeClass( 'pin-body' );
		}
        var $cont = this.$el.find( '.errorTipWrapper .errorTip' ).clone(),
            isBtn = $target.hasClass( 'btn' ) || $target.is( 'input' );

        //if old qtip still visible, obliterate!
        if ( $target.data( 'qtip_error' ) ) { $target.data( 'qtip_error' ).destroy(); }

        if ( isDestroyOnly ) {
            return;
        }

        $cont.find( '.' + msgClass ).show(); // show our message
        if ( errorMsg ) {
            $cont.find( '#errorText' ).text( errorMsg );
        }
        //attach qtip and show
        $target.qtip( {
            content: { text: $cont },
            position: {
                my: isBtn ? 'bottom center' : 'top center',
                at: isBtn ? 'top center' : 'bottom center',
                effect: this.isIe7OrIe8 ? false : true,
                viewport: $( 'body' ),
                container: this.$el,
                adjust: {
                    method: 'shift none'
                }
            },
            show: {
                event: false,
                ready: true
            },
            hide: {
                event: 'unfocus click',
                fixed: true,
                delay: 200
            },
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-red nominationErrorQTip',
                tip: {
                    corner: true,
                    width: 10,
                    height: 5
                }
            }
        } );
        $target.data( 'qtip_error', $target.data( 'qtip' ) ).removeData( 'qtip' );
    },


    // display a qtip for next buttons or tab clicks (uses class name to show proper error)
   onNavErrorTip1: function( msgClass, $target, isDestroyOnly, errorMsg ) {
        var $cont = this.$el.find( '.errorTipWrapper .errorTip' ).clone(),
            isBtn = $target.hasClass( 'btn' ) || $target.is( 'input' );
        //if old qtip still visible, obliterate!
        if ( $target.data( 'qtip_error' ) ) { $target.data( 'qtip_error' ).destroy(); }
        if ( isDestroyOnly ) {
            return;
        }
        $cont.find( '.' + msgClass ).show(); // show our message
        if ( errorMsg ) {
            $cont.find( '#errorText' ).text( errorMsg );
        }
        //attach qtip and show
        $target.qtip( {
            content: { text: $cont },
            position: {
                my: isBtn ? 'bottom center' : 'top center',
                at: isBtn ? 'top center' : 'bottom center',
                effect: this.isIe7OrIe8 ? false : true,
                viewport: $( 'body' ),
                container: this.$el.find( '#addAwardModal' ),
				adjust: {
                    method: 'shift none'
                }
            },
            show: {
                event: false,
                ready: true
            },
            hide: {
                event: 'unfocus click',
                fixed: true,
                delay: 200
            },
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-red nominationErrorQTip',
                tip: {
                    corner: true,
                    width: 10,
                    height: 5
                }
            }
        } );

        $target.data( 'qtip_error', $target.data( 'qtip' ) ).removeData( 'qtip' );
    },

    showSameForAllTip: function( e ) {
        var $tar = $( e.target ),
            $table = this.$el.find( '#nominationApprovalTable' );

        // NO TIP: single pending product
        if ( $table.find( 'tbody tr.paxRow' ).length <= 1 ) { return; }

        //error tip active? then do nothing (just let the error have the stage)
        if ( $tar.closest( '.validateme' ).hasClass( 'error' ) && $tar.qtip().elements.tooltip.is( ':visible' ) ) {
            return;
        }

        //have it already?
        if ( $tar.data( 'qtip_sfa' ) ) {
            $tar.data( 'qtip_sfa' ).show();
            setTimeout( function() {
                $tar.data( 'qtip_sfa' ).reposition();
            }, 1000 );
            return;
        }

        $tar.qtip( {
            content: this.$el.find( '#sameForAllTipTpl' ).clone().removeAttr( 'id' ),
            position: {
                my: 'left center',
                at: 'right center',
                container: $table,
                viewport: $( 'body' )
            },
            show: { ready: true, event: false },
            hide: {
                event: 'unfocus click',
                fixed: true,
                delay: 200
            },
            //only show the qtip once
            events: { hide: function() { $tar.qtip( 'destroy' ); } },
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-light',
                tip: {
                    width: 10,
                    height: 5
                }
            }
        } );

        $tar.data( 'qtip_sfa', $tar.data( 'qtip' ) ).removeData( 'qtip' );
    },

    doSameForAllClick: function( e ) {
        var $tar = $( e.target ),
            // find the active input: the link is in a tooltip with a qtip
            // which has a reference to the triggering element (target)
            $actInp = $tar.closest( '.ui-tooltip' ).data( 'qtip' ).elements.target,
            $allInp = this.$el.find( '#nominationApprovalTable .date:not([disabled])' ),
            inpVal = $actInp.val();

        e.preventDefault();

        $allInp.val( inpVal );

        this.$el.find( '#sameForAllTipTpl' ).closest( '.qtip' ).qtip( 'hide' );
    },

    cancelQtip: function( e ) {
        e.preventDefault();

        $( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );
    },
    showOptOutTip: function( e ) {

    var $cont;
    var $tar = $( e.target );
    if( $tar.parents( '.modal-body' ).length ) {
        $cont = this.$el.find( '.modal-body' );
    }else{
        $cont = $tar.parents( 'table' );
    }
    $tar.addClass( 'ignorePageOffset' );
    var content = $tar.data( 'msg' );
    $tar.qtip( {
        content: {
            text: content
        },
        position: {
            my: 'top center',
            at: 'bottom center',
            adjust: {
                // x: getQtipOffset(),
                // y: 0
                method: 'shift none' // shift x-axis inside viewport
            },
            container: $cont,
            //Used to fix qtip position in in ie7&8.
            effect: false,
            viewport: true// $('body'), // shift will try to keep tip inside this
        },
        show: {
            ready: true,
            event: false
        },
        hide: {
            event: false,
            fixed: true,
            delay: 200
        },
        style: {
            padding: 0,
            classes: 'ui-tooltip-shadow ui-tooltip-light',
            tip: {
                corner: true,
                width: 20,
                height: 10
            }
        }
    } );
    },
    hideOptOutTip: function ( e ) {

        $( e.target ).qtip( 'destroy' );
    },
    // *******************
    // UI STUFF
    // *******************
    responsiveTable: function () {
        var tableData = this.model.get( 'tabularData' ),
            colSpanCount = tableData.meta.columns.length,
            id = 0;

        this.$el.find( '.detailRow td' ).attr( 'colspan', colSpanCount );

        _.each( this.$el.find( '.table tr' ), function( row ) {
            id++;

            $( row ).attr( 'data-row-id', id );
        } );

        this.$el.find( '#nominationApprovalTable tbody tr:visible:odd' ).addClass( 'odd' );

        this.$el.find( '#nominationApprovalTable tr.detailRow:odd' ).addClass( 'odd' );
    },

    calcMaxNominees: function( e ) {
        var tabularData = document.querySelectorAll( '#nominationApprovalTable tbody tr' ),
            rows = [];

            $( tabularData ).each( function( ) {
                if( $( this ).hasClass( 'paxRow' ) || $( this ).hasClass( 'teamRow' ) ) {
                    rows.push( this );
                }
            } );
            var winners = {};
            var maxNominees = {};
            var noOfWinners = {};
            $( rows ).each( function() {
                var timeSelect = $( this ).find( '.timePeriod' );
                var maxNoms = $( timeSelect ).find( ':selected' ).data( 'maxnomine' );
                var winnerNo = $( timeSelect ).find( ':selected' ).data( 'noofwinner' );
                var selectedTime = $( timeSelect ).val();
                if( selectedTime ) {
                    selectedTime.toString();
                }
                selectedTime = 's' + selectedTime;
                if( $( this ).find( '.statusSelect' ).val() === 'winner' ) {

                    if ( typeof winners[ selectedTime ] === 'undefined' ) {
                        winners[ selectedTime ] = 0;
                    }
                    winners[ selectedTime ] += 1;
                    maxNominees[ selectedTime ] = maxNoms;
                    noOfWinners[ selectedTime ] = winnerNo;
                }
            } );
            $.each( winners, function( key, value ) {
                if( ( maxNominees[ key ] ) < ( value + noOfWinners[ key ] ) ) {
                    //do your validation stuff here.
                    var selVal = key.substring( 1 );
                    $( rows ).each( function() {
                      var timeSelect = $( this ).find( '.timePeriod' ),
                      container = $( this ).find( '.timePeriod' ).parent();
                      if( $( timeSelect ).val() === selVal ) {
                        container.data( 'overlimit', true );
                      }
                    } );
                }else {
                  var selVal = key.substring( 1 );
                  $( rows ).each( function() {
                    var timeSelect = $( this ).find( '.timePeriod' ),
                    container = $( this ).find( '.timePeriod' ).parent();
                    if( $( timeSelect ).val() === selVal ) {
                      container.data( 'overlimit', false );
                    }
                  } );
                }
            } );
    },

    changeStatus: function( e ) {
        var that = this,
            $tar = $( e.target ),
            $statusModal,
            row,
            $status = $tar.find( 'option:selected' ).val();
            $tar.addClass( 'changed' );

            if ( that.statusCount >= 100 ) {
              $statusModal = $( 'body' ).find( '#statusModal' );
              e.preventDefault();
              $statusModal.modal();
            } else {
              that.statusCount++;
              console.log( that.statusCount );

              switch ( $status ) {
                  case 'winner':
                      $tar.siblings( '.attachMessageCont' ).hide();
                      $tar.siblings( '.winnerMessage' ).show();

                      $tar.parents( 'tr' ).find( '#winnerMessage' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( '.timePeriod, button, .date, .levelSelect' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( 'input:not(.teamAwardAmount, .optOut)' ).removeAttr( 'readonly' );
                      $tar.parents( 'tr' ).find( 'input:not(.teamAwardAmount, .optOut)' ).removeAttr( 'disabled' );
                      // Disabling the date input if it already has a value in it and adding the value from input attr to actual val
                      if( $tar.parents( 'tr' ).find( '.notificationDate .date' ).attr( 'value' ) !== '' ) {
                        $tar.parents( 'tr' ).find( '.notificationDate .date, .notificationDate .awardDateIcon' ).attr( { 'readonly': true, 'disabled': true } );
                        $tar.parents( 'tr' ).find( '.notificationDate .date' ).val( $tar.parents( 'tr' ).find( '.notificationDate .date' ).attr( 'value' ) );
                      }
                      $tar.parents( 'tr' ).find( 'textarea' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( '.teamAddAward' ).removeClass( 'disabled' );
                      $tar.parents( 'tr' ).find( '.awardPointsInp' ).parents( 'td:not(.optout)' ).addClass( 'validateme' );
                      if ( $tar.parents( 'tr' ).find( '.awardPointInp' ).length === 0 && that.model.get( 'tabularData' ).awardType === 'calculated' ) {
                          $tar.parents( 'tr' ).find( '.calcLink' ).parents( 'td' ).addClass( 'validateme' );
                          row = $tar.parents( 'tr' ).data( 'row-id' );
                          $tar.parents( 'tr' ).find( '.calcLink' ).parents( 'td' ).append( "<input id='validateForCalc" + row + "' class='validateForCalc' type='text'>" );
                      }

                      $tar.parents( 'tr' ).find( '.timePeriod' ).parents( 'td' ).addClass( 'validateme' );
                      $tar.parents( 'tr' ).find( '.levelSelect' ).parents( 'td' ).addClass( 'validateme' );

                      this.calcMaxNominees( e );
                    break;
                  case 'approv':
                      $tar.siblings( '.attachMessageCont' ).hide();

                      $tar.parents( 'tr' ).find( '#winnerMessage' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( '.timePeriod, button, .levelSelect' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( 'input' ).removeAttr( 'readonly' );
                      $tar.parents( 'tr' ).find( 'input:not(.teamAwardAmount)' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( '.teamAddAward' ).removeClass( 'validateme' );
                      $tar.parents( 'tr' ).find( '.levelSelect' ).removeClass( 'validateme' );

                      break;
                  case 'non_winner':
                      $tar.siblings( '.attachMessageCont' ).hide();
                      $tar.siblings( '.nonwinnerMessage' ).show();

                      $tar.parents( 'tr' ).find( '#deinalReason' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( '.timePeriod, button, .levelSelect' ).attr( 'disabled', 'disabled' );
                      $tar.parents( 'tr' ).find( 'input' ).attr( 'readonly', 'readonly' );
                      $tar.parents( 'tr' ).find( 'input:not(.teamAwardAmount)' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( 'input.compare' ).attr( 'readonly', false );
                      // $tar.parents('tr').find('.awardInput, input.date, .timePeriod').val('');
                      $tar.parents( 'tr' ).find( 'input.date' ).val( '' );
                      $tar.parents( 'tr' ).find( '.teamAddAward' ).addClass( 'disabled' );
                      $tar.parents( 'tr' ).find( '.awardPointsInp' ).parents( 'td' ).removeClass( 'validateme' );
		      		  $tar.parents( 'tr' ).find( '.timePeriod' ).parents( 'td' ).removeClass( 'validateme' );
                      $tar.parents( 'tr' ).find( '.levelSelect' ).parents( 'td' ).removeClass( 'validateme' );

                      break;
                  case 'more_info':
                      $tar.siblings( '.attachMessageCont' ).hide();
                      $tar.siblings( '.moreinfoMessage' ).show();

                      $tar.parents( 'tr' ).find( '#moreinfoMessage' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( '.timePeriod, button, .levelSelect' ).attr( 'disabled', 'disabled' );
                      $tar.parents( 'tr' ).find( 'input' ).attr( 'readonly', 'readonly' );
                      $tar.parents( 'tr' ).find( 'input:not(.teamAwardAmount)' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( 'input.compare' ).attr( 'readonly', false );
                      // $tar.parents('tr').find('.awardInput, input.date, .timePeriod').val('');
                      $tar.parents( 'tr' ).find( 'input.date' ).val( '' );
                      $tar.parents( 'tr' ).find( '.teamAddAward' ).addClass( 'disabled' );
                      $tar.parents( 'tr' ).find( '.awardPointsInp' ).parents( 'td' ).removeClass( 'validateme' );
		      		  $tar.parents( 'tr' ).find( '.timePeriod' ).parents( 'td' ).removeClass( 'validateme' );
                      $tar.parents( 'tr' ).find( '.levelSelect' ).parents( 'td' ).removeClass( 'validateme' );

                      break;

                  default:
                      $tar.siblings( '.attachMessageCont' ).hide();

                      $tar.parents( 'tr' ).find( '.timePeriod, button' ).attr( 'disabled', 'disabled' );
                      $tar.parents( 'tr' ).find( '.timePeriod, button, .levelSelect' ).attr( 'disabled', 'disabled' );
                      $tar.parents( 'tr' ).find( 'input' ).attr( 'readonly', 'readonly' );
                      $tar.parents( 'tr' ).find( 'input.compare' ).attr( 'readonly', false );
                      $tar.parents( 'tr' ).find( 'input.date' ).val( '' );
                      $tar.parents( 'tr' ).find( '.teamAddAward' ).addClass( 'disabled' );
                      $tar.parents( 'tr' ).find( '.levelSelect' ).parents( 'td' ).removeClass( 'validateme' );
                      $tar.parents( 'tr' ).find( '.awardPointsInp' ).parents( 'td' ).removeClass( 'validateme' );
                      $tar.parents( 'tr' ).find( '.timePeriod' ).parents( 'td' ).removeClass( 'validateme' );
                      $tar.parents( 'tr' ).find( '.awardPointsInp' ).parents( 'td' ).removeClass( 'error' );
                      $tar.parents( 'tr' ).find( '.timePeriod' ).parents( 'td' ).removeClass( 'error' );
                      $tar.parents( 'tr' ).find( '.qtip' ).hide();
                      $tar.parents( 'tr' ).find( '#moreInfoMessage' ).attr( 'disabled', 'disabled' );
                      $tar.parents( 'tr' ).find( '#deinalReason' ).attr( 'disabled', 'disabled' );
                      $tar.parents( 'tr' ).find( '#winnerMessage' ).attr( 'disabled', 'disabled' );
                      this.calcMaxNominees( e );
              }
          }
    },

    filterStatusSelect: function( e ) {
        var $tar = $( e.target ),
            promo = this.model.get( 'promotion' ),
            $statusSelect = this.$el.find( '#statusFilter' ),
            $defaultTimePeriod = $tar.find( 'option:selected' ).hasClass( 'defaultOption' );

        if ( !$defaultTimePeriod ) {
            if ( promo.finalLevelApprover || promo.payoutAtEachLevel ) {
                $statusSelect.find( 'option:not([value="winner"])' ).attr( { 'disabled': true, 'selected': false } );

                $statusSelect.find( 'option[value="winner"]' ).attr( 'selected', 'selected' );
            } else {
                 $statusSelect.find( 'option:not([value="approv"])' ).attr( { 'disabled': true, 'selected': false } );

                $statusSelect.find( 'option[value="approv"]' ).attr( 'selected', 'selected' );
            }
        } else {
            $statusSelect.find( 'option' ).attr( 'disabled', false );
        }
    },

    // Custom for coco cola wip#58122 starts
    setLevelData: function() {                
        var levelInputs = $( '.levelInputs' ),            
            capperpax = $( '.capPerPax' ).val(),
            levelData = {
                levels:{},
                capperpax: 0
            };

        _.each( levelInputs, function( level, index ) {
            var levelvalue = $(level).val();
            levelData.levels[ 'level' + ( index + 1 ) ] = levelvalue ;
        } );

        levelData[ 'capperpax' ] = capperpax;
        this.levelData = levelData;
    },

    updateAward: function(e) {
        var that = this,            
            selectBoxes = $( '.levelSelect' ),
            maxCapPerPax = that.levelData.capperpax,
            nonEmptySelectBoxes;

        nonEmptySelectBoxes = _.filter( selectBoxes, function( selectBox ) {
            return $(selectBox).val() !== "";
        });

        var groupedSelectBoxes = _.groupBy( nonEmptySelectBoxes, function( selectBox ) {
            return $(selectBox).val();
        });

        _.each( groupedSelectBoxes, function( selectBox ) {
            _.each( selectBox, function( selectEach ) {
                var levelAwardValue = that.levelData.levels[ $(selectEach).val() ],
                    teamSizeCount = $( selectEach ).parents( 'tr' ).find( '.teamSizeValue' ).data( 'teamSize' ),
                    updateValue = ( levelAwardValue / teamSizeCount ),
                    cappedUpdateValue = Math.floor( Math.min( updateValue, maxCapPerPax ) );                    
                    $( selectEach ).parents('tr').find('.awardInput').val( cappedUpdateValue );
            } );
        } );
    },
    // Custom for coco cola wip#58122 ends

    setStateLoading: function( mode ) {
        var spinProps = {};

        if ( mode ) {
            spinProps.classes = mode;
        }

        G5.util.showSpin( this.$el, spinProps );
    },

    // *******************
    // TABLE EVENTS
    // *******************
    sortModal: function( e ) {
        var $tar = $( e.target ).closest( '.sortable' );

            this.$el.find( '#sortModal' ).modal( 'show' );

            $tar.addClass( 'sortIt' );

    },
    handleTableSort: function() {
        var $tar = this.$el.find( '.sortIt' ),
            sortOn = $tar.data( 'sortOn' ),
            sortBy = $tar.data( 'sortBy' ),
            $status = this.$el.find( '#statusFilter option:selected' ).val();

            console.log( 'sort!' );

        $tar.removeClass( 'sortIt' );
        G5.util.showSpin( this.$el, {
            cover: true
        } );

        this.model.loadApprovalTable( {
            promotionId: this.promotionId,
            nodeId: this.nodeId,
            levelId: this.levelId,
            statusFilter: $status,
            currentPage: this.model.get( 'tabularData' ).currentPage,
            sortedOn: sortOn,
            sortedBy: sortBy,
            sorting: true
        } );
    },

    getCumulativeDetails: function( e ) {
        var $form = this.$el.find( '#approvalsSearchForm' ),
            $tar = $( e.currentTarget ),
            formData = $form.serializeArray(),
            data,
            paxId = $tar.parents( 'td' ).data( 'paxid' ),
            groupClaimId = $tar.parents( 'td' ).data( 'groupclaimid' ),
            arrData = {};

        e.preventDefault();
        groupClaimId = parseInt( groupClaimId );
        _.each( formData, function( el ) {
            var value = _.values( el );

            if ( value[ 0 ] === 'statusFilter' ) {

                if ( !arrData.hasOwnProperty( value[ 0 ] ) ) {
                    arrData[ value[ 0 ] ] = value[ 1 ];

                } else {
                	   arrData[ value[ 0 ] ] += ',' + value[ 1 ];
                }
            } else {
                arrData[ value[ 0 ] ] = value[ 1 ];
            }
        } );

        data = {
            promotionId: this.promotionId,
            nodeId: this.nodeId,
            levelId: this.levelId,
            paxId: paxId,
            claimGroupId: groupClaimId
        };

        this.model.loadCumulativeInfo( $.extend( {}, data, arrData ) );
    },

    expandCollapseDetails: function( e ) {
        var $tar = $( e.currentTarget ),
            $detailRow = $tar.parents( 'tr' ).next( '.detailRow' );

        $detailRow.toggle();

        $tar.addClass( 'hide' );
        $tar.siblings( 'i' ).removeClass( 'hide' );

    },

    expandCollapseAll: function( e ) {
        var $detailRows = this.$el.find( '.detailRow' ),
            isCollapsed = $( e.target ).hasClass( 'collapsed' ),
            $expIcon = this.$el.find( '.expandIcon' ),
            $collIcon = this.$el.find( '.collapseIcon' );

        if ( isCollapsed === true ) {
            $detailRows.each( function() {
                if ( $( this ).is( ':visible' ) === false ) {
                    $( this ).toggle();
                }
            } );

            $( e.target ).removeClass( 'collapsed' );
            $expIcon.addClass( 'hide' );
            $collIcon.removeClass( 'hide' );

        } else {
            $detailRows.each( function() {
                if ( $( this ).is( ':visible' ) === true ) {
                    $( this ).toggle();
                }
            } );

            $( e.target ).addClass( 'collapsed' );
            $collIcon.addClass( 'hide' );
            $expIcon.removeClass( 'hide' );
        }

        this.$el.find( '.cycle' ).parents( '.detailCarousel.active' ).css( 'height', this.$el.find( '.cycle .item' ).first().height() );
    },

    viewMoreTableRows: function() {
        var isMore = true,
        allData,
        $form,
        formData,
        data,
        status,
        arrData = {},
        tabularData = this.model.get( 'tabularData' );
        if ( this.model.get( 'allData' ) ) {
          allData = this.model.get( 'allData' );
          _.each( tabularData.results, function( data ) {
            allData.results.push( data );
          } );
          this.model.set( 'allData', allData );
        } else {
          this.model.set( 'allData', tabularData );
        }
        $form = this.$el.find( '#approvalsSearchForm' );
        formData = $form.serializeArray();


        console.log( formData );
        this.$el.find( '.compareNomineeBlock' ).remove();

        _.each( formData, function( el ) {
            var value = _.values( el );
            if ( value[ 0 ] === 'statusFilter' ) {

                if ( !arrData.hasOwnProperty( value[ 0 ] ) ) {
                    arrData[ value[ 0 ] ] = [];
                    arrData[ value[ 0 ] ].push( value[ 1 ] );

                } else {

                    arrData[ value[ 0 ] ].push( value[ 1 ] );
                }
            } else {
                arrData[ value[ 0 ] ] = value[ 1 ];
            }
        } );
        status = arrData[ 'statusFilter' ];
        arrData[ 'statusFilter' ] = '';

        _.each( status, function( value, index ) {

            if ( index < status.length - 1 ) {
                arrData[ 'statusFilter' ] += value + ',';

            } else if ( index === status.length - 1 ) {
                arrData[ 'statusFilter' ] += value;
            }
        } );

        data = {
            promotionId: this.promotionId,
            nodeId: this.nodeId,
            sortedOn: this.model.get( 'tabularData' ).sortedOn,
            sortedBy: this.model.get( 'tabularData' ).sortedBy,
            currentPage: this.model.get( 'tabularData' ).currentPage,
            isMore: isMore,
            levelId: this.levelId,
            statusFilter: this.statusSel

        };
        this.model.loadApprovalTable( $.extend( {}, data, arrData ) );

    },

    doPointsFocus: function( e ) {
        var $tar = $( e.currentTarget );

        if ( $tar.val() === '0' || $tar.val() === 0 ) {
            setTimeout( function() {

                if ( $tar[ 0 ].setSelectionRange ) {
                    $tar[ 0 ].setSelectionRange( 0, 4000 );
                } else {
                    $tar.select();
                }
            }, 0 );
        }
    },

    doPointsKeyup: function( e ) {
        var $tar = $( e.target );

        this.setAwardPointsFor( $tar, true );
    },

    // sync model to input element and validate
    setAwardPointsFor: function( $el ) {
        // validate range
        this.validateRangeOf( $el );
    },

    doPointsKeydown: function( e ) {
        var $tar = $( e.target ),
            $nxtInp = $tar.closest( '.paxRow' )
                .nextAll( '.paxRow:first' ).find( '.awardPointsInp' ),
                that = this;

        // filter non-numerics
        console.log( that );
        if ( that.promoModel.payoutType === 'cash' ) {
            this.filterNonNumericKeydown( e );
        } else {
            this.filterNonNumericKeydownNoCash( e );
        }

        // if enter||tab press, focus on next input
        if ( e.which === 13 || e.which === 9 ) {
            e.preventDefault(); // stop it
            $nxtInp.length ? $nxtInp.focus().select() : false; // move to next inp
        }
    },

    // helper, filter non-numeric keydown
    filterNonNumericKeydown: function( event ) {
        // http://stackoverflow.com/questions/995183/how-to-allow-only-numeric-0-9-in-html-inputbox-using-jquery
        // Allow: backspace, delete, tab, escape, and enter
        if ( event.keyCode === 46 || event.keyCode === 8 || event.keyCode === 9 || event.keyCode === 27 || event.keyCode === 13 || event.keyCode === 110 || event.keyCode === 190 ||
             // Allow: Ctrl+A
            ( event.keyCode === 65 && event.ctrlKey === true ) ||
             // Allow: home, end, left, right
            ( event.keyCode >= 35 && event.keyCode <= 39 ) ) {
                 // let it happen, don't do anything
                 return;
        } else {
            // Ensure that it is a number and stop the keypress
            if ( event.shiftKey || ( event.keyCode < 48 || event.keyCode > 57 ) && ( event.keyCode < 96 || event.keyCode > 105 ) ) {
                event.preventDefault();
            }
        }
    },
    filterNonNumericKeydownNoCash: function( event ) {
        // http://stackoverflow.com/questions/995183/how-to-allow-only-numeric-0-9-in-html-inputbox-using-jquery
        // Allow: backspace, delete, tab, escape, and enter
        if ( event.keyCode === 46 || event.keyCode === 8 || event.keyCode === 9 || event.keyCode === 27 || event.keyCode === 13 ||
             // Allow: Ctrl+A
            ( event.keyCode === 65 && event.ctrlKey === true ) ||
             // Allow: home, end, left, right
            ( event.keyCode >= 35 && event.keyCode <= 39 ) ) {
                 // let it happen, don't do anything
                 return;
        } else {
            // Ensure that it is a number and stop the keypress
            if ( event.shiftKey || ( event.keyCode < 48 || event.keyCode > 57 ) && ( event.keyCode < 96 || event.keyCode > 105 ) ) {
                event.preventDefault();
            }
        }
    },
    setCalcObj: function( calcObj ) {
        this.calcObj = calcObj;
    },

    doViewRecognitionCalculator: function( event ) {
        var that = this,
            tplName = 'nominationsCalcTemplate',
            theWeightLabel,
            theScoreLabel,
            thisQtip,
            $anchor = $( event.target );


        var getCriteriaElements = function() {
            var theCriteriaElement = '',
                thisCriteriaRow,
                thisRowLabel,
                thisRowId,
                theCriteriaWrapper,
                thisRatingSelectInner,
                thisRatingSelectOutter,
                thisRatingOption,
                i = 0,
                j = 0;

            for ( i = 0; i < that.calcObj.criteria.length; i++ ) {
                thisCriteriaRow = that.calcObj.criteria[ i ];
                thisRowLabel = thisCriteriaRow.label;
                thisRowId = thisCriteriaRow.id;
                theCriteriaWrapper = "<div class='nominationsCalcCriteriaWrapper' data-criteria-id='" + thisRowId + "'>";
                thisRatingSelectInner = [];
                // filling the text of first option later from a TPL element (should do the whole select element in handlebars, but this is calc and its not worth the effort ATM)
                thisRatingSelectOutter = "<select class='nominationsCalcRatingSelect'><option value='undefined' class='msgSelRatingTarget'>[dynamic js]</option>";

                for ( j = 0; j < thisCriteriaRow.ratings.length; j++ ) {
                    thisRatingOption = thisCriteriaRow.ratings[ j ];

                    thisRatingSelectInner.push( "<option value='" + thisRatingOption.value + "' data-ratingId='" + thisRatingOption.id + "'>" + thisRatingOption.label + '</option>' );
                }

                //close up the select element
                thisRatingSelectOutter += thisRatingSelectInner + '</select>';

                theCriteriaElement += theCriteriaWrapper + '<label>' + thisRowLabel + '</label>' + thisRatingSelectOutter + "<span id='criteriaWeightElm' class='criteriaWeightElm'></span><span id='criteriaScoreElm' class='criteriaScoreElm'></span></div>";
            }

            return theCriteriaElement;
        };

        var selectsAllReady = function( $elms ) {
            var isReady = true;

            $elms.each( function() {
                var $this = $( this );

                if ( $this.val() === 'undefined' || $this.val() === undefined ) {
                    isReady = false;
                }
            } );

            return isReady;
        };

        var bindTheSelects = function( theQtip ) {
            var $theSelects = theQtip.find( '.nominationsCalcRatingSelect' );

            $theSelects.change( function() {
                if ( selectsAllReady( $theSelects ) ) {
                    that.doSendCalculatorRatings( $theSelects, theQtip );
                }
            } );
        };

        var bindCloseButton = function( theQtip ) {
            var $theCloseBtn = theQtip.find( '#nominationCalcCloseBtn' );

            $theCloseBtn.click( function() {
               theQtip.qtip( 'hide' );

            } );
        };

        var bindPayoutLink = function( $this ) {
            $this.find( '#nominationsCalcPayoutTableLink' ).off().click( function( event ) {
                event.preventDefault ? event.preventDefault() : event.returnValue = false;
                that.showRecogCalcPayout( this );
            } );
        };
        var renderedTemplate = '';

        if ( this.calcObj.attributes.hasWeight ) {
            theWeightLabel = this.calcObj.attributes.weightLabel;
        } else {
            theWeightLabel = '';
        }

        if ( this.calcObj.attributes.hasScore ) {
            theScoreLabel = this.calcObj.attributes.scoreLabel;
        } else {
            theScoreLabel = '';
        }

        $anchor.qtip( {
            content: {
                text: ''
            },
            position: {
                my: 'top center',
                at: 'bottom center',
                adjust: {
                    // x: getQtipOffset(),
                    // y: 0
                    method: 'shift none' // shift x-axis inside viewport
                },
                container: $( 'body' ),
                //Used to fix qtip position in in ie7&8.
                effect: false,
                viewport: $( 'body' )// $('body'), // shift will try to keep tip inside this
            },
            show: {
                ready: false,
                event: false
            },
            hide: {
                event: false,
                fixed: true,
                delay: 200
            },
            style: {
                padding: 0,
                classes: 'ui-tooltip-shadow ui-tooltip-light nominationsCalcTipWrapper',
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            },
            events: {
                show: function() {
                    var $this = $( this );
                    bindTheSelects( $this );
                    bindCloseButton( $this );
                    thisQtip = $this;
                    $( '.nominationsCalcTipWrapper' ).not( thisQtip ).hide(); //hide any other open calculators
                    $( '.nominationsCalcPayoutTableLink' ).not( $this.find( '#nominationsCalcPayoutTableLink' ) ).qtip( 'hide' );
                    that.trigger( 'nominationsCalcShowing' );
                },
                visible: function() {
                    var $this = $( this ),
                        scrollOffset = -( $( window ).height() / 2 ) + ( $this.height() / 2 );
                    bindPayoutLink( $this );
                    $this.find( '#nominationsCalcPayoutTableLink' ).click(); //open the payout link
                    $.scrollTo( $this, 500, { offset: { top: scrollOffset, left: 0 } } );
                },
                hide: function() {
                    $( this ).find( '#nominationsCalcPayoutTableLink' ).qtip( 'hide' );
                }
            }
        } );

        event.preventDefault ? event.preventDefault() : event.returnValue = false;

        TemplateManager.get( tplName, function( tpl ) {
                var data = {
                    weightLabel: theWeightLabel,
                    scoreLabel: theScoreLabel,
                    criteriaDiv: getCriteriaElements(),
                    hasWeight: that.calcObj.attributes.hasWeight,
                    hasScore: that.calcObj.attributes.hasScore,
                    showPayTable: that.calcObj.attributes.showPayTable
                };
                renderedTemplate = $( tpl( data ) );
                renderedTemplate.find( '.msgSelRatingTarget' ).html( renderedTemplate.find( '.msgSelectRatingInstruction' ).html() );

                $anchor.qtip( 'option', 'content.text', renderedTemplate );
                $anchor.qtip( 'show' );
                that.doRenderPreviousCalcInfo( $anchor.closest( 'tr' ), thisQtip );
            }, this.tplPath );
    },

    doSendCalculatorRatings: function( $theSelects, theQtip ) {
        var that = this,
        dataSent;

        var getCriteriaInfo = function() {
            var theObj = {};

            $theSelects.each( function( i ) {
                var $this = $( this );
                var thisRatingValue = parseInt( $this.val() );
                var thisCriteriaId = parseInt( $this.parent().attr( 'data-criteria-id' ) );

                theObj[ 'criteria[' + i + '].criteriaRating' ] = thisRatingValue;
                theObj[ 'criteria[' + i + '].criteriaId' ] = thisCriteriaId;

            } );

            return theObj;
        };

        var theData = {
            promotionId: this.promotionId,
            nodeId: this.nodeId,
            levelId: this.levelId,
            participantId: $( $( '.nominationsCalcTipWrapper' ).qtip( 'api' ).options.position.target ).closest( 'tr' ).attr( 'data-participant-id' )
        };

        theData = $.extend( {}, theData, getCriteriaInfo() );

        dataSent = $.ajax( {
                type: 'POST',
                dataType: 'g5json',
                url: G5.props.URL_JSON_NOMINATIONS_CALCULATOR_SEND_INFO,
                 data: theData,
                 beforeSend: function() {
                    console.log( '[INFO] NominationCollectionView: doSendCalculatorRatings ajax post starting. Sending this: ', theData );
                 },
                success: function( serverResp ) {
                    console.log( '[INFO] NominationCollectionView: doSendCalculatorRatings ajax post sucess: ', serverResp );
                    that.doUpdateRecogCalcValues( serverResp.data, theQtip, false );
                }
            } );

            dataSent.fail( function( jqXHR, textStatus ) {
                console.log( '[INFO] NominationCollectionView: doSendCalculatorRatings ajax post failed: ' + textStatus, jqXHR );
            } );
    },

    doUpdateRecogCalcValues: function( data, theQtip, hasPreviousData ) {
        var that = this,
            $theCalcElm = theQtip.find( '#nominationsCalcInnerWrapper' ),
            scorElmTplName = 'nominationsCalcScoreWrapperTpl',
            scorElmTplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/',
            $theCalc = theQtip.find( '#nominationsCalcScoreWrapper' ),
            rangedElm = '',
            theFixedAmount = '',
            theAwardLevel,
            savedData = data,
            i,

            hasRangedAward = function() {
                var hasRanged = false;

                if ( that.calcObj.attributes.awardType === 'range' ) {
                    hasRanged = true;
                    rangedElm = '<span>(' + data.awardRange.min + ' - ' + data.awardRange.max + ")</span><input type='text' data-range-max='" + data.awardRange.max + "' data-range-min='" + data.awardRange.min + "'>";
                }

                return hasRanged;
            },

            hasFixedAward = function() {
                var hasFixed = false;

                if ( that.calcObj.attributes.awardType === 'fixed' ) {
                    theFixedAmount = data.fixedAward;
                    hasFixed = true;
                }

                return hasFixed;
            },

            hasAwardLevel = function() {
                var hasAwardLevel = false;

                if ( that.calcObj.attributes.awardType === 'merchlevel' ) {
                    theAwardLevel = data.awardLevel.name + ' (' + data.awardLevel.value + ')' + "<input type='hidden' id='calcAwardLevelId' value='" + data.awardLevel.value + "'>";
                    hasAwardLevel = true;
                }
                console.log( theAwardLevel );
                console.log( hasAwardLevel );
                return hasAwardLevel;
            },

            isInt = function( value ) {
                if ( ( parseFloat( value ) === parseInt( value ) ) && !isNaN( value ) ) {
                    return true;
                } else {
                    return false;
                }
            },

            bindAwardInput = function() {
                var $theInput = theQtip.find( 'input' ),
                    $saveButton = theQtip.find( '#nominationsCalcScoreWrapper' ).find( 'button' ),
                    $rangeText = $theInput.closest( 'p' ),
                    maxAward = parseInt( $theInput.attr( 'data-range-max' ) ),
                    minAward = parseInt( $theInput.attr( 'data-range-min' ) );

                if ( hasRangedAward() ) {
                    $theInput.keyup( function() {
                        if ( $theInput.val() > maxAward || $theInput.val() < minAward || !isInt( $theInput.val() ) ) {
                            $theInput.addClass( 'input-error' );
                            $saveButton.attr( 'disabled', 'disabled' );
                            $rangeText.addClass( 'range-error' );
                        } else if ( $theInput.hasClass( 'input-error' ) ) {
                            $theInput.removeClass( 'input-error' );
                            $saveButton.removeAttr( 'disabled' );
                            $rangeText.removeClass( 'range-error' );
                        } else if ( $saveButton.attr( 'disabled' ) === 'disabled' ) {
                            $saveButton.removeAttr( 'disabled' );
                        }
                    } );
                } else {
                    $saveButton.removeAttr( 'disabled' );
                }
            },

            renderPreviousRatingSeletions = function() {
                var thisCriteria,
                i = 0;
                for ( i = 0; i < data.criteria.length; i++ ) {
                    thisCriteria = data.criteria[ i ];

                    $( ".nominationsCalcCriteriaWrapper[data-criteria-id='" + thisCriteria.criteriaId + "']" ).find( 'select' ).val( thisCriteria.criteriaRating );
                }
            },

            allCriteriaSelected = function() {
                var allSelected = true,
                    $criteriaSelects = theQtip.find( 'select' );

                $criteriaSelects.each( function() {
                    var $theSelect = $( this );

                    if ( $theSelect.val() === 'undefined' ) {
                        allSelected = false;
                        $theSelect.addClass( 'input-error' );
                    } else {
                        $theSelect.removeClass( 'input-error' );
                    }
                } );

                return allSelected;
            },

            bindSaveButton = function() {
                var $theButton = theQtip.find( '#nominationsCalcScoreWrapper' ).find( 'button' );

                $theButton.off();

                $theButton.click( function() {
                    if ( allCriteriaSelected() ) {
                        that.doSaveCalculatedAward( savedData, theQtip );
                    }
                } );
            },

            theData = {
                isRange: hasRangedAward(),
                awardRange: rangedElm,
                totalScore: data.totalScore,
                hasFixed: hasFixedAward(),
                hasAwardLevel: hasAwardLevel(),
                awardLevel: theAwardLevel,
                fixedAmount: theFixedAmount
            },
            theCriteria,
            $theRow,
            startTxt,
            selectAmtText;

        for ( i = data.criteria.length - 1; i >= 0; i-- ) {
            theCriteria = data.criteria[ i ];
            $theRow = $theCalcElm.find( "div[data-criteria-id='" + theCriteria.criteriaId + "']" );

            if ( that.calcObj.attributes.hasWeight ) {
                $theRow.find( '#criteriaWeightElm' ).html( theCriteria.criteriaWeight );
            }

            if ( that.calcObj.attributes.hasScore ) {
                $theRow.find( '#criteriaScoreElm' ).html( theCriteria.criteriaScore );
            }
        }

        //check to see if score template is there. If not, make it and populate it. If it is, just populate.
        if ( !$theCalc.hasClass( 'nominationsCalcScoreWrapper' ) ) {
            TemplateManager.get( scorElmTplName, function( tpl ) {
                theQtip.find( '#nominationsCalcWrapper' ).append( tpl( theData ) );
                bindAwardInput();
                bindSaveButton();

                if ( hasPreviousData ) {
                    renderPreviousRatingSeletions();
                }
            }, scorElmTplUrl );
        } else {
            if ( theData.isRange ) {
                startTxt = $theCalc.find( 'p' ).text();
                selectAmtText = startTxt.substring( 0, startTxt.indexOf( '(' ) );
                $theCalc.find( 'p' ).html( selectAmtText + ' ' + theData.awardRange ); //update the range award
                bindAwardInput();
                bindSaveButton();
            } else if ( theData.hasFixed ) {
                startTxt = $theCalc.find( 'p' ).text();
                selectAmtText = startTxt.substring( 0, startTxt.indexOf( ':' ) );
                bindSaveButton();
                $theCalc.find( 'p' ).html( selectAmtText + ': ' + theData.fixedAmount ); //update the fixed award
            } else {
                //must be merch level
                startTxt = $theCalc.find( 'p' ).text();
                selectAmtText = startTxt.substring( 0, startTxt.indexOf( ':' ) );
                bindSaveButton();
                $theCalc.find( 'p' ).html( selectAmtText + ': ' + data.awardLevel.name + ' (' + data.awardLevel.value + ')' ); //update the fixed award
            }

            $theCalc.find( '#nominationsCalcTotal' ).html( data.totalScore ); //update total score

        }

    },

    doSaveCalculatedAward: function( data, theQtip ) {
        var theCalcAwardObj = data,
            $anchor = $( theQtip.qtip( 'api' ).options.position.target ),
            $thisRow = $anchor.closest( 'tr' ),
            paxIndex,
            memberCount,
            $rowSiblings = $thisRow.siblings( 'tr.participant-item' );
            console.log( this.calcObj.attributes );
            console.log( theQtip.find( '#calcAwardLevelId' ).val() );
            console.log( theQtip );
        if ( this.calcObj.attributes.awardType === 'range' ) {
            theCalcAwardObj.awardedAmount = theQtip.find( 'input' ).val();
        } else if ( this.calcObj.attributes.awardType === 'fixed' ) {
            theCalcAwardObj.awardedAmount = data.fixedAward;
        } else {
            theCalcAwardObj.awardedAmount = theQtip.find( '#calcAwardLevelId' ).val();
        }

        this.savedCalcObj = theCalcAwardObj;

        console.log( '[INFO] NominationCollectionView: doSaveCalculatedAward: saved this calcInfo: ', this.savedCalcObj );
        paxIndex = $anchor.parents( 'tr' ).data( 'index' );
        //if the calc obj is not a merchlevel award, do the normal save
        if ( this.calcObj.attributes.awardType !== 'merchlevel' ) {
            $anchor.siblings( 'input' ).val( theCalcAwardObj.awardedAmount );
            $anchor.text( theCalcAwardObj.awardedAmount );
        } else {
            if ( this.model.get( 'tabularData' ).results[ paxIndex ].isTeam === true ) {
                console.log( 'true' );
                memberCount = this.model.get( 'tabularData' ).results[ paxIndex ].teamMembers.length;

                $anchor.siblings( "[name*='awardLevel']" ).val( theCalcAwardObj.awardedAmount * memberCount );
                $anchor.text( theCalcAwardObj.awardLevel.name + ' (' + theCalcAwardObj.awardLevel.value * memberCount + ')' );
            } else {
                $anchor.siblings( "[name*='awardLevel']" ).val( theCalcAwardObj.awardedAmount );
                $anchor.text( theCalcAwardObj.awardLevel.name + ' (' + theCalcAwardObj.awardLevel.value + ')' );
            }
        }

        //not a shared data-set. Add it's personal row info to global var
        $thisRow.attr( 'data-shared-calc-info', 'false' );
        this.savedCalcRows[ $thisRow.attr( 'data-participant-cid' ) ] = data;

        this.setAwardPointsForCalc( $anchor, theCalcAwardObj );

        theQtip.qtip( 'hide' );

        if ( $rowSiblings.length > 0 ) {
            this.doShowSameForAllCalc( $anchor, theCalcAwardObj );
        }

    },

    setAwardPointsForCalc: function( $el, calcData ) {
        var pax = $el,
            $paxPar = $el.parent(),
            paxIndex = $paxPar.parents( 'tr' ).data( 'index' ),
            $thisRowInputs,
            theData,
            teamAward;

        theData = this.model.get( 'tabularData' ).results[ paxIndex ];
        //clear our previous hidden inputs
        pax.siblings( "[name*='award']" ).remove();

        $paxPar.find( 'input.validateForCalc' ).remove();
        $paxPar.find( '.qtip' ).hide();
        if ( theData.isTeam === true ) {
            teamAward = ( theData.teamMembers.length * calcData.awardedAmount );
            $paxPar.append( '<input type="hidden" name="tabularData.results[' + paxIndex + '].award" value="' + teamAward + '" class="teamAwardCalc">' );
            $thisRowInputs = $( '.teamAwardCalc' );
            _.each( theData.teamMembers, function( member, index ) {
                console.log( member );
                $( $thisRowInputs ).parent().append( '<input type="hidden" name="tabularData.results[' + paxIndex + '].team[' + index + '].paxId" value="' + member.paxId + '" class="hiddenTeamInp" /> <input type="hidden" name="tabularData.results[' + paxIndex + '].team[' + index + '].value" value="' + calcData.awardedAmount + '" class="hiddenTeamInp" />' );
            } );
        } else {
            $paxPar.append( "<input type='hidden' name='tabularData.results[" + paxIndex + "].award' value='" + calcData.awardedAmount + " '>" );
        }
    },

    doShowSameForAllCalc: function( $anchor, theCalcAwardObj ) {
        var that = this;
        $anchor.qtip( {
            content: this.$wrapper.find( '#sameForAllTipTpl' ).clone().removeAttr( 'id' ),
            position: {
                my: 'left center', at: 'right center', adjust: { x: 5, y: 0 }, container: this.$wrapper, effect: false, viewport: false
            },
            show: { ready: true, event: false },
            hide: 'unfocus',
            //only show the qtip once
            events: {
                hide: function() { $anchor.qtip( 'destroy' ); },
                show: function() {
                    var $theDoSameLink = $( '.sameForAllTip' );
                    $theDoSameLink.click( function( event ) {
                        event.preventDefault ? event.preventDefault() : event.returnValue = false;
                        that.doSameForAllCalc( $anchor, $theDoSameLink, theCalcAwardObj );
                    } );
                }
            },
            style: { classes: 'ui-tooltip-shadow ui-tooltip-light' }
        } );

        $anchor.qtip( 'show' );
    },

    doSameForAllCalc: function( $anchor, $theToolTip, theCalcAwardObj ) {
        var that = this,
            $original = $anchor.closest( 'tr' ),
            $theRows = $( '.participant-item' ).not( $original );

        this.didSameForAll = true;

        if ( $theRows.length > 0 ) {

            $theRows.each( function() {
                var $this = $( this ),
                    $theInput = $this.find( "[name*='awardQuantity']" ),
                    $thisAnchor = $this.find( '.calcLink' );

                var thisInputName = $thisAnchor.siblings( 'input' ).attr( 'name' );
                var thisRatingIdName = thisInputName.substr( 0, thisInputName.indexOf( '.awardQuantity' ) );

                $original.attr( 'data-shared-calc-info', 'true' );

                $this.attr( 'data-shared-calc-info', 'true' );
                $theInput.val( that.savedCalcObj.awardedAmount );

                if ( that.calcObj.attributes.awardType !== 'merchlevel' ) {
                    $theInput.siblings( '.calcLink' ).text( that.savedCalcObj.awardedAmount );
                } else {
                    $theInput.siblings( '.calcLink' ).text( that.savedCalcObj.awardLevel.name + ' (' + that.savedCalcObj.awardLevel.value + ')' );
                }

                if ( that.calcObj.attributes.awardType !== 'merchlevel' ) {
                    $this.find( '.calcDeduction' ).text( that.savedCalcObj.awardedAmount );
                    that.setAwardPointsForCalc( $this.find( '.calcLink' ), theCalcAwardObj );
                } else {
                    that.setAwardPointsForCalc( $this.find( '.calcLink' ), theCalcAwardObj );
                    // $this.find('.calcDeduction').text();

                    if ( thisRatingIdName === '' || thisRatingIdName === undefined ) {
                        thisRatingIdName = thisInputName.substr( 0, thisInputName.indexOf( '.awardLevel' ) ); //in case we don't need to change the name
                    }
                    $thisAnchor.siblings( 'input' ).not( "[name*='calculatorResultBeans']" ).attr( 'name', thisRatingIdName + '.awardLevel' ).val( theCalcAwardObj.awardedAmount );
                    $thisAnchor.text( theCalcAwardObj.awardLevel.name + ' (' + theCalcAwardObj.awardLevel.value + ')' );
                    $thisAnchor.append( "<input type='hidden' name='" + thisRatingIdName + ".awardLevelName' value='" + theCalcAwardObj.awardLevel.name + "'>", "<input type='hidden' name='" + thisRatingIdName + ".awardLevelValue' value='" + theCalcAwardObj.awardLevel.value + "'>" );
                }

                $theToolTip.parent( '.ui-tooltip-content' ).parent().qtip( 'hide' );
            } );

        } else {
            $theToolTip.parent( '.ui-tooltip-content' ).parent().qtip( 'hide' );
        }
    },
    reloadOrGoBack: function( e ) {
        var that = this;

        if ( that.model.get( 'tabularData' ).totalRows === that.statusCount ) {
            window.history.back();
        } else {

            $( e.target ).parents( '.approvalSuccessModal' ).modal( 'hide' );
            location.reload( true );
        }
    },
    doRenderPreviousCalcInfo: function( $theRow, theQtip ) {
        if ( $theRow.attr( 'data-shared-calc-info' ) === 'true' ) {
            this.doUpdateRecogCalcValues( this.savedCalcObj, theQtip, true );
        } else if ( this.savedCalcRows[ $theRow.attr( 'data-participant-cid' ) ] ) {
            this.doUpdateRecogCalcValues( this.savedCalcRows[ $theRow.attr( 'data-participant-cid' ) ], theQtip, true );
        } else if ( $( '#dataForm' ).find( "[name*='].id']" ).filter( "[value='" + $theRow.attr( 'data-participant-id' ) + "']" ).val() !== undefined ) {
            this.doRenderRecogCalcValuesOld( $( '#dataForm' ).find( "[name*='].id']" ), theQtip );
        }
    },

    doRenderRecogCalcValuesOld: function( recipIdInput, theQtip ) {

        var theRecipNameRaw = recipIdInput.attr( 'name' );
        var theRecipCalcNameRaw = theRecipNameRaw.substr( 0, theRecipNameRaw.indexOf( '.id' ) ) + '.calculatorResultBeans';
        var finalSelect;


        $( '#dataForm' ).find( "[name*='" + theRecipCalcNameRaw + "']" ).filter( "[name*='criteriaId']" ).each( function() {
            var $this = $( this ),
                thisCriteriaId = $this.val(),
                thisInputName = $this.attr( 'name' ),
                thisRatingIdName = thisInputName.substr( 0, thisInputName.indexOf( '.criteriaId' ) ),
                thisRatingId = $( "[name='" + thisRatingIdName + ".ratingId']" ).val(),
                theRatingSelectPar = theQtip.find( '.recCalcCriteriaWrapper' ).filter( "[data-criteria-id='" + thisCriteriaId + "']" ),
                theRatingSelect = theRatingSelectPar.find( 'select' ),
                theRatingOption = $( theRatingSelect ).find( "[data-ratingid='" + thisRatingId + "']" );
            theRatingSelect.val( theRatingOption.val() );
            finalSelect = theRatingSelect;
        } );

        if ( finalSelect ) {
            finalSelect.change();
        }

    },

    showRecogCalcPayout: function( anchor ) {
        var that = this,
            $theAnchor = $( anchor ),
            tplName = 'nominationsCalcPayoutGridTpl',
            finishedTemplate = '',
            payTableLength = that.calcObj.payTable.rows.length,
            i = 0,
            thisPayoutRow,

            populateGrid = function( $theQtip ) {
                $theQtip.find( 'td' ).remove(); //empty it out.

                for ( i = 0; i < payTableLength; i++ ) {
                    thisPayoutRow = that.calcObj.payTable.rows[ i ];

                    $theQtip.find( 'tbody' ).append( '<tr><td>' + thisPayoutRow.score + '</td><td>' + thisPayoutRow.payout + '</td></tr>' );
                }

            },

            bindCloseButton = function( $this ) {
                var $theButton = $this.find( '#nominationsCalcPayoutCloseBtn' );
                $theButton.click( function() {
                    $this.qtip( 'hide' );
                } );
            };

        $theAnchor.qtip( {
            content: {
                text: 'Cannot find content'
            },
            position: {
                my: 'top left',
                at: 'bottom left',
                container: $( 'body' )
            },
            show: {
                ready: false,
                event: false
            },
            hide: {
                event: false,
                fixed: true,
                delay: 200
            },
            style: {
                padding: 0,
                classes: 'ui-tooltip-shadow ui-tooltip-light nominationsCalcPayoutTableWrapper',
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            },
            events: {
                visible: function() {
                    var $this = $( this );
                    populateGrid( $this );
                    bindCloseButton( $this );
                }
            }
        } );

        TemplateManager.get( tplName, function( tpl ) {
            finishedTemplate = tpl().toString();
            $theAnchor.qtip( 'option', 'content.text', finishedTemplate );
            $theAnchor.qtip( 'show' );
        }, this.tplPath );

    },

    doComparison: function( e ) {
        var $tar = $( e.target ),
            tarId = $tar.data( 'index' ),
            $compareBlock = this.$el.find( '.compareNomineeBlock' ),
            results = this.model.get( 'tabularData' ).results,
            selectedResult = _.where( results, { index: tarId } ),
            $container = this.$el.find( '.compareSection' ),
            isRemoved = false,
            that = this;

        if ( $container.children().length >= 2 ) {
            this.$el.find( '.compare:not(:checked)' ).attr( 'disabled', true );
        }

        _.each( $compareBlock, function( cb ) {
            var compareId = $( cb ).data( 'index' );

            if ( tarId === compareId ) {
                $( cb ).remove();

                isRemoved = true;

                that.$el.find( '.compare:not(.disabledJSON)' ).attr( 'disabled', false );
            }
        } );

        if ( !isRemoved ) {
            $container.append( this.subTpls.comparePaxTpl( selectedResult[ 0 ] ) );
        }
    },

    removeComparison: function( e ) {
        var $tar = $( e.target ),
            tarId = $tar.parents( '.compareNomineeBlock' ).data( 'index' ),
            $compareInp = this.$el.find( 'tbody .compare' );

        $tar.parents( '.compareNomineeBlock' ).remove();

        _.each( $compareInp, function( ci ) {
            var compareId = $( ci ).data( 'index' );

            if ( tarId === compareId ) {
                $( ci ).attr( 'checked', false );
            }
        } );
    },

    viewCompareTableRow: function( e ) {
        var $tar = $( e.target ),
            $compareBlock = $tar.parents( '.compareNomineeBlock' ),
            tarId = $compareBlock.data( 'index' ),
            $compareInp = this.$el.find( 'input.compare' );

        _.each( $compareInp, function( ci ) {
            var compareId = $( ci ).data( 'index' );

            if ( tarId === compareId ) {
                $.scrollTo( $( ci ).parents( 'tr' ), G5.props.ANIMATION_DURATION );

                setTimeout( function() {
                    G5.util.animBg( $( ci ).parents( 'tr' ).find( 'td' ), 'flash' );
                }, 500 );

                return;
            }
        } );
    },

    // *******************
    // SUBMITS AND VALIDATION
    // *******************
    validateDates: function( event ) {
        'use strict';
        var $startDate = this.$el.find( '#dateStart' ),
            $endDate = this.$el.find( '#dateEnd' ),
            $target = $( event.currentTarget ),
            startDate = $startDate.closest( '.datepickerTrigger' ).data( 'datepicker' ).date,
            endDate = $endDate.closest( '.datepickerTrigger' ).data( 'datepicker' ).date;

        event.preventDefault();

        switch ( $target.attr( 'id' ) ) {
            case $startDate.attr( 'id' ):
            case undefined:

                // once start date has been set, other dates cannot occur before it
                $endDate.closest( '.datepickerTrigger' ).datepicker( 'setStartDate', $startDate.val() ).datepicker( 'setEndDate', '+0d' );

                // if the new startDate is later than the current endDate AND a value already exists in endDate, clear the value & indicate change
                if ( startDate.valueOf() > endDate.valueOf() && $endDate.val() !== '' ) {
                    G5.util.animBg( $endDate, 'background-flash' );
                    $endDate.val( '' ).closest( '.datepickerTrigger' ).datepicker( 'update' );
                }
                break;
            case $endDate.attr( 'id' ):
                G5.util.formValidate( $( '#approvalsSearchForm' ).find( '.validateme' ) );

                // if endDate is updated, displayEndDate updates automatically to endDate + 10 days
                break;
        }
    },

    validateNumeric: function( e ) {
        var $tar = $( e.target ),
            v = $tar.val(),
            decPlaces = this.model.get( 'promotion' ).payoutType === 'cash' ? '2' : '4',
            reStr = '^-?(\\d{1,9})?(\\.\\d{0,' + ( decPlaces ) + '})?$',
            regEx = new RegExp( reStr );

        if ( !regEx.test( v ) ) {
            // if not matching our regex, then set the value back to what it was
            // or empty string if no last val
            $tar.val( $tar.data( 'lastVal' ) || '' );
        } else {
            // store this passing value, we will use this to reset the field
            // if a new value doesn't match
            $tar.data( 'lastVal', v );
        }
    },

    validateRangeOf: function( $el ) {
        var castNum = '',
            val,
            rMin,
            rMax;
            if ( this.model.get( 'promotion' ).payoutType === 'cash' ) {
              castNum = function( x ) { // convert strings to nums
                      return typeof x === 'string' ? parseFloat( x ) : x;
                  };
            } else {
              castNum = function( x ) { // convert strings to nums
                      return typeof x === 'string' ? parseInt( x ) : x;
                  };
            }

        val = castNum( $el.val() || 0 ); // if '' then put a zero in there
        rMin = castNum( this.model.get( 'tabularData' ).awardMin );
        rMax = castNum( this.model.get( 'tabularData' ).awardMax );

        // OUT of RANGE ERROR
        if ( val < rMin || val > rMax ) {
            // disp error
            if( $el.val() == '' ) {
                this.onNavErrorTip1( 'fillAward', $el );
            }else {
                this.onNavErrorTip1( 'awardOutOfRange', $el );
            }
            return false;
        }

        this.onNavErrorTip( '', $el, true );

        return true;
    },

    validateTeamAward: function() {
        var awardValid = true,
            that = this;

        this.$el.find( '#addAwardModal .awardPointsInp:not(.optOut)' ).each( function() {
            if ( !that.validateRangeOf( $( this ) ) ) {
                awardValid = false;

                return false;
            } else {
                awardValid = true;

                return { valid: true };
            }
        } );

        if ( awardValid ) {
            return { valid: true };

        } else {
            this.onNavErrorTip1( 'msgGenericError', this.$el.find( '.addAwardBtn' ) );
        }
    },

    checkForServerErrors: function () {
        if ( $( '#serverReturnedErrored' ).val() === 'true' ) {
            $( '#approvalsErrorBlock' ).slideDown( 'fast' ); //show error block
        }
    },

    submitTeamAward: function() {
        var tindex = this.$el.find( '#addAwardModal .teamRecipientList ul' ).data( 'teamIndex' ),
            teamAward = 0,
            that = this,
            $thisRowInputs,
            $teamAwardInputs,
            $paxRow,
            paxId,
            paxAward;

        if ( !this.validateTeamAward() ) {
            return false;
        }
        console.log( that );
        console.log( tindex );
        $teamAwardInputs = $( '.teamAddAward' );
        _.each( ( $teamAwardInputs ), function( value, index ) {
            if ( tindex === $( value ).data( 'team-id' ) ) {
                $thisRowInputs = $teamAwardInputs[ index ];
            }
        } );
        $paxRow = this.$el.find( '#addAwardModal li' );
        $( $thisRowInputs ).closest( '.hiddenTeamInp' ).remove();

        _.each( $paxRow, function( row, index ) {
            paxId = parseInt( $( row ).find( '.profile-popover' ).data( 'participantIds' ) );
            paxAward = null;
            console.log( that );
            if ( that.promoModel.payoutType === 'cash' ) {
              paxAward = parseFloat( $( row ).find( '.awardPointsInp' ).val() );
            } else {
              paxAward = parseInt( $( row ).find( '.awardPointsInp' ).val() );
            }

            teamAward += paxAward;

            that.model.setTeamIndividualAward( tindex, paxId, paxAward );

            $( $thisRowInputs ).siblings( '.teamAwardAmount' ).append( '<input type="hidden" name="tabularData.results[' + tindex + '].team[' + index + '].paxId" value="' + paxId + '" class="hiddenTeamInp" /> <input type="hidden" name="tabularData.results[' + tindex + '].team[' + index + '].value" value="' + paxAward + '" class="hiddenTeamInp" />' );
        } );

        this.model.setTeamAward( tindex, teamAward );
        if ( teamAward === 0 ) {
            teamAward = null;
        }
        $( $thisRowInputs ).siblings( '.teamAwardAmount' ).val( teamAward );

        this.$el.find( '#addAwardModal' ).modal( 'hide' );
    },

    submitBudgetRequest: function( e ) {
        var $budgetPopover = this.$el.find( '.budgetIncreasePopover' ),
            $validate = $budgetPopover.find( '.validateme' ),
            isValid = G5.util.formValidate( $validate ),
            budget = this.$el.find( '.budgetIncrease' ).val();

        e.preventDefault();

        // failed generic validation tests (requireds mostly)
        if ( !isValid ) {
            return false;
        }

        this.model.sendBudget( budget, this.promotionId );

        $budgetPopover.closest( '.qtip' ).qtip( 'hide' );
    },
    submitFilterForm: function( e ) {
        //TODO: Make this JSON format instead of form data
        var $form = this.$el.find( '#approvalsSearchForm' ),
            formData = $form.serializeArray(),
            data,
            arrData = {},
            status;

        e.preventDefault();
        console.log( formData );
        this.$el.find( '.compareNomineeBlock' ).remove();
        this.levelId = $( '#levelsFilter' ).val();
        _.each( formData, function( el ) {
            var value = _.values( el );
            if ( value[ 0 ] === 'statusFilter' ) {

                if ( !arrData.hasOwnProperty( value[ 0 ] ) ) {
                    arrData[ value[ 0 ] ] = [];
                    arrData[ value[ 0 ] ].push( value[ 1 ] );

                } else {

                    arrData[ value[ 0 ] ].push( value[ 1 ] );
                }
            } else {
                arrData[ value[ 0 ] ] = value[ 1 ];
            }
        } );
        status = arrData[ 'statusFilter' ];
        arrData[ 'statusFilter' ] = '';

        _.each( status, function( value, index ) {

            if ( index < status.length - 1 ) {
                arrData[ 'statusFilter' ] += value + ',';

            } else if ( index === status.length - 1 ) {
                arrData[ 'statusFilter' ] += value;
            }
        } );

                data = {
                    promotionId: this.promotionId,
                    nodeId: this.nodeId,
                    levelId: this.levelId
                };

        this.model.loadApprovalTable( $.extend( {}, data, arrData ) );
    },

    doCancelApproval: function( e ) {
        var $tar = $( e.target ),
            url = $tar.attr( 'href' );

        e.preventDefault();

        window.location = url;
    },

    validate: function() {
        var $form = this.$el.find( '.approvalTableWrap' ),
            $validate = $form.find( '.validateme:visible' ),
            isValid = G5.util.formValidate( $validate ),
            awardValid = true,
            that = this;

         // failed generic validation tests (requireds mostly)
        if ( !isValid ) {
            return false;
        }

        this.$el.find( 'table .awardPointsInp' ).each( function() {
            if ( $( this ).prop( 'readOnly' ) ) {
                awardValid = true;
                return true;
            }

            if ( !that.validateRangeOf( $( this ) ) ) {

                awardValid = false;

                return false;
            } else {

                awardValid = true;

                return { valid: true };
            }
        } );

        if ( awardValid ) {
            return { valid: true };

        } else {
            this.onNavErrorTip( 'msgGenericError', this.$el.find( '.submitApproval' ) );
            return false;
        }
    },

    submitApproval: function( e ) {
        var $form = this.$el.find( '.approvalTableWrap' ),
            $formFields,
            isValid = this.validate(),
            $tar = $( e.target ),
            i,
            that = this,
            $levelId,
            $startDate = this.$el.find( '#dateStart' ).val(),
            $endDate = this.$el.find( '#dateEnd' ).val();

        e.preventDefault();
        this.$el.find( '.startDateFilter' ).val( $startDate );
        this.$el.find( '.endDateFilter' ).val( $endDate );
        this.$el.find( '.promotionIdInp' ).val( this.promotionId );
        // failed generic validation tests (requireds mostly)
        if ( !isValid ) {
            return false;
        }
        if( this.$el.find( '#levelsFilter' ).length ) {
            $levelId = this.$el.find( '#levelsFilter option:selected' ).val();
        } else {
            $levelId = this.$el.find( '.levelsFilterInp' ).val();
        }

        this.$el.find( '.levelIdFilter' ).val( $levelId );


        G5.util.showSpin( this.$el, {
            cover: true
        } );

        $tar.attr( 'disabled', 'disabled' );
        // $form.find('input, select').removeAttr('disabled');
        $formFields = $form.serializeArray();

        // Adding the notification date value manually to the form submit data since disabled fields are ignored on submit
        if( $form.find( 'input[name="tabularData.results[0].notificationDate"]' ).val() ) {
            $formFields.push( { name: 'tabularData.results[0].notificationDate', value: $form.find( 'input[name="tabularData.results[0].notificationDate"]' ).val() } );
        }

        for ( i = 0; i < $formFields.length; i++ ) {
            if ( $formFields[ i ].value === 'pend' ) {
                $formFields.splice( i, 1 );
                i--;
            }
        }

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: $form.attr( 'action' ),
            data: $formFields,
            success: function( serverResp ) {
                console.log( serverResp.data );

                if ( serverResp.data.messages && serverResp.data.messages.length > 0 ) {
                    if ( serverResp.data.messages[ 0 ].type === 'error' ) {
                        that.onNavErrorTip( 'overBudget', that.$el.find( '.submitApproval' ), false, serverResp.data.messages[ 0 ].text );
                    }
                    $tar.removeAttr( 'disabled' );
                } else {
                    that.$el.find( '.approvalSuccessModal' ).modal( 'show' );
                    $tar.removeAttr( 'disabled' );
                }

                G5.util.hideSpin( that.$el );
            }

        } );
    }
} );
