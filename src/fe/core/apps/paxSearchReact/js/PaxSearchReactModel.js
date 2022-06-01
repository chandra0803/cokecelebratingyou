/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported PaxSearchReactModel */
/*global
console,
Backbone,
PaxSearchReactModel:true
*/
PaxSearchReactModel = Backbone.Model.extend( {

	initialize: function() {
		console.log( '[INFO] PaxSearchReactModel: PaxSearchReact Model initialized', this, this.collection );
	},

	fetchRecipientList: function( value ) {
		this.dataFetched = false;
        if( !value ) {
            return;
        }

        var self          = this,
            params        = { lastName: value },
            requestedData = null;

        requestedData = $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_EZ_RECOGNITION_RECIPIENT_LIST,
            data: params,
            success: function( serverResp ) {
                console.log( '[INFO] ParticipantSearch: requestRecipientList ajax call successfully returned this JSON object: ', serverResp.data );

                console.log( 'serverResp.data.participants', serverResp.data.participants );
                self.recipientList = _.sortBy( serverResp.data.participants, function( r ) { return [ r.lastName, r.firstName ]; } );
                self.recipientList = _.map( self.recipientList, function( object, key ) {
                    return _.extend( object, { index: key } );
                } );
                this.dataFetched = true;

                //self.renderRecipientList();
            },
            complete: function() {
                //self.recipientSearchSpinner("stop");
            }
        } );

        requestedData.fail( function( jqXHR, textStatus ) {
        	this.dataFetched = false;
            console.log( '[INFO] loadActivityFeed: loadActivityFeed Request failed: ' + textStatus );
        } );
    },


} );
