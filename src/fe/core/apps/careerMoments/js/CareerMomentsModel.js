/*exported CareerMomentsModel*/
/*jslint browser: true, nomen: true, unparam: true*/
/*global
$,
_,
Backbone,
console,
G5,
CareerMomentsModel:true
*/
CareerMomentsModel = Backbone.Model.extend( {
    initialize: function( opts ) {
        if( !this.get( 'isModule' ) ) {
            //expect JSON urls in the options
            this.autocompUrl = opts.autocompUrl;
            this.searchUrl = opts.searchUrl;
        }
    },

    loadRibbonData: function( setId, tabClicked, select, searchName, listValue, cmType ) {
        var that = this;            
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_CAREERMOMENTS_RIBBON_DATA,            
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data;
                that.set( data );
                that.trigger( 'dataLoaded' );
            }
        } );
    },

    loadData: function( setId, tabClicked, select, searchName, listValue, cmType ) {
        var that = this,
            data = {
                'cmId': setId,
                'sortedOn': this.get( 'sortedOn' ),
                'sortedBy': this.get( 'sortedBy' ),
                'currentPage': this.get( 'currentPage' ),
                'cmPastPresentSelect': select ? select : 'upcoming',
                'cmType' : cmType,
                'name': searchName,
                'listValue': listValue
            };
            console.log( listValue );
        

        if( tabClicked ) {
            data = {
                'cmId': setId,
                'cmPastPresentSelect': select,
                'name': searchName,
                'cmType' : cmType,
                'listValue': listValue
            };
        }

        $.ajax( {
            dataType: 'g5html',
            type: 'POST',
            url: G5.props.URL_JSON_CAREERMOMENTS_DATA,
            data: data,
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp;
                var $tableContent = $( data );
                that.set( 'tableContent', data );
                that.trigger( 'renderTableContent', $tableContent );
                //that.trigger( 'dataLoaded', setId, tabClicked );

                ////console.log('[INFO] CareerMomentsModel['+that.get('id')+'] - LOADED');
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
            this.loadData( opts.nameId, null, opts.selectedView, null, opts.cmType );
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

    loadParticipants: function( id, selectedName, selectedView, listVal, cmType ) {
        var that = this,
            data = {
                cmId: id,
                name: selectedName,
                currentPage: this.get( 'currentPage' ),
                sortedOn: this.get( 'sortedOn' ),
                sortedBy: this.get( 'sortedBy' ),
                cmPastPresentSelect: selectedView,
                listValue: listVal,
                cmType : cmType
            };

        this.trigger( 'startBusy:loadParticipants' );

        $.ajax( {
            dataType: 'g5html',
            type: 'POST',
            url: this.searchUrl,
            data: data,
            success: function( serverResp ) {                
                var data = serverResp,
                $tableContent = $( data );
                that.set( 'tableContent', data );
               // that.trigger( 'renderTableContent', $tableContent );
                that.trigger( 'participantsChanged', selectedName, id, $tableContent );
            }
        } );
    },

    getDefaultSet: function() {
        var defSet = _.where( this.get( 'careerMomentsSets' ), { isDefault: true } );

        return defSet.length ? defSet[ 0 ] : null;
    }
} );
