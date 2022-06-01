/*exported ProfileGroupsModel */
/*global
ProfileGroupsModel:true,
*/

ProfileGroupsModel = Backbone.Model.extend( {

    initialize: function( opts ) {


    },

    /***
    *         888          888                                    888 888
    *         888          888                                    888 888
    *         888          888                                    888 888
    *     .d88888  8888b.  888888  8888b.        .d8888b  8888b.  888 888 .d8888b
    *    d88" 888     "88b 888        "88b      d88P"        "88b 888 888 88K
    *    888  888 .d888888 888    .d888888      888      .d888888 888 888 "Y8888b.
    *    Y88b 888 888  888 Y88b.  888  888      Y88b.    888  888 888 888      X88
    *     "Y88888 "Y888888  "Y888 "Y888888       "Y8888P "Y888888 888 888  88888P'
    *
    *
    *
    */

    // G5.props.URL_JSON_GLOBAL_DELETE_GROUP
    // G5.props.URL_JSON_GLOBAL_FETCH_GROUP_BY_ID
    // G5.props.URL_JSON_GLOBAL_FETCH_GROUPS
    // G5.props.URL_JSON_GLOBAL_SAVE_CREATE_GROUP

    saveCreateGroup: function( groupId, groupName, groupMembers ) {
        /*
            Request parameters  :
            groupId :   < if existing group you can send it otherwise if its a new group then you can ignore it >
            groupName : name of the group
            groupMemebers : List of Id's of the group members

            Response :
            { success : true, type : success }
            Or
            { success : false, type : error }
        */
        var qdata = {};
            qdata.groupId = groupId;
            qdata.groupName = groupName;
            qdata.groupMembers = groupMembers;

        var that = this;
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_GLOBAL_SAVE_CREATE_GROUP,
            data: this.queryStringForStruts( $.param( qdata ) ),
            success: function( serverResp ) {
                 if( serverResp.data.message && serverResp.data.message.length > 0 ) {
                    if( serverResp.data.message[ 0 ].type == 'error' ) {
                        that.trigger( 'groupError', serverResp.data.message[ 0 ].text );
                        return;
                    }
                }
                that.trigger( 'groupSaved', serverResp.data );
            },
            error: function( jqXHR, textStatus ) {
                if( jqXHR.responseJSON.message && jqXHR.responseJSON.message.length > 0 ) {
                    if( jqXHR.responseJSON.message[ 0 ].type == 'error' ) {
                        that.trigger( 'groupError', jqXHR.responseJSON.message[ 0 ].text );
                        return;
                    }
                }
                console.log( '[INFO] ProfileGroupsModel->fetchGroupById->error: Request failed: ' + textStatus );
            }
        } );
    },

    fetchGroupById: function( groupId ) {
        /*
            Request parameters :
                    groupId : <mandatory>

           Response :
                                {  groupId :  ,
           groupName : ,
           groupCreatedBy : ,
           dateCreated: ,
           groupMemebers : }
       */
        var qdata = {};
            qdata.groupId = groupId;
        var that = this;
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_GLOBAL_FETCH_GROUP_BY_ID,
            data: qdata,
            success: function( serverResp ) {
                if( serverResp.data.message && serverResp.data.message.length > 0 ) {
                    if( serverResp.data.message[ 0 ].type == 'error' ) {
                        that.trigger( 'groupError', serverResp.data.message[ 0 ].text );
                        return;
                    }
                }
                that.trigger( 'groupDetailsReceived', serverResp.data );
            },
            error: function( jqXHR, textStatus ) {
                if( jqXHR.responseJSON.message && jqXHR.responseJSON.message.length > 0 ) {
                    if( jqXHR.responseJSON.message[ 0 ].type == 'error' ) {
                        that.trigger( 'groupError', jqXHR.responseJSON.message[ 0 ].text );
                        return;
                    }
                }
                console.log( '[INFO] ProfileGroupsModel->fetchGroupById->error: Request failed: ' + textStatus );
            }
        } );
    },


    fetchAllGroups: function( userId ) {
        /*
            Request Parameters :
                userId : <mandatory>

            Response :
            groups :  [  {
               id:
              name:
                                            paxCount
            } ]
        */
        var that = this,
            qdata = {};
            qdata.userId = userId;
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_GLOBAL_FETCH_GROUPS,
            data: qdata,
            success: function( serverResp ) {
                if( serverResp.data.message && serverResp.data.message.length > 0 ) {
                    if( serverResp.data.message[ 0 ].type == 'error' ) {
                        that.trigger( 'groupError', serverResp.data.message[ 0 ].text );
                        return;
                    }
                }
                that.trigger( 'groupsReceived', serverResp.data );
            },
            error: function( jqXHR, textStatus ) {
                if( jqXHR.responseJSON.message && jqXHR.responseJSON.message.length > 0 ) {
                    if( jqXHR.responseJSON.message[ 0 ].type == 'error' ) {
                        that.trigger( 'groupError', jqXHR.responseJSON.message[ 0 ].text );
                        return;
                    }
                }
                console.log( '[INFO] ProfileGroupsModel->fetchGroupById->error: Request failed: ' + textStatus );
            }
        } );
    },

    deleteGroup: function( groupId ) {
        /*
            Request :
                groupId : <required>

            Response :
            { success : true, type : success }
            Or
            { success : false, type : error }
        */
        var that = this,
            qdata = {};
            qdata.groupId = groupId;
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_GLOBAL_DELETE_GROUP,
	        data: qdata,
            success: function( serverResp ) {
                if( serverResp.data.message && serverResp.data.message.length > 0 ) {
                    if( serverResp.data.message[ 0 ].type == 'error' ) {
                        that.trigger( 'groupError', serverResp.data.message[ 0 ].text );
                        return;
                    }
                }
                that.trigger( 'groupDeleted' );
            },
            error: function( jqXHR, textStatus ) {
                if( jqXHR.responseJSON.message && jqXHR.responseJSON.message.length > 0 ) {
                    if( jqXHR.responseJSON.message[ 0 ].type == 'error' ) {
                        that.trigger( 'groupError', jqXHR.responseJSON.message[ 0 ].text );
                        return;
                    }
                }
                console.log( '[INFO] ProfileGroupsModel->fetchGroupById->error: Request failed: ' + textStatus );
            }
        } );
    },

    getGroupById: function( groupId ) {
        return _.findWhere( this.get( 'groups' ), { groupId: groupId } );
    },

    queryStringForStruts: function( qs ) {
        var rem = null;

        // this replaces the arrayName[0][subArrayName][0][keyName] notation with:
        // arrayName[0].subArrayName[0].keyName something dumb

        while ( rem = qs.match( /(\?|&).*?%5B([a-zA-Z_]+)%5D.*?=/ ) ) {
            qs = qs.replace( '%5B' + rem[ 2 ] + '%5D', '.' + rem[ 2 ] );
        }

        // this removes [] from arrays (struts does not recognize this style)

        qs = qs.replace( /%5B%5D=/g, '=' );

        return qs;
    },
    openRecognitionPage: function( idArray ) {
        var url = G5.props.URL_START_RECOGNITION,
            s = '',
            form;

         //console.log(e.target)
         // need to get all  participants ids, and send them via post to the participants page
        _.each( idArray, function( id, i ) {
            console.log( id );
            s += '<input type="hidden" name="claimRecipientFormBeans[' + i + '].userId" value="' + id + '" />';
         // participantIDs.push(model.id);
        } );

        form = $( '<form action="' + url + '" method="post">' + s + '</form>' );
        $( 'body' ).append( form );
        $( form ).submit();
    }

} );

