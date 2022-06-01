/*exported TipModuleView */
/*global
LaunchModuleView,
TipCollection,
TipModuleView:true
*/
TipModuleView = LaunchModuleView.extend( {

    initialize: function( opts ) {

        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        // on the completion of the module template load and render
        this.on( 'templateLoaded', function() {
            var thisView = this;

            //create the tip data model
            this.dataModel = new TipCollection();

            this.dataModel.on( 'loadDataFinished', function() {

                var randomTips = thisView.dataModel.getRandomTips( 10 ); // closure keeps the tips array
                //var specicficTip = thisView.dataModel.getSpecificTip( 1001 );

                thisView.renderTips( null, null, randomTips );

            } );

            this.dataModel.on( 'preLoadDataFinished', function() {
               // console.log("[INFO] TipModuleView: preLoadDataFinished triggered");

                var randomizedTips = thisView.dataModel.getRandomTips( 10 );

                thisView.renderTips( null, null, randomizedTips );

            } );

            //retrieve the tip data
            this.dataModel.loadData();

        } );

    },
    renderTips: function( tipID, opts, tipArray ) {

        //console.log("[INFO] TipModuleView: renderTips called using these tips: ", tipArray);

        var defaults = {
                $target: this.$el.find( '#tipText' ),  // JQ object
                classe: null,       // array
                callback: null      // function
            },
            settings = opts ? _.defaults( opts, defaults ) : defaults,
            //tip = this.model.length > 0 ? this.model.get( tipID ) : this.model,
            //tplName = 'tipModel',
            //tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'tip/tpl/',
            i,
            tipArrayLength = tipArray.length - 1;

        for ( i = 0; i <= tipArrayLength; i++ ) {
            var theTip = tipArray[ i ].get( 'text' );

            settings.$target.append( '<div class="item aTip"><p>' + theTip + '</p></div>' );
        }


        if ( tipArrayLength === 0 ) { //if only one tip, slick won't init.
            $( '#tipContainer' ).addClass( 'singleItem' );
        }
        else {
            //console.log(tipArrayLength);
            this.startSlick();
        }


    },
    startSlick: function() {
        var $slickEl = this.$el.find( '#tipText' ); //find all modules with a carousel
        $slickEl.slick( {
            autoplay: false,
            dots: true,
            arrows: true,
            infinite: false,
            speed: 1000,
            slidesToShow: 4,
            slidesToScroll: 4,
            prevArrow: '<i class="icon-arrow-1-left slick-arrow slick-prev"></i>',
            nextArrow: '<i class="icon-arrow-1-right slick-arrow slick-next"></i>',
            responsive: [
            {
              breakpoint: 1279,
              settings: {
                slidesToShow: 3,
                slidesToScroll: 3
              }
            },
            {
              breakpoint: 1023,
              settings: {
                slidesToShow: 2,
                slidesToScroll: 2
              }
            },
            {
              breakpoint: 640,
              settings: {
                slidesToShow: 1,
                slidesToScroll: 1
              }
            }
            ]
        } );
    }
} );
