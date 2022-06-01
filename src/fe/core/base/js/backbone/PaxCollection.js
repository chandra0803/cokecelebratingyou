/*exported PaxCollection */
/*global
PaxCollection:true
*/

/**

add participants (optionally with a server sync)
remove participants (optionally with a server sync)
sort by criteria
filter by criteria
paginate by an arbitrary number per page and by appending to or replacing current page
direct POST to server (with optional additional data)
extract data for external use (*.toJSON() or as a Backbone Collection)

 **/
PaxCollection = Backbone.Collection.extend( {
    initialize: function() {
        // console.log('PaxCollection opts',opts);
        // add global event listeners


    },
    setSelectUrl: function( val ) {
        this.selectUrl = val;
    },
    setDeselectUrl: function( val ) {
        this.deselectUrl = val;
    },
    getSelectUrl: function() {
        return this.selectUrl;
    },
    getDeselectUrl: function() {
        return this.deselectUrl;
    },
    selectedCheckUpdate: function( participant, addOnFollow ) {
        var that = this,
            url,
            adding;
        // check for sync on selected
        /*console.log('selectedCheckUpdate');
        //var deselectOthers = 'junk';
        console.log('this.deselectUrl',this.deselectUrl);
        console.log('this.selectUrl',this.selectUrl);*/
        if ( this.deselectUrl || this.selectUrl ) {
            if ( participant.get( 'selected' ) ) {
                url = this.selectUrl;
                adding = true;
            } else {
                url = this.deselectUrl;
                adding = false;
            }

            $.ajax( {
                dataType: 'g5json', //must set this so SeverResponse can parse and return to success()
                type: 'POST',
                url: url,
                data: { participantId: participant.id },
                success: function( serverResp ) {
                    //regular .ajax json object response
                    var msg = serverResp.getFirstError();
                        //pax = participant; // get the full participant data model from the collection

                    if ( msg ) {
                        console.log( 'fail' );
                        //failed - revert changes if necessary
                        that.trigger( 'selectParticipantError', msg );
                        //success good to go
                        if ( adding && addOnFollow ) {
                            participant.set( 'follow', false );
                        } else if ( addOnFollow ) {
                            participant.set( 'follow', true );
                        }

                        participant.set( 'selected', !participant.get( 'selected' ), { silent: true } );
                    } else {
                        //console.log('success');
                        //success good to go
                        if ( adding && addOnFollow ) {
                            participant.set( 'follow', true );
                        } else if ( addOnFollow ) {
                            participant.set( 'follow', false );
                        }
                    }
                }
            } );
        } else {
            //go ahead and do the select work
           // participant.set('selected', !participant.get('selected'), {silent:true});
            //this.selectParticipantInner(this, deselectOthers);
        }


    },

    numberItemsSelected: function() {
        return this.where( { 'selected': true } ).length;
    },

    follow: function( parameters ) {
        var that = this;
        $.ajax( {
            dataType: 'g5json', //must set this so SeverResponse can parse
            type: 'POST',
            url: G5.props.URL_JSON_PARTICIPANT_FOLLOW,
            data: parameters,
            success: function( serverResp ) {
                //console.log('serverResp',serverResp);
                var pax = that.get( parameters.participantIds );
                if ( serverResp.data.messages[ 0 ].isSuccess ) {
                    // hide one, show the other
                    pax.set( 'follow', !parameters.isFollowed );

                } else {
                    // need to trigger response?
                    console.error( '[ERROR] ParticipantPopoverView.follow() - Server Error : ' + serverResp.data.messages[ 0 ].text );

                    pax.set( 'follow', parameters.isFollowed );
                }
            }
        } );
    },

    // data-select-url="assets/ajax/profilePageProxiesTabProxy_add.json"

    destroy: function() {
        //console.log('destroy base');
        //G5._globalEvents.off('updateItemSelection', this.updateItemSelection, this);
    }

} );
