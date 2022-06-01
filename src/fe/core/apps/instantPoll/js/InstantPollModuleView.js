/*exported InstantPollModuleView */
/*global
TemplateManager,
SurveyTakeView,
InstantPollCollection,
InstantPollModuleView:true
*/
InstantPollModuleView = Backbone.View.extend( {

    initialize: function( opts ) {

        this.tplName = 'instantPollModule';
        this.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'instantPoll/tpl/';

        this.$el = this.options.$el || $( '<div />' );

        this.pollCollection = new InstantPollCollection( opts.pollId );

        // start the spinner
        G5.util.showSpin( this.$el );

        this.pollCollection.loadData( opts.pollId );

        this.pollCollection.on( 'pollDataLoaded', function() {
            this.render();
         }, this );

        this.on( 'templateLoaded', this.renderTpl, this );
    },

    events: {

    },

    render: function() {
        var that = this;
        /*
               {
                   _comment : "Polls are a single-question survey",
                   endDate : "08/16/2015",
                   id : 1234,
                   isComplete : false,
                   name : "This is a survey name",
                   promotionId : "IfItExists",
                   questions : Array[1],
                   showResults : true,
                   startDate : "08/16/2013"
               }
               */

        this.instantPollData = this.pollCollection.get( this.options.pollId );

        TemplateManager.get( this.tplName, function( tpl, vars, subTpls ) {

            //when the module template is loaded, store the subTpl for later
            that.surveyTakeTpl = subTpls.surveyTakeTpl;
            that.cmVars = vars.cm;

            that.trigger( 'templateLoaded', tpl );

        }, this.tplUrl );
    },

    renderTpl: function( tpl ) {
        this.$el.empty().append( tpl() );

        this.trigger( 'rendered' );

        this.surveyView = new SurveyTakeView( {
            el: '#pollsContainer',
            surveyModel: this.instantPollData,
            tpl: this.surveyTakeTpl,
            cm: this.cmVars
        } );

}

} );