/*exported PaxSearchModel */
/*global
PaxSearchModel:true,
*/

PaxSearchModel = Backbone.Model.extend( {

    initialize: function( opts ) {
        //console.log('PaxSearchModel opts.participantsCollection||new',opts);
        this.recordsCount = 0;
        //this.participants = opts.participantsCollection||new Backbone.Collection();
        this.data = {};
        this.searchUrl = opts.searchUrl;
        this.searchParams = opts.searchParams;
        this.autoCompletionsData = {};
        this.filters = [];
        this.totalRecordsFound = 0;
        this.maxAllowedToRecognize = 0;
        this.searchFilterTypeCounts = null;

        _.bind( this.fetchFilters, this );

    },

    /***
    *         888          888                                    888 888
    *         888          888                                    888 888
    *         888          888                                    888 888
    *     .d88888  8888b.  888888  8888b.        .d8888b  8888b.  888 888 .d8888b
    *    d88" 888     "88b 888        "88b      d88P"        "88b 888 888 88K
    *    888  888 .d888888 888    .d888888      888      .d888888 888 888 "Y8888b.
    *    Y88b 888 888  888 Y88b.  888  888      Y88b.    888  888 888 888      X88
    *     "Y88888 "Y888888  "Y888 "Y888888       "Y8888P "Y888888 888 888  88888P'
    *
    *
    *
    */


    fetchFilters: function() {
        var that = this;
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_GLOBAL_SEARCH_FILTERS,
            success: function( serverResp ) {
                that.filters = serverResp.data.filters;
                that.trigger( 'filtersDone' );
            },
            error: function( jqXHR, textStatus ) {
                console.log( '[INFO] PaxSearch->fetchAutoCompleteResult->error: Request failed: ' + textStatus );
            }
        } );
    },

    fetchRecipientList: function( queryData, lazyLoad, preselected ) {
        var that = this;
        if ( !queryData || !Object.keys( queryData ).length ) {
            // reset len
            this.totalRecordsFound = 0;
            this.trigger( 'setNoQuery' );
            return;
        }
        //console.log( queryData, lazyLoad, preselected, this.searchParams );
        if( !lazyLoad ) {this.trigger( 'startBusy:loadParticipants' );}
        // else lazyload has its own spinner
        //console.log('making call???');
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: this.searchUrl,
            data: _.extend( {}, queryData, this.searchParams ), //,queryData,
            success: function( serverResp ) {
                //console.log('[INFO] PaxSearch->fetchRecipientList->success: ajax call successfully returned this JSON object: ', serverResp);
                if ( typeof serverResp.data.messages !== 'undefined' && serverResp.data.messages.length &&  serverResp.data.messages[ 0 ].type === 'error' ) {
                    //console.log(serverResp.data.messages[0].text);
                    that.trigger( 'setNoResultsFound' );
                    that.trigger( 'endBusy:loadParticipants' );
                } else {
                    if ( serverResp.data.header ) { that.totalRecordsFound = serverResp.data.header.totalRecordsFound; }
                    if ( serverResp.data.searchFilterTypeCounts ) { that.searchFilterTypeCounts = serverResp.data.searchFilterTypeCounts; }
                    if ( serverResp.data.maxAllowedToRecognize ) { that.maxAllowedToRecognize = serverResp.data.maxAllowedToRecognize; }

                    // if we only have a filter search, just update the count
                    if( queryData.filter ) {
                        that.trigger( 'updateCounts'  );
                     // if we have results and participants and not filter search
                    }else if( serverResp.data.participants ) {
                        that.trigger( 'renderRecipientList', { participants: serverResp.data.participants, lazyLoad: lazyLoad, preselected: preselected } );
                    // if we have data no participants
                    } else if ( serverResp.data.searchFilterTypeCounts && serverResp.data.participants === null ) {
                        that.trigger( 'updateCounts' );
                        //that.participants.reset();
                    // no participants
                    } else {
                        that.trigger( 'setNoResultsFound' );
                    }
                    that.trigger( 'endBusy:loadParticipants' );
                }
            },
            error: function( jqXHR, textStatus ) {
                console.error( '[Error] PaxSearch->fetchRecipientList->error: Request failed: ' + textStatus );
                //var errorText = serverResp.data.messages[0].text;
                that.trigger( 'setNoResultsFound', textStatus );
                    that.trigger( 'endBusy:loadParticipants' );
            },
            complete: function() {
                that.trigger( 'endBusy:loadParticipants' );
            },
            fail: function( jqXHR, textStatus ) {
                console.log( '[INFO] PaxSearch->fetchRecipientList->fail: Request failed: ' + textStatus );
            }
        } );
    },

    fetchAutoCompleteResult: function( queryData ) {
        var that = this;
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_GLOBAL_SEARCH_AUTOCOMPLETE,
            data: queryData,
            success: function( serverResp ) {
                //console.log('[INFO] PaxSearch->fetchAutoCompleteResult->success: ", serverResp.data);
                that.autoCompletionsData = serverResp.data.completions;
                //that.renderAutoCompleteList(queryData.type);

                that.trigger( 'renderAutoCompleteList', { type: queryData.type } );
            },
            error: function( jqXHR, textStatus ) {
                console.log( '[INFO] PaxSearch->fetchAutoCompleteResult->error: Request failed: ' + textStatus );
            }
        } );
    }

    /*getParticipants: function(){
        return this.participants.toJSON();
    },
    getParticipantById:function(id){
        return this.participants.get(id);
    }*/

} );
