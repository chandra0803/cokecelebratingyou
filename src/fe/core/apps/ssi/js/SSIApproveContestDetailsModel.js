/*exported SSIApproveContestDetailsModel */
/*global
_,
$,
G5,
Backbone,
SSIApproveContestDetailsModel:true
*/
SSIApproveContestDetailsModel = Backbone.Model.extend( {
    initialize: function( opts ) {
        this.clientState = opts.clientState;
    },

    loadTableData: function( pageData ) {
        var that = this,
            params = {
                clientState: this.clientState
            };

            params = $.extend( true, {}, params, pageData );

            $.ajax( {
                dataType: 'g5json',
                type: 'POST',
                url: params.url,
                data: params,
                cache: true,
                success: function ( serverResp ) {
                    //regular .ajax json object response
                    var data = serverResp.data;

                    data = $.extend( true, {}, data, params );

                    that.set( data );

                    //notify listener
                    that.trigger( 'loadDataFinished', data );
                },
                error: function( jqXHR, textStatus, errorThrown ) {
                    console.log( '[ERROR] SSIApproveContestDetailsModel: ', jqXHR, textStatus, errorThrown );
                }
            } );
    }
} );
