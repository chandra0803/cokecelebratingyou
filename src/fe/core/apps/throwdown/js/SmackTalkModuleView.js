/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported SmackTalkModuleView*/
/*global
_,
LaunchModuleView,
SmackTalkSetCollectionView,
SmackTalkModuleView:true
*/
SmackTalkModuleView = LaunchModuleView.extend( {

    /**
     * Event listeners for G5.throwdown.dispatcher:
     *        event: promoChanged
     *       action: calls initSetCollectionView()
     *      purpose: reinitialize this view's SmackTalkSetCollectionView when
     *               promotion is changed
     */

    //EVENTS
    events: {
        // "click .hideSmackTalkLnk" : "hideSmackTalk",
        'click .smackTalkHideRecognitionConfirm': 'doHide',
        'click .smackTalkHideRecognitionCancel': 'hideHideConfirmTip'
    },

    initialize: function() {

        //this is how we call the super-class initialize function
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //merge events from the superclass ModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        // save the initial order value for this module (for hiding/showing module)
        G5.throwdown.moduleOrder.smackTalk = this.model.get( 'filters' ).throwdown.order;

        //when the module template is loaded, then add the SmackTalkModelView
        this.on( 'templateLoaded', this.initSetCollectionView, this );
        G5.throwdown.dispatcher.on( 'promoChanged', this.initSetCollectionView, this );

    },

    initSetCollectionView: function() {
        this.smackTalkSetCollectionView = new SmackTalkSetCollectionView( {
            el: this.$el, //sharing the same element as Module
            //when using a local template it couldn't find these
            //"$tabsParent":$('.smackTalkTabs'),
            //"$smackTalksParent":$('.smackTalkItemsCont')
            '$tabsParent': this.$el.find( '.smackTalkTabs' ),
            '$smackTalksParent': this.$el.find( '.smackTalkItemsCont' )
        } );
        this.smackTalkSetCollectionView.model.on( 'dataLoaded', this.processData, this );
    },

    updateHref: function() {
        this.$el
            .find( '.visitAppBtn' )
            .attr( 'href', this.smackTalkSetCollectionView.model.smackTalkUrl );
    },

    processData: function() {
        var
        filters = this.model.get( 'filters' ),
        visible = this.smackTalkSetCollectionView.model.visible;

        if ( visible ) {
            // set the order in case the module is currently hidden
            filters.throwdown.order = G5.throwdown.moduleOrder.smackTalk;
            this.updateHref();
        } else {
            filters.throwdown.order = 'hidden';
        }

        this.model.trigger( 'change' );
    },

    doHide: function( e ) {
        var modelView = $( e.target ).closest( '.ui-tooltip' ).data( 'modelView' );
        modelView.doHide();
    },

    hideHideConfirmTip: function( e ) {
        var modelView = $( e.target ).closest( '.ui-tooltip' ).data( 'modelView' );
        modelView.hideHideConfirmTip();
    }
} );
