/*exported CommunicationsManageModel */
/*global
CommunicationsManageModel:true
*/
CommunicationsManageModel = Backbone.Model.extend( {
    mode: null,

    initialize: function( attributes, options ) {
        this.mode = options.mode;
        this.urlMap = {
            'banners': G5.props.URL_JSON_COMMUNICATION_BANNERS_TABLE,
            'news': G5.props.URL_JSON_COMMUNICATION_NEWS_TABLE,
            'resources': G5.props.URL_JSON_COMMUNICATION_RESOURCE_CENTER_TABLE,
            'tips': G5.props.URL_JSON_COMMUNICATION_TIPS_TABLE
        };
    },
    loadData: function(  ) {
        var that = this,
            data = {
                currentPage: this.get( 'currentPage' ),
                sortedOn: this.get( 'sortedOn' ),
                sortedBy: this.get( 'sortedBy' ),
                statusType: this.get( 'statusType' )
            };

            $.ajax( {
                dataType: 'g5json',
                type: 'POST',
                url: this.urlMap[ this.mode ],
                data: data,
                cache: true,
                success: function ( serverResp ) {
                    //regular .ajax json object response
                    var data = serverResp.data;

                    that.set( data );

                    //notify listener
                    that.trigger( 'loadDataFinished', data );
                },
                error: function( jqXHR, textStatus, errorThrown ) {
                    console.log( '[ERROR] CommunicationsManageModel: mode:' + this.mode + ': ', jqXHR, textStatus, errorThrown );
                }
            } );
    },
    update: function( opts ) {
         if ( opts.type === 'getContent' ) {
            this.set( 'currentPage', opts.data.pageNumber );
         }
         if ( opts.type === 'tabular' ) {
            this.set( 'sortedOn', opts.data.sortedOn );
            this.set( 'sortedBy', opts.data.sortedBy );
            this.set( 'currentPage', opts.data.pageNumber );
         }
         if ( opts.type === 'status' ) {
            this.set( 'statusType', opts.data.statusType );
         }
         this.loadData();
    }
} );
