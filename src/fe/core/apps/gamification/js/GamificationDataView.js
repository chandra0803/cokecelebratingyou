/*exported GamificationDataView */
/*global
TemplateManager,
GamificationCollection,
GamificationDataView:true
*/

//debugger;
GamificationDataView = Backbone.View.extend( {

    //override super-class initialize function
    initialize: function ( opts ) {
        'use strict';
        //ARNxyzzy// console.log("[INFO] GamificationDataView.initialize  ...creating GamificationCollection          ARNlogging");
        this.size = opts.size;

        //create our data collection
        this.model = new GamificationCollection();

        //retrieve the gamification data
        this.model.loadData();

        //listen to the gamification collection
        this.model.on( 'reset', this.modelOnReset, this );
    },

    modelOnReset: function () {
        'use strict';
        //ARNxyzzy//// console.log("[INFO] ...size flag added ["+propID+"="+this.model.first().get(propID)+"]      ARNlogging");
        this.massageBadgeData();
        this.render();
    },

    render: function () {
        'use strict';
        //ARNxyzzy// console.log("[INFO] GamificationDataView.render TPL with MODEL              "+this.size+" ARNlogging");
        var that    = this,
            tplName = 'gamificationDataView',
            tplUrl  = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'gamification/tpl/';

        TemplateManager.get(
            tplName,
            function ( tpl ) {
                var gamModel        = that.model.toJSON(),
                    $newElements = $( tpl( gamModel ) );

                that.$el.
                    empty().
                    append( $newElements );

                that.trigger( 'renderDone' );

            },
            tplUrl
        );
        $( '.progress' ).prev().addClass( 'hasProgressBar' );
        $( '.progress.hide' ).prev().removeClass( 'hasProgressBar' );
        return this;
    },

    massageBadgeData: function() {
        var badges = this.model,
            pN,
            pD;

        badges.each( function( badge ) {
            if ( badge.get( 'type' ) === 'progress' ) {
                pN = badge.get( 'progressNumerator' );
                pD = badge.get( 'progressDenominator' );

                badge.set( 'progress', pN / pD * 100 );
            }
        } );
    }

} );
