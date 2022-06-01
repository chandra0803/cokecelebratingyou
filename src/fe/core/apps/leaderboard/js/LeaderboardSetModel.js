/*exported LeaderboardSetModel */
/*global
LeaderboardCollection,
LeaderboardSetModel:true
*/
LeaderboardSetModel = Backbone.Model.extend( {
    defaults: {

    },

    initialize: function( ) {
        //console.log("[INFO] LeaderboardSetModel: Leaderboard set initialized", this, opts);
        var that = this,
            leaderboards = this.get( 'leaderboards' );

        this.id = this.get( 'nameId' );
        this.leaderboards = new LeaderboardCollection();

        this.leaderboards.setModel = this;

        _.each( leaderboards, function( board ) {
            board.setId = that.id;
        } );

        // using the add method so I can trigger the add event inside the LeaderboardCollection
        this.leaderboards.add( leaderboards );
    }

} );