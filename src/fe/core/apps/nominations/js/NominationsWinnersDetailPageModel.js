/*jslint browser: true, nomen: true, unparam: true*/
/*exported NominationsWinnersDetailPageView, NominationsWinnersDetailPageModel */
/*global
$,
_,
Backbone,
console,
NominationsWinnersDetailPageView,
G5,
NominationsWinnersDetailPageModel:true
*/
NominationsWinnersDetailPageModel = Backbone.Model.extend( {
	initialize: function() {
		console.log( 'Nominations Winners Detail Page Model initialized' );
    },

	translateData: function( claimId, $tar ) {
        var that = this,
            data = {},
            urlTranslate = G5.props.URL_JSON_NOMINATIONS_WINNERS_DETAIL_TRANSLATE_COMMENT  + '&claimId=' + claimId;

            console.log( urlTranslate );

        $.ajax( {
            dataType: 'g5json', //must set this so SeverResponse can parse and return to success()
            data: data,
            url: urlTranslate,
            type: 'POST',
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data;
                    // comment = data.comment;
					data.target = $tar;
                if( serverResp.getFirstError() ) {return;}//ERROR just return for now

                that.trigger( 'translated', data );
                console.log( '[INFO] NominationsWinnerDetailPageModel - Comment Translated' );
            }
        } );
    },

    loadData: function( opts ) {
        var self = this,
            data = {};

            data = $.extend( {}, data, opts );

			$.ajax( {
                dataType: 'g5json',
                type: 'POST',
                url: G5.props.URL_JSON_NOMINATIONS_WINNERS_DETAIL,
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
                    console.log( '[ERROR] NominationsWinnerDetailPageModel: ', jqXHR, textStatus, errorThrown );
                }
            } );
    }
} );
