/*exported LaunchModuleContainerView */
/*global
LaunchModuleLayoutManager,
LaunchModuleContainerView:true
*/
//TODO: CLEAN THIS FILE

LaunchModuleContainerView = Backbone.View.extend( {

    tagName: 'div',

    moduleViews: [], //current module views in our DOM container

    initialize: function( opts ) {

        //a reference to the main app
        this.app = opts.app || false;
        // this.loadOnInit = opts.loadOnInit||false;
        this.loadOnInit = false; // always false for now

        this.sleeping = false;

        this.$woke = this.$el.find( '.modules-woke' );
        this.$slept = this.$el.find( '.modules-slept' );

        //listen for add event on moduleCollection
        this.model.on( 'add', function( moduleModel ) {
            //console.log('[INFO] LaunchModuleContainerView: model ADD event');
            this.addModuleView( moduleModel );
            this.layoutModules();
        }, this );

        //listen for reset event on moduleCollection
        this.model.on( 'reset', function() {
            //console.log('[INFO] LaunchModuleContainerView: model RESET event - rendering and laying out modules');
            this.render();
            this.layoutModules();
        }, this );

        //listen for changes to the module models
        this.model.on( 'change', function() {
            //console.log('[INFO] LaunchModuleContainerView: model CHANGE event - laying out modules');
            this.layoutModules();
        }, this );

        //listen for filter changes on the collection
        this.model.on( 'filterChanged', function( filter, oldfilter ) {
            this.$el.removeClass( 'filter-' + oldfilter ).addClass( 'filter-' + filter );
            this.$el[ this.model.getFilter() == this.options.userHomeFilter ? 'addClass' : 'removeClass' ]( 'is-home' );
            //refresh the layout on filter change
            this.layoutModules();
        }, this );


        //set the layout manager
        this.launchLayoutManager = new LaunchModuleLayoutManager( this );

    },

    //add a single modView to the container
    addModuleView: function( module ) {
        var pageUpdate;

        //FIND THE VIEW
        var modName = module.get( 'name' );

        //defaults to modName if viewName is false
        var modViewName = module.get( 'viewName' ) || modName;

        //this will be populated with the backbone view instance
        var modView = false;

        //default module view name
        var viewName = 'LaunchModuleView';

        //if no view name possibilities, then log it and return
        if( !modViewName ) {
            console.log( '[ERROR] LaunchModuleContainerView: module must have "viewName" or "name" in order to find a backbone View' );
            return;
        }

        //look for custom backbone view object to display this module model
        var ucName = modViewName.charAt( 0 ).toUpperCase() + modViewName.slice( 1 );

        //custom view names = upper-case name of the module +'View'
        var potViewName = ucName + 'View';

        //if the view name exists as a function, we assume it is our view obj constructor
        if( typeof window[ potViewName ] === 'function' ) {
            //found custom view for this name
            viewName = potViewName;
            //console.log('[INFO] LaunchModuleContainerView: found CUSTOM module view ['+modName+' -> '+viewName+']');
        }else{
            //no custom view for this name
            //console.log('[INFO] LaunchModuleContainerView: DEFAULT module view ['+modName+' -> '+viewName+']');
        }

        //instantiate the proper view (NOTE: expects the viewName to live on window -- might change)
        modView = new window[ viewName ]( { model: module, app: this.app } );

        //add the view to our array of views
        this.moduleViews.push( modView );


        // LOADING CONTENT...

        // LOADING STRATEGY A -- straight up render straight away
        //append this module to our DOM element
        if( this.loadOnInit ) {
            // modView.wake(this.$woke);
        }

        // LOADING STRATEGY B -- delay render
        else {
            // append the module to our DOM with a temporary div, do not yet render()!
            // modView.wake(this.$woke);

            // append an empty module-liner for the empty state
            modView.$el.append( '<div class="module-liner module-empty"></div>' );


            // PageUpdate is called when the page changes ('filter change etc.')
            pageUpdate = function() {
                if( modView.$el.find( '.module-empty' ).length ) {

                    // if module is visible on this filter and doesn't yet have a spinner, add it
                    if( modView.$el.is( ':visible' ) && !modView.$el.find( '.spin' ).length ) {
                        // add loading visuals
                        modView.$el
                            .find( '.module-empty' ).append( '<span class="spin" />' )
                            .find( '.spin' ).spin( { color: '#fff' } );
                    }

                    // if module is visible to the user inside the window (it's been scrolled into view)
                    if( modView.isOnScreen() ) {
                        // render it
                        modView.render();
                        // remove the pageUpdate check
                        modView.off( 'pageUpdate', pageUpdate );
                        // remove the scroll listener so it doesn't render twice
                        $( window ).off( 'scroll.' + modName );
                    }
                }
            };

            // on pageUpdate, if visible and in viewport but not previously rendered, render
            modView.on( 'pageUpdate', pageUpdate, this );

            // on templateLoaded, remove spinner
            modView.on( 'templateLoaded', function() {
                // after the template is loaded, there may still be activity with
                // loading of a module, but we can't have a generic visual indicator
                // for these specific cases. Slow module detail loading will have to
                // be identified and handled on a case-by-case basis.
                modView.$el.find( '.module-empty' ).remove();
            }, this );

            // listen to various module layout changing events
            modView.on( 'layoutChange', function( type ) {
                this.trigger( 'moduleLayoutChange', {
                    type: type,
                    view: modView
                } );
            }, this );

            // on scroll, if visible and in viewport but not previously rendered, render
            // using a namespaced event so we can remove it later :)
            $( window ).on( 'scroll.' + modName, function() {

                // module visible
                if( modView.isOnScreen() ) {
                    // kill event
                    $( window ).off( 'scroll.' + modName );
                    // kill pageUpdate event too so it doesn't render twice
                    modView.off( 'pageUpdate' );
                    //  and has module-empty.module-liner
                    if( modView.$el.find( '.module-empty' ).length ) {
                        // modView.$el.find('.module-empty').remove();
                        modView.render();
                    }
                }
            } );

        } // /else

        // EOF LOADING STRATEGY

    },

    //render this element and its modules into DOM
    render: function() {
        //console.log('render');
        _.each( this.moduleViews, function( v ) {
            v.destroy();
        } );
        this.moduleViews = [];//empty it

        var that = this;

        _.each( this.model.models, function( module ) {
            that.addModuleView( module );
        } );

        this.$el.addClass( 'filter-' + this.model.getFilter() );
        this.$el[ this.model.getFilter() == this.options.userHomeFilter ? 'addClass' : 'removeClass' ]( 'is-home' );
        this.rendered = true;
    },

    //layout the moduleViews
    layoutModules: function() {
        if( this.launchLayoutManager && !this.sleeping && this.rendered ) {
            this.launchLayoutManager.updateLayout( this.model.getFilter() );
        }
    },

    //moduleViews getter
    getModuleViews: function() {
        return this.moduleViews;
    },

    //hide the collection (and modviews) and perform necessary hygiene (turn off setTimeout stuff happeing, animations etc.)
    sleep: function() {
        var that = this;

        if( this.sleeping ) {return;}//already sleeping

        this.sleeping = true;

        //this.transitionEffect('left','slide',true);
        //this.$el.fadeOut(function(){
            _.each( that.moduleViews, function( mv ) {
                mv.sleep( that.$slept );
            } );
        //});


    },

    //activate this view (and modules)
    wake: function() {
        var that = this;
        if( !this.sleeping ) {return;}//already awake

        this.sleeping = false;
        //this.$el.fadeIn();

        _.each( this.moduleViews, function( mv ) {
            mv.wake( that.$woke );
        } );
    }

} );
