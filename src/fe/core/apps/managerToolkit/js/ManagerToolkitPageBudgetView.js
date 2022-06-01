/*exported ManagerToolkitPageBudgetView */
/*global
_,
PageView,
ParticipantCollectionView,
PaxSelectedPaxCollection,
PaxSearchStartView,
TemplateManager,
ManagerToolkitPageBudgetView:true
*/
ManagerToolkitPageBudgetView = PageView.extend( {

    //override super-class initialize function
    initialize: function( opts ) {

        console.log( '[INFO] ManagerToolkitPageBudgetView: Manager Toolkit Page Budget View initialized', this );

        var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'managerToolkit';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        this.checkForServerErrors();

        this.$selectionForm = this.$el.find( '#promotionSelect' );
        // added for budgetTransfer
        // get the form that holds the
        this.$paxForm = this.$el.find( '#managerToolkitFormAllocatePoints' );

        // CUSTOM ENDS HERE
        this.$cancelButton = this.$el.find( '.previewCancelBtn' );

        this.createCancelTip( this.$cancelButton );
        // added for budgetTransfer
        // add selected pax data
        this.paxCollection = new PaxSelectedPaxCollection();
        // instantiate the elastic search start view`
        this.participantSearchView = new PaxSearchStartView( {
            el: this.$el.find( '.paxSearchStartView' ),
            multiSelect: true,
            recognition: false,
            selectedPaxCollection: this.paxCollection,
            dontRenderSelf: false,
            addSelectedPaxView: true
        } );

        var getBudgetParams = function ()  {
            // get the values of the selects from the top of the page
            var params = {
                ownerBudgetNodeId : that.$el.find( "#ownerBudgetNodeId" ).val(),
                budgetSegmentId : that.$el.find( "#budgetSegmentId" ).val(),
                budgetMasterId : that.$el.find( "#budgetMasterId" ).val()
            };
            // that.participantSearchView.searchParams is the form data that gets sent when the user is typing
            // _.extend merges the data from the params object with the collection data
            that.participantSearchView.searchParams = _.extend( that.participantSearchView.searchParams, params );
        }
        // extending the events hash for PaxSearchStartView
        // so it includes the event triggered when the search input is focused
        // that calls getBudgetParams and adds the data from the selects at the top of the page
        // to the form and posts all of the form data before refreshing the page.
        this.participantSearchView.events = _.extend( this.participantSearchView.events, {

            'focus .paxSearchInput': getBudgetParams

        } );
        // call delegateEvents to bind the focus event that calls getBudgetParams to the search view
        this.participantSearchView.delegateEvents();
        // when the elastic search is closed, add the value for each new model's id key
        // to the hidden inputs in #paxSearchReturnedResultsForm and post the form data
        // so the back end can re-render the table with the new pax data added
        G5._globalEvents.on( 'paxSearch:exitSearch', function () {

            that.addSearchResultsParticipantsToBudgetTransferTable();

        } );
        // CUSTOM ENDS HERE
    },

    events: {
        'change #budgetMasterId': 'submitSelectionForm',
        'change #budgetSegmentId': 'submitSelectionForm',
        'change #ownerBudgetNodeId': 'submitSelectionForm',
        // scrub/autopopulate/validate date & numeric input
        'blur .reallocationAmount input': 'scrubOnBlur',
        'click .reallocationAmount input': 'selectAll',
        'click .form-actions button': 'formActions',
        'click #recognitionButtonChangePromoConfirm': 'returnHome',
        'click #recognitionButtonChangePromoCancel': 'hideQtip'
    }, // events

    submitSelectionForm: function( e ) {
        e.preventDefault();

        G5.util.showSpin( this.$el, { cover: true } );

        // find every <select> after the one that triggered this event and erase the value
        $( e.target ).closest( '.control-group' ).nextAll( '.control-group' ).find( 'select' ).val( '' );

        this.$selectionForm.submit();
    },
    // handles adding the selected pax data to the form
    // and posting the form data before refreshing the page
    addSearchResultsParticipantsToBudgetTransferTable: function () {

        var that = this;
        // get the models from the collection
        this.selectedPaxCollectionModels = this.participantSearchView.selectedPaxCollection.models;
        // remove any existing inputs to avoid duplicates
        this.$el.find( '.budgetTransferDataInput' ).remove();
        // get the userIds from each model and add them in a corresponding new hidden input to the form
        $.each( this.selectedPaxCollectionModels, function ( i ) {

            var userId = this.get( 'id' );

            that.$paxForm.append( '<input type="hidden" class="budgetTransferDataInput" name="participants[' + i + '].id" value="' + userId + '"/>' );

        } );
        // get the values from each select at the top of the page and add them in a corresponding new hidden input to the form
        $.each( this.participantSearchView.searchParams, function ( i, selectValue ) {

            that.$paxForm.append( '<input type="hidden" class="budgetTransferDataInputSelectValue" name=" ' + i + '" value="' + selectValue + '"/>' );

        } );

        var form = this.$paxForm,
            formData = new FormData( form[ 0 ] );
        // post all of the form data and the selected pax id's from the elastic search results
        // and refresh the page when the ajax response comes back
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            url: G5.props.URL_JSON_PARTICIPANT_SEARCH_RESULTS_BUDGET_TRANSFER,
            success: function () {
                // console.log( '[INFO] PaxSearch->submitSearchResult->success: Request received: ' );
                location.reload( true );
            },
            error: function( jqXHR, textStatus ) {
                console.log( '[INFO] PaxSearch->submitSearchResult->error: Request failed: ' + textStatus );
            }
        } );

    },
    // CUSTOM ENDS HERE

    formActions: function( e ) {
        var that = this;
        var $form = $( e.target ).closest( 'form' );
        var $clickedBtn = $( e.target ).closest( 'button' );
        var formaction = $clickedBtn.attr( 'formaction' );

        if ( formaction !== null )
        {
            $form.attr( 'action', formaction );
        }

        $form.data( 'trigger', $( e.target ) );

        setTimeout( function() {that.disableFormOptions();}, 1 );
    },

    returnHome: function( e ) {
        window.location.assign( $( e.target ).data( 'url' ) );
    },

    hideQtip: function( event ) {
        $( event.target ).closest( '.qtip' ).qtip( 'hide' );
    },

    createCancelTip: function( $trigger ) {
        var $cancelTip = $( '#cancelConfirmDialog' );

        $trigger.qtip( {
            content: $cancelTip,
            position: {
                my: 'left center',
                at: 'right center',
                container: this.$el
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
                classes: 'ui-tooltip-shadow ui-tooltip-light participantPopoverQtip',
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            }
        } );
    },

    scrubOnBlur: function( event ) {
        'use strict';
        var valueAsArray = $( event.currentTarget ).val().toString().split( '' ),
            value = $( event.currentTarget ).val(),
            format = $( event.currentTarget ).data( 'numberType' ),
            valueAsString,
            numericValue;

        if ( ( value === '' ) || ( value === '.' ) || ( ( parseInt( value, 10 ) === 0 ) && ( format === 'whole' ) ) || ( ( parseFloat( value ) === 0 ) && ( format === 'decimal' ) ) ) {
            $( event.currentTarget ).val( 0 );
        } else {
            // if last character is a '.' then remove it
            if ( _.indexOf( valueAsArray, '.' ) === valueAsArray.length - 1 ) {
                valueAsArray.pop();
                valueAsString = valueAsArray.join( '' );
                if ( format === 'whole' ) {
                    numericValue = parseInt( valueAsString, 10 );
                } else {
                    numericValue = parseFloat( valueAsString );
                }
                $( event.currentTarget ).val( numericValue );
            } // if
        } // else


    }, // fillWithZero

    selectAll: function( event ) {
        'use strict';

        $( event.currentTarget ).select();

    }, // selectAll

    checkForServerErrors: function() {
        if ( $( '#serverReturnedErrored' ).val() === 'true' ) {
            $( '#managerBudgetErrorBlock' ).slideDown( 'fast' ); //show error block
        }
    }, //checkForServerErrors
    disableFormOptions: function( e ) {
        //disable all buttons
        $( '.form-actions .btn' ).attr( 'disabled', 'disabled' );
    }

} );