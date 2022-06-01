/*jslint browser: true, nomen: true, unparam: true*/
/*exported NominationsListPageView, NominationWinnersListPageModel */
/*global
$,
_,
Backbone,
console,
NominationsListPageView,
G5,
NominationWinnersListPageModel:true
*/
NominationWinnersListPageModel = Backbone.Model.extend( {
	initialize: function( opts ) {
		console.log( 'Nominations Winners List Page Model initialized' );

        if( !this.get( 'isModule' ) ) {
            //expect JSON urls in the options
            this.autocompUrl = opts.autocompUrl;
            this.searchUrl = opts.searchUrl;
        }
    },

    loadData: function( opts ) {
        var self = this,
            data = $.extend( {}, data, opts );

            $.ajax( {
                dataType: 'g5json',
                type: 'POST',
                url: G5.props.URL_JSON_NOMINATIONS_LIST,
                data: data,
                cache: true,
                success: function ( serverResp ) {
                    //regular .ajax json object response
                    var data = serverResp.data;

                    self.set( data );

                    //notify listener
                    self.trigger( 'loadDataFinished', data );
                },
                error: function( jqXHR, textStatus, errorThrown ) {
                    console.log( '[ERROR] NominationsInprogressListPageModel: ', jqXHR, textStatus, errorThrown );
                }
            } );
    },

	displayComments: function( opts ) {
		var self = this,
            data = $.extend( {}, data, opts );
            // console.log(data.toJSON());
            console.log( opts );

        $.ajax( {
            type: 'POST',
            dataType: 'g5json',
            url: G5.props.URL_JSON_NOMINATIONS_WINNERS_LIST,
            data: opts,
            success: function ( serverResp ) {
				var comments = serverResp.data;
                console.log( comments );
                if( comments.messages.length > 0 && comments.messages[ 0 ].type == 'error' ) {
                    self.clear( [ { silent: true } ] );
                    self.set( comments );
                    //notify listener
                    self.trigger( 'loadCommentsNoResults', comments );
                } else {
                    self.clear( [ { silent: true } ] );
                    self.set( comments );
                    //notify listener
                    self.trigger( 'loadCommentsFinished', comments );
                }
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( '[ERROR] NominationsInprogressListPageModel Winners List: ', jqXHR, textStatus, errorThrown );
            }
        } );
    },

	queryAutocomplete: function( query ) {
        var that = this;

        this.trigger( 'startBusy:queryAutocomplete' );
        console.log( query );
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: this.autocompUrl,
            data: _.extend( {
                query: query.query,
                type: query.type
            } ),
            success: function( serverResp ) {
                //regular .ajax json object response
                // console.log(serverResp);
                var data = serverResp.data,
                    completions,
                    msg = serverResp.getFirstError();

                //extract the text of the error if there is one
                if( msg ) {msg = msg.text;}

                //sorting by name, then id. Not sure if this is the best order, but it works well.
                completions = _.sortBy( data.completions, function( r ) { return [ r.name, r.id ]; } );

                console.log( '[INFO] ParticipantSearchModel - ' + completions.length + ' autocomplete results received' );

                that.trigger( 'autocompleted', completions, msg );
                that.trigger( 'endBusy:queryAutocomplete' );
            }
        } );
    },

	loadWinners: function( selectedName ) {
        var that = this,
            data = {
                name: selectedName
            };
            console.log( data );
        this.trigger( 'startBusy:loadParticipants' );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: this.searchUrl,
            data: data,
			success: function ( serverResp ) {
				var comments = serverResp.data,
					completions,
					msg = serverResp.getFirstError();
                that.clear( [ { silent: true } ] );
                that.set( comments );
                //notify listener

			    //extract the text of the error if there is one
                if( msg ) {msg = msg.text;}

				 //sorting by name, then id. Not sure if this is the best order, but it works well.
                completions = _.sortBy( data.completions, function( r ) { return [ r.name, r.id ]; } );

				that.trigger( 'autocompleted', completions, msg );

                that.trigger( 'loadCommentsFinished', comments );
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( '[ERROR] NominationsInprogressListPageModel Winners List: ', jqXHR, textStatus, errorThrown );
            }
        } );
    }
} );
