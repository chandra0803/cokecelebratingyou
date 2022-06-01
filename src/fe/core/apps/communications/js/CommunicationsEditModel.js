/*exported CommunicationsEditModel */
/*global
CommunicationsEditModel:true
*/
CommunicationsEditModel = Backbone.Model.extend( {
    mode: null,

    imagesMap: {
        'banners': 'bannerImages',
        'news': 'newsImages'
    },

    tableMap: {
        'banners': {
            'key': 'banner',
            'table': 'bannerTable',
            'array': 'banners'
        },
        'news': {
            'key': 'news',
            'table': 'newsTable',
            'array': 'news'
        },
        'resources': {
            'key': 'resource',
            'table': 'resourceTable',
            'array': 'resources'
        },
        'tips': {
            'key': 'tip',
            'table': 'tipsTable',
            'array': 'tips'
        }
    },

    // Bug 58246 - New language Content in Banners not getting saved.
    // contentLength is use to keep track of the index so it is unique
    contentLength: 0,

    initialize: function( attributes, options ) {
        this.urlMap = {
            'banners': G5.props.URL_JSON_COMMUNICATION_BANNER_DATA,
            'news': G5.props.URL_JSON_COMMUNICATION_NEWS_DATA,
            'resources': G5.props.URL_JSON_COMMUNICATION_RESOURCE_CENTER_DATA,
            'tips': G5.props.URL_JSON_COMMUNICATION_TIPS_DATA
        };    	
        this.mode = options.mode;

        this.contentIdIncrementer = 0;
        this.imageIdIncrementer = 0;
    },

    loadData: function(  ) {
        var that = this,
            data = {};

            $.ajax( {
                dataType: 'g5json',
                type: 'POST',
                url: this.urlMap[ this.mode ],
                data: data,
                cache: true,
                success: function ( serverResp ) {
                    //regular .ajax json object response
                    var data = serverResp.data;

                    if( that.mode == 'banners' || that.mode == 'news' ) {
                        // convert the images array into a Backbone Collection to take advantage of duplicate removal and then back into an array to have the duplicate-free version
                        data[ that.imagesMap[ that.mode ] ].images = new Backbone.Collection( data[ that.imagesMap[ that.mode ] ].images ).toJSON();
                    }

                    // start the index off with elements from the backend
                    that.contentLength = data[ that.tableMap[ that.mode ].table ][ that.tableMap[ that.mode ].array ].length;

                    that.set( data );

                    //notify listener
                    that.trigger( 'loadDataFinished', data );
                },
                error: function( jqXHR, textStatus, errorThrown ) {
                    console.log( '[ERROR] CommunicationsEditModel: mode:' + this.mode + ': ', jqXHR, textStatus, errorThrown );
                }
            } );
    },

    getContent: function( item ) {
        if( item ) {
            return this.get( this.tableMap[ this.mode ].key + item );
        } else {
            return this.get( this.tableMap[ this.mode ].table )[ this.tableMap[ this.mode ].array ];
        }
    },

    getImages: function() {
        return this.get( this.imagesMap[ this.mode ] ).images;
    },

    addContent: function( newData ) {
        var content = this.getContent(),
            isAdded = true;

            newData.id = 'addedContent' + this.contentIdIncrementer;

            newData.index = this.contentLength;

            content.push( newData );

            this.contentIdIncrementer += 1;
            this.contentLength += 1;

            this.trigger( 'contentAdded', newData, isAdded );
    },

    updateContent: function( updatedData ) {
        var content = this.getContent(),
            that = this;

        _.each( content, function( item, index ) {
            if ( item.id === updatedData.id ) {

                item = $.extend( {}, item, updatedData );
                content[ index ] = item;

                that.trigger( 'contentUpdated', item );
            }
        } );
    },

    removeContent: function( id ) {
        var content = this.getContent(),
            i;

        for ( i = 0; i < content.length; i++ ) {
            if ( content[ i ].id && content[ i ].id === id ) {
                content.splice( i, 1 );
                break;
            }
        }
        this.trigger( 'contentRemoved', content );
    },

    addImage: function( newData ) {
        var images = this.getImages();

            newData.id = 'addedImage' + this.imageIdIncrementer;

            images.push( newData );

            this.imageIdIncrementer += 1;

            this.trigger( 'imageAdded', newData );
    }
} );