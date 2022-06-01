/*exported GlobalSidebarView */
/*global
TemplateManager,
ParticipantProfileView,
SidebarModule,
GlobalSidebarView:true
*/
GlobalSidebarView = Backbone.View.extend( {

    el: '.sidebar',

    //override super-class initialize function
    initialize: function( opts ) {
        'use strict';
        var that = this;
        this.options = opts;
        this.page = opts.page;

        //console.log('[INFO] GlobalSidebarView:', this, opts, this.options);

        //Set up a model for profile data
        this.model = new Backbone.Model();

        //listen for global data updates intended for parti. profile - and update data
        G5.ServerResponse.on( 'dataUpdate_participantProfile', function( data ) {
            this.model.set( data );
            // init size of sidebar - as height is based on header height
            G5.breakpoint.refreshValue();
            that.resizeListener();

            // MAKE THE SIDEBAR APPEAR
            that.$el.show();

            G5._globalEvents.trigger( 'partProfileUpdate', {} );
        }, this );

        // listen for full data load (id change) and render
        this.model.on( 'change:id', this.render, this );

        //Set Up Options for Sidebar
        this.options.globalSidebar = that.options.globalSidebar || G5.props.globalSidebar || {};
        this.options.globalSidebar.loggedIn = opts.loggedIn;
        this.partProf = new ParticipantProfileView();

        // need to append to #alert-box-inner: '<a href="layout.html?tplPath=apps/instantPoll/tpl/&amp;tpl=instantPollModule.html" data-page-id="instantPollModuleView" data-toggle="modal">Instant Poll</a>'

        this.partProf.on( 'profileLoaded', this.showDelegators, this );
        this.partProf.on( 'profileLoaded', this.renderDashboard, this );

        // listen to global nav routes to mark things as active in the sidebar
        G5._globalEvents.on( 'route', this.navFilterChanged, this );

        // set up a holding place for the dashboard tab to store data (or whatever)
        this.dashboard = {};

        // if logged in, Show Sidebar
        if( opts.loggedIn === true ) {
            this.render();
        }
        else {
            // otherwise remove the sidebar container from the DOM
            this.$el.remove();
            this.page.$sidebar = null;
            $( 'body' ).addClass( 'no-sidebar' );
            G5.views.globalSidebar = 'hidden';
        }
        //load upper sidebar
        this.on( 'rendered', this.postRender, this );

    }, // initialize

    events: {
        'click .sidebar-tab': 'handleSidebarTabs',
        'click .drawer-trigger': 'handleDrawerTrigger',
        'click .toggle-sidebar': 'toggleSidebar'
    },

    render: function() {
        'use strict';
        var that = this;

        //Load Sidebar Template
        TemplateManager.get( 'globalSidebarView', function( tpl, vars, subTpls ) {
            that.globalSidebarShowDelegatesTpl = subTpls.globalSidebarShowDelegatesTpl;
            that.globalSidebarShowRulesTpl = subTpls.globalSidebarShowRulesTpl;


            that.$el.find( '.sidebar-container' ).append( tpl( that.options.globalSidebar ) );

            that.trigger( 'rendered' );

        }, G5.props.URL_TPL_ROOT || G5.props.URL_BASE_ROOT + 'tpl/' );

        return this;
    }, // render

    postRender: function() {
        // Load Upper Sidebar Profile
        this.$el.find( '.upper-sidebar' ).append( this.partProf.el );
        this.partProf.render(); // wait until after append to call render

        //load rules for Rules Link
        this.loadRules();

        // cache convenient elements
        this.$body = $( 'body' );
        this.$tabs = this.$el.find( '.sidebar-tab' );
        this.$panels = this.$el.find( '.sidebar-panel' );
        this.transitionDuration = parseFloat( this.$el.css( 'transition-duration' ) );
        this.transitionDuration = this.transitionDuration < 1 ? this.transitionDuration * 1000 : this.transitionDuration;

        G5._globalEvents.on( 'windowResized', this.resizeListener, this );
        G5._globalEvents.on( 'windowScrolled', this.scrollListener, this );

        this.isFixed = false;
        this.isHeaderFixed = false;
        this.resizeListener();
        this.scrollListener();

        this.activateInitialTab();

        this.navFilterChanged();


    },

    renderDashboard: function() {
        var that = this;
        this.dashboard.modules = this.partProf.model.get( 'applicableSidebarModules' );
        this.dashboard.views = [];
        this.dashboard.$el = this.$el.find( '.sidebar-panel.dashboard' );

        if( !this.dashboard.modules || this.dashboard.modules.length <= 0 ) {
            this.$el.addClass( 'dashboard-hidden' );
            this.activateInitialTab( '.settings' );
            return false;
        }

        _.each( this.dashboard.modules, function( module ) {
            var ucName = module.name.charAt( 0 ).toUpperCase() + module.name.slice( 1 );
            this.dashboard.views.push( new window[ ucName + 'View' ]( {
                model: new SidebarModule( module ),
                sidebar: this
            } ) );
        }, this );

        _.each( this.dashboard.views, function( view ) {
            view.on( 'renderDone', this.navFilterChanged, this );
            view.render();
            this.dashboard.$el.append( view.$el );
            that.resizeListener();
        }, this );

        this.on( 'tabActivated', function( $t, $p ) {
            if( $p.hasClass( 'dashboard' ) ) {
                _.each( this.dashboard.views, function( view ) {
                    view.updateView();
                } );
            }
        }, this );

        this.on( 'sidebarCollapsed', function() {
            this.scrollListener();
        }, this );

        this.on( 'sidebarExpanded', function() {
            this.scrollListener();

            _.each( this.dashboard.views, function( view ) {
                view.updateView();
            } );
        }, this );

        G5._globalEvents.on( 'breakpointChanged', function() {
            _.each( this.dashboard.views, function( view ) {
                view.updateView();
            } );

            this.scrollListener( true );
        }, this );

         this.$el.find( '.delegate [data-toggle=tooltip]' ).tooltip();
    },

    scrollListener: function( remeasure ) {
        var $sidebar = this.$el,
            $partProf = $sidebar.find( '#participantProfileView' ),
            $profCont = $partProf.find( '.profile-container' ),
            scroll = window.pageYOffset,
            pcH,
            pcMH,
            resetSidebar;

        resetSidebar = function() {
            $sidebar.removeClass( 'abs-header fix-header' );
            $partProf.css( 'padding-top', '' );
            $profCont.css( 'margin-bottom', '' );
        };

        // if the sidebar is collapsed we bail
        if( $sidebar.length && this.isExpanded === false ) {
            resetSidebar();
            return false;
        }

        // just in case the height of the sidebar/body isn't right
        if( G5.breakpoint.value == 'mini' || G5.breakpoint.value == 'mobile' || G5.breakpoint.value == 'tablet' ) {
            if( $sidebar.outerHeight( true ) != this.$body.height() ) {
                this.$body.css( 'height', $sidebar.outerHeight( true ) );
            }
        }

        // only do the work if we have a sidebar
        if( $sidebar.length ) {
            pcH = $profCont.data( 'origHeight' ) || $profCont.height();
            pcMH = parseInt( $profCont.css( 'min-height' ) );

            // if the profile height is equal to or smaller than the min-height, the measuring isn't done or is wrong
            // let's bail and try again later
            if( pcH <= pcMH ) {
                return false;
            }

            // if we need to remeasure
            if( remeasure === true ) {
                // but only if our header isn't fixed
                if( this.headerIsFrozen === true ) {
                    return false;
                }

                // stalling to wait for the sidebar transition
                setTimeout( function() {
                    pcH = $profCont.find( '.profile-avatar' ).outerHeight() + $profCont.find( '.profile-meta' ).outerHeight();
                    $profCont.data( 'origHeight', pcH );
                }, this.transitionDuration );
            }

            // store the original height so we can retrieve it for later when measuring doesn't work
            $profCont.data( 'origHeight', $profCont.data( 'origHeight' ) || pcH );

            // if we scroll past the min height...
            if( scroll > pcMH ) {
                // console.log( 'sidebarfreeze: past min height' );

                // change over to the absolute positioning to make animations work
                $sidebar.addClass( 'abs-header' );
                $partProf.css( 'padding-top', pcH );
                $profCont.css( 'margin-bottom', ( -1 * pcH ) );

                // console.log( 'sidebarfreeze: stuff ', scroll, $sidebar.offset().top, pcH, pcMH, ' ... ', this.page.m.sidebar.top );
                // console.log( 'sidebarfreeze: stuff ', this.page.m.sidebar.top, pcMH - pcH );

                // if we scroll past the point where the min-height is all that's left
                if( this.page.m.sidebar.top < pcMH - pcH ) {
                    $sidebar.addClass( 'fix-header' );
                    $profCont.css( 'margin-bottom', '' );

                    this.headerIsFrozen = true;
                }
                else {
                    $sidebar.removeClass( 'fix-header' );

                    this.headerIsFrozen = false;
                }
            }
            else {
                // console.log( 'sidebarfreeze: false and reset' );

                resetSidebar();
            }
        }
    },

    toggleSidebar: function( e ) {
        e.preventDefault();

        if( G5.breakpoint.value == 'mini' || G5.breakpoint.value == 'mobile' || G5.breakpoint.value == 'tablet' ) {
            if( this.isExpanded === false ) {
                this.expandSidebar();
            }
            else {
                this.collapseSidebar();
            }
        }
    },

    collapseSidebar: function() {
        if( this.isExpanded === false ) {
            return;
        }

        var that = this;

        if( G5.breakpoint.value == 'mini' || G5.breakpoint.value == 'mobile' || G5.breakpoint.value == 'tablet' ) {
            this.$body
                .addClass( 'sidebar-collapsed' )
                .removeClass( 'sidebar-expanded' );
        }
        else {
            return false;
        }

        this.isExpanded = false;

        this.trigger( 'sidebarCollapsed' );

        if( G5.breakpoint.value == 'mini' || G5.breakpoint.value == 'mobile' || G5.breakpoint.value == 'tablet' ) {
            setTimeout( function() {
                that.$body
                    .addClass( 'sidebar-hidden' )
                    .css( 'height', '' );
            }, this.transitionDuration );
        }
    },
    expandSidebar: function() {
        if( this.isExpanded === true ) {
            return;
        }

        var that = this;

        this.$body
            .removeClass( 'sidebar-collapsed sidebar-hidden' )
            .addClass( 'sidebar-expanded' );

        setTimeout( function() {
            if( G5.breakpoint.value == 'mini' || G5.breakpoint.value == 'mobile' || G5.breakpoint.value == 'tablet' ) {
                that.$body.css( 'height', that.$el.outerHeight( true ) );
            }

            that.isExpanded = true;

            that.trigger( 'sidebarExpanded' );
        }, this.transitionDuration );
    },

    resizeListener: function() {
        var $sidebar = this.$el;
        var breakpoint = G5.breakpoint.value;

        $sidebar.css( { 'min-height': window.innerHeight } );

        if( breakpoint == 'desktop' || breakpoint == 'desktopLarge' || breakpoint == 'siteMax' ) {
            //desktop
            this.expandSidebar();
            $( 'body' ).removeClass( 'sidebar-expanded' );
            $sidebar.removeClass( 'sidebar-tablet' );
            $sidebar.removeClass( 'sidebar-mobile' );
        }
        else if( breakpoint == 'tablet' ) {
            if( $sidebar.hasClass( 'sidebar-tablet' ) ) {
                return false;
            }
            //tablet
            this.collapseSidebar();
            $sidebar.addClass( 'sidebar-tablet' );
            $sidebar.removeClass( 'sidebar-mobile' );
        }
        else {
            if( $sidebar.hasClass( 'sidebar-mobile' ) ) {
                return false;
            }
            //mobile
            this.collapseSidebar();
            $sidebar.addClass( 'sidebar-mobile' );
            $sidebar.removeClass( 'sidebar-tablet' );
        }
    },

    // Sidebar Manual Tab Function
    handleSidebarTabs: function( e ) {
        e.preventDefault();

        // if we can see both panels, bail out
        if( G5.breakpoint.value == 'desktopLarge' || G5.breakpoint.value == 'siteMax' ) {
            return false;
        }

        var $tab = $( e.target ).closest( '.sidebar-tab' );

        this.activateTab( $tab );

        this.scrollListener();
    },

    // on load, we activate one of the tabs (or both)
    activateInitialTab: function( selector ) {
        // if we can see both panels, bail out
        if( this.$panels.filter( ':visible' ).length > 1 ) {
            return false;
        }

        this.activateTab( selector ? selector : 0 );
    },

    activateTab: function( criteria ) {
        var $tab,
            $panel;

        // if an integer is passed, assume it's an index
        if( typeof criteria === 'number' ) {
            $tab = this.$tabs.filter( ':eq(' + criteria + ')' );
        }
        // if a jQuery object is passed, it better be the tab, because we're picking it
        else if( criteria instanceof jQuery ) {
            $tab = criteria;
        }
        // strings are assumed to be selectors
        else if( typeof criteria === 'string' ) {
            $tab = this.$tabs.filter( criteria );
        }
        // otherwise, pick the first one
        else {
            $tab = this.$tabs.filter( ':eq(0)' );
        }

        $panel = this.$el.find( $tab.find( 'a' ).attr( 'href' ) );

        this.$tabs.removeClass( 'active' );
        $tab.addClass( 'active' );
        this.$panels.removeClass( 'active' );
        $panel.addClass( 'active' );

        this.trigger( 'tabActivated', $tab, $panel );
    },

    //sidebar expandable links
    handleDrawerTrigger: function( e ) {
        e.preventDefault();

        var $trigger = $( e.target ).closest( '.drawer-trigger' ),
            $drawer = this.$el.find( $trigger.attr( 'href' ) );

        $trigger.toggleClass( 'expanded collapsed' );
        $drawer.toggleClass( 'expanded collapsed' );
    },

    showDelegators: function() {
        var data = this.partProf.model.get( 'delegators' );

        if( data && data.length ) {
            data.termsAcceptedMsg = this.partProf.model.get( 'termsAcceptedMsg' );
            var delegatesTemplate = this.globalSidebarShowDelegatesTpl( data );
            this.$el.find( '.delegatesAct' ).html( delegatesTemplate );
        }
    },

    loadRules: function() {
        var that = this;
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_RULES,
            data: {},
            success: function( servResp ) {
                if( servResp.data.promotions.length ) {
                    that.showRules( servResp.data.promotions );
                }
                else {
                    that.hideRules();
                }
            }
        } );
    },

    showRules: function( data ) {
        if( data && data.length ) {
            var rulesTemplate = this.globalSidebarShowRulesTpl( data );
            this.$el.find( '.drawer.rules' ).append( rulesTemplate );
        }
    },

    hideRules: function() {
        this.$el.find( '.sidebar-section.sidebar-rules' ).remove();
    },

    navFilterChanged: function( event, route ) {
        var $items = this.$el.find( '.item' );

        route = route || G5.props.CURRENT_ROUTE;

        $items.removeClass( 'selected' );

        if( route && route.length ) {
            $items
                .filter( function() {
                    var itemRoute = $( this ).data( 'route' ),
                        currRoute = route.toString().replace( /,/g, '/' );

                    return currRoute.indexOf( itemRoute ) >= 0 ? true : false;
                } )
                .addClass( 'selected' );
        }

        if( $items.filter( '.selected' ).length ) {
            var $drawer = $items.filter( '.selected' ).closest( '.drawer' ),
                $panel = $items.filter( '.selected' ).closest( '.sidebar-panel' );

            if( !$panel.hasClass( 'active' ) ) {
                this.activateTab( $panel.index() );
            }

            if( $drawer.hasClass( 'collapsed' ) ) {
                $panel.find( 'a[href="#' + $drawer.attr( 'id' ) + '"]' ).click();
            }
        }
    },


} );
