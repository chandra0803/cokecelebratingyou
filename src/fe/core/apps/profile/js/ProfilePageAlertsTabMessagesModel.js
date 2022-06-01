/*exported ProfilePageAlertsTabMessagesModel */
/*global
Backbone,
ProfilePageAlertsTabMessagesModel:true
*/
ProfilePageAlertsTabMessagesModel = Backbone.Model.extend( {
    defaults: {
        sortDate: 'Message ID not set',
        messageDate: 'Message Date not set',
        messageTitle: 'Message Title not set',
        messageText: 'Message Text not set',
        messageLinkUrl: 'Message Link URL not set'
    },
    idAttribute: 'messageId',
    initialize: function () {
        'use strict';
    }
} );
