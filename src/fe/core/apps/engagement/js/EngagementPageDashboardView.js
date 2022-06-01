/*exported EngagementPageDashboardView */
/*global
PageView,
EngagementModelView,
EngagementPageDashboardView:true
*/
EngagementPageDashboardView = PageView.extend( {
    initialize: function() {
        //console.log('[INFO] EngagementPageDashboardView: initialized', this);
        var defaults = {
            mode: 'user'
        };
        // this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        // inherit events from the superclass PageView
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        this.options = $.extend( true, {}, defaults, this.options );

        this.modelView = new EngagementModelView( {
            el: this.$el.find( '.engagementModelView' ),
            mode: this.options.mode,
            // userId is only used by user mode, nodeId by team mode
            userId: this.options.userId || null,
            nodeId: this.options.nodeId || null
        } );

        // ROUTER IS NOW GLOBALNAV and it triggers FilterChange
        G5._globalEvents.on( 'route', this.handleRoute, this );

        // call to set initial route
        G5._globalEvents.trigger( 'route', {}, G5.props.CURRENT_ROUTE.length ? G5.props.CURRENT_ROUTE : [], { trigger: true } );

        this.modelView.on( 'tabActivated', function( $tab, view ) {
            G5._globalEvents.trigger( 'navigate', {}, [ view.nameId ], { trigger: true } );
        }, this );
    },

    events: {},

    render: function() {

        // this.$el.find('.span12').html( this.modelView.$el );
    },

    handleRoute: function( event, route ) {
        var name;
        if ( route && route[ 0 ] ) {
            name = route[ 0 ];

            this.modelView.handleRoute( name );
        }
    }
} );
