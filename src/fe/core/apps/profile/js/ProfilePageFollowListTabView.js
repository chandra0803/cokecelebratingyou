/*exported ProfilePageFollowListTabView */
/*global
TemplateManager,
ParticipantCollectionView,
PaxSearchStartView,
PaxSelectedPaxCollection,
ProfilePageFollowListTabView:true
*/
ProfilePageFollowListTabView = Backbone.View.extend( {
    initialize: function ( opts ) {
        this.tplName    = opts.tplName || 'profilePageFollowListTab';
        this.tplUrl     = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/';

        this.on( 'tabRendered', function() {

            // TODO: will need to trigger error on participants in awardee and set prop on collection
            // TODO: make sure our data matches coming back from server so collection model is the same
            // need to pass paramaters all the way through


            var collection = new PaxSelectedPaxCollection( null, {
                addOnFollow: true
            } );

            this.followeesView = new ParticipantCollectionView( {
                el: this.$el.find( '#followeesView' ),
                model: collection,

                tplName: 'participantRowItem' //override the default template
            } );

            this.participantSearchView = new PaxSearchStartView( {
                el: this.$el.find( '.paxSearchStartView' ),
                multiSelect: true, //this.isSingleSelectMode(),
                selectedPaxCollection: collection,
                //disableSelect:true,
                addSelectedPaxView: true,
                follow: true,
                addOnFollow: true,
                hideCheckAll: true

            } );

            //load the list of followees
            this.initializeFolloweesRequest();

        } );
    },

    activate: function () {
        'use strict';

        this.render();
    },

    render: function () {
        'use strict';
        var that = this;

        this.$el
            .append( '<span class="spin" />' )
            .find( '.spin' ).spin();

        TemplateManager.get( this.tplName,
            function ( tpl ) {
                that.$el.empty().append( tpl( {} ) );
                that.trigger( 'tabRendered' );
            },
            this.tplUrl );

        return this;
    },

    initializeFolloweesRequest: function() {
        //console.log('inside initializeFolloweesRequest.', this.followeesView);
        var that = this;

        this.$el
            .find( '.emptyMsg' ).hide()
            .after( '<span class="spin" />' )
            .end().find( '.spin' ).spin();

        $.ajax( {
            dataType: 'g5json', //must set this so SeverResponse can parse and return to success()
            type: 'POST',
            url: G5.props.URL_JSON_PUBLIC_RECOGNITION_FOLLOWEES_LIST,
            data: {},
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data;
                //console.log('initializeFolloweesRequest.data ', data);
                //console.log(that.followeesView);
                that.$el.find( '.spin' ).remove();

                that.followeesView.model.reset( data.participants );
            }
        } );
    },

    events: {

    }
} );
