/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported PlateauAwardsModuleView*/
/*global
_,
$,
G5,
Backbone,
LaunchModuleView,
TemplateManager,
PlateauAwardsModuleView:true
*/
PlateauAwardsModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function( opts ) {

        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        //doing the product model in the view (do not use the 'model' prop, this has a module model)
        this.products = new Backbone.Collection();

        //when products model gets reset, then render this
        this.products.on( 'reset', function() {this.renderProducts();}, this );

        // resize the text to fit
        this.moduleCollection.on( 'filterChanged', function() {
            G5.util.textShrink( this.$el.find( '.module-content h3' ) );
        }, this );
    },

    events: {
        'click .item': 'linkToCurrentProduct',
        // hack to catch IE capturing the click that should happen on the above absolute element
        //"click .title-icon-view":"linkToCurrentProduct",
        'ontouchstart .item': 'linkToCurrentProduct'
    },

    //override - add the call to loadProducts()
    render: function() {
        var that = this;

        this.getTemplateAnd( function( tpl ) {
            //when template manager has the template, render it to this element
            that.$el.append( tpl( _.extend( {}, that.model.toJSON(), { cid: that.cid } ) ) );

            //the carousel element (now it exists -- the template has been loaded)
            that.$carousel = that.$el.find( '#plateauCarousel' );

            //it is now safe to load products (trigger render)
            that.loadProducts();

            // start the loading state and spinner
            that.dataLoad( true );
        } );

        return this;//chaining
    },

    renderProducts: function() {
        var that = this,
            tplName = 'plateauAwardsModuleItem',
            tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'plateauAwards/tpl/',
            $cont = this.$carousel;

        // stop the loading state and spinner
        this.dataLoad( false );

        //empty the carousel items
        $cont.empty();

        TemplateManager.get( tplName, function( tpl ) {
            //for each budget item
            _.each( that.products.models, function( product ) {
                $cont.append( tpl( product.toJSON() ) );
            } );

            // resize the text to fit
            // the delay is to wait for custom fonts to load
            _.delay( G5.util.textShrink, 100, that.$el.find( '.wide-view h3' ) );
            G5.util.textShrink( that.$el.find( '.wide-view h3' ) );

            that.startSlick();

        }, tplUrl );

        return this;
    },
    startSlick: function() {
        var $slickEl = this.$el.find( '#plateauCarousel' ); //find all modules with a carousel
        $slickEl.slick( {
            autoplay: false,
            dots: true,
            arrows: true,
            infinite: false,
            speed: G5.props.ANIMATION_DURATION,
            slidesToShow: 4,
            slidesToScroll: 4,
            prevArrow: "<i class='icon-arrow-1-left slick-arrow slick-prev'></i>",
            nextArrow: "<i class='icon-arrow-1-right slick-arrow slick-next'></i>",
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
    },


    //shamefull, model method inside a view, lazy, lazy
    loadProducts: function( props ) {
        var that = this;
        props = _.extend( {}, this.$el.data(), props );
        // clean out data params that are objects (plugins for example)
        _.each( props, function( v, k ) {
            if( typeof v === 'object' ) {
                delete props[ k ];
            }
        } );
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_PLATEAU_AWARDS_MODULE,
            data: props || {},
            success: function( servResp ) {
                that.products.reset( servResp.data.products );
            }
        } );
    },

    // follow link to currently visible product
    linkToCurrentProduct: function( e ) {
        var //$frstUrl = this.$el.find('.plateauSlides .item').first().attr("data-url"),
            $url = $( e.target ).closest( '.item' ).attr( 'data-url' );


        // if there is a visible slide item, follow its link
        if( $url ) {
            location.href = $url;
            //console.log("linkToCurrentProduct " + $url);
            e.preventDefault();
        }
        // else, follow the link generated on the HTML (JSP) (no not prevent default)

    }

} );
