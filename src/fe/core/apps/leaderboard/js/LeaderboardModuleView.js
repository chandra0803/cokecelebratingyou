/*exported LeaderboardModuleView */
/*global
LaunchModuleView,
LeaderboardSetCollection,
LeaderboardModelView,
LeaderboardModuleView:true
*/
LeaderboardModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function() {
        var that = this;
        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass ModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        this._hasRendered = false;

        // on the completion of the module template load and render
        this.on( 'templateLoaded', function() {

            // start the loading state and spinner
            this.dataLoad( true );

            // create the leaderboard data model
            this.dataModel = new LeaderboardSetCollection();

            // retrieve the leaderboard data
            this.dataModel.loadData();

            // listen to the completion of the data load
            this.dataModel.on( 'loadDataFinished', function() {

                // stop the loading state and spinner
                this.dataLoad( false );

                // narrow down the loaded data model to just the most recent active leaderboard and pass the resulting collection as the model for the new view
                that.leaderboardModel = that.dataModel.where( { nameId: 'active' } )[ 0 ].leaderboards.at( 0 );

                // create the view for the specific leaderboard data
                that.leaderboardView = new LeaderboardModelView( {
                    el: that.$el.find( '.leaderboardModel' ),
                    model: that.leaderboardModel,
                    parentViewType: 'module'
                } );

                // render all the leaderboards into the carousel
                that.renderLeaderboards();

            }, this );

        }, this );

        this.on( 'renderLeaderboardsDone', function() {
            //console.log('[INFO] LeaderboardModuleView: All leaderboards rendered');

            // stop the loading state and spinner
            this.dataLoad( false );

            this._hasRendered = true;
        } );

    },

    events: {
    },

    renderLeaderboards: function() {
        var that = this;

        // start the loading state and spinner
        this.dataLoad( true );

        this.leaderboardView.on( 'renderLeaderboardDone', function() {
            that.trigger( 'renderLeaderboardsDone' );
        } );

        this.leaderboardView.renderLeaderboard(
            this.leaderboardModel.get( 'id' ),
            {
                classes: [ 'item' ]
            }
        );
    }

} );
