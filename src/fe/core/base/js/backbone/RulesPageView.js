/*exported RulesPageView */
/*global
PageView,
RulesPromotionCollectionView,
RulesPageView:true
*/
RulesPageView = PageView.extend( {

    //override super-class initialize function
    initialize: function( opts ) {
        //set the appname (getTpl() method uses this)
        this.appName = 'rules';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        //retrieve the leaderboard data
        //add the leaderboard model view
        this.rulesPromotionCollectionView = new RulesPromotionCollectionView( {
            el: this.$el,
            promoId: opts.promoId ? opts.promoId : null
        } );

        // ROUTER IS NOW GLOBALNAV and it triggers FilterChange
        G5._globalEvents.on( 'route', this.navFilterChanged, this );

        if( G5.props.CURRENT_ROUTE.length && G5.props.CURRENT_ROUTE[ 0 ] == 'rules' ) {
            this.rulesPromotionCollectionView.on( 'renderDone', function() {
                // call to set initial route
                G5._globalEvents.trigger( 'route', {}, G5.props.CURRENT_ROUTE, { trigger: true } );
            }, this );
        }
    },

    navFilterChanged: function( event, route ) {
        if( route && route[ 0 ] == 'rules' && route[ 1 ] ) {
            var id = route[ 1 ];

            this.rulesPromotionCollectionView.displayRulesById( id );

            if( this.globalSidebar.isExpanded === true ) {
                this.globalSidebar.collapseSidebar();
            }
        }
    }

} );
