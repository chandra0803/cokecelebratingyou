/*exported SSIBarChartView */
/*global
SSISharedHelpersView,
SSIBarChartView:true
*/

/*
 * Helper view for drawing circular charts,
 * currently used in Step It Up and Objectives contests.
 */
SSIBarChartView = SSISharedHelpersView.extend( {

    initialize: function ( opts ) {
        'use strict';
        var that = this;

        // console.log( '[SSI] initialize SSICircleChartView' );

        // alias for parent view
        this.parentView = opts.parentView;
        this.grandparentView = opts.parentView.parentView; // wearther's original, son?

        this.chartData = opts.chartData;

        // ssiCreatorPage - may have a hidden chart - so update when the tabs change
        this.parentView.on( 'tabChange', this.render, this );

        // for modules visibility changes
        // note: figure out how to do it on wake only
        this.grandparentView.on( 'layoutChange', function( type ) {
            if ( type == 'woke' ) {
                that.render( true );
            }
        }, this );

        this.setup();

        return this;
    },

    // update this chart after its been rendered ( like when its goemetry might have changed )
    update: function() {
    },

    /**
     * Attaches settings to the view
     *
     * Split into it's own function mostly for readiblity.
     *
     * @returns { object }
     */
    setup: function () {
        'use strict';
        var that = this,
            max = _.max( _.pluck( this.chartData, 'participantsCount' ) );

        this.$values = this.$el.find( '.value' );

        this.chartData = _.sortBy( this.chartData, function( level ) { return level.index; } );

        _.each( this.chartData, function( chart ) {
            chart.pct = chart.participantsCount / max * 100;
        } );

        setTimeout( function() {
            that.render();
        }, G5.props.ANIMATION_DURATION );

        return this;
    },

    /**
     * creates and adds paths to the stage
     *
     * @returns { object }
     */
    render: function ( reRender ) {
        'use strict';
        var that = this;

        if ( reRender ) {
            this.$values.find( '.bar' ).css( 'height', '' );
            this.$values.find( '.label' ).css( 'bottom', '' );

            setTimeout( function() {
                that.render();
            }, G5.props.ANIMATION_DURATION );

            return this;
        }

        // element should be visible
        if ( !this.$el.is( ':visible' ) ) {
            //console.warn( 'SSIBarChartView.render()... $el not visible', this.$el );
            return;
        }

        this.animate();

        return this;
    },

    animate: function() {
        var that = this;

        this.$values.each( function( index ) {
            $( this ).find( '.bar' ).animate( {
                    'height': that.chartData[ index ].pct + '%'
                },
                {
                    duration: G5.props.ANIMATION_DURATION * 5
                } );

            $( this ).find( '.label' ).animate( {
                    'bottom': that.chartData[ index ].pct + '%'
                },
                {
                    duration: G5.props.ANIMATION_DURATION * 5
                } );

        } );
    }

} );
