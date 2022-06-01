/*exported InstantPollCollection */
/*global
SurveyModel,
InstantPollCollection:true
*/
InstantPollCollection = Backbone.Collection.extend( {
    initialize: function(  ) {
        //console.log('[info] InstantPollCollection: initialized');

       // this.on('pollDataLoaded', this.processData);

    },

    comparator: function( model ) {
        // this pulls all the polls with 'isComplete' = false to the front of the list, despite the comparison appearing to do the opposite.
        return model.get( 'isComplete' ) === true;
    },

    loadData: function( pollId ) {
        var that = this;
        this.pollId = pollId;

        $.ajax( {
            url: G5.props.URL_JSON_INSTANT_POLL_COLLECTION,
            type: 'POST',
            data: {
                id: pollId
            },
            dataType: 'g5json',
            success: function( servResp ) {
                that.servResp = servResp;

                // that.reset(servResp.data.polls ? servResp.data.polls : {});
                _.each( servResp.data.polls, function( poll ) {
                    var s = new SurveyModel( {}, {
                        surveyJson: poll,
                        url: {
                            submit: G5.props.URL_JSON_INSTANT_POLL_SUBMIT_SURVEY
                        }
                    } );
                    s.loadSurveyData();

                    that.add( s );
                } );

                that.trigger( 'pollDataLoaded' );
                //return that.instantPoll;
            }
        } );
    }/*,
    processData: function() {
    var that = this,
        polls = this.servResp.data.polls,
        pollIds = _.uniq(_.pluck(polls, 'id')),
        instantPoll = {};

        _.each(pollIds, function(pollId) {
            var poll = _.where(polls, {'id': pollId});
            if(pollId == that.pollId){
                instantPoll = poll[0];
            }
        });

        this.instantPoll = instantPoll;

        //return this.instantPoll;
    }*/
} );