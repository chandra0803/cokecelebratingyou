/*exported ApprovalsIndexModelView */
/*global
PageView,
Handlebars,
ApprovalsCollection,
ApprovalsIndexModelView:true
*/
ApprovalsIndexModelView = PageView.extend( {

    //override super-class initialize function
    initialize: function(  ) {

        //console.log( '[INFO] ApprovalsIndexModelView: ApprovalsIndex View model view initialized', this );

        var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'approvals';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        this.model = new ApprovalsCollection();

        this.model.loadData();

        this.model.on( 'loadDataFinished', function( approvals ) {

            console.log( '[INFO] ApprovalsIndexModelView: data load finished', approvals );

            // render the view
            that.renderApprovalIndex( null, approvals );

        }, this );

    },

    renderApprovalIndex: function( opts, approvalArr ) {
        //console.log( '[INFO] ApprovalsIndexModelView: renderApprovalIndex called using these approval objects: ', approvalArr );
        var i, theApprovalObj, data, noAlertsSource, templateNoAlerts,
            //that = this,
            source = $( '#claimIndex-Template' ).html(),
            template = Handlebars.compile( source ),
            approvalsExist,

            defaults = {
                $target: this.$el.find( '#approvalIndexTable tbody' ),  // JQ object
                classe: null,       // array
                callback: null      // function
            },

            settings = opts ? _.defaults( opts, defaults ) : defaults;

        if ( approvalArr.RecognitionApprovals.length > 0 || approvalArr.claimApprovals.length > 0 || approvalArr.nominationApprovals.length > 0 || approvalArr.ssiApprovals.length > 0 ) {
            approvalsExist = true;
        } else {
            approvalsExist = false;
        }

        if ( approvalsExist ) {


            for ( i = 0; i < approvalArr.nominationApprovals.length; i++ ) {
                theApprovalObj = approvalArr.nominationApprovals[ i ];

                data = {
                    name: theApprovalObj.name,
                    alertMessage: theApprovalObj.alertMessage,
                    url: theApprovalObj.url
                };

                settings.$target.append( template( data ) );
            }

            for ( i = 0; i < approvalArr.RecognitionApprovals.length; i++ ) {
                theApprovalObj = approvalArr.RecognitionApprovals[ i ];

                data = {
                    name: theApprovalObj.name,
                    alertMessage: theApprovalObj.alertMessage,
                    url: theApprovalObj.url
                };

                settings.$target.append( template( data ) );
            }

            for ( i = 0; i < approvalArr.claimApprovals.length; i++ ) {
                theApprovalObj = approvalArr.claimApprovals[ i ];

                data = {
                    name: theApprovalObj.name,
                    alertMessage: theApprovalObj.alertMessage,
                    url: theApprovalObj.url
                };

                settings.$target.append( template( data ) );
            }

            for ( i = 0; i < approvalArr.ssiApprovals.length; i++ ) {
                theApprovalObj = approvalArr.ssiApprovals[ i ];

                data = {
                    name: theApprovalObj.name,
                    alertMessage: theApprovalObj.alertMessage,
                    url: theApprovalObj.url
                };

                settings.$target.append( template( data ) );
            }

        } else {
            noAlertsSource = $( '#claimIndex-NoAlertsTemplate' ).html();
            templateNoAlerts = Handlebars.compile( noAlertsSource );

            settings.$target.append( templateNoAlerts() );
        }
    }

} );