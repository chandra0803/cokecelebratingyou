/*exported ProfilePageStatementTabSummaryView */
/*global
TemplateManager,
ProfilePageStatementTabSummaryModel,
ProfilePageStatementTabSummaryView:true
*/
ProfilePageStatementTabSummaryView = Backbone.View.extend( {

    el: '#profilePageStatementTabSummary', // el attaches to existing element

    initialize: function ( ) {
        'use strict';

        var that = this;

        this.profilePageStatementTabSummaryModel = new ProfilePageStatementTabSummaryModel();
        this.profilePageStatementTabSummaryModel.loadModel();
        this.profilePageStatementTabSummaryModel.on( 'dataLoaded', function () {
            that.render( that.profilePageStatementTabSummaryModel.toJSON() );
        } );
    },

    render: function ( summary ) {
        'use strict';

        var //that = this,
            tplName = 'profilePageStatementTabSummary',
            tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/';
        this.$cont = $( '#profilePageStatementTabSummaryContent' );

        TemplateManager.get( tplName,
            function ( tpl ) {
                $( '#profilePageStatementTabSummaryContent' ).append( tpl( summary[ 0 ] ) ); //for some reason, that.$cont isn't assigning
            },
            tplUrl );
    }
} );
