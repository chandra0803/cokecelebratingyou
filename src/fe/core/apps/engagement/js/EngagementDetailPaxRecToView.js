/*exported EngagementDetailPaxRecToView */
/*global
EngagementDetailPaxView,
EngagementDetailPaxRecToView:true
*/
EngagementDetailPaxRecToView = EngagementDetailPaxView.extend( {
    initialize: function() {

        //this is how we call the super-class initialize function (inherit its magic)
        EngagementDetailPaxView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass EngagementDetailPaxView
        this.events = _.extend( {}, EngagementDetailPaxView.prototype.events, this.events );

    }
} );