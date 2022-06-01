/*exported PaxSelectedPaxCollection */
/*global
PaxCollection,
PaxSelectedPaxCollection:true
*/

/**

add participants ( optionally with a server sync )
remove participants ( optionally with a server sync )
sort by criteria
filter by criteria
paginate by an arbitrary number per page and by appending to or replacing current page
direct POST to server ( with optional additional data )
extract data for external use ( *.toJSON( ) or as a Backbone Collection )

 **/
PaxSelectedPaxCollection = PaxCollection.extend( {
    initialize: function( models, opts ) {

        PaxCollection.prototype.initialize.apply( this,  opts );
        if ( opts ) {
            this.addOnSelect = opts.addOnSelect;
            this.addOnFollow = opts.addOnFollow;
            this.setSelectUrl( opts.selectUrl );
            this.setDeselectUrl( opts.deselectUrl );
        }

        this.syncSearchCollectionRef = null;

        this.on( 'remove',  this.modelRemoved,  this );
        this.on( 'add',  this.modelAdded,  this );
    },

    modelRemoved: function( participant ) {
        //console.log( 'removed', this.getDeselectUrl( ), participant.get( 'isLocked' ) );
    	//Set participant selected object to false when the model is removed
    	participant.set( 'selected', false );

        if ( this.get( participant.get( 'isLocked' ) ) || this.get( participant.get( 'locked' ) ) ) {
            console.log( 'PaxSelectedPaxCollection item is locked cant remove' );
        } else {
            if ( this.getDeselectUrl( ) ) { this.selectedCheckUpdate( participant,  this.addOnFollow ); }
            this.trigger( 'itemRemoved', participant );
            // notify ref if we have it
            if ( this.syncSearchCollectionRef ) { this.syncSearchCollectionRef.syncItemSelection( participant.id,  false ); }
        }
    },
    modelAdded: function( participant ) {
        //console.log( 'added', this.getSelectUrl( ) );
        if ( this.getSelectUrl( ) ) { this.selectedCheckUpdate( participant,  this.addOnFollow ); }

        //console.log( participant.get( 'selected' ) );
    },
    addManyModels: function( models,  notBackboneModels ) {
        this.add( models );
        //console.log( 'paxSelectedPaxCollection : addManyModels' );
        if ( notBackboneModels ) {
            this.trigger( 'updatePaxSelectedMany',  this.models );
        } else {
            this.trigger( 'updatePaxSelectedMany',  models );
        }
    },
    // this is called from a synced colleciton
    updatePaxSelectedItem: function( participant ) {
        // need to check if we are maxed out or not as well
        // check if we are adding or removing
        if ( participant.get( 'selected' ) ) {
            // check if it exists - do nothing as we already have an instance of it
            if ( this.get( participant.get( 'id' ) ) ) { return; }
            // add it in
            this.add( participant );
        } else {
            // we are removing item
            if ( this.get( participant.get( 'isLocked' ) ) || this.get( participant.get( 'locked' ) ) ) {
                // user is locked - what do we do
            } else {
                this.remove( participant );

            }
        }
        this.trigger( 'updatePaxSelectedItemView',  participant );

    },
    setSyncSearchCollectionRef: function( collection ) {
        this.syncSearchCollectionRef = collection;
    },
    destroy: function( ) {
        this.syncSearchCollectionRef = null;
        //console.log( 'destroyInstance PaxSelectedPaxCollection' );
        PaxCollection.prototype.destroy.apply( );
    }

} );
