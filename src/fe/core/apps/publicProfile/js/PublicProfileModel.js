/*exported PublicProfileModel*/
/*global
JSON,
PublicProfileModel:true
*/
PublicProfileModel = Backbone.Model.extend( {

    defaults: {
        recipient: null,
        contributor: null,        
        attachMode: 'select', // select, video, photo, display
        commentImage: null,
        commentVideo: null,
        comments: null,
        loggedInUserId : null
    },

    initialize: function( opts ) { 
        console.log();
        if ( opts.contributor ) {
            this.set('loggedInUserId', opts.contributor.id)
        }
    },

    fetchComments: function() {
        var that = this,
            params = {
            recipientId: this.get( 'recipient' ).id,
            // not sure why we are asking the server for this since we are only loading once
            // we can easily sort this on FE
            commentOrderDescending: 'true'
        };

        this.trigger( 'start:fetchComments' );

        $.ajax( {
            type: 'POST',
            dataType: 'g5json',
            url: G5.props.URL_JSON_CAREERMOMENTS_ACTIVITY_FEED,
            data: { data: JSON.stringify( params ) },
            success: function( serverResp ) {
                var comments = serverResp.data.activityPods;
                //console.log('[INFO] PublicProfileModel.fetchComments - resp: ', serverResp);
                that.set( 'comments', comments );                
                // trigger the success
                that.trigger( 'success:fetchComments' );
            }
        } ).fail( function( jqXHR, textStatus ) {
            //console.log( '[INFO] PurlModal.fetchComments - failed: ' + textStatus);
        } ).always( function() {
            that.trigger( 'end:fetchComments' );
        } );
    },


    saveComment: function( comment ) {
        var that = this,
            c = this.get( 'contributor' ),
            paxId = this.get( 'recipient' ).id,
            // actual object being sent to server, super bizzare
            data = [
                {
                    messages: [],
                    profileUserId: paxId,

                    // not clear what this ID is from old FE code
                    // * this is a new item, so setting it to 0 for now (old was setting it to 1001+8 !!! wtf)
                    // ** this data is really wierd, would be nice to clean it up w/ cooperation of JAVA (it'll happen, right?)
                    // *** 0? every new comment gets an id of 0? Naw. Setting this to a really big random number
                    commentId: Math.round( Math.random() * 1000000000 ),
                    userInfo: [
                        {
                            // I am commenting out data that seems to not be used in old version
                            userName: c.firstName + ' ' + c.lastName,
                            firstName: c.firstName,
                            lastName: c.lastName,                                                        
                            profilePhoto: c.avatarUrl
                        }
                    ],
                    commentText: comment,
                    numLikers : 0,
                    // this is for video links
                    videoWebLink: '', // not doing video as discrete links any more. this array gets populated dynamically by oEmbed at render time
                    // media is only for image files that have been uploaded
                    media: !this.get( 'commentImage' ) && !this.get( 'commentVideo' ) ? '' : [
                        {
                            'video': !this.get( 'commentVideo' ) ? '' : 
                                {
                                    'src': this.get( 'commentVideo' ).url,
                                    'thumbSrc': this.get( 'commentVideo' ).thumbUrl,
                                    'fileType': this.get( 'commentVideo' ).fileType
                                }
                            ,
                            'photo': !this.get( 'commentImage' ) ? '' : 
                                {
                                    'src': this.get( 'commentImage' ).url,
                                    'thumbSrc': this.get( 'commentImage' ).thumbUrl
                                }
                            
                        }
                    ] // media (yeah, its an array)
                }
            ],
            item = $.extend( true, {}, data[ 0 ] );

        // clean up image URLs before submitting to the server
        if( data[ 0 ].media ) {
            if( data[ 0 ].media[ 0 ].photo ) {
                data[ 0 ].media[ 0 ].photo[ 0 ] = {
                    src: this.cleanImgUrl( this.get( 'commentImage' ).url ),
                    thumbSrc: this.cleanImgUrl( this.get( 'commentImage' ).thumbUrl )
                };
            }
            else if( data[ 0 ].media[ 0 ].video ) {
                data[ 0 ].media[ 0 ].video[ 0 ] = {
                    src: this.cleanImgUrl( this.get( 'commentVideo' ).url ),
                    thumbSrc: this.cleanImgUrl( this.get( 'commentVideo' ).thumbUrl )
                };
                 // if it is a video we need to send data in photo object as Java code is always expecting the photo attribute also for video images
                 data[ 0 ].media[ 0 ].photo = [ {
                        'src': data[ 0 ].media[ 0 ].video[ 0 ].src,
                        'thumbSrc': data[ 0 ].media[ 0 ].video[ 0 ].thumbSrc
                    } ];
            }
        }

        this.trigger( 'start:saveComment' );

        $.ajax( {
            type: 'POST',
            dataType: 'g5json',
            url: G5.props.URL_JSON_CAREERMOMENTS_POST_COMMENT,
            data: { data: JSON.stringify( data ) }, // bizarre
            success: function( serverResp ) {
                var msg = serverResp.data.messages[ 0 ];

                //console.log('[INFO] PublicProfileModel.saveComment - resp: ', serverResp);

                if( msg.isSuccess ) {
                    if ( msg.imageServiceUrlSrc ) {
                        item.media[0].photo.src = msg.imageServiceUrlSrc;
                        item.media[0].photo.thumbSrc = msg.imageServiceUrlThumb;
                    }
                    
                    // update our model with new item
                    that.get( 'comments' ).unshift( item );                    
                    // clear out comment media
                    that.set( 'commentImage', null );
                    that.set( 'commentVideo', null );
                    // this will trigger view update of visual state of contribute comment
                    that.set( 'attachMode', 'select' );
                    // trigger the success -- will trigger a clear of the textarea, maybe more
                    that.trigger( 'success:saveComment' );
                } else {
                    //console.log( '[INFO] PurlModal.saveComment - serverError: ' + msg.text);
                    alert( msg.text ); // yep
                }

            }
        } ).fail( function( jqXHR, textStatus ) {
            //console.log( '[INFO] PurlModal.saveComment - ajax fail: ' + textStatus );
        } ).always( function() {
            that.trigger( 'end:saveComment' );
        } );
    },

    //save a comment to the server
    saveLevelOneComment: function( params, comment, cId, jsonUrl, callback ) {
        var that = this,
        paxId = this.get( 'recipient' ).id,
            e = params[ 'e' ],
            params = {};

            params = {
                commentId: cId,
                comment: comment,
                profileUserId: paxId
            };

        $.ajax( {
            dataType: 'g5json', //must set this so SeverResponse can parse and return to success()
            type: 'POST',
            url: jsonUrl || G5.props.URL_JSON_CAREERMOMENTS_SAVE_COMMENT,
            data: params,
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data,
                    newComment;

                if( serverResp.getFirstError() ) {return;}//ERROR just return for now

                newComment = data.comment;
                // console.log(e);
                that.addComment( newComment, e );

                //console.log('[INFO] PublicRecognitionModel['+that.get('id')+'] - SAVED a new comment');

                if( typeof callback === 'function' ) {callback( newComment );}
            }
        } );
    },

    //add a comment to the model
    addComment: function( comment, e ) {
        e.showAnyway = true;
        //this.get( 'comments' ).push( comment );
        this.trigger( 'levelOneCommentAdded', comment );
        this.trigger( 'showNewComment', e );
    },

    //save a like to server
    saveIamLike: function( id, paxId ) {
        var that = this;
        $.ajax( {
            dataType: 'g5json', //must set this so SeverResponse can parse and return to success()
            type: 'POST',
            data: { iamId: id, profileUserId: paxId },
            url: G5.props.URL_JSON_CAREERMOMENTS_IAMSAVE_LIKE,
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data;

                if( serverResp.getFirstError() ) {return;}//ERROR just return for now
                that.set( 'isLiked', true );
                that.set( 'numLikers', data.numLikes );
                that.trigger( 'iamliked', data.numLikes, id );
                if( typeof callback === 'function' ) {callback();}
            }
        } );
    },

    //save a like to server
    unsaveIamUnlike: function( id, paxId ) {
        var that = this;
        $.ajax( {
            dataType: 'g5json', //must set this so SeverResponse can parse and return to success()
            type: 'POST',
            data: { iamId: id, profileUserId: paxId },
            url: G5.props.URL_JSON_CAREERMOMENTS_IAMSAVE_UNLIKE,
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data;

                if( serverResp.getFirstError() ) {return;}//ERROR just return for now
                that.set( 'isLiked', false );
                that.set( 'numLikers', data.numLikes );
                that.trigger( 'iamliked', data.numLikes, id );
               // console.log('[INFO] PublicRecognitionModel['+that.get('id')+'] - SAVED an unlike');

                if( typeof callback === 'function' ) {callback();}
            }
        } );
    },



    //save a like to server
    saveLike: function( id, paxId ) {
        var that = this;
        $.ajax( {
            dataType: 'g5json', //must set this so SeverResponse can parse and return to success()
            type: 'POST',
            data: { commentId: id, profileUserId: paxId },
            url: G5.props.URL_JSON_CAREERMOMENTS_SAVE_LIKE,
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data;

                if( serverResp.getFirstError() ) {return;}//ERROR just return for now


                that.set( 'isLiked', true );
                that.set( 'numLikers', data.numLikes );
                that.trigger( 'liked', data.numLikes, id );
                //console.log('[INFO] PublicRecognitionModel['+that.get('id')+'] - SAVED a like');

                if( typeof callback === 'function' ) {callback();}
            }
        } );
    },

    //save a like to server
    saveUnlike: function( id, paxId ) {
        var that = this;
        $.ajax( {
            dataType: 'g5json', //must set this so SeverResponse can parse and return to success()
            type: 'POST',
            data: { commentId: id, profileUserId: paxId },
            url: G5.props.URL_JSON_CAREERMOMENTS_SAVE_UNLIKE,
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data;

                if( serverResp.getFirstError() ) {return;}//ERROR just return for now

                that.set( 'isLiked', false );
                that.set( 'numLikers', data.numLikes );
                that.trigger( 'liked', data.numLikes, id );
               // console.log('[INFO] PublicRecognitionModel['+that.get('id')+'] - SAVED an unlike');

                if( typeof callback === 'function' ) {callback();}
            }
        } );
    },

    // helper - remove prefix strings from IMG URLs for cleaner saving in the DB
    cleanImgUrl: function( url ) {
        var stagerPfx = this.get( 'stagerPrefixURL' ),
            finalPfx = this.get( 'finalPrefixURL' );

        return url.replace( stagerPfx, '' ).replace( finalPfx, '' );
    }

} );
