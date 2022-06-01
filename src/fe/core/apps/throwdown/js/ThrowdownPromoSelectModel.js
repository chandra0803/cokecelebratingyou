/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported ThrowdownPromoSelectModel*/
/*global
Backbone,
ThrowdownPromoSelectModel:true
*/
ThrowdownPromoSelectModel = Backbone.Model.extend( {
    defaults: {

    },

    initialize: function() {
        this.set( 'id', this.get( 'promoId' ) );
    }

} );
