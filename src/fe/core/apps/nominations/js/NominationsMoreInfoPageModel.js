/*jslint browser: true, nomen: true, unparam: true*/
/*exported NominationsMoreInfoPageModel */
/*global
$,
_,
Backbone,
console,
G5,
NominationsMoreInfoPageModel:true
*/
NominationsMoreInfoPageModel = Backbone.Model.extend( {
	initialize: function() {
		console.log( 'Nominations More Info Page Model initialized' );
    },

	translateData: function() {
        var that = this,
         data = {};

        $.ajax( {
            dataType: 'g5json', //must set this so SeverResponse can parse and return to success()
            data: data,
            url: G5.props.URL_JSON_NOMINATIONS_MORE_INFO_TRANSLATE_COMMENT,
            type: 'POST',
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data;

                if ( serverResp.getFirstError() ) { return; }//ERROR just return for now

                that.trigger( 'translated', data );
                console.log( '[INFO] NominationsMoreInfoPageModel - reason Translated' );
            }
        } );
    },

    loadData: function( opts ) {
        var that = this,
            data = {};

            data = $.extend( {}, data, opts );

			$.ajax( {
                dataType: 'g5json',
                type: 'POST',
                url: G5.props.URL_JSON_NOMINATIONS_MORE_INFO,
                data: data,
                cache: true,
                success: function ( serverResp ) {
                    //regular .ajax json object response
                    var data = serverResp.data;

                    that.set( data );
                    console.log( that );
                    //notify listener
                    that.trigger( 'loadDataFinished', data );
                },
                error: function( jqXHR, textStatus, errorThrown ) {
                    console.log( '[ERROR] NominationsMoreInfoPageModel: ', jqXHR, textStatus, errorThrown );
                }
            } );
    },

    setAttachmentLink: function( link, url, name, id ) {
        var updatedLink = this.get( 'nominationLinks' ),
        lid;

        if( !id) {
            lid = updatedLink.length + 1;
        }
        else {
            lid = id;
        }        

        updatedLink.push({
            nominationLink: link,
            nominationUrl: url,
            fileName: name,
            linkid: lid            
        });        

        this.set( 'nominationLinks', updatedLink );
        this.updateAttachmentCount();
    },

    updateAttachmentCount: function() {
        var linkObj = this.get('nominationLinks'),
        uploadsCount = linkObj.length == 0 ? null : linkObj.length;        
        this.set('updatedDocCount', uploadsCount);
    },

    removeAttachedDocuments: function( ) {
        var updatedLink = this.get( 'promotion' );
        this.get( 'promotion' ).nominationLinks = [];
        this.set( 'promotion', updatedLink );
        this.updateAttachmentCount();
        this.trigger( 'docUploaded' );
    },

    deleteAttachments: function( linkId ) {
        var updatedLink = this.get( 'nominationLinks' ),
        nominationLinks = _.without(updatedLink, _.findWhere(updatedLink, {
          linkid: parseInt(linkId)
        }));       
        this.set('nominationLinks', nominationLinks)
        this.updateAttachmentCount();
        this.trigger( 'docUploaded' );
    }

} );
