
/*exported CelebrationImageFillerModuleView */
/*global
LaunchModuleView,
CelebrationImageFillerModel,
CelebrationImageFillerModuleView:true
*/
CelebrationImageFillerModuleView = LaunchModuleView.extend( {

    initialize: function( opts ) {
        var that = this;//,
            //pageModules;

        //this is how we call the super-class initialize function
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //merge events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        //allowed dimensions, sorted from smallest - biggest
        this.model.set( 'allowedDimensions', [
            { w: 2, h: 2 }
             // 2x2 square
        ], { silent: true } );

        this.pageModules = opts.app.moduleCollection.models;

        if( !G5.models.imageFillerModel ) {
            G5.models.imageFillerModel = new CelebrationImageFillerModel();
        }

        that.on( 'templateLoaded', function() {
                //load initial destination
                G5.models.imageFillerModel.loadData();
            } );

        G5.models.imageFillerModel.on( 'dataLoaded', function() {
                that.fillTiles();
                that.resizeText();
        } );

    },

    fillTiles: function() {
        var that = this,
            $page = $( '.celebrationPage .moduleContainerViewElement' ),
            //$allmods = $page.find('.module'),
            $module = $page.find( '.celebrationImageFiller' ),
            $container = this.$el.find( '.imageFillerWrapper' ),
            i = $module.index( this.$el ),
            placeholderImages = G5.models.imageFillerModel.get( 'placeholderImgs' );

            if( placeholderImages[ i ] ) {
                if( placeholderImages[ i ].url ) {
                    $container.html( '' );
                    $container.append( '<img src="' + placeholderImages[ i ].url + '"/>' );

                } else if( placeholderImages[ i ].text ) {
                    $container.html( '' );
                    $container.append( '<span>' + placeholderImages[ i ].text + '</span>' );
                }
            } else {
                that.$el.hide();
            }
    },

    resizeText: function() {
        var $imageNumber = this.$el.find( '.imageFillerWrapper span' );

        _.delay( G5.util.textShrink, 100, $imageNumber, { minFontSize: 100 } );
        G5.util.textShrink( $imageNumber, { minFontSize: 100 } );
    }
} );
