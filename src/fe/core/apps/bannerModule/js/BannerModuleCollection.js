/*exported BannerModuleCollection */
/*global
BannerModuleModel,
BannerModuleCollection:true
*/
BannerModuleCollection = Backbone.Collection.extend( {

    model: BannerModuleModel,

    initialize: function() {
        //console.log("[INFO] : Banner Module Collection initialized", this);

    },

    loadData: function() {

        var that = this,
            dataReturned,
            params = {};

        //console.log('[INFO] BannerModuleCollection: LoadData started');

        if ( G5.props.preLoadedBanners ) {
            //console.log('[INFO] BannerModuleCollection: preLoadedBanners found, no ajax call required');

            this.add( G5.props.preLoadedBanners );

            that.trigger( 'loadDataFinished' );

        }        else {

            //console.log('[INFO] BannerModuleCollection: preLoadedBanners not found, ajax call required');

            dataReturned = $.ajax( {
                    dataType: 'g5json',
                    url: G5.props.URL_JSON_BANNER_SLIDES,
                    data: params,
                    type: 'POST',
                    success: function( serverResp ) {
                        //regular .ajax json object response
                        var data = serverResp.data,
                            slides = data.slides;
                        //console.log("[INFO] BannerModuleCollection: LoadData ajax call successfully returned this JSON object: ", data);

                        that.add( slides );

                        //notify listener
                        that.trigger( 'loadDataFinished' );
                    }
                } );

            dataReturned.fail( function( jqXHR, textStatus ) {
                console.log( '[INFO] BannerModuleCollection: LoadData Request failed: ' + textStatus );
            } );
        }

    }

} );
