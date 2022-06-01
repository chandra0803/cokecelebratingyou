/*exported EngagementSummaryCollectionView */
/*global
TemplateManager,
EngagementSummaryModelView,
EngagementSummaryCollection,
EngagementSummaryCollectionView:true
*/
EngagementSummaryCollectionView = Backbone.View.extend( {
    className: 'engagementSummaryCollectionView',

    initialize: function() {
        //console.log('[INFO] EngagementSummaryCollectionView: initialized', this);

        var defaults = {
            mode: 'user'
        };

        this.options = $.extend( true, {}, defaults, this.options );

        this.tplName = this.options.tplName || 'engagementSummaryCollection';
        this.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'engagement/tpl/';

        this.moduleView = this.options.moduleView || null;


        this.collection = this.options.collection || new EngagementSummaryCollection( this.options.summary || null, { autoLoad: false, engagementModel: this.options.engagementModel || null, mode: this.options.mode } );

        // collection listeners
        this.collection.on( 'loadDataStarted', function() {
            G5.util.showSpin( this.$el );
        }, this );
        this.collection.on( 'loadDataDone', function() {
            G5.util.hideSpin( this.$el );
        }, this );
        this.collection.on( 'reset', this.render, this );

        G5._globalEvents.on( 'breakpointChanged', this.handleBreakpointChange, this );

        // ready for the collection to do stuff with data
        // if the collection already has models, render
        if ( this.collection.length ) {
            this.render();
        } else {
            this.collection.loadData();
        }

        // view listeners
        this.on( 'renderDone', this.postRender, this );
    },

    events: {
        'shown a[data-toggle="tab"]': 'activateTab'
    },

    render: function() {
        var that = this,
            json = {
                summaries: that.collection.toJSON(),
                unique: Math.round( Math.random() * 1000000 )
            };

        this.preRender( json );

        TemplateManager.get( this.tplName, function( tpl ) {
            that.$el.empty().append( tpl( json ) );

            that.$el.addClass( 'mode_' + that.collection.engagementModel.get( 'mode' ) );
            that.$el[ that.collection.engagementModel.get( 'isScoreActive' ) ? 'addClass' : 'removeClass' ]( 'scoreActive' );
            that.$el[ that.collection.engagementModel.get( 'isScoreActive' ) ? 'removeClass' : 'addClass' ]( 'scoreInactive' );
            that.$el[ that.collection.engagementModel.get( 'areTargetsShown' ) ? 'addClass' : 'removeClass' ]( 'targetsShown' );
            that.$el[ that.collection.engagementModel.get( 'areTargetsShown' ) ? 'removeClass' : 'addClass' ]( 'targetsHidden' );
            that.$tabs = that.$el.find( '.tab' );
            that.$panes = that.$el.find( '.tab-pane' );

            that.$tabs.tooltip( {
                container: that.$el.closest( '.sidebar' ).length ? that.$el.closest( '.sidebar' ) : that.$el.closest( '.page-content' ),
                placement: 'bottom',
                delay: 200
            } );

            that.trigger( 'renderDone' );
        }, this.tplUrl );
    },

    preRender: function( json ) {
        json.isScoreActive = json.summaries[ 0 ].isScoreActive;
        json.areTargetsShown = json.summaries[ 0 ].areTargetsShown;

        return json;
    },

    postRender: function() {
        this.attachModelViews();

        if ( this.options.startTab ) {
            this.showTabByName( this.options.startTab );
        }        else {
            this.showTab( 0 );
        }

        if ( this.collection.engagementModel && this.collection.engagementModel.get( 'isScoreActive' ) ) {
            this.drawScoreMeters();
        }
    },

    attachModelViews: function() {
        var that = this;

        this.modelViews = [];

        this.collection.each( function( model, i ) {
            that.modelViews.push( new EngagementSummaryModelView( {
                model: model,
                el: that.$panes.filter( '.' + model.get( 'type' ) ),
                nameId: model.get( 'type' ),
                parentView: that
            } ) );

            that.$tabs.eq( i ).data( 'view', that.modelViews[ i ] );
        } );
    },

    drawScoreMeters: function( noAnim ) {
        this.$el.find( '.tab-tab .tab' ).engagementDrawScoreMeter( {
            redraw: noAnim || false,
            noAnim: noAnim || false,
            animationDuration: G5.props.ANIMATION_DURATION * 5
        } );
    },

    handleBreakpointChange: function() {
        if ( this.$el.find( '.tab-tab .tab .meter' ).is( ':visible' ) ) {
            this.drawScoreMeters( true );
        }        else {
            this.$el.find( '.tab-tab .tab' ).removeClass( 'achieved' );
        }
    },

    getTabName: function() {
        var $active = this.$tabs.filter( '.active' ),
            activeNameId = $.trim( $active.attr( 'class' ).replace( 'tab', '' ).replace( 'active', '' ) );

        return activeNameId;
    },

    showTab: function( i ) {
        this.$tabs.eq( i ).find( 'a' ).tab( 'show' );
    },

    showTabByName: function( name ) {
        this.$tabs.filter( '.' + name ).find( 'a' ).tab( 'show' );
    },

    activateTab: function( e ) {
        var $tab,
            view;
        if ( !this.$tabs ) {
            return false;
        }

        $tab = e ? $( e.target ) : this.$tabs.length ? this.$tabs.filter( '.active' ) : this.$tabs.eq( 0 );
        view = $tab.closest( '.tab' ).data( 'view' );

        view.activate();
        this.trigger( 'tabActivated', $tab, view );
    }
} );
