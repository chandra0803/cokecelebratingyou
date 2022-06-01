/*exported LaunchModuleView */
/*global
TemplateManager,
LaunchModuleView:true
*/
LaunchModuleView = Backbone.View.extend( {

    tagName: 'div',

    className: 'launchModule',

    alertTpl: '<div class="alert "><button type="button" class="close">×</button><strong></strong><span></span></div>',

    initialize: function( opts ) {

        this.app = opts.app || false;
        this.moduleCollection = this.model.collection;

        //add a style class that identifies this by app and module name
        this.$el.addClass(
            this.model.get( 'appName' )
            + ' ' + this.model.get( 'name' )
            + ' ' + ( this.getBaseName() || '' ) // this is useful in certain cases
        );

        //start out hidden to avoid any trouble
        // this.$el.css('display','none');
    },

    // return everything before the word 'Module' in the name
    getBaseName: function() {
        var baseName = this.model.get( 'name' ).match( /(.+)Module/ );

        if( baseName && baseName.length === 2 ) {
            return baseName[ 1 ];
        } else {
            return null;
        }
    },

    destroy: function() {
        this.undelegateEvents();
        this.remove();
    },

    render: function() {
        var that = this;

        this.getTemplateAnd( function( tpl ) {
            //when template manager has the template, render it to this element
            that.$el.append( tpl( _.extend( {}, that.model.toJSON(), { cid: that.cid } ) ) );

            // store the .module-liner(s)
            that.$liner = that.$el.find( '.module-liner' );

            that.trigger( 'layoutChange', 'renderDone' );
        } );

        return this;//chaining
    },

    //get a template using the template manager, and execute a callback
    getTemplateAnd: function( callback ) {

        var that = this;

        //use the module name, or template property if set, to find a template
        var tmplBaseName = this.model.get( 'templateName' ) || this.model.get( 'name' );

        //if set, this will be used by template manager to find the template
        var url = G5.props.URL_TPL_ROOT || this.getAppUrl() + 'tpl/';

        TemplateManager.get( tmplBaseName, function( tpl, vars, subTpls ) {
            that.subTpls = subTpls;
            that.tplVariables = vars;
            callback( tpl );
            //this trigger is usefull for any module which needs to do sth after template load
            that.trigger( 'templateLoaded', tpl, vars, subTpls );
        }, url );

        return this;//chaining

    },

    //get base url for this Module(g5 app)
    getAppUrl: function() {
        var appName = this.getAppName();
        return appName ? G5.props.URL_APPS_ROOT + appName + '/' : G5.props.URL_BASE_ROOT;
    },

    //get app name for this module
    getAppName: function() {
        return this.model.get( 'appName' );
    },

    pageUpdate: function() {
        this.trigger( 'pageUpdate' );
    },

    //shut this guy down, hide and stop resource draining stuff
    sleep: function( $container ) {
        var that = this;

        this.$el.appendTo( $container );

        this.$el.animate( { opacity: 0 }, {
            duration: G5.props.ANIMATION_DURATION,
            done: function() {
                that.$el.removeClass( 'woke' );
                that.$el.detach();
                that.trigger( 'layoutChange', 'slept' );
            }
        } );
    },

    //wake up and start draining resources!
    wake: function( $container ) {
        var that = this;

        this.$el.appendTo( $container );

        if( !this.model.isHiddenForCurrentFilter() ) {
            this.$el.animate( { opacity: 1 }, {
                duration: G5.props.ANIMATION_DURATION,
                done: function() {
                    that.$el.addClass( 'woke' );
                    that.trigger( 'layoutChange', 'woke' );
                }
            } );
        }
    },

    // // module sizing based on scroll and resize events
    // // must be explicitly bound in each instance of LaunchModuleView BECAUSE PERFORMANCE
    // // commented out for 6.1 because I learned how to do ratio sizing with CSS
    // sizeModule: function() {
    //     if(!this.$liner) {
    //         return false;
    //     }

    //     var $mod = this.$liner.not('.module-empty'),
    //         data = $mod.data().size,
    //         methods = {};

    //     // ratio sizing
    //     methods.ratio = function() {
    //         G5.util.sizeByRatio($mod, data.ratio, ['height']);
    //     };

    //     if(data && data.ratio) {
    //         methods.ratio();
    //     }
    // },

    //all module views get these events
    events: {

    },

    isOnScreen: function() {
        var winTop = window.pageYOffset || $( 'html' ).scrollTop(),
            winBot = winTop + $( window ).innerHeight(),
            myTop = this.$el.offset().top,
            myBot = myTop + this.$el.height(),
            inVwPt = ( myTop > winTop && myTop < winBot ) || // top in window
                    ( myBot > winTop && myBot < winBot ) || // bottom in window
                    ( myTop < winTop && myBot > winBot ); // middle in window (top and bot not in window)
        return inVwPt && this.$el.is( ':visible' );
    },

    dataLoad: function( start, opts ) { // use to put module into/outof dataLoading state -- pass true to start, false to stop (for display purposes only)
        var $mod = this.$el;

        opts = _.extend( {
            className: 'dataLoading',
            spinner: true
        }, opts );

        // add/remove a loading class
        $mod[ start ? 'addClass' : 'removeClass' ]( opts.className );
        // spin/unspin the module if option was passed
        this.spinModule( opts.spinner && start ? true : false, opts );

        if( start === false ) {
            this.trigger( 'layoutChange', 'dataLoadDone' );
        }
    },

    spinModule: function( start, opts ) { //use this to start or stop a spinner on the module -- pass true to start, false to stop
        var $mod = this.$el;

        if( start ) {
            G5.util.showSpin( $mod, opts );
        }
        else {
            G5.util.hideSpin( $mod );
        }
    }

} );
