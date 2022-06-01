/*exported SidebarModuleView */
/*global
TemplateManager,
SidebarModuleView:true,
*/
SidebarModuleView = Backbone.View.extend( {

    tagName: 'div',

    className: 'sidebarModule sidebar-section',

    alertTpl: '<div class="alert "><button type="button" class="close">×</button><strong></strong><span></span></div>',

    initialize: function( opts ) {

        this.app = opts.app || false;

        //add a style class that identifies this by app and module name
        this.$el.addClass(
            this.model.get( 'appName' )
            + ' ' + this.model.get( 'name' )
            + ' ' + ( this.getBaseName() || '' ) // this is useful in certain cases
        );
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

    //shut this guy down, hide and stop resource draining stuff
    sleep: function() {

        this.$el.hide();

    },

    //wake up and start draining resources!
    wake: function() {

        if( !this.model.isHiddenForCurrentFilter() ) {
            this.$el.show();
        }

    },

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
        this.spinModule( opts.spinner && start ? true : false );
    },

    spinModule: function( start ) { //use this to start or stop a spinner on the module -- pass true to start, false to stop
        var $mod = this.$el;

        if( start ) {
            G5.util.showSpin( $mod, { spinopts: { color: '#fff' } } );
        }
        else {
            G5.util.hideSpin( $mod );
        }
    },

    updateView: function() {
        // console.log('updateView', this);
    }

} );
