/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported PublicRecognitionPageView, pubRecId, pubRecSetNameId, pubCommentId */
/*global
_,
G5,
PageView,
PublicRecognitionSetCollectionView,
PublicRecognitionPageView:true
*/
PublicRecognitionPageView = PageView.extend( {

    //override super-class initialize function
    initialize: function( opts ) {

        //set the appname (getTpl() method uses this)
        this.appName = 'publicRecognition';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        //add the pub rec set collection view
        this.pubRecSetCollectionView = new PublicRecognitionSetCollectionView( {
            el: this.$el, //sharing the same element as Module
            '$tabsParent': this.$el.find( '.pubRecTabs' ),
            '$recognitionsParent': this.$el.find( '.pubRecItemsCont' )
        } );

    }

} );
