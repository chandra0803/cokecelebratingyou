/* exported SSIParticipantModuleView */
/*global
_,
G5,
SSIModuleView,
SSIParticipantModuleView:true
*/
SSIParticipantModuleView = SSIModuleView.extend( {

    getDataUrl: function () {
        return G5.props.URL_JSON_SSI_MASTER_PARTICIPANT;
    }
} );
