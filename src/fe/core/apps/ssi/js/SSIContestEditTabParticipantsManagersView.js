/*exported SSIContestEditTabParticipantsManagersView */
/*global
TemplateManager,
PaxSelectedPaxCollection,
PaxSearchStartView,
ParticipantPaginatedView,
SSIContestEditTabParticipantsManagersView:true
*/
SSIContestEditTabParticipantsManagersView = Backbone.View.extend( {

    initialize: function( opts ) {
        this.cm = null;
        this.opts = opts;
        this.context = 'participant';
        this.urlUpload = G5.props.URL_JSON_SSI_PAX_UPLOAD_DOCUMENT;
        this.errorFileUrl = ''; // need to discuss 'download the error file here' as shown in wireframe
        this.errorCount = 0; // default 0, only displayed for participants upload error resoponse.
        // SSIContestPageEditView parent container for this tab
        this.containerView = opts.containerView;
        this.contMod = this.containerView.contestModel;
        this.cm = this.containerView.contestModel;

        this.paginatedViews = [];

        this.initPaxCollections( 'pax' );
        if ( this.cm.get( 'contestType' ) !== 'awardThemNow' ) {
            this.initPaxCollections( 'superViewers' );
        }
        // participant collection view ( paginated )
        this.initPaxPaginatedView( 'pax', '#ssiParticipants', G5.props.URL_JSON_CONTEST_PARTICIPANTS );

        if ( this.cm.get( 'contestType' ) !== 'awardThemNow' ) {
            this.initPaxPaginatedView( 'man', '#ssiManagers', G5.props.URL_JSON_CONTEST_MANAGERS );
            this.initPaxPaginatedView( 'superViewers', '#ssiSuperViewers', G5.props.URL_JSON_CONTEST_SUPERVIEWERS );

        } else {
            this.$el.find( '.hideOnAwardThemNow' ).hide();
            this.$el.find( '.showOnAwardThemNow' ).show();
            this.$el.find( '.doSelectManagers' ).hide();
            this.$el.find( '.doSelectSuperViewers' ).hide();
        }

        this.setupEvents();

        this.fileUpload();

        // everything starts up in the update() function
        // initialize is too soon to start the AJAX calls for data
        // we only want to start bothering the server when
        // users visit this step

    },

    events: {
        'click .doSelectManagers': 'doSelectManagers',
        'click .doSelectSuperViewers': 'activateContextSection',
        'click .doSelectParticipants': 'activateContextSection',
        'click .ssiUploadParticipantsSave': 'saveParticipants',
        'click .ssiUploadParticipantsCancel': 'resetParticipantsUploadForm',
        'click .managerSearchTableWrapper .sortHeader': 'doManSearchTableSort',
        'click .managerSearchTableWrapper .participantSelectControl': 'doManSearchSelect',
        'click .managerSearchTableWrapper .selectAllBtn': 'doManSearchSelect',
        'click .remParticipantControl': 'doRemParticipant'
    },

    setupEvents: function() {
        var cm = this.cm;
        // fetchTeamSearchFilters only called before step 2 render
        cm.on( 'start:fetchTeamSearchFilters', this.handleTeamSearchFiltersStart, this );
        cm.on( 'end:fetchTeamSearchFilters', this.handleTeamSearchFiltersEnd, this );
        cm.on( 'success:fetchTeamSearchFilters', this.handleTeamSearchFiltersSuccess, this );

        cm.on( 'start:ajaxRemoveParticipant', this.handleAjaxParticipantStart, this );
        cm.on( 'end:ajaxRemoveParticipant', this.handleAjaxParticipantEnd, this );
        cm.on( 'success:ajaxRemoveParticipant', this.handleAjaxAddRemoveParticipant, this );
        cm.on( 'start:ajaxAddParticipant', this.handleAjaxParticipantStart, this );
        cm.on( 'end:ajaxAddParticipant', this.handleAjaxParticipantEnd, this );
        cm.on( 'success:ajaxAddParticipant', this.handleAjaxAddRemoveParticipant, this );

        cm.on( 'start:ajaxRemoveManager', this.handleAjaxManagerStart, this );
        cm.on( 'end:ajaxRemoveManager', this.handleAjaxManagerEnd, this );
        cm.on( 'success:ajaxRemoveManager', this.handleAjaxRemoveManager, this );
        cm.on( 'start:ajaxAddManager', this.handleAjaxManagerStart, this );
        cm.on( 'end:ajaxAddManager', this.handleAjaxManagerEnd, this );
        cm.on( 'success:ajaxAddManager', this.handleAjaxAddManager, this );

        cm.on( 'start:fetchManagersSearch', this.handleFetchManagersSearchStart, this );
        cm.on( 'end:fetchManagersSearch', this.handleFetchManagersSearchEnd, this );
        cm.on( 'success:fetchManagersSearch', this.fetchSelectedManagers, this );

        cm.on( 'sort:managersSearchResults', this.renderManagersSearchTable, this );

        cm.on( 'start:ajaxRemoveSuperViewer', this.handleAjaxParticipantStart, this );
        cm.on( 'end:ajaxRemoveSuperViewer', this.handleAjaxParticipantEnd, this );
        cm.on( 'success:ajaxRemoveSuperViewer', this.handleAjaxAddRemoveParticipant, this );
        cm.on( 'start:ajaxAddSuperViewer', this.handleAjaxParticipantStart, this );
        cm.on( 'end:ajaxAddSuperViewer', this.handleAjaxParticipantEnd, this );
        cm.on( 'success:ajaxAddSuperViewer', this.handleAjaxAddRemoveParticipant, this );

        this.eachPaginated( function ( pView ) {
            pView.model.on( 'change:recordsTotal', this.updateSectionCounts, this );
        } );
        this.on( 'error:genericAjax', this.handleAjaxError, this );
        this.on( 'submitSuccess:ssiUploadParticipantsSubmit', this.handleSubmitParticipantsSuccess, this );
        if ( cm.get( 'contestType' ) !== 'awardThemNow' ) {
            this.manPaginatedView.on( 'success:fetchParticipants', this.gatherSelectedManagerIds, this );
        }
    },

/***
 *    d8b          d8b 888
 *    Y8P          Y8P 888
 *                     888
 *    888 88888b.  888 888888 .d8888b
 *    888 888 "88b 888 888    88K
 *    888 888  888 888 888    "Y8888b.
 *    888 888  888 888 Y88b.       X88
 *    888 888  888 888  "Y888  88888P'
 *
 *
 *
 */

    initPaxCollections: function( context ) {
        // selected collection
        this[ context + 'SelectedCollection' ] = new PaxSelectedPaxCollection();

        // serarch selected collection
        this[ context + 'SearchSelectedCollection' ] = new PaxSelectedPaxCollection();
    },

    // init pax search stuff
    initPaxSearch: function( context ) {
        // context could = 'pax' or 'superViewers', for example
        // page level reference to participant search

        this[ context + 'SearchView' ] = new PaxSearchStartView( {
            el: this.$el.find( '.' + context + 'SearchStartView' ),
            multiSelect: true,
            clearOnClose: true,
            selectedPaxCollection: this[ context + 'SearchSelectedCollection' ],
            disableSelect: false,
            addSelectedPaxView: true,
            follow: false
        } );

        this[ context + 'SearchFilterView' ] = new PaxSearchStartView( {
                el: this.$el.find( '.' + context + 'SearchFilterStartView' ),
                filterSearch: true,
                multiSelect: true,
                clearOnClose: true,
                selectedPaxCollection: this[ context + 'SearchSelectedCollection' ],
                disableSelect: false,
                addSelectedPaxView: true,
                presetFilters: this.paxPresetSearchFilters,
                follow: false
        } );

        // add search closed event
        this[ context + 'SearchView' ].on( 'closedsearch', this.searchClosed, this );
        this[ context + 'SearchFilterView' ].on( 'closedsearch', this.searchClosed, this );

        this.hideShowTeamFilter( this.paxPresetSearchFilters );
        // set the contestId in the search view model ajax params
        this[ context + 'SearchView' ].setAjaxParam( 'contestId', this.cm.get( 'id' ) );
        this[ context + 'SearchView' ].setAjaxParam( 'ssiContestClientState', this.cm.get( 'ssiContestClientState' ) );

        this[ context + 'SearchFilterView' ].setAjaxParam( 'contestId', this.cm.get( 'id' ) );
        this[ context + 'SearchFilterView' ].setAjaxParam( 'ssiContestClientState', this.cm.get( 'ssiContestClientState' ) );

        //TODO: CHECK ON THIS
        if ( context === 'pax' && this.contMod.get( 'awardThemNowStatus' ) ) {
            this[ context + 'SearchView' ].setAjaxParam( this.contMod.get( 'awardThemNowStatus' ) );
            this[ context + 'SearchFilterView' ].setAjaxParam( this.contMod.get( 'awardThemNowStatus' ) );
        }
    },

    initPaxPaginatedView: function( context, elel, url ) { // could remove extra params by handling 'participants' if context = 'pax'

        // Example: this.initPaxPaginatedView( 'pax', '#ssiParticipants', G5.props.URL_JSON_CONTEST_PARTICIPANTS );
        // Example: this.initPaxPaginatedView( 'superViewers', '#ssiSuperViewers', G5.props.URL_JSON_CONTEST_SUPERVIEWERS ); // Important Notice: 'superViewers' is plural (s), to match element/variable names.
        var that = this;

        this[ context + 'PaginatedView' ] = new ParticipantPaginatedView( {
            el: this.$el.find( elel ),
            participantsUrl: url,
            participants: this[ context + 'SelectedCollection' ],
            tplName: 'ssiParticipantsPaginatedView',
            fetchParamsFunc: function() {
                if ( context === 'pax' && that.contMod.get( 'awardThemNowStatus' ) ) {
                    return { awardThemNowStatus: that.contMod.get( 'awardThemNowStatus' ) };
                }
                return {};
            },
            delayFetch: true // manual call to start the fetch operation
        } );

        this.paginatedViews.push( this[ context + 'PaginatedView' ] );
    },

    // init UI for different states of model ( pax and managers populated or not )
    initSections: function() {
        var paxCount = this.paxPaginatedView.getCount(),
            manCount = 0,
            superViewerCount = 0;
        if ( this.cm.get( 'contestType' ) !== 'awardThemNow' ) {
            manCount = this.manPaginatedView && this.manPaginatedView.getCount();
            superViewerCount = this.superViewersPaginatedView && this.superViewersPaginatedView.getCount();
        }

        // CASE: if we have paxes and managers available but none selected
        if ( paxCount && !manCount ) {
            this.doSelectManagers();
        }

        // CASE: if we have paxes and managers and superviewers available but none selected
        if ( paxCount && manCount && !superViewerCount  ) {
            this.activateContextSection( 'superViewers' );
        }

        // CASE: if we have no paxes and no managers
        if ( !paxCount && !manCount && !superViewerCount ) {
            this.activateContextSection();
        }

        this.updateSectionCounts();
    },




/***
 *                      888                        888
 *                      888                        888
 *                      888                        888
 *    888  888 88888b.  888  .d88b.   8888b.   .d88888
 *    888  888 888 "88b 888 d88""88b     "88b d88" 888
 *    888  888 888  888 888 888  888 .d888888 888  888
 *    Y88b 888 888 d88P 888 Y88..88P 888  888 Y88b 888
 *     "Y88888 88888P"  888  "Y88P"  "Y888888  "Y88888
 *             888
 *             888
 *             888
 */

    fileUpload: function() {
        var that = this,
            $form = this.$el.find( '#ssiUploadParticipantsForm' ),
            $input = $form.find( '#ssiHiddenUploadParticipants' );

        $input.fileupload( {
            url: that.urlUpload,
            method: 'POST',
            dataType: 'g5json',
            formData: {
                method: 'save',
                form: $form
            },
            replaceFileInput: false,
            add: function( e, data ) {

                that.clearUploadParticipantsError();

                if ( !that.validateFileExtension( $form ) ) {
                    that.toggleParticipantsUploadControls( true );
                    return false;
                } else {
                    $form.data( 'fileData', data );

                    $( '.ssiUploadFormActions' ).removeClass( 'hidden' ).hide().slideDown();
                    that.toggleParticipantsUploadControls( false );
                }
            },
            done: function( e, data ) {
                // default '' ignored if no errors
                that.errorFileUrl = data.result.data.response.errorFileUrl;
                // default 0 ignored if no errors
                that.errorCount = data.result.data.response.errorCount;
                that.errorText = data.result.data.messages[ 0 ].text;
                var errors = data.result.getFirstError(),
                step = 'stepParticipantsManagers';
                if ( errors ) {
                    that.trigger( 'error:genericAjax', errors );

                    return false;
                } else {
                    that.resetParticipantsUploadForm();
                    that.trigger( 'submitSuccess:ssiUploadParticipantsSubmit', null, step, step, false );
                }
            }
        } );
    },

    validateFileExtension: function( target ) {
        var regEx = /\.(csv|xls|xlsx)$/i,
            $inp = target.find( '#ssiHiddenUploadParticipants' ),
            msg = this.$el.find( '.ssiUploadSSButton' ).data( 'extraValidate' );

        if ( regEx.test( $inp.val() ) ) {
            $inp.qtip( 'hide' );

            return true;
        } else {

            $inp.qtip( {
                content: { text: msg },
                position: {
                    my: 'bottom center',
                    at: 'top center',
                    container: this.$el,
                    adjust: {
                        method: 'shift none'
                    }
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
                    classes: 'ui-tooltip-shadow ui-tooltip-red validate-tooltip',
                    tip: {
                        corner: true,
                        width: 20,
                        height: 10
                    }
                }
            } );

            // clear the input so the form fails validation and the file cannot be submitted
            $inp.val( '' );

            return false;
        }

    },

    toggleParticipantsUploadControls: function( bool ) {
        $( '.ssiUploadFormActions button' ).prop( 'disabled', bool );

        if( bool ) {
            $( '.ssiUploadFormActions button' ).addClass( 'disabled' );
        } else {
            $( '.ssiUploadFormActions button' ).removeClass( 'disabled' );
        }
    },

    clearUploadParticipantsError: function() {
        var $m = this.$el.find( '#ssiUploadParticipantsError' );
        if ( $m .is( ':visible' ) ) {
            $m.empty().hide();
        }
    },

    saveParticipants: function( e ) {
        var $form = $( '#ssiUploadParticipantsForm' );

        e.preventDefault();

       if ( $form.data() && $form.data().fileData ) {
           $form.data().fileData.submit();
        }
    },

    resetParticipantsUploadForm: function() {
        $( '#ssiHiddenUploadParticipants' ).val( '' );
        this.clearUploadParticipantsError();
        this.toggleParticipantsUploadControls( true );
        $( '#ssiUploadParticipantsForm .ssiUploadFormActions' ).slideUp();
    },

    handleSubmitParticipantsSuccess: function() {
        this.paxPaginatedView.fetchParticipants( null, true );
        this.manPaginatedView.fetchParticipants( null, true );
        this.superViewersPaginatedView.fetchParticipants( null, true );
    },




    handleAjaxError: function( error ) {
        var $m = this.$el.find( '#ssiUploadParticipantsError' ),
            errorFileUrl = this.errorFileUrl,
            errorCount = parseInt( this.errorCount ) || 0;

        // If document error...
        if( errorCount > 0 ) {
            errorCount > 1 ? errorCount = errorCount + ' rows' : errorCount = errorCount + ' row';
            $m.html( '<p><b>Import error:</b> We found errors on <b>' + errorCount + '</b>. <a href="' + errorFileUrl + '">Download the error file here</a> to fix manually, then import the correct file.</p>' );

        } else {
        // If comman error...
            $m.html( '<p><b>Import error:</b> ' + this.errorText + '</p>' );
        }
        $m.show();

        $( '.ssiUploadFormActions .ssiUploadParticipantsSave' ).addClass( 'disabled' ).prop( 'disabled', true );
    },

    eachPaginated: function ( fn ) { // Note: Maybe does not need to load in each step
        try {
            _.each( this.paginatedViews, _.bind( fn, this ) );
        } catch ( e ) {}
    },

    hideShowTeamFilter: function( show ) {
        var filterTab = this.$el.find( '.filter-search' ),
            nameTab = this.$el.find( '.name-search' );
        if ( show ) {
            filterTab.show();
            nameTab.show();
            this.$el.find( '.filter-search a' ).tab( 'show' );
        } else {
            filterTab.hide();
            nameTab.hide();
            this.$el.find( '.name-search a' ).tab( 'show' );
        }
    },


/***
 *                                               888
 *                                               888
 *                                               888
 *    .d8888b   .d88b.   8888b.  888d888 .d8888b 88888b.
 *    88K      d8P  Y8b     "88b 888P"  d88P"    888 "88b
 *    "Y8888b. 88888888 .d888888 888    888      888  888
 *         X88 Y8b.     888  888 888    Y88b.    888  888
 *     88888P'  "Y8888  "Y888888 888     "Y8888P 888  888
 *
 *
 *
 */
    handleTeamSearchFiltersStart: function() {
        this.$el.find( '.paxManLoadSpinnerWrapper' ).show()
            .find( '.paxManLoadSpinner' ).spin();
    },

    handleTeamSearchFiltersEnd: function() {
        this.$el.find( '.paxManLoadSpinner' ).spin( false )
            .closest( '.paxManLoadSpinnerWrapper' ).hide();
    },

    handleTeamSearchFiltersSuccess: function( data ) {
        this.paxPresetSearchFilters = data.presetSearchFilters;
        this.initPaxSearch( 'pax' );
        if ( this.cm.get( 'contestType' ) !== 'awardThemNow' ) {
            this.initPaxSearch( 'superViewers' );
        }
        this.initSections();
        this.containerView.updateBottomControls();
    },



    doSelectManagers: function( e ) {
        this.context = 'managers';
        var ppvParams = {
                contestId: this.containerView.contestModel.get( 'id' ),
                ssiContestClientState: this.containerView.contestModel.get( 'ssiContestClientState' )
            };

        if ( e ) { e.preventDefault(); }

        this.containerView.contestModel.fetchManagersSearch(); // all managers for selected paxes
        if ( this.cm.get( 'contestType' ) !== 'awardThemNow' ) {
            this.manPaginatedView.setFetchParams( ppvParams );
        }
    },

    activateContextSection: function( e ) {

        // if dynamically triggered with no parameters, default context = 'participants'
        this.context = 'participants';

        if( e ) {
            // if button triggered with no parameters...
            if ( e.currentTarget ) {
                e.preventDefault();
                this.context = $( e.currentTarget ).data( 'context' );
            } else {
                // if dynamically triggered with parameter string to override default... ie: 'managers'
                this.context = e;
            }
        }

        switch( this.context ) {

            case 'managers':
                this.curPaginatedView = this.manPaginatedView;
            break;
            case 'superViewers':
                this.curPaginatedView = this.superViewersPaginatedView;
                this.curPaginatedView.fetchParticipants();

            break;
            default:
                this.curPaginatedView = this.paxPaginatedView;
        }

        $( '.paxSelectContextWrapper:not(.' + this.context + 'Wrapper)' ).slideUp( G5.props.ANIMATION_DURATION );
        $( '.' + this.context + 'Wrapper' ).slideDown( G5.props.ANIMATION_DURATION );
    },


    // remove participant control clicked
    doRemParticipant: function( e ) {
        var $tar = $( e.target ),
            paxId;
        this.context = $tar.parents( '.paxSelectContextWrapper' ).data( 'context' );
        $tar = $tar.hasClass( '.remParticipantControl' ) ? $tar : $tar.closest( '.remParticipantControl' );
        paxId = $tar.data( 'participantId' );

        if( G5.util.capitalize( this.context, false ) === 'Participant' ) {
            this.containerView.contestModel.ajaxRemoveParticipant( paxId );
        }else if( G5.util.capitalize( this.context, false ) === 'SuperViewer' ) {
            this.containerView.contestModel.ajaxRemoveSuperViewer( paxId );
        }else{
            this.containerView.contestModel[ 'ajaxRemove' + G5.util.capitalize( this.context, false ) ]( paxId );
        }
    },


    // update UI state for buttons and things
    updateSectionCounts: function() {
            var $paxSec = this.$el.find( '.participantsWrapper' ),
            $manSec = this.$el.find( '.managersWrapper' ),
            $superViewerSec = this.$el.find( '.superViewersWrapper' ),
            $selMan = $paxSec.find( '.doSelectManagers' ),
            $selSuperViewer = $paxSec.find( '.doSelectSuperViewers' ),
            paxCount = this.paxPaginatedView.getCount(),
            manCount = this.manPaginatedView ? this.manPaginatedView.getCount() : 0,
            superViewerCount = this.superViewersPaginatedView ? this.superViewersPaginatedView.getCount() : 0,
            isAwdThemNow = this.cm.get( 'contestType' ) === 'awardThemNow';

        if ( !isAwdThemNow ) {
            // select manager button state
            $selMan.prop( 'disabled', paxCount ? false : true );
            $selMan[ paxCount ? 'removeClass' : 'addClass' ]( 'disabled' );
            // select superViewer button state
            $selSuperViewer.prop( 'disabled', paxCount ? false : true );
            $selSuperViewer[ paxCount ? 'removeClass' : 'addClass' ]( 'disabled' );
        }

        // select participants button state
        // * always available

        // lock down tab navigation if we don't have pax
            if ( !paxCount || paxCount == 1 ) {
                this.containerView.lockNav( 'PaxManupdateSectionCounts', [ 'next' ] );
                this.$el.find( '.errorPaxCount' ).show();
            } else {
                this.containerView.unlockNav( 'PaxManupdateSectionCounts' );
                this.$el.find( '.errorPaxCount' ).hide();
            }

        if( !isAwdThemNow ) {

            // pax count stuff on managersWrapper>paxSelectWrap
            $manSec.find( '.showIfHasPax' )[ paxCount ? 'show' : 'hide' ]();
            $manSec.find( '.hideIfHasPax' )[ paxCount ? 'hide' : 'show' ]();
            $manSec.find( '.paxCountBind' ).text( paxCount );

             // superViewer count stuff on managersWrapper>paxSelectWrap
            $manSec.find( '.showIfHasSuperViewer' )[ superViewerCount ? 'show' : 'hide' ]();
            $manSec.find( '.hideIfHasSuperViewer' )[ superViewerCount ? 'hide' : 'show' ]();
            $manSec.find( '.superViewerCountBind' ).text( superViewerCount );

           // pax count stuff on managersWrapper>paxSelectWrap
            $superViewerSec.find( '.showIfHasPax' )[ paxCount ? 'show' : 'hide' ]();
            $superViewerSec.find( '.hideIfHasPax' )[ paxCount ? 'hide' : 'show' ]();
            $superViewerSec.find( '.paxCountBind' ).text( paxCount );

            // superViewer count stuff on managersWrapper>paxSelectWrap
            $superViewerSec.find( '.showIfHasMan' )[ manCount ? 'show' : 'hide' ]();
            $superViewerSec.find( '.hideIfHasMan' )[ manCount ? 'hide' : 'show' ]();
            $superViewerSec.find( '.manCountBind' ).text( manCount );

            // manager count stuff on
            $paxSec.find( '.showIfHasMan' )[ manCount ? 'show' : 'hide' ]();
            $paxSec.find( '.hideIfHasMan' )[ manCount ? 'hide' : 'show' ]();
            $paxSec.find( '.manCountBind' ).text( manCount );

             // super viewer count stuff on
            $paxSec.find( '.showIfHasSuperViewer' )[ superViewerCount ? 'show' : 'hide' ]();
            $paxSec.find( '.hideIfHasSuperViewer' )[ superViewerCount ? 'hide' : 'show' ]();
            $paxSec.find( '.superViewerCountBind' ).text( superViewerCount );
        }
    },
    /***
     *             888    d8b 888 d8b 888
     *             888    Y8P 888 Y8P 888
     *             888        888     888
     *    888  888 888888 888 888 888 888888 888  888
     *    888  888 888    888 888 888 888    888  888
     *    888  888 888    888 888 888 888    888  888
     *    Y88b 888 Y88b.  888 888 888 Y88b.  Y88b 888
     *     "Y88888  "Y888 888 888 888  "Y888  "Y88888
     *                                            888
     *                                       Y8b d88P
     *                                        "Y88P"
     */
    // get reference context name for search and collections etc.
    getSmallContext: function() {
        // fixx this  and man managers etc.
        var context = G5.util.capitalize( this.context, false );
        if ( context === 'Participants' || context === 'Participant' ) {
            return 'pax';
        }else if ( context === 'SuperViewers' || context === 'SuperViewer' ) {
            return 'superViewers';
        }else if ( context === 'Managers' || context === 'Manager' ) {
            return 'man';
        }
    },
/***
 *    888 d8b          888
 *    888 Y8P          888
 *    888              888
 *    888 888 .d8888b  888888 .d88b.  88888b.   .d88b.  888d888 .d8888b
 *    888 888 88K      888   d8P  Y8b 888 "88b d8P  Y8b 888P"   88K
 *    888 888 "Y8888b. 888   88888888 888  888 88888888 888     "Y8888b.
 *    888 888      X88 Y88b. Y8b.     888  888 Y8b.     888          X88
 *    888 888  88888P'  "Y888 "Y8888  888  888  "Y8888  888      88888P'
 *
 *
 *
 */
    searchClosed: function(  ) {
        var ctx = this.getSmallContext();
            var searchCollection = this[ ctx + 'SearchSelectedCollection' ];

        // add participants to collection
        var paxIds = searchCollection.pluck( 'id' );
        if( paxIds.length ) {
            if( ctx === 'pax' ) { this.containerView.contestModel.ajaxAddParticipant( paxIds );}
            if( ctx === 'superViewers' ) { this.containerView.contestModel.ajaxAddSuperViewer( paxIds );}
            // reset search collection
            searchCollection.reset();
            // update selected participants
        }else{
            // do nothing just close spinners if open
            this.curPaginatedView.spinLock( false );
        }

    },

    handleAjaxParticipantStart: function() {
        this.curPaginatedView.spinLock();
    },

    handleAjaxParticipantEnd: function( params ) {
        console.log( 'handleAjaxParticipantEnd' );
      //this.curPaginatedView.spinLock( false );
    },

    handleAjaxAddRemoveParticipant: function( data, origParams ) {
        console.log( 'handleAjaxAddRemoveParticipant' );
        this.curPaginatedView.fetchParticipants();
    },

/***
 *
 *
 *
 *    88888b.d88b.   8888b.  88888b.   8888b.   .d88b.   .d88b.  888d888 .d8888b
 *    888 "888 "88b     "88b 888 "88b     "88b d88P"88b d8P  Y8b 888P"   88K
 *    888  888  888 .d888888 888  888 .d888888 888  888 88888888 888     "Y8888b.
 *    888  888  888 888  888 888  888 888  888 Y88b 888 Y8b.     888          X88
 *    888  888  888 "Y888888 888  888 "Y888888  "Y88888  "Y8888  888      88888P'
 *                                                  888
 *                                             Y8b d88P
 *                                              "Y88P"
 */

    fetchSelectedManagers: function() {
        this.manPaginatedView.fetchParticipants(); // paginated list of selected managers
    },

    gatherSelectedManagerIds: function( data ) {
        this.selectedManagers = data.participants;
        this.selectedManagerIds = _.pluck( this.selectedManagers, 'id' );
        this.renderManagersSearchTable();
    },

    renderManagersSearchTable: function() {
        var that = this,
            cm = this.containerView.contestModel,
            manSearch = cm.managersSearchResults,
            managersForTpl = [],
            $manSearch = this.$el.find( '.managerSearchTableWrapper' ),
            $scrTable = $manSearch.find( '.scrollTable' ),
            $mstBody = $manSearch.find( 'tbody' );

        $scrTable.height( $scrTable.height() ).css( 'visibility', 'hidden' );

        // massage data, we might have _isChecked on the naked manager object
        manSearch.each( function( man ) {
            var x = man.toJSON();
            x._isChecked = that.selectedManagerIds.indexOf( man.id ) === -1 ? false : true;
             managersForTpl.push( x );

        } );
        $mstBody.empty();

        if ( manSearch.length === 0 ) {
            $manSearch.append( $manSearch.find( 'table' ).data( 'msgNoResults' ) );
        } else {
            TemplateManager.get( 'manSearchRows', function( tpl ) {
                $mstBody.append( tpl( { managers: managersForTpl } ) );
                $scrTable.height( '100%' ).css( 'visibility', 'visible' );
            } );
        }

        this.updateManagersSearchTable();

    },

    handleFetchManagersSearchStart: function() {
        //console.log( 'handleFetchManagersSearchStart', this.context );
        var context = this.context === 'managers' ? 'Man' : G5.util.capitalize( this.context, false );

        this.$el.find( '.pax' + context + 'SearchSpinnerWrapper' ).show()
            .find( '.pax' + context + 'SearchSpinner' ).spin();
    },

    handleFetchManagersSearchEnd: function() {
       // console.log( 'handleFetchManagersSearchEnd', this.context );
        this.$el.find( '.paxManSearchSpinner' ).spin( false )
            .closest( '.paxManSearchSpinnerWrapper' ).hide();
        this.activateContextSection( 'managers' );
    },

    updateManagersSearchTable: function() {

        var mstModel = this.containerView.contestModel.managersSearchTableModel,
            $sc = this.$el.find( '.managerSearchTableWrapper thead .sortControl' );

        $sc.each( function() {
            var $t = $( this ),
                s = $t.data( 'sort' );
            // clear styles
            $t.closest( '.sortHeader' ).removeClass( 'sorted' );
            $t.removeClass( 'asc desc' );
            // add styles to apropo
            if ( s === mstModel.get( 'by' ) ) {
                $t.closest( '.sortHeader' ).addClass( 'sorted' );
                $t.addClass( mstModel.get( 'asc' ) ? 'asc' : 'desc' );
            }
        } );

    },

    doManSearchTableSort: function( e ) {
        var $tar = $( e.target ).closest( '.sortControl' ),
            by = $tar.data( 'sort' ),
            stm = this.containerView.contestModel;

        e.preventDefault();
        stm.managersSearchTableSort( by );
    },

    spinManSearchAddCont: function( manId, isStop ) {
        var $td = this.getManSearchSelContById( manId );

        $td.spin( typeof isStop === 'undefined' || isStop );
    },

    spinManSearchAddAllCont: function( isStop ) {
        var $aa = this.$el.find( '.managerSearchTableWrapper .selectAllBtn' );

        $aa.spin( typeof isStop === 'undefined' || isStop );
    },

    // triggered in select managers add all
    doManSearchSelect: function( e ) {
        var $tar = $( e.target ),
            paxId = $tar.data( 'participantId' ),
            cm = this.containerView.contestModel,
            paxIds;

        e.preventDefault();

        if ( paxId ) { // if single selected
            this.containerView.contestModel.ajaxAddManager( paxId );
        } else { // select all
            paxIds = cm.managersSearchResults.pluck( 'id' );
            this.containerView.contestModel.ajaxAddManager( paxIds );
        }
    },

    // triggered by add manager in table
    handleAjaxManagerStart: function( params ) {
        //console.log( 'handleAjaxManagerStart', this.context );
        this.manPaginatedView.spinLock();

        if ( params.id ) { // when is this ever the case?
            this.spinManSearchAddCont( params.id );
       } else if ( params.paxIds ) { // changed params.ids to params.paxIds
            this.spinManSearchAddAllCont();
       }
    },

    // triggered at end of add manager
    handleAjaxManagerEnd: function( params ) {
        //console.log( 'handleAjaxManagerEnd', this.context );
        this.manPaginatedView.spinLock( false );

        if ( params.id ) {
            this.spinManSearchAddCont( params.id, false );
       } else if ( params.ids ) {
            this.spinManSearchAddAllCont( false );
       }
    },

    // triggered on remove manager
    handleAjaxRemoveManager: function( data, origParams ) {
        //console.log( 'handleAjaxRemoveManager', this.context );
        this.manPaginatedView.fetchParticipants();
        this.setCheckStateOnManagerSearchResult( origParams.paxIds[ 0 ], false );
    },

    // triggered on add manager
    handleAjaxAddManager: function( data, origParams ) {
        //console.log( 'handleAjaxAddManager:', this.context );
        // this was a single add
        if ( origParams.paxIds.length === 1 ) {
            this.setCheckStateOnManagerSearchResult( origParams.paxIds[ 0 ], true );
        } else { // this was an "add all"
            this.setCheckStateOnManagerSearchResult( null, true );
        }

        // NOTE: what happens on add is not clear since the selected managers are paginated
        //       prepare for repeated discussions during QA about what to do in this
        //       situation

        // UPDATE the paginated table
        this.manPaginatedView.fetchParticipants();
    },

    //Set a/all manager search result as checked or unchecked
    setCheckStateOnManagerSearchResult: function( id, checked ) {
        var msr = this.contMod.managersSearchResults,
            $td = id || id === 0 ?
            this.getManSearchSelContById( id ) : // single
            this.$el.find( '.managerSearchTableWrapper .manSearchSelCell' ); // all

        $td.find( '.participantSelectControl' )[ checked ? 'hide' : 'show' ]();
        $td.find( '.deselTxt' )[ checked ? 'show' : 'hide' ]();

        // some model stuff, pretty dirty
        // so if the table is resorted ( re rendered ), it knows about the last check state
        // not putting it in BB Model attributes
        if ( id || id === 0 ) {
            msr.get( id )._isChecked = checked;
        } else {
            msr.each( function( man ) {
                man._isChecked = checked;
            } );
        }
    },

    getManSearchSelContById: function( manId ) {
        return $( '.managerSearchTableWrapper #manSearchSelCell' + manId );
    },

/***
 *    888             888
 *    888             888
 *    888             888
 *    888888  8888b.  88888b.  .d8888b
 *    888        "88b 888 "88b 88K
 *    888    .d888888 888  888 "Y8888b.
 *    Y88b.  888  888 888 d88P      X88
 *     "Y888 "Y888888 88888P"   88888P'
 *
 *
 *
 */
    /* **************************************************
        TAB FUNCTIONS - ContestEditTab*View interface
    ***************************************************** */
    // sync the visual elements with the model only called before step 2 render
    updateTab: function() {
        var ppvParams = {
                contestId: this.containerView.contestModel.get( 'id' ),
                ssiContestClientState: this.containerView.contestModel.get( 'ssiContestClientState' )
            };

        // call to load participants and managers ( if any )
        this.cm.fetchTeamSearchFilters();

        // manual call initial load of paxes
        this.eachPaginated( function ( pView ) {
            pView.setFetchParams( ppvParams );
        } );
        this.paxPaginatedView.setFetchParams( ppvParams );
        if ( this.cm.get( 'contestType' ) !== 'awardThemNow' ) {
            this.manPaginatedView.setFetchParams( ppvParams );
        }
        this.paxPaginatedView.fetchParticipants();
        // Set the name in UI
        this.$el.find( '.defaultName' ).text( this.cm.getDefaultTranslationByName( 'names' ).text );

        this.updateSectionCounts(); // may need to call this when returning to this tab after its been loaded once
        this.containerView.updateBottomControls();

    },

    // SSIContestPageEditView will use this on goFromStepToStep() instead of SSIContestModel.save()
    // we don't save anything and just trigger the save, so the parent view can take over
    // we do this because the data for this step is progressively saved, and we don't require validation at this juncture
    save: function( fromStep, toStep ) {
        // no save, everything is updated for this page
        // trigger handleSave on parentview
        // handleSaveSuccess: function( serverData, fromStep, toStep, isDraft )
        this.trigger( 'saveSuccess', null, fromStep, toStep, false );
    },

    saveAsDraft: function() {
        this.trigger( 'saveSuccess', null, null, null, true );
    },

    // validate the state of elements within this tab
    validate: function() {
        return { valid: true };
    },

    validateForm: function( form ) {
        var $form = form,
        $valTips;

        if ( !G5.util.formValidate( $form.find( '.validateme' ) ) ) {
            $valTips = $form.find( '.validate-tooltip:visible' );
            // if val tips, then we have errors
            if ( $valTips.length ) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

} );
