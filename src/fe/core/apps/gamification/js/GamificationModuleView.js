/*exported GamificationModuleView */
/*global
GamificationDataView,
SidebarModuleView,
GamificationModuleView:true
*/

/**
    Module for Gamification application.
    <li>ModuleContainerView: found CUSTOM module view </li>
    @class
    @extends SidebarModuleView
*/
GamificationModuleView = SidebarModuleView.extend( {

    //override super-class initialize function
    initialize: function ( opts ) {
        'use strict';
        //ARNxyzzy//// console.log("[INFO] GamificationModuleView.initialize          ARNlogging");
        //ARNxyzzy////debugger;

        //this is how we call the super-class initialize function (inherit its magic)
        SidebarModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass ModuleView
        this.events = _.extend( {}, SidebarModuleView.prototype.events, this.events );

        this.dashboard = opts.sidebar.dashboard;

        this.on( 'templateLoaded', this.postRender, this );
    },

    postRender: function() {
        //add the gamification model view
        this.gamificationDataView = new GamificationDataView( {
            el: this.$el.find( '.sidebar-list.badges' )
        } );

        // start the loading state and spinner
        this.dataLoad( true );

        // do something after the model view is done rendering
        this.gamificationDataView.on( 'renderDone', function () {
            // stop the loading state and spinner
            this.dataLoad( false );

            switch ( this.dashboard.modules.length ) {
                case 1:
                    this.$el.addClass( 'state-large' );
                    break;
                case 4:
                    this.$el.addClass( 'state-small' );
                    break;
                default:
                    this.$el.addClass( 'state-medium' );
            }

            this.trigger( 'renderDone' );
        }, this );
    }
} );
