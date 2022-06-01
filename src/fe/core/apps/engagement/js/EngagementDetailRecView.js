/*exported EngagementDetailRecView */
/*global
PublicRecognitionSetCollectionView,
EngagementDetailView,
EngagementDetailRecView:true
*/
EngagementDetailRecView = EngagementDetailView.extend( {
    initialize: function() {
        console.log( '[INFO] EngagementDetailRecView: initialized', this );

        //this is how we call the super-class initialize function (inherit its magic)
        EngagementDetailView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass EngagementDetailView
        this.events = _.extend( {}, EngagementDetailView.prototype.events, this.events );

        this.tplName = this.options.tplName || 'engagementDetailRec';
        this.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'engagement/tpl/';

        // build a non-Backbone model inside this view
        this.model = this.processData( this.options && this.options.data );

        this.on( 'chartDataLoaded', function() {
            this.renderChart( this.model.byPromo );
        }, this );
    },

    events: {
    },

    processData: function( data ) {
        var max;
        data.type = this.options.type;

        if ( data.byBehavior ) {
            max = _.max( _.pluck( data.byBehavior, 'count' ) );
            _.each( data.byBehavior, function( i ) {
                i.percent = Math.round( i.count / max * 1000 ) / 10;
            } );

            // sort by the name (secondary sort comes first)
            // sortBy is ascending, so reverse() when finished for descending
            data.byBehavior = _.sortBy( data.byBehavior, function( b ) { return b.name; } ).reverse();
            // sort by the count (primary sort comes last)
            data.byBehavior = _.sortBy( data.byBehavior, function( b ) { return b.count; } ).reverse();
            // doing this twice like this seems like a hack
        }

        return data;
    },

    renderPreAppend: function() {
        this.model.mode = this.options.mode;
    },

    renderDone: function() {
        if ( this.model.byPromo ) {
            this.model.byPromo.$chartContainer = this.$el.find( '.chartContainer' );
            this.loadChartData( this.model.byPromo );
        }
        this.initUserExtended( this.options.type );
        this.postRender();

        //this is how we call the super-class renderDone function (inherit its magic)
        EngagementDetailView.prototype.renderDone.apply( this, arguments );
    },

    postRender: function() {
        this.setUpByPromoHelpTip();

        this.$el.find( '.byBehavior .progress .bar' ).each( function() {
            var $this = $( this );

            // for some reason, when the byPromo box is missing the progress bars don't animate. this is an awful hack solution but it works
            setTimeout( function() {
                $this.css( 'width', $this.data( 'percent' ) + '%' );
            }, 1 );
        } );
    },

    initUserExtended: function( which ) {
        var temp;
        if ( this.options.mode !== 'user' ) {
            return false;
        }

        if ( this[ 'extended_' + which ] ) {
            this.$el.find( '.engagementRecognitionsView' ).replaceWith( this.$pubRec );
            return;
        }

        temp = G5.props.URL_JSON_PUBLIC_RECOGNITION;

        G5.props.URL_JSON_PUBLIC_RECOGNITION = _.where( this.options.engagementModel.get( 'detail' ), { type: which } )[ 0 ].data.recognitionsUrl || temp;

        this[ 'extended_' + which ] = new PublicRecognitionSetCollectionView( {
            el: this.$el.find( '.engagementRecognitionsView' ),
            $tabsParent: $(),
            $recognitionsParent: this.$el.find( '.engagementRecognitionsView .pubRecItemsCont' ),
            recogSetId: 'global',
            participantId: this.options.engagementModel.get( 'userId' ),
            modelParams: _.omit( $.extend( {}, this.options.engagementModel.params, { type: which } ), [ 'timeframeNavigate', 'timeframeName' ] ),
            suppressScrolling: true
        } );
    },

    destroy: function() {
        this.$pubRec = this.$el.find( '.engagementRecognitionsView' ).detach();

        //this is how we call the super-class destroy function (inherit its magic)
        EngagementDetailView.prototype.destroy.apply( this, arguments );
    },

    setUpByPromoHelpTip: function() {
        var $tar = this.$el.find( '.byPromoHelp' );

        $tar.popover( {
            title: this.$el.find( '.byPromoHelpTip strong' ).text(),
            content: this.$el.find( '.byPromoHelpTip .tipContent' ).html(),
            html: true,
            trigger: 'hover',
            container: 'body',
            placement: 'top'
        } );
    }
} );