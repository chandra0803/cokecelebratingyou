/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported ThrowdownMatchModel*/
/*global
Backbone,
Handlebars,
ThrowdownMatchModel:true
*/
ThrowdownMatchModel = Backbone.Model.extend( {
    defaults: {

    },

    initialize: function() {

    }

} );
Handlebars.registerHelper( 'comma', function( commaFormat ) {
      return commaFormat.toString().replace( /\B(?=(\d{3})+(?!\d))/g, ',' );
} );

Handlebars.registerHelper( 'displayCount', function( earnCount ) {
      if( earnCount > 1 ) {
		  return earnCount;
	  }else {
		  return;
	  }
} );
