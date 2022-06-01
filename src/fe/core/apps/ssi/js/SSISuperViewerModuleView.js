/* exported SSISuperViewerModuleView */
/*global
LaunchModuleView,
SSISharedHelpersView,
SSISuperViewerModuleView:true
*/
SSISuperViewerModuleView = LaunchModuleView.extend( {

    initialize: function() {
        'use strict';

        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        var helpers = new SSISharedHelpersView( {} );
        // this.setStatus = _.bind(helpers.setStatus, this);
        this.requestWrap = _.bind( helpers.requestWrap, helpers );
    },

    render: function () {
        'use strict';

        this.on( 'templateLoaded', function( tpl, vars, subTpls ) {

            var listTpl = subTpls.contestListItems,
                $wrap = this.$el.find( '.ssiSuperViewerModuleView' ),
                data = {
                    $status: $wrap.find( '.contestStatus' ),
                    url: G5.props.URL_JSON_SSI_ACTIVE_CONTESTS,
                    data: {}
                };

            this.requestWrap( data )
                .then(
                    _.bind( function( servResp ) {
                        if ( !listTpl ) {
                            console.warn( '[SSI] listTpl not found in SSISuperViewerModuleView' );
                            return;
                        }

                        if ( servResp.data.activeContestsList ) {

                            servResp.data.activeContestsList = _.sortBy(
                                servResp.data.activeContestsList,
                                function ( contest ) { // make sure sort is case insensitive
                                    return contest.name.toLowerCase();
                                }
                            );
                        }

                        var html = listTpl( servResp.data );

                        $wrap.html( html );
                    }, this ),
                    function() {
                        console.error( '[SSI] error requesting data from G5.props.URL_JSON_SSI_ACTIVE_CONTESTS' );
                    }
                );
        } );

        LaunchModuleView.prototype.render.apply( this );

        return this;
    }

} );
