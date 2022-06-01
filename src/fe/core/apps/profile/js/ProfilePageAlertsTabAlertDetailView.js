/*exported ProfilePageAlertsTabAlertDetailView */
/*global
PageView,
ProfilePageAlertsTabAlertDetailView:true
*/
ProfilePageAlertsTabAlertDetailView = PageView.extend( {
    el: '#profilePageMessageDetailView', // el attaches to existing element

    initialize: function (  ) {
        'use strict';
        //var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'profile';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        // this.profilePageAlertsTabMessageDetailCollection = new ProfilePageAlertsTabMessageDetailCollection();
        // this.profilePageAlertsTabMessageDetailCollection.loadMessage();
        // this.profilePageAlertsTabMessageDetailCollection.on('dataLoaded', function () {
        //     that.render(that.profilePageAlertsTabMessageDetailCollection.toJSON());
        // });
    }

    // render: function (listOfMessages) {
    //     'use strict';
    //     var self = this,
    //         tplName = 'profilePageAlertsTabMessageDetailContent',
    //         tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/';
    //     this.$cont = $('#profilePageMessageDetailContent');

    //     this.$cont.empty();

    //     _.each(listOfMessages,
    //         function (alertMessage) { //there's only 1 in the JSON, but this should still function normally.
    //             TemplateManager.get(tplName,
    //                 function (tpl) {
    //                     self.$cont.append(tpl(alertMessage));
    //                 },
    //                 tplUrl);
    //         });
    // }
} );
