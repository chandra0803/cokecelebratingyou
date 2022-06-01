/*exported PurlCelebrateModel*/
/*jslint browser: true, nomen: true, unparam: true*/
/*global
$,
_,
Backbone,
console,
G5,
PurlCelebrateModel:true
*/
PurlCelebrateModel = Backbone.Model.extend( {
    initialize: function( opts ) {

        if( !this.get( 'isModule' ) ) {
            //expect JSON urls in the options
            this.autocompUrl = opts.autocompUrl;
            this.searchUrl = opts.searchUrl;
        }
    },

    loadData: function( setId, tabClicked, select, searchName, listValue ) {
        var that = this,
            data = {
                'celebrationId': setId,
                'sortedOn': this.get( 'sortedOn' ),
                'sortedBy': this.get( 'sortedBy' ),
                'currentPage': this.get( 'currentPage' ),
                'purlPastPresentSelect': select,
                'name': searchName,
                'listValue': listValue
            };
            console.log( listValue );
        //if we are on search tab no need to reload the data since it comes from a separate url
        // if(setId === 'search'){
        //     that.trigger('dataLoaded', setId);
        //     return;
        // }

        if( tabClicked ) {
            data = {
                'celebrationId': setId,
                'purlPastPresentSelect': select,
                'name': searchName,
                'listValue': listValue
            };
        }

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_PURL_CELEBRATE_DATA,
            data: data,
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data;
                that.set( data );
                that.trigger( 'dataLoaded', setId, tabClicked );

                ////console.log('[INFO] PurlCelebrateModel['+that.get('id')+'] - LOADED');
            }
        } );
    },

    update: function( opts ) {
         if( opts.type === 'getPage' ) {
            this.set( 'currentPage', opts.data.pageNumber );
         }
         if( opts.type === 'tabular' ) {
            this.set( 'sortedOn', opts.data.sortedOn );
            this.set( 'sortedBy', opts.data.sortedBy );
            this.set( 'currentPage', opts.data.pageNumber );
         }
         //if this is coming from the search tab load participant data otherwise load tab data
         if( opts.selectedName ) {
            this.loadParticipants( opts.nameId, opts.selectedName, opts.selectedView );
         } else {
            this.loadData( opts.nameId, null, opts.selectedView );
         }
    },

    queryAutocomplete: function( query ) {
        var that = this;

        this.trigger( 'startBusy:queryAutocomplete' );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: this.autocompUrl,
            data: _.extend( {
                query: query,
                type: 'lastName'
            } ),
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data,
                    completions,
                    msg = serverResp.getFirstError();

                //extract the text of the error if there is one
                if( msg ) {msg = msg.text;}

                //sorting by name, then id. Not sure if this is the best order, but it works well.
                completions = _.sortBy( data.completions, function( r ) { return [ r.name, r.id ]; } );

                //console.log('[INFO] ParticipantSearchModel - '+completions.length+' autocomplete results received');

                that.trigger( 'autocompleted', completions, msg );
                that.trigger( 'endBusy:queryAutocomplete' );
            }
        } );
    },

    loadParticipants: function( id, selectedName, selectedView, listVal ) {
        var that = this,
            data = {
                celebrationId: id,
                name: selectedName,
                currentPage: this.get( 'currentPage' ),
                sortedOn: this.get( 'sortedOn' ),
                sortedBy: this.get( 'sortedBy' ),
                purlPastPresentSelect: selectedView,
                listValue: listVal
            };

        this.trigger( 'startBusy:loadParticipants' );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: this.searchUrl,
            data: data,
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data,
                    //celebrations = data.celebrations,
                    msg = serverResp.getFirstError();

                //extract the text of the error if there is one
                if( msg ) {msg = msg.text;}

                //console.log('[INFO] PurlCelebrateModel - ', id, data);
                that.set( data );
               //that.trigger( 'dataLoaded', setId, tabClicked );
                that.trigger( 'participantsChanged', selectedName, id );
            }
        } );
    },

    getDefaultSet: function() {
        var defSet = _.where( this.get( 'celebrationSets' ), { isDefault: true } );

        return defSet.length ? defSet[ 0 ] : null;
    }
} );
