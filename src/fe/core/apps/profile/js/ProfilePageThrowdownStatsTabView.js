/*exported ProfilePageThrowdownStatsTabView */
/*global
TemplateManager,
ProfilePageThrowdownStatsTabView:true
*/
ProfilePageThrowdownStatsTabView = Backbone.View.extend( {

    initialize: function (  ) {
        'use strict';

        this.on( 'templateloaded', function() {
			this.$el.find( '.td-matches-schedule tbody tr:even' ).addClass( 'stripe' );
        } );
    },

    activate: function () {
        'use strict';

        this.render();

    },
	events: {
		'click .profile-popover': 'attachParticipantPopover',
        'change #promotionSelect': 'changeTdStatsPromo'
	},
    render: function () {
        'use strict';

        var that    = this,
            tplName = 'profilePageThrowdownStatsTab',
            tplUrl  = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/';
        this.$cont = this.$el;

        // if there is no html in the tab content element, go get the remote contents
        if ( this.$cont.html().length === 0 ) {
            this.$el
                .append( '<span class="spin" />' )
                .find( '.spin' ).spin();

            TemplateManager.get( tplName,
                function ( tpl ) {
                    that.$cont.empty().append( tpl( 0 ) );  //loads a template without any args i.e. a static view
                    that.trigger( 'templateloaded' );

                },
                tplUrl );
        } else {
            // otherwise, just trigger the completion event
            this.trigger( 'templateloaded' );
        }

    },
	attachParticipantPopover: function( e ) {
        var $tar = $( e.target ),
            isSheet = ( $tar.closest( '.modal-stack' ).length > 0 ) ? { isSheet: true } : { isSheet: false };
        if ( $tar.is( 'img' ) ) {
            $tar = $tar.closest( 'a' );
        }
        isSheet.containerEl = this.$el;
        //attach participant popovers
        if ( !$tar.data( 'participantPopover' ) ) {
            $tar.participantPopover( isSheet ).qtip( 'show' );
        }
        e.preventDefault();
    },
    changeTdStatsPromo: function() {
        var that = this;

        //submit the throwdown tab page data
        that.$el.find( '#profilePagePlayerStatsTab' ).submit();

        //empty the page
        that.$el.find( '#tabPlayerStatsCont' ).empty();

        //re-render the page
        that.$el.find( '#tabPlayerStatsCont' )
            .load(
                G5.props.URL_HTML_THROWDOWN_PUBLIC_PROFILE,
                { responseType: 'html' },
                function( responseText ) {
                    G5.serverTimeout( responseText );
                }
            );

    }

} );
