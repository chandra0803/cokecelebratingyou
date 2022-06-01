/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported NominationsWizardTabsVerticalModel */
/*global
$,
G5,
Backbone,
NominationsWizardTabsVerticalModel:true
*/
NominationsWizardTabsVerticalModel = Backbone.Model.extend( {
    initialize: function ( opts ) {
        this.calcSetup = null;
    },

    loadTabs: function( id ) {
        var data = {},
            self = this;

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NOMINATIONS_TABS_DATA,
            data: data,
            success: function ( serverResp, textStatus, jqXHR ) {
               var tabsJson = serverResp.data.tabsJson;

               self.set( 'tabs', tabsJson );
               self.trigger( 'tabsLoaded', tabsJson );
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( '[ERROR] CommunicationsBannerEditModel: ', jqXHR, textStatus, errorThrown );
            }
        } );
    }
} );
