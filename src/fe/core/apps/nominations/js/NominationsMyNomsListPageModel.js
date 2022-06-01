/*jslint browser: true, nomen: true, unparam: true*/
/*exported NominationsMyNomsListPageModel */
/*global
$,
_,
Backbone,
console,
G5,
NominationsMyNomsListPageModel:true
*/
NominationsMyNomsListPageModel = Backbone.Model.extend( {

    loadData: function( opts ) {
        var that = this,
            data = {};

            data = $.extend( {}, data, opts );

            $.ajax( {
                dataType: 'g5json',
                type: 'POST',
                url: G5.props.URL_JSON_NOMINATIONS_MY_LIST_DATA,
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
                    console.log( '[ERROR] NominationsApprovalPromoListPageModel: ', jqXHR, textStatus, errorThrown );
                }
            } );
    }
} );
