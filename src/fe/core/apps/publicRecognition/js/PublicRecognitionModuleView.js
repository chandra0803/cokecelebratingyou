/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported PublicRecognitionModuleView*/
/*global
_,
$,
LaunchModuleView,
PublicRecognitionSetCollectionView,
PublicRecognitionModuleView:true
*/
PublicRecognitionModuleView = LaunchModuleView.extend( {

    initialize: function() {

        //this is how we call the super-class initialize function
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //merge events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        //when the module template is loaded, then add the PublicRecognitionModelView
        this.on( 'templateLoaded', function() {
            //add the budget collection view
            this.pubRecSetCollectionView = new PublicRecognitionSetCollectionView( {
                el: this.$el, //sharing the same element as Module
                '$tabsParent': this.$el.find( '.pubRecTabs' ),
                '$recognitionsParent': this.$el.find( '.pubRecItemsCont' )
            } );
        }, this );

    },

    events: {
        // proxying some clicks from here down to the specific items for the confirmation tip
        'click .publicRecognitionHideRecognitionConfirm': 'doHide',
        'click .publicRecognitionHideRecognitionCancel': 'hideHideConfirmTip',
        'click .publicRecognitionDeleteRecognitionConfirm': 'doDelete',
        'click .publicRecognitionDeleteRecognitionCancel': 'hideDeleteConfirmTip',
        'click .sa-button': 'invokeCelebration'
    },
    invokeCelebration: function( event ) {
        event.preventDefault();
        var cid = $( event.target ).attr( 'data-cid' );
        // console.log( $( event.target ).attr( 'data-cid' ) );
        function openMemory( result ) {
                window.open( result.url, '_blank' );
        }
        function handleError( error ) {
            console.log( error );
        }
        // Utilizing Common Util function to call SA Stuffs
        G5.util.getSAConsumables( G5.props.URL_JSON_SA_SHARE_A_MEMORY, 'post',  JSON.stringify( {
            'celebrationId': cid
            } ), 'same-origin', {
                'post-type': 'ajax',
                'content-type': 'application/json',
                'cache-control': 'no-cache'
        }, openMemory, handleError );
    },
    doHide: function( e ) {
        var modelView = $( e.target ).closest( '.ui-tooltip' ).data( 'modelView' );
        modelView.doHide();
    },

    hideHideConfirmTip: function( e ) {
        var modelView = $( e.target ).closest( '.ui-tooltip' ).data( 'modelView' );
        modelView.hideHideConfirmTip();
    },
    doDelete: function( e ) {
        var modelView = $( e.target ).closest( '.ui-tooltip' ).data( 'modelView' );
        modelView.doDelete();
    },

    hideDeleteConfirmTip: function( e ) {
        var modelView = $( e.target ).closest( '.ui-tooltip' ).data( 'modelView' );
        modelView.hideDeleteConfirmTip();
    }


} );
