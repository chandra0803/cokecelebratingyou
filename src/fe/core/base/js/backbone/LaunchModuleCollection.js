/*exported LaunchModuleCollection */
/*global
LaunchModule,
LaunchModuleCollection:true
*/

/*
	This is the container (backbone ModuleCollection) model for Module models.
*/
LaunchModuleCollection = Backbone.Collection.extend( {
	model: LaunchModule,

	initialize: function( models, options ) {
		this._filter = G5.props.CURRENT_ROUTE.length && G5.props.CURRENT_ROUTE[ 0 ] == 'launch' && G5.props.CURRENT_ROUTE[ 1 ] != 'home' ? G5.props.CURRENT_ROUTE[ 1 ] : G5.props.DEFAULT_FILTER; //private variable to hold current filter
		this._allfilters = [];
		this._homefilter = options.userHomeFilter;

		this.on( 'reset add remove', this.buildFilters, this );
	},

	buildFilters: function() {
		var filters = [];

		_.each( this.pluck( 'filters' ), function( v ) {
			filters.push( _.keys( v ) );
		} );

		this._allfilters = _.uniq( _.flatten( filters ) );
	},

	setFilter: function( filter ) {

		//do nothing if this filter is set already
		if( this._filter === filter ) {return;}

		//also do nothing if this filter is not part of the allfilters array
		if( _.indexOf( this._allfilters, filter ) < 0 ) {return;}

		var oldfilter = this._filter;
		this._filter = filter;

		//generate an event
		this.trigger( 'filterChanged', filter, oldfilter );
	},

	getFilter: function() {
		return this._filter;
	},

	getHomeFilter: function() {
		return this._homefilter;
	}
} );
