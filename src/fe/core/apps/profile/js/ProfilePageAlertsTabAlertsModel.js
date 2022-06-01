/*exported ProfilePageAlertsTabAlertModel */
/*global
Backbone,
ProfilePageAlertsTabAlertModel:true
*/
ProfilePageAlertsTabAlertModel = Backbone.Model.extend( {
    defaults: {
        alertSubject: '',
        alertText: '',
        datePostedSort: '',
        datePostedDisplay: '',
        dueDateSort: '',
        dueDateDisplay: '',
        isLink: false,
        alertLinkText: '',
        alertLinkUrl: '',
        isManagerAlert: false,
        openNewWindow: false
    },
    idAttribute: 'alertId',
    initialize: function () {
        'use strict';
    }
} );
