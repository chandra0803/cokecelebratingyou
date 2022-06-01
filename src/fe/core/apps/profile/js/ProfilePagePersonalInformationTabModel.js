/*exported ProfilePagePersonalInformationTabModel */
/*global
Backbone,
ProfilePagePersonalInformationTabModel:true
*/
ProfilePagePersonalInformationTabModel = Backbone.Model.extend( {
    defaults: {
        questionList: [ { question: 'No Question has been set', answer: 'No Answer has been set' } ]
    },

    initialize: function () {
        'use strict';
    }
} );
