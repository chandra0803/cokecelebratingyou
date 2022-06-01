/* exported SSIModuleView */
/*global
LaunchModuleView,
SSIParticipantStepItUpView,
SSIParticipantDoThisGetThatView,
SSIParticipantObjectivesView,
SSIParticipantStackRankView,
SSICreatorListModuleView,
SSIModuleView:true
*/
SSIModuleView = LaunchModuleView.extend( {

    initialize: function() {
        'use strict';

        var that = this;

        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass ModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        this.on( 'templateLoaded', function() {
            if ( !that.model.get( 'ssiJson' ) ) { // this is the master module
                this.loadMasterData();
            } else { // this must be a slave module
                this.processData();
            }
            // G5.util.textShrink( this.$el.find('.moduleHeader h4'), {minFontSize:14} );
            //  _.delay(G5.util.textShrink, 200, this.$el.find('.moduleHeader h4'), {minFontSize:14} );

            // G5.util.textShrink( this.$el.find('.activityDescription'), {minFontSize:11} );
            //  _.delay(G5.util.textShrink, 200, this.$el.find('.activityDescription'), {minFontSize:11} );

            // G5.util.textShrink( this.$el.find('.dataSection h5'), {vertical:false, minFontSize:14} );
            // _.delay(G5.util.textShrink, 200, this.$el.find('.dataSection h5'), {vertical:false,minFontSize:14} );

            // G5.util.textShrink( this.$el.find('.contestDataWrapper span'), {vertical:false, minFontSize:14} );
            // _.delay(G5.util.textShrink, 200, this.$el.find('.contestDataWrapper span'), {vertical:false,minFontSize:14} );
        } );


    },

    // once we have contest data, call this
    processData: function() {
        'use strict';

        var /*ssiJson     = this.model.get('ssiJson'),
            contestType = _.isArray(ssiJson) ? 'list' : ssiJson.contestType,*/
            that = this,
            contests = this.model.get( 'masterModuleList' ),
            contestMap  = {
                'stepItUp': SSIParticipantStepItUpView,
                'doThisGetThat': SSIParticipantDoThisGetThatView,
                'objectives': SSIParticipantObjectivesView,
                'stackRank': SSIParticipantStackRankView,
                'list': SSICreatorListModuleView
            };

        if( contests.length == 1 ) {
            that.$el.find( '.ssiContestViews' ).addClass( 'single' );
        }

        _.each( contests, function( contest ) {
            var contestType = _.isArray( contest ) ? 'list' : contest.contestType,
                contestView;

            // if (!ssiJson) {
            //     console.error('[SSI] `ssiJson` data not found in `this.model`:', this.model);
            //     return this;
            // }

            // probably pass ssiJson to a contest view as a contest model or some such tomfoolery, and rendering and etc.

            if ( !contestMap[ contestType ] ) {
                throw new Error( 'can\'t find view contestType ' + contestType );
            }

            /**
             * Using try/catch allows one view to fail while the remaining are still created.
             */
            try {
                contestView = new contestMap[ contestType ]( {
                    el: $( '<div class="ssiParticipantContestView card"></div>' ),
                    wrapperTpl: 'ssiModuleView',
                    parentView: that,
                    data: contest
                } );
                contestView.render();

                contestView.on( 'compiled', function() {
                    // G5.util.textShrink( this.$el.find('.moduleHeader h4'), {minFontSize:14} );
                    // _.delay(G5.util.textShrink, 100, this.$el.find('.moduleHeader h4'), {minFontSize:14} );
                    // G5.util.textShrink( this.$el.find('.activityDescription'), {minFontSize:10} );
                    // _.delay(G5.util.textShrink, 100, this.$el.find('.activityDescription'), {minFontSize:10} );
                    // G5.util.textShrink( this.$el.find('.dataSection h5'), {vertical:false, minFontSize:14} );
                    // _.delay(G5.util.textShrink, 100, this.$el.find('.dataSection h5'), {vertical:false,minFontSize:14} );
                    // G5.util.textShrink( this.$el.find('.contestDataWrapper span'), {vertical:false, minFontSize:14} );
                    // _.delay(G5.util.textShrink, 100, this.$el.find('.contestDataWrapper span'), {vertical:false,minFontSize:14} );
                } );

                contestView.on( 'layoutChange', function( args ) {
                    that.trigger( 'layoutChange', args );
                } );

                that.masterViewList.push( contestView );
            } catch ( err ) {
                console.error( err );
            }

            that.$el.find( '.ssiContestViews' ).append( contestView.$el );
            contestView.$el.addClass( contestType ).addClass( contest.contestType || _.first( contest ).contestType );
        } );
    },

    /**
     * Converts `this.model` to values usable by the templates
     *
     * @returns {object}
     */
    getTemplateDataJSON: function () {
        'use strict';

        var model = this.model.toJSON(),
            keys  = [ 'isManager', 'isCreator', 'isSuperViewer' ],
            data  = _.pick( model, keys );

        if ( data.isManager || data.isCreator || data.isSuperViewer ) {
            data.isManagerOrCreatorOrSuperViewer = true;
        } else {
            data.isParticipant = true;
        }

        return data;
    },

    // get the list of all SSI Module data (first loaded SSIModuleView calls this)
    loadMasterData: function() {
        'use strict';

        var that = this,
            params = {};

        console.log( '[INFO] SSIModuleView.loadMasterData() started' );

        // start the loading state and spinner
        this.dataLoad( true );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: this.getDataUrl(),
            data: params,
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data;

                // stop spinner
                that.dataLoad( false );

                if ( data.masterModuleList.length ) {
                    // console.log('[INFO] SSIModuleView.loadMasterData() got ssi master list - ' + that.masterModuleList.length + ' modules');
                    that.createListAfter = data.createListAfter || null;
                    that.masterModuleList = that.mapData( data.masterModuleList );
                    that.model.set( 'masterModuleList', that.masterModuleList, { silent: true } );
                    that.renderContests();
                } else {
                    console.error( '[SSI] no modules returned from server' );
                }

            },
            error: function( jqXHR, textStatus ) {
                console.log( '[INFO] SSIModuleView: loadMasterData() Request failed: ' + textStatus );
            }
        } );
    },

    /**
     * (todo)
     *
     * @param {string} foo - foo description
     * @returns {string}
     */
    mapData: function( json ) {
        'use strict';

        if ( !this.model.get( 'isCreator' ) ) {
            return json;
        }

        var types = {};

        _.each( json, function( contest ) {
            var type = types[ contest.contestType ] = types[ contest.contestType ] || [];

            type.push( contest );

            contest.isCreator = true;
        } );

        return _.chain( json )
            .map( function( contest ) {
                var typeMap = types[ contest.contestType ];

                if ( !typeMap ) {
                    return null;
                } else if ( typeMap.length >= this.createListAfter ) {
                    delete types[ contest.contestType ];
                    return typeMap;
                } else {
                    return contest;
                }
            }, this )
            .compact()
            .value();
    },

    renderContests: function() {
        if ( !this.masterModuleList ) { return; }

        this.masterViewList = [];

        this.processData();
    }

} );
