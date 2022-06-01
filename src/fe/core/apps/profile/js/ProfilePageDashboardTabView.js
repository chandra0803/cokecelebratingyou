/*exported ProfilePageDashboardTabView */
/*global
TemplateManager,
EngagementModelView,
ProfilePageDashboardTabView:true
*/
ProfilePageDashboardTabView = Backbone.View.extend( {
    initialize: function( opts ) {
        //console.log('[INFO] ProfilePageDashboardTabView: initialized', this);

        var settings = {
            mode: 'user'
        };

        this.settings = $.extend( true, {}, settings, opts );

        this.tplName    = this.settings.tplName || 'profilePageDashboardTab';
        this.tplUrl     = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/';
    },

    activate: function () {
        var that = this;

        // check to see if the modelView exists yet
        // if not, we can assume that this view has never been rendered before
        if ( !this.modelView ) {
            TemplateManager.get( this.tplName, function( tpl ) {
                that.$el.html( tpl( {} ) );

                that.setUpModelView();
            }, this.tplUrl );
        } else {
            // if it does exist, we assume the view has been rendered
            // when we're navigating via Backbone router, we need to activate the tab stored in the route
            this.modelView.summaryView.showTabByName( this._params[ 0 ] );
            // we need to reactivate the current tab in the summary view
            this.$el.find( '.engagementModelView .engagementSummaryCollectionView .tab.active' ).removeClass( 'active' ).find( 'a' ).tab( 'show' );
        }
    },

    events: {},

    setUpModelView: function() {
        this.modelView = new EngagementModelView( {
            el: this.$el.find( '.engagementModelView' ),
            mode: this.settings.mode,
            summaryStartTab: this._params[ 0 ],
            userId: this.settings.userId || null
        } );

        this.modelView.on( 'tabActivated', function( $tab, view ) {
            // call to set initial filter if router triggers before loaded
            G5._globalEvents.trigger( 'navigate', {}, [ 'profile', 'Dashboard', view.nameId ], { trigger: true } );
        }, this );

        // listen for changes to the RPM module in the dashboard
        G5._globalEvents.on( 'EngagementModuleViewChanged', function( event, $tab, view ) {
            G5._globalEvents.trigger( 'navigate', event, [ 'profile', 'Dashboard', view.nameId ], { trigger: true } );
        }, this );
    }
} );
