/*exported LaunchModule */
/*global
LaunchModule:true
*/
LaunchModule = Backbone.Model.extend( {

    defaults: {

        //module name
        // - we find templates from this name, specify the 'templateName' prop to override
        // - we find view obj from this name, specify the 'viewName' prop to override
        name: 'blankModule',

        //if module name is diff. from template name, set this property
        templateName: false,

        //if module name is diff. from view name, set this property
        viewName: false,

        //default filter to size mapping
        filters: {}
    },

    //return the order for this module in this filter/context
    getOrder: function() {
        // there is no default filter so if a filter is not found nothing will show
        if( this.get( 'filters' )[ this.collection.getFilter() ] ) {
            var order = ( this.get( 'filters' )[ this.collection.getFilter() ] || this.get( 'filters' )[ 'default' ] ).order;
            if( order === 'hidden' ) {
                return 'hidden';
            } else {
                return parseInt( order, 10 );
            }
        }else{
            console.log( 'Filter does not exist - could not read getOrder of : ', this.collection.getFilter() );
            console.log( 'choices are :', this.get( 'filters' ) );

            return 'hidden';
        }
    },

    setOrder: function( order ) {

        //dig into the filters array and set order for current filter
        this.get( 'filters' )[ this.collection.getFilter() ].order = order;

        //fake a backbone event for order change
        this._pending.order = true;
        this.change( { changes: { order: true } } );

    },

    //is hidden, this is when the order='hidden' for this module
    isHiddenForCurrentFilter: function() {
        return this.getOrder() === 'hidden';
    }

} );
