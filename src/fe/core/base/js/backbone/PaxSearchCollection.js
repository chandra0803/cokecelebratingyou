/*exported PaxSearchCollection */
/*global
PaxCollection,
PaxSearchCollection:true
*/

/**

add participants (optionally with a server sync)
remove participants (optionally with a server sync)
sort by criteria
filter by criteria
paginate by an arbitrary number per page and by appending to or replacing current page
direct POST to server (with optional additional data)
extract data for external use (*.toJSON() or as a Backbone Collection)

 **/
PaxSearchCollection = PaxCollection.extend( {
    initialize: function( models, opts ) {
        PaxCollection.prototype.initialize.apply( this, opts );
        this.syncSelectedCollectionRef = null;

        this.on( 'add', this.onModelsAdded, this );
        this.on( 'reset', this.onModelsAdded, this );
        this.on( 'change:follow', this.followed, this );
        //this.on('set', this.thisSetTest, this);
        this.on( 'change:selected', this.selected, this );
        // MUST REMOVE ALL EVENT LISTNERS ON DESTROY OR GARBAGE COLLECTION WILL NOT RUN
        //G5._globalEvents.on('updateItemSelection', this.updateItemSelection, this);
    },
    followed: function( participant ) {
        //check if we should sync
        //if (this.syncSelectedCollectionRef.addOnFollow)this.syncSelectedCollectionRef.add(participant);
        this.trigger( 'followResponse', participant );
    },
    setSyncSelectedCollectionRef: function( collection ) {
        this.syncSelectedCollectionRef = collection;
        // add event listeners to
    },
    /*thisSetTest:function(e){
        console.log('hello we set something',e);
    },*/
    selected: function( participant ) {
        console.log( 'selected' );
        this.trigger( 'updateItemSelectionView', participant.get( 'id' ), participant.get( 'selected' ) );
        if ( this.syncSelectedCollectionRef ) { this.syncSelectedCollectionRef.updatePaxSelectedItem( participant ); }
        if ( this.syncSelectedCollectionRef && this.syncSelectedCollectionRef.addOnSelect ) { this.syncSelectedCollectionRef.add( participant, { silent: true } ); }
    },

    // when models are added we need to check if it should be selected
    onModelsAdded: function() {
        //console.log('onModelAdded',e,this.models);
        var that = this,
            selfModel;
        if ( this.syncSelectedCollectionRef ) {
           // console.log('this.syncSelectedCollectionRef',this.syncSelectedCollectionRef);
            _.each( this.syncSelectedCollectionRef.models, function( model ) {
                //console.log('loop',model);
                if ( model.get( 'selected' ) ) {
                    selfModel = that.get( model.id );
                    if ( selfModel ) { selfModel.set( 'selected', true ); }
                }
            } );
        }
    },
    removeAll: function() {
        console.log( 'resetALL' );
        if ( this.syncSelectedCollectionRef ) { this.syncSelectedCollectionRef.reset(); }
    },
    addAllToSynced: function() {
        console.log( 'SEARCH addAllToSynced' );
        //if (this.syncSelectedCollectionRef) this.syncSelectedCollectionRef.add(this.models);
        if ( this.syncSelectedCollectionRef ) { this.syncSelectedCollectionRef.addManyModels( this.models ); }
    },
    //
    updateItemSelection: function( id ) {
        var participant = this.get( id ),
            selected;
        // check if selecting or deselecting
        if ( participant ) {
            selected = !participant.get( 'selected' );
            participant.set( 'selected', selected );
        }
    },

    syncItemSelection: function( id, selected ) {
        //console.log('syncItemSelection');
        var participant = this.get( id );
        if ( participant ) {
            participant.set( 'selected', selected );
        }
    },

    destroy: function() {
        this.syncSelectedCollectionRef = null;
        //console.log('destroyInstance PaxSearchCollection');
        //G5._globalEvents.off('updateItemSelection', this.updateItemSelection, this);
        PaxCollection.prototype.destroy.apply();
    }

} );
