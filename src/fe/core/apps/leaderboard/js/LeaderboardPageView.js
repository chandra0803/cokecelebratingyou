/*exported LeaderboardPageView */
/*global
PageView,
LeaderboardSetCollection,
LeaderboardModelView,
LeaderboardPageView:true
*/
LeaderboardPageView = PageView.extend( {

    //override super-class initialize function
    initialize: function( opts ) {
        var that = this;

        // this._chosenSet = null,
        this.showLeaderboard = opts.showLeaderboard ? opts.showLeaderboard : null;

        //set the appname (getTpl() method uses this)
        this.appName = 'leaderboard';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        //create handy references to our important DOM bits
        this.$setSelect = this.$el.find( '#leaderboardSetSelect' );
        this.$boardSelect = this.$el.find( '#leaderboardSelect' );
        this.$board = this.$el.find( '.leaderboardModel' );

        // ROUTER IS NOW GLOBALNAV and it triggers handleRoute
        G5._globalEvents.on( 'route', this.handleRoute, this );

        // call to set initial route
        G5._globalEvents.trigger( 'route', {}, G5.props.CURRENT_ROUTE.length ? G5.props.CURRENT_ROUTE : [], { trigger: true } );

        // create the leaderboard data model
        this.model = new LeaderboardSetCollection();

        // retrieve the leaderboard data
        this.wait();
        this.model.loadData( this.routedSet ? this.routedSet : 'active', this.routedBoard ? this.routedBoard : null );

        // listen to the completion of the data load
        this.model.on( 'loadDataFinished', function() {

            console.log( '[INFO] LeaderboardPageView: data load finished', that );

            if ( that.showLeaderboard ) {
                that.model.setChosenSetById( this.model.getSetIdByBoardId( that.showLeaderboard ) );
                that.showLeaderboard = null;
            }

            // render the view
            that.render();

        }, this );

    },

    events: {
        'change #leaderboardSetSelect': 'lbSetChange',
        'change #leaderboardSelect': 'lbChange'
    },

    handleRoute: function( event, route ) {
        var set, board;
        if ( route && route[ 0 ] === 'set' ) {
            set = route[ 1 ];
            board = route[ 2 ];

            this.routedSet = set;
            this.routedBoard = board;

            // if the data model already exists
            if ( this.model ) {
                // if the set is already active
                if ( this.$setSelect.val() === set ) {
                    // console.log('set already active');
                    this.$boardSelect.val( board ).change();
                } else {
                    // console.log('set not yet active');
                    this.$setSelect.val( set );
                    this.lbSetChange( null, true );
                    this.$boardSelect.val( board ).change();
                }
            }
        }
    },

    //wait for model update
    wait: function() {
        //start the spinner
        // this.$board.spin();
    },


    lbSetChange: function() {
        var val = this.$setSelect.val();

        this.model._chosenSet = this.model.getSetById( val );

        this.$board.empty();

        this.renderLeaderboardSelect();
    },

    lbChange: function( e, silent ) {
        var val = this.$boardSelect.val();

        this.model._chosenSet.leaderboards._chosenBoard = this.model.getBoardById( val );

        if ( !silent ) {
            G5._globalEvents.trigger( 'navigate', e || {}, [ 'set', this.model._chosenSet.get( 'id' ), this.model._chosenSet.leaderboards._chosenBoard.get( 'id' ) ], { trigger: true } );
        }
        this.renderLeaderboard();
    },

    render: function() {
        // stop the spinner
        // this.$board.spin(false);

        this.renderSetSelect();

        if ( this.options.userRole === 'participant' ) {
            this.$el.addClass( 'participant' );
        }
    },

    renderLeaderboard: function() {
        this.leaderboardView = new LeaderboardModelView( {
            el: this.$board,
            model: this.model._chosenSet.leaderboards._chosenBoard,
            parentViewType: 'page'
        } );

        this.$board.empty();

        if ( this.model._chosenSet.leaderboards._chosenBoard ) {
            this.leaderboardView.renderLeaderboard(
                this.model._chosenSet.leaderboards._chosenBoard.get( 'id' ),
                {
                    $target: this.$board
                }
            );
        }
    },

    renderLeaderboardSelect: function() {
        var that = this,
            leaderboards = this.model._chosenSet.leaderboards;

        // check to see if any leaderboards exist and if the data load has not yet been attempted on this set
        if ( leaderboards.length === 0 && !this.model._chosenSet.get( 'dataloaded' ) ) {
            console.log( '[INFO] LeaderboardPageView: renderLeaderboardSelect: leaderboards empty and no data has been explicitly loaded for the set with id', this.model._chosenSet.get( 'id' ) );

            this.$el.find( '#controlLeaderboardSelect' ).show();
            // retrieve the leaderboard data
            this.model.loadData( this.model._chosenSet.get( 'nameId' ) );

        } else if ( leaderboards.length === 0 ) {
            this.$board.append( this.model._chosenSet.get( 'emptyMessage' ) || this.$el.find( '#leaderboardSelect' ).data( 'noLeaderboards' ) );
            this.$el.find( '#controlLeaderboardSelect' ).hide();
        }        else {
            console.log( '[INFO] LeaderboardPageView: renderLeaderboardSelect: leaderboards not empty or data has been explicitly loaded for the set with id', this.model._chosenSet.get( 'id' ) );
            this.$el.find( '#controlLeaderboardSelect' ).show();
            that.renderSelectField(
                leaderboards.models,
                that.$boardSelect,
                function() {
                    if ( leaderboards._chosenBoard ) {
                        that.$boardSelect.val( leaderboards._chosenBoard.get( 'id' ) );
                        // that.$boardSelect.find('[value='+leaderboards._chosenBoard.get("id")+']').attr('selected', 'selected');
                    }
                    that.$boardSelect.change();

                }
            );
        }

    },

    renderSelectField: function( models, $target, callback ) {
        var that = this;

        $target.empty();

        // loop through the sets
        _.each( models, function( model ) {
            model = model.toJSON();

            // append an option to the select for each set
            $target.append(
                that.make( 'option',
                {
                    value: model.id
                },
                model.name )
            );
        } );

        if ( callback ) {
            callback();
        }
    },

    renderSetSelect: function() {
        var that = this;

        this.renderSelectField(
            this.model.models,
            this.$setSelect,
            function() {
                that.$setSelect.val( that.model._chosenSet.get( 'id' ) );
                // that.$setSelect.find('[value='+that.model._chosenSet.get("id")+']').attr('selected', 'selected');
                that.$setSelect.change();
            }
        );
    }

} );
