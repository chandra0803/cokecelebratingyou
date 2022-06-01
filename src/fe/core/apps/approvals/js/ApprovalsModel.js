/*exported ApprovalsModel */
/*global
ApprovalsModel:true
*/
ApprovalsModel = Backbone.Model.extend( {
	defaults: {

	},

	initialize: function() {
		console.log( '[INFO] ApprovalsModel: Approval Model initialized', this, this.collection );
	}

} );