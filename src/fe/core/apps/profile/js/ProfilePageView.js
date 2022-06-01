/*exported ProfilePageView */
/*global
PageView,
ProfilePagePersonalInfoTabView,
ProfilePageBadgesTabView,
ProfilePageAlertsAndMessagesTabView,
ProfilePageStatementTabView,
ProfilePageFollowListTabView,
ProfilePageActivityHistoryTabView,
ProfilePageDashboardTabView,
ProfilePageThrowdownStatsTabView,
ProfilePageSecurityTabView,
ProfilePagePreferencesTabView,
ProfilePageProxiesTabView,
ProfilePageGroupsSaveEditView,
ProfilePageView:true
*/
ProfilePageView = PageView.extend( {

    //override super-class initialize function
    initialize: function ( opts ) {
        'use strict';

        //ARNxyzzy// //console.log("[INFO] ARNlogging ProfilePageView initializing...");

        //set the appname (getTpl() method uses this)
        this.appName = 'profile';
        this.currentTabName = '';

        // this.$tabSwitcher = this.$el.find('#tabSwitcher');

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        // All nine tab view in either ViewingTabSet or EditTabSet
        this.PersonalInfoTabView            = new ProfilePagePersonalInfoTabView( { el: $( '#profilePagePersonalInfoTab' ) } );
        this.BadgesTabView                  = new ProfilePageBadgesTabView( {
            el: $( '#profilePageBadgesTab' ),
            tplName: 'profilePageBadgesTab',
            profilePageView: this
        } );
        this.AlertsAndMessagesTabView       = new ProfilePageAlertsAndMessagesTabView( { el: $( '#profilePageAlertsAndMessagesTab' ) } );
        this.StatementTabView               = new ProfilePageStatementTabView( { el: $( '#profilePageStatementTab' ) } );
        this.FollowListTabView              = new ProfilePageFollowListTabView( { el: $( '#profilePageFollowListTab' ) } );
        this.ActivityHistoryTabView         = new ProfilePageActivityHistoryTabView( { el: $( '#profilePageActivityHistoryTab' ) } );
        this.DashboardTabView               = new ProfilePageDashboardTabView( {
            el: $( '#profilePageDashboardTab' ),
            userId: opts.userId || null,
            profilePageView: this
        } );
        this.ThrowdownStatsTabView          = new ProfilePageThrowdownStatsTabView( { el: $( '#profilePageThrowdownStatsTab' ) } );
        this.SecurityTabView                = new ProfilePageSecurityTabView( { el: $( '#profilePageSecurityTab' ) } );
        this.PreferencesTabView             = new ProfilePagePreferencesTabView( {
            el: $( '#profilePagePreferencesTab' ),
            profilePageView: this
        } );
        this.ProxiesTabView             = new ProfilePageProxiesTabView( { el: $( '#profilePageProxiesTab' ) } );

        this.GroupsTabView   = new ProfilePageGroupsSaveEditView( { el: $( '#profilePageGroupsTab' ) } );


        // ROUTER IS NOW GLOBALNAV and it triggers FilterChange
        G5._globalEvents.on( 'route', this.navFilterChanged, this );

        // call to set initial route
        G5._globalEvents.trigger( 'route', {}, G5.props.CURRENT_ROUTE.length ? G5.props.CURRENT_ROUTE : [ 'profile', 'PersonalInfo' ], { trigger: true } );

        this.PersonalInfoTabView.on( 'updateAvatar', function( avatarUrl ) {
            // set the avatar in the page header
            $( '#participantProfileView' ).find( '.profile-pic' ).attr( 'src', avatarUrl );
        } );

        this.updatePartProf();

        // Watch for Model change in sidebar
        this.globalSidebar.partProf.model.on( 'change', this.updatePartProf, this );

        //ARNxyzzy// //console.log("[INFO] ARNlogging ProfilePageView ...initialized");
    },

    navFilterChanged: function( event, route ) {
        var newroute,
            name,
            params;
        if ( route && route[ 0 ] === 'profile' && route[ 1 ] ) {
            name = route[ 1 ];
            params = route.slice( 2 );

            if ( this[ name + 'TabView' ] ) {
                this.currentTabView = this[ name + 'TabView' ];
                this.currentTabName = name;
            }            else {
                this.currentTabView = this.PersonalInfoTabView;
                this.currentTabName = 'PersonalInfo';
            }

            this.currentTabView._params = params;
        }        else {
            // if a route has been passed that *might* work...
            if ( route && route.length >= 2 ) {
                newroute = route;
                newroute[ 0 ] = 'profile';

                G5._globalEvents.trigger( 'navigate', {}, newroute, { trigger: true } );
            } else {
                // otherwise, default to PersonalInfo
                G5._globalEvents.trigger( 'navigate', {}, [ 'profile', 'PersonalInfo' ], { trigger: true } );
            }
        }

        this.activateTab();
    },

    activateTab: function () {
        'use strict';
        var that = this,
            currentTabPane = $( '#profilePage' + this.currentTabName + 'Tab' );
        //ARNxyzzy// //console.log("[INFO] ARNlogging ProfilePageView.activateTab...");

        if ( !currentTabPane.hasClass( 'active' ) ) {

            // fix the height before we scroll and hide/show things
            this.$el.css( 'height', this.$el.height() );

            // scroll to the top of the page
            $.scrollTo( $( 'body' ), G5.props.ANIMATION_DURATION, {
                axis: 'y',
                onAfter: function() {
                    // unfix the height
                    that.$el.css( 'height', '' );
                }
            } );

            this.currentTabView.setElement( currentTabPane );
            $( '#profilePageShellActiveTabSet .tab-pane' ).removeClass( 'active' );
            currentTabPane.addClass( 'active' );

            if ( this.globalSidebar.isExpanded === true ) {
                this.globalSidebar.collapseSidebar();
            }

        }

        // Must be after the above
        this.currentTabView.activate();

        // this.$tabSwitcher.find('.tabSelected').html( this.$tabSwitcher.find('#tab-'+this.currentTabName).html() );

        //ARNxyzzy// //console.log("[INFO] ARNlogging ProfilePageView.activateTab");
    },

    updatePartProf: function () {
        'use strict';
        $( '#partProfFirstName' ).empty().append( this.getParticipantProfile( 'firstName' ) );
        $( '#partProfLastName' ).empty().append( this.getParticipantProfile( 'lastName' ) );

        // must pay attention to programs that don't use points
        if ( this.getParticipantProfile( 'points' ) === -1 ) {
            $( '#partProfPoints' ).parent().hide();
        }        else {
            $( '#partProfPoints' ).empty().append(
                $.format.number( this.getParticipantProfile( 'points' ) )
            );
        }

        //ARNxyzzy// //console.log("[INFO] ARNlogging ProfilePageView.updatePartProf");
    }

} );
