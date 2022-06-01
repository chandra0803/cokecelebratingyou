/*exported XpromoStoryCollection */
/*global
XpromoStoryModel,
XpromoStoryCollection:true
*/
XpromoStoryCollection = Backbone.Collection.extend( {
    model: XpromoStoryModel,
     sort_key: 'noSorting', // default sort key was sortDate

    initialize: function() {
        this._dataLoaded = false;
    },

    // comparator: function(item) {
    //  return item.get(this.sort_key);
    // },

    //COMMENTING OUT AS BACKEND SHOULD BE SENDING IN CORRECT ORDER

    // comparator: function( a, b ) {
    //     var comp = a.get( this.sort_key ) > b.get( this.sort_key ) ? 1 : -1;
    //     var j = ( this.sort_key === 'sortDate' ) ? -comp : comp;
    //
    //     return j;
    // },

} );
