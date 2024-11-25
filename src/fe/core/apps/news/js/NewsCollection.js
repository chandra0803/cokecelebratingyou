/*exported NewsStoryCollection */
/*global
NewsStoryModel,
NewsStoryCollection:true
*/
NewsStoryCollection = Backbone.Collection.extend( {
    model: NewsStoryModel,
    sort_key: 'sortDate', // default sort key was sortDate

    initialize: function() {
        this._dataLoaded = false;
    },

    // comparator: function(item) {
    //  return item.get(this.sort_key);
    // },

    //COMMENTING OUT AS BACKEND SHOULD BE SENDING IN CORRECT ORDER

    comparator: function( a, b ) {
       var comp = a.get( this.sort_key ) > b.get( this.sort_key ) ? 1 : -1;
       var j = ( this.sort_key === 'sortDate' ) ? -comp : comp;
    
       return j;
    },

    sortByName: function() {
        this.sort_key = 'storyName';
        this.sort();
        return( this );
    },

    sortBySortDate: function() {
        this.sort_key = 'sortDate';
        this.sort();
        return( this );
    },

    sortedBy: function() {
        return this.sort_key;
    },

    loadStories: function( props ) {
        var that = this;
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NEWS,
            data: props || {},
            success: function( servResp ) {
                that._dataLoaded = true;
                that.reset( servResp.data.news );
                that.trigger( 'dataLoaded' );
            }
        } );
    }

} );
