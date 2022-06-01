/*exported EngagementSummaryModelView */
/*global
TemplateManager,
EngagementSummaryModel,
EngagementSummaryModelView:true
*/
EngagementSummaryModelView = Backbone.View.extend( {
    initialize: function() {
        //console.log('[INFO] EngagementSummaryModelView: initialized', this);

        var that = this,
            defaults = {};

        this.options = $.extend( true, {}, defaults, this.options );

        this.model = this.options.model || new EngagementSummaryModel();
        this.tplName = this.options.tplName || 'engagementSummaryModel';
        this.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'engagement/tpl/';

        this.nameId = this.options.nameId || null;
        this.parentView = this.options.parentView || this;


        // listen to the model
        this.model.on( 'change', this.render, this );

        // listen to the view
        this.on( 'templateLoaded', this.render, this );


        G5._globalEvents.on( 'breakpointChanged', this.handleBreakpointChange, this );


        TemplateManager.get( this.tplName, function( tpl, vars, subTpls ) {
            that.tpl = tpl;
            that.tplVariables = vars;
            that.subTpls = subTpls;

            that.trigger( 'templateLoaded' );
        }, this.tplUrl, null, false ); // noHandlebars = null, async = false
    },

    events: {
        'click .showDescription': 'initDescriptionTip',
        'click .largeAudience': 'handleLargeAudienceLink',
        'click .title a, .actual a': 'disablePageLinks'
    },

    // render simply inserts the HTML
    render: function() {
        var json = this.model.toJSON();

        G5.util.showSpin( this.$el );

        this.preRender( json );

        this.$el.empty().append( this.tpl( json ) );

        this.$el.find( '.showDescription' ).tooltip( {
            container: this.$el,
            placement: 'top',
            delay: 200
        } );

        this._rendered = true;

        this.trigger( 'renderDone' );
    },

    preRender: function( json ) {
        // each rate string has special CM keys containing the full translated string for the "X of Y team members have achieved the target" text
        // the key output will have a {0} and {1} placeholders where the numbers of people are inserted
        // this allows the translations to have plain text and the numbers in any order
        // we embed this CM output as a tplVariables in our engagementSummaryModel Handlebars template
        // we also have subTpls embedded in our engagementSummaryModel Handlebars template
        // we pass the various values from the JSON to the subTpls, then replace the {0} and {1} with the rendered output
        // the final string is assigned to rateFormatted in the JSON to be passed to the main template
        if ( this.tplVariables.rate ) {
            json.rateFormatted = G5.util.cmReplace( {
                cm: json.type === 'score' ? this.tplVariables.rateScore : this.tplVariables.rate,
                subs: [
                    this.subTpls.teamMemMetTarget( { teamMemMetTarget: json.teamMemMetTarget } ),
                    this.subTpls.teamMemCount( { teamMemCount: json.teamMemCount } )
                ]
            } );
        }

        return json;
    },

    // activate animates the meter
    activate: function() {
        var that = this,
            activate = function() {
                that.drawScoreMeters();
            };

        if ( this._rendered === true ) {
            activate();
        }        else {
            this.on( 'renderDone', function() {
                activate();
            }, this );
        }
    },

    initDescriptionTip: function( e ) {
        e.preventDefault();
    },

    disablePageLinks: function() {
        if ( !this.$el.parents( '.engagementSummaryCollectionView' ).hasClass( 'moduleSummary' ) ) {
            return false;
        }
    },

    drawScoreMeters: function( noAnim ) {
        this.$el.find( '.summary .actual' ).engagementDrawScoreMeter( {
            actual: this.model.get( 'actual' ),
            target: this.model.get( 'target' ),
            redraw: noAnim || false,
            noAnim: noAnim || false,
            animationDuration: G5.props.ANIMATION_DURATION * 5
        } );

        this.$el.find( '.extended .avg .box' ).engagementDrawScoreMeter( {
            redraw: noAnim || false,
            noAnim: noAnim || false,
            animationDuration: G5.props.ANIMATION_DURATION * 5
        } );
    },

    handleBreakpointChange: function() {
        this.drawScoreMeters( true );
    },

    handleLargeAudienceLink: function( e ) {
        var $tar = $( e.target ).closest( 'a' ),
            href = $tar.attr( 'href' );

        e.preventDefault();
        $tar.spin();

        $.ajax( {
            url: href,
            dataType: 'g5json',
            success: function() {
                // nothing to do. Response should be a ServerCommand that opens a modal
                $tar.spin( false );
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                $tar.spin( false );
                //alert( textStatus + ': ' +errorThrown );
                console.log( jqXHR, textStatus, errorThrown );
            }
        } );
    }
} );
