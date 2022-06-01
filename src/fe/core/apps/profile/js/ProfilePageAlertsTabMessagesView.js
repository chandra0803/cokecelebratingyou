/*exported ProfilePageAlertsTabMessagesView */
/*global
TemplateManager,
ProfilePageAlertsTabMessagesCollection,
ProfilePageAlertsTabMessagesView:true
*/
ProfilePageAlertsTabMessagesView = Backbone.View.extend( {
//    el: '#profilePageAlertsTabMessages', // el attaches to existing element

    initialize: function (  ) {
        'use strict';
        //var that = this;

        this.profilePageAlertsTabMessagesCollection = new ProfilePageAlertsTabMessagesCollection();
        this.profilePageAlertsTabMessagesCollection.loadMessages();
    },

    render: function ( listOfMessages, messageCounter ) {
        'use strict';
        var that    = this,
            tplName = 'profilePageAlertsTabMessages',
            tplUrl  = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/',
            $pageCounter = $( '#messagePaginationCounter' );

        this.$cont = $( '#profilePageAlertsTabMessagesCollection' );
        //empty the carousel items
        this.$cont.empty();

        _.each( listOfMessages,
            function ( alertMessage ) {
                TemplateManager.get( tplName,
                    function ( tpl ) {
                        //alertMessage.messageText = alertMessage.messageText.replace(/<(?:.|\n)*?>/gm, ''); //strip out html
                        that.$cont.append( tpl( alertMessage ) );

                        // Defect #2911
                        // maybe someday, and even then, not like this, anything but this
                        // if ((index+1) >= listOfMessages.length){
                        //     that.$cont.find("a").each(function() {
                        //         $(this).popover(); //build each one's popover
                        //     });
                        // }
                    },
                    tplUrl );
            } );

        $pageCounter.html( messageCounter );
    }
} );
