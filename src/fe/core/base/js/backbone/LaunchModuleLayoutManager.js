/*exported LaunchModuleLayoutManager */
/*global
LaunchModuleLayoutManager:true
*/
LaunchModuleLayoutManager =  function( modContView ) {
    this.moduleContainerView = modContView;
    this.busy = false;
    this.layoutOnFinish = false;
};

_.extend( LaunchModuleLayoutManager.prototype, {

    updateLayout: function( filter ) {
        //console.log('updateLayout',filter);
        var container = this.moduleContainerView, //alias
            modules = container.getModuleViews(); //alias

        this.filter = filter;

        //GATEWAY
        //if we are already laying stuff out, make a note to layout when done, return
        if( this.busy ) {
            //console.log("[INFO] LaunchModuleLayoutManager - updateLayout() called but already laying out");
            this.layoutOnFinish = true;
            return;
        }

        this.busy = true;

        //no modules, then run for hills
        if( modules.length === 0 ) {
            //exit with immediate retry if requested
            this.exitUpdateLayout( 0 );
            return;
        }

        // pull out the hidden moduleViews
        var hidden = _.filter( modules, function( m ) {return m.model.getOrder() === 'hidden';} );
        var visible = _.filter( modules, function( m ) {return m.model.getOrder() !== 'hidden';} );

        //update DOM
        this.updateDomElements( visible, hidden );

        //call exit function with anim time est
        this.exitUpdateLayout();

        return;
    },

    //EXIT
    //call this when exiting update layout
    // - checks if another layout call is to be made
    // - opens up updateLayout for new calls
    exitUpdateLayout: function ( timeMs ) {
        var that = this;
        //check for another layout request after we finish, estimate time anims take
        setTimeout( function() {
            that.busy = false;
            if( that.layoutOnFinish ) {
                console.log( '[INFO] LaunchModuleLayoutManager - executing SCHEDULED updateLayout()' );
                that.layoutOnFinish = false;
                that.updateLayout( that.filter );
            }
        }, timeMs );
    },


    //helper function - update DOM elements
    updateDomElements: function( visibleModView, hiddenModViews ) {
        var that = this,
            filter = that.filter;

        visibleModView.sort( function ( a, b ) {
            //console.log( $(b).data(filter) +' --- '+ $(a).data(filter) );
            // return (b.$el.data(filter)) < (a.$el.data(filter)) ? 1 : -1;
            return parseInt( b.model.get( 'filters' )[ filter ].order, 10 ) < parseInt( a.model.get( 'filters' )[ filter ].order, 10 ) ? 1 : -1;
        } );

        // set hide and show on dom elements
        _.each( hiddenModViews, function( modView ) {
            that.hideModule( modView );
        } );

        _.each( visibleModView, function( modView ) {
            that.showModule( modView );
        } );
    },

    hideModule: function( modView ) {
        modView.sleep( this.moduleContainerView.$slept );
    },

    showModule: function( modView ) {
        modView.wake( this.moduleContainerView.$woke );

        // need to get the pageUpdate out the main thread so a batch showModule does all the waking first
        setTimeout( function() {
            modView.pageUpdate();
        }, 0 );
    }

} );
