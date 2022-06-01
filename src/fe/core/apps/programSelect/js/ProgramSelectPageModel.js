/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported ProgramSelectPageModel*/
/*global
Backbone,
ProgramSelectPageModel:true
*/
ProgramSelectPageModel = Backbone.Model.extend( {

    initialize: function() {

    },

    loadPrograms: function( opts ) {
        var data = {},
            self = this;

        data = $.extend( {}, data, opts );

        this.trigger( 'saveStarted' );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_PROGRAM_SELECT_DATA,
            data: data,
            success: function ( serverResp, textStatus, jqXHR ) {
               var programs = serverResp.data.programs;
               self.set( 'programs', programs );
               self.set( 'nodes', serverResp.data.nodes );
			   self.set( 'raEnable', serverResp.data.raEnable );
               self.trigger( 'approvalDataLoaded', programs );
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( '[ERROR] nominationsApprovalPageModel: ', jqXHR, textStatus, errorThrown );
            },
            complete: function( jqXHR, textStatus ) {
                // self.trigger('saveEnded');
            }
        } );
    }

} );
