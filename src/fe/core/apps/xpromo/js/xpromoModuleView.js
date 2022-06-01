/*exported XpromoModuleView */
/*global
TemplateManager,
LaunchModuleView,
XpromoStoryCollection,
XpromoModuleView:true
*/
XpromoModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function() {
        var that = this;
        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        //doing the product model in the view (do not use the 'model' prop, this has a module model)
        this.xpromoStoryCollection = new XpromoStoryCollection();

        //when products model gets reset, then render this
        this.xpromoStoryCollection.on( 'reset', function() {
            // stop the loading state and spinner
            that.dataLoad( false );

            that.renderStories();
        }, this );

        // listen for global resize and scroll events
        this.on( 'templateLoaded', function() {
            //it is now safe to load stories (trigger render)
            that.loadStories();

            // start the loading state and spinner
            that.dataLoad( true );

            G5.util.textShrink( this.$el.find( 'h4' ), { minFontSize: 14 } );
             _.delay( G5.util.textShrink, 200, this.$el.find( 'h4' ), { minFontSize: 14 } );

            G5._globalEvents.on( 'breakpointChanged', this.responsiveImages, this );

            this.on( 'storiesRendered', this.responsiveImages, this );
        }, this );
    },

    renderStories: function() {
        var that = this,
            tplName = 'xpromoModuleItem',
            tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'xpromo/tpl/',
            $cont = this.$el.find( '.xpromo-inner' );
           // that.xpromoStoryCollection.sort(  );
        // if we have no items in our collection, we need to hide the module permanently
        if( this.xpromoStoryCollection.length < 1 ) {
            _.each( this.model.get( 'filters' ), function( val, key ) {
                return {
                    name: key,
                    props: _.extend( val, { order: 'hidden' } )
                };
            } );

            this.sleep();
            this.destroy();

            // this is a gnarly and dirty-feeling way of forcing a layout update after this module has been removed
            this.app.launchModuleContainerView.layoutModules();

            // no need to continue anything else
            return;
        }

        // if we have just one item
        else if( this.xpromoStoryCollection.length === 1 ) {
            this.$el.addClass( 'single-story' );
        }

        //empty the carousel items
        $cont.empty();

        //for each story item
        TemplateManager.get( tplName, function( tpl ) {

            that.xpromoStoryCollection.each( function( story ) {
                story.$el = $( tpl( story.toJSON() ) );
                $cont.append( story.$el );

                //if it has one child, then give it the active class
                $cont.children().addClass( $cont.children().length === 1 ? 'active' : null );
            } );

            that.$stories = that.$el.find( '.xpromo-story' );

            that.trigger( 'storiesRendered' );

        }, tplUrl );

        return this;
    },

    responsiveImages: function() {
        var that = this;

        this.$stories.each( function( index, story ) {
            var $story = $( story ).find( '.comm-story' ),
                imageUrl = $story.data( 'image' ),
                imageUrl_mobile = $story.data( 'imageMobile' ),
                imageUrl_max = $story.data( 'imageMax' );

            if( G5.breakpoint.value == 'mini' || ( !that.$el.hasClass( 'single-story' ) && G5.breakpoint.value == 'mobile' ) ) {
                $story.css( 'background-image', 'url(' + imageUrl_mobile + ')' );
            }
            else if( G5.breakpoint.value == 'desktopLarge' || G5.breakpoint.value == 'siteMax' ) {
                $story.css( 'background-image', 'url(' + imageUrl_max + ')' );
            }
            else {
                $story.css( 'background-image', 'url(' + imageUrl + ')' );
            }
        } );
    },

    processJson: function( myJson ) {
        _.each( myJson, function( mj ) {
            if ( mj.type === 'banner' ) {
                var classTxt = mj.data && mj.data.classes ? mj.data.classes : '';
                classTxt = classTxt.replace( /\,/g, '' );
                mj.data.classes = classTxt;
            }
            else {}
        } );

        return myJson;
    },

    //shamefull, model method inside a view, lazy, lazy
    loadStories: function( props ) {
        var that = this;
        props = _.extend( {}, this.$el.data(), props );

        // plugins passed to ajax cause issues
        // might be better to have selective pulls from $el.data('selectiveAttrName')
        if( props.spinner ) { delete props.spinner;}
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_XPROMO,
            data: props || {},
            success: function( servResp ) {
                //servResp.data.xpromos.reverse();
                that.xpromoStoryCollection.reset( that.processJson( servResp.data.xpromos ) );
            }
        } );
    }
} );
