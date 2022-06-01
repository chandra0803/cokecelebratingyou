/*exported ProfilePageAlertsTabAlertsView */
/*global
TemplateManager,
ProfilePageAlertsTabAlertsCollection,
ProfilePageAlertsTabAlertsView:true
*/
ProfilePageAlertsTabAlertsView = Backbone.View.extend( {

    initialize: function (  ) {
        'use strict';
        //var that = this;

        this.profilePageAlertsTabAlertsCollection = new ProfilePageAlertsTabAlertsCollection();
        this.profilePageAlertsTabAlertsCollection.loadAlerts();

    }, // initialize

    render: function ( listOfAlerts ) {
        'use strict';
        var that = this,
            tplName = 'profilePageAlertsTabAlerts',
            tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/';
        this.$el = $( '.profilePageAlertsTabAlertsCollection' );

        this.$el.empty();
        TemplateManager.get( tplName, function ( tpl ) {
            _.each( listOfAlerts, function ( alertMessage ) {
                ////console.log('alertMessage',alertMessage)
                //alertMessage.alertText = alertMessage.alertText.replace(/<(?:.|\n)*?>/gm, ''); //strip out html
                that.$el.append( tpl( alertMessage ) );

            } );
            that.trigger( 'rendered' );
        }, tplUrl );

        } // render
} );
