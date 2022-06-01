/*jslint browser: true, nomen: true, unparam: true*/
/*exported PublicRecognitionPageFollowListView*/
/*global
PageView,
ParticipantCollectionView,
PaxSelectedPaxCollection,
PaxSearchStartView,
PublicRecognitionPageFollowListView:true
*/
PublicRecognitionPageFollowListView = PageView.extend( {

    //override super-class initialize function
    initialize: function() {
        //var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'publicRecognition';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );


        var collection = new PaxSelectedPaxCollection();

        //followees ParticipantCollectionView
        this.followeesView = new ParticipantCollectionView( {
            el: this.$el.find( '#followeesView' ),
            model: collection,
            tplName: 'participantRowItem' //override the default template
        } );

        //load the list of followees
        this.initializeFolloweesRequest();

        this.participantSearchView = new PaxSearchStartView( {
               el: this.$el.find( '.paxSearchStartView' ),
                //multiSelect:true,//this.isSingleSelectMode(),
                selectedPaxCollection: collection,
                disableSelect: true,
                addSelectedPaxView: false,
                follow: true
            } );

    },

    initializeFolloweesRequest: function() {
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
                var data = serverResp.data;

                that.$el.find( '.spin' ).remove();

                that.followeesView.model.reset( data.participants );
            }
        } );
    }

} );
