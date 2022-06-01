/*jslint browser: true, nomen: true, unparam: true*/
/*exported NominationsInprogressListPageModel */
/*global
$,
Backbone,
console,
G5,
NominationsInprogressListPageModel:true
*/
NominationsInprogressListPageModel = Backbone.Model.extend( {
    loadData: function( opts ) {
        var that = this,
            data = {};

            data = $.extend( {}, data, opts );

            $.ajax( {
                dataType: 'g5json',
                type: 'POST',
                url: G5.props.URL_JSON_NOMINATIONS_INPROGRESS_TABLE,
                data: data,
                cache: true,
                success: function ( serverResp ) {
                    //regular .ajax json object response
                    var data = serverResp.data;

                    that.set( data );

                    //notify listener
                    that.trigger( 'loadDataFinished', data );
                },
                error: function( jqXHR, textStatus, errorThrown ) {
                    console.log( '[ERROR] NominationsInprogressListPageModel: ', jqXHR, textStatus, errorThrown );
                }
            } );
    },

    removeNomination: function( params ) {
        var data = {};

        if ( params ) {
            data = params;
        }

        $.ajax( {
            url: G5.props.URL_JSON_NOMINATIONS_INPROGRESS_LIST_REMOVE,
            type: 'POST',
            data: data,
            success: function () {
                console.log( 'Current Nomination is getting deleted' );
            }
        } );
    }
} );
