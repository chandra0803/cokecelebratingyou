/*exported ApprovalsModuleView */
/*global
LaunchModuleView,
ApprovalsCollection,
ApprovalsModuleView:true
*/
ApprovalsModuleView = LaunchModuleView.extend( {

    initialize: function() {
        var that = this;

        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass ModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        this.collection = new ApprovalsCollection();

        this.on( 'templateLoaded', function( tpl, vars, subTpls ) {
            that.approvalModuleTpl = subTpls.approvalDashboardTpl;
            that.collection.loadData();
        } );

        this.collection.on( 'loadDataFinished', function( approvals ) {
            that.renderList( approvals );
        } );

        G5._globalEvents.on( 'windowResized', this.resizeListener, this );

    },

    renderList: function( approvals ) {
        var $approvalWrap = this.$el.find( '.approvalDashboardWrap' ),
            that = this,
            approvalsLength;


        _.each( approvals, function( index, approve ) {
            var count = 0;

            if ( index.length ) {
                approvalsLength = true;
                _.each( index, function( ap ) {
                    count += ap.numberOfApprovables;
                } );
                if ( approve === 'nominationApprovals' || approve === 'RecognitionApprovals' ) {
                    //do nothing - this is to fix backend sending badly formed data.
                } else {
                    approvals[ approve.toLowerCase() + 'Count' ] = count;
                }
            }
        } );


        $approvalWrap.append( that.approvalModuleTpl( approvals ) );

        if( !approvalsLength ) {
           this.$el.hide();
        }else{
           this.$el.show();
        }
        that.resizeListener();
    },
    resizeListener: function() {
        var breakpoint = G5.breakpoint.value,
            $selectWrap = this.$el.find( '.approvalDashboardWrap' ),
            containerClass = '.card';
        if ( breakpoint === 'mobile' || breakpoint === 'mini' ) {
           $selectWrap.find( containerClass ).height( '' );
        } else {
            G5.util.equalheight( containerClass, $selectWrap );
        }
    }
} );
