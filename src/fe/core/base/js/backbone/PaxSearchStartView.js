/*exported PaxSearchStartView */
/*global
PaxSearchView,
TemplateManager,
ProfileGroupsModel,
PaxSearchStartView:true
*/
PaxSearchStartView = Backbone.View.extend( {
    initialize: function( opts ) {
        //console.log('[INFO] PaxSearchStartView: Participate Global Search Start View initialized');

        this.paxSearchView = null;
        this.follow = opts.follow;
        this.presetFilters = opts.presetFilters;
        this.disableSelect = opts.disableSelect;
        this.multiSelect = opts.multiSelect;
        this.addSelectedPaxView = opts.addSelectedPaxView;
        this.recognition = opts.recognition;
        this.searchParams = opts.searchParams || {};
        this.selectedPaxCollection = opts.selectedPaxCollection;
        this.preSelectedParticipants = opts.preSelectedParticipants;
        this.selectUrl = opts.selectUrl;
        this.searchUrl = opts.searchUrl;
        this.deselectUrl = opts.deselectUrl;
        this.clearOnClose = opts.clearOnClose;
        this.addOnSelect = opts.addOnSelect;
        this.addOnFollow = opts.addOnFollow;
        this.filterSearch = opts.filterSearch;
        this.hideCheckAll = opts.hideCheckAll;
        this.saveGroup = opts.saveGroup;
        this.selectGroup = opts.selectGroup;
        this.options = opts;

        // check if we data attributes and overwrite
        if ( this.$el.data( 'startPlaceholder' ) ) { this.startPlaceholder = this.$el.data( 'startPlaceholder' ); }

        if ( this.$el.data( 'searchUrl' ) ) { this.searchUrl = this.$el.data( 'searchUrl' ); }
        if ( this.$el.data( 'selectUrl' ) ) { this.selectUrl = this.$el.data( 'selectUrl' ); }
        if ( this.$el.data( 'deselectUrl' ) ) { this.deselectUrl = this.$el.data( 'deselectUrl' ); }
        if ( this.$el.data( 'saveGroup' ) ) { this.saveGroup = this.$el.data( 'saveGroup' ); }
        if ( this.selectedPaxCollection ) {
            this.selectedPaxCollection.setSelectUrl( this.selectUrl );
            this.selectedPaxCollection.setDeselectUrl( this.deselectUrl );
        }
        if( this.selectGroup ) {
           // need to update styles etc.
            this.groupsModel = new ProfileGroupsModel();
            this.groupsModel.on( 'groupsReceived', this.groupsReceived, this );
        }

        this.render();
    },

    events: {
        'keyup .paxSearchInput': 'handleTyping',
        'click .pubRecTabs a': 'doTabClick',
        'blur .paxSearchInput': 'searchBlur',
        'focus .paxSearchInput': 'forceIOSredraw',
        'click .groupTabs a': 'doTabClickGroup',
        'click .create-a-group': 'openGroupPage',
    },
    searchBlur: function() {
        console.log( 'start blur' );
        //this.updateKeys();
    },
    updateKeys: function() {
        console.log( 'updateKeys start', this.$el.find( '.paxSearchInput' ).val() );
        if ( this.paxSearchView ) { this.paxSearchView.updateKeyTypes( this.$el.find( '.paxSearchInput' ).val() ); }
    },
    render: function() {
        var that = this,
            tplName = this.options.tplName || 'paxSearchStartView',
            tplUrl = this.options.tplUrl || G5.props.URL_TPL_ROOT || G5.props.URL_BASE_ROOT + 'tpl/';

        TemplateManager.get( tplName, function( tpl ) {
            that.$el.append( tpl() );
            that.$input = that.$el.find( '.paxSearchInput' );
            if( that.selectGroup ) {
                that.$el.addClass( 'select-groups-enabled' );
                that.initGroups();
            }
            if ( that.startPlaceholder ) { that.$input.attr( 'placeholder', that.startPlaceholder ); }
            // hide or show the right search
            if ( that.filterSearch ) {
                that.$el.find( '.pubRecFilterWrap' ).show();

                //console.log(that.presetFilters);
                setTimeout( function() { // IE8 + JQ1.8.3 -- push target.remove() to end of stack
                    that.renderTabs( that.presetFilters );
                } );
            } else {
                that.$el.find( '.paxSearchOuterWrap' ).show();
            }
        }, tplUrl );
    },





    initGroups: function() {
        // disable drop down until loaded - maybe spinner?
        this.$el.find( '.select-groups' ).addClass( 'disabled' );
        // show group dropdown
        this.$el.find( '.select-groups' ).show().css( 'display', 'inline-block' );

        // call to load groups
        this.groupsModel.fetchAllGroups( );
    },

    groupsReceived: function( data ) {

        // clear and load template
        var groupTabs =  this.$el.find( '.groupTabs' );
        this.$el.find( '.select-groups' ).removeClass( 'disabled' );

        this.groups = data.groups;

         // should arder by name desc - they get prepended so they will be asc
        this.groups.sort( function( a, b ) {return ( a.name > b.name ) ? 1 : ( ( b.name > a.name ) ? -1 : 0 );} );
        this.groups.reverse();
        if( this.groups.length ) {
             _.each( this.groups, function( group ) {
                   var $a = $( '<a />' )
                            .attr( {
                                'title': group.name,
                                'href': '#',
                                'class': 'group-tab',
                                'data-id': group.id,
                            } )
                            .html( group.name ),
                    $li = $( '<li class="drop-item" />' ).html( $a );
                groupTabs.prepend( $li );
            } );
        }else{
            // if we have no groups show create a group
            this.$el.find( '.group-prompt' ).show();
        }
        // enabel dropdown

    },
    doTabClickGroup: function( e ) {
        e.preventDefault;
        var gId = $( e.currentTarget ).data( 'id' );
        this.goToRecognition( gId );
    },
    openGroupPage: function( e ) {
        window.location = $( e.currentTarget ).data( 'href' );
    },

    goToRecognition: function( groupId ) {
        console.log( groupId, this.groups[ 0 ], this.groups[ 0 ].id );
        var groupObj = _.find( this.groups, function( v, k ) {
            if ( v.id === groupId ) {
              return true;
            } else {
              return false;
            }
          } );
        if( groupObj.groupUserIds ) {
            this.groupsModel.openRecognitionPage( groupObj.groupUserIds );
        }
        // else we are missing data
    },











    handleTyping: function ( e ) {
        var keycode = e.keyCode,
            keyTyped;

        if ( keycode !== 32 ) {
            keyTyped = $( e.target ).val();
            //console.log(keyTyped);
            if ( keyTyped && !this.paxSearchView ) {
                this.paxSearchView =  new PaxSearchView( {
                    searchKey: keyTyped,
                    follow: this.follow,
                    presetFilters: this.presetFilters,
                    startPlaceholder: this.startPlaceholder,
                    disableSelect: this.disableSelect,
                    multiSelect: this.multiSelect,
                    addSelectedPaxView: this.addSelectedPaxView,
                    recognition: this.recognition,
                    searchParams: this.searchParams,
                    selectedPaxCollection: this.selectedPaxCollection,
                    preSelectedParticipants: this.preSelectedParticipants,
                    selectUrl: this.selectUrl,
                    searchUrl: this.searchUrl,
                    deselectUrl: this.deselectUrl,
                    clearOnClose: this.clearOnClose,
                    addOnSelect: this.addOnSelect,
                    addOnFollow: this.addOnFollow,
                    filterSearch: this.filterSearch,
                    hideCheckAll: this.hideCheckAll,
                    saveGroup: this.saveGroup

                } );
                this.paxSearchView.on( 'getInputValue', this.updateKeys, this );
                this.paxSearchView.on( 'closed', this.paxSearchViewClosed, this );
            } else if ( keyTyped ) {
                // handle on the blur of the text box now
                // if keys are typed super fast update paxSearch key vals
                // this.paxSearchView.updateKeyTypes(keyTyped);
            }
        }
    },

     // reset filter presets
    setFilterPresets: function( filters ) {
        this.presetFilters = filters;
        // need to clear out tabs that exist and rerender
        this.$el.find( '.pubRecTabs' ).html();
        this.renderTabs( this.presetFilters );

    },

    renderTabs: function( filtersObj ) {
        var that = this;

        if ( !filtersObj || !filtersObj.filters.length ) {
            this.hideShowTeamFilter( null );
            return false;
        }

        _.each( filtersObj.filters, function( filter ) {
            var numNew = 0;
            //build the li>a element
            var $a = $( '<a />' )
                        .attr( {
                            'title': filter.name,
                            'href': '#',
                            'class': 'pub-rec-tab',
                            'data-id': filter.id,
                            'data-type': filter.type,
                            'data-typename': filter.typeName
                            //'data-total-count':filter.get('totalCount')
                        } )
                        .html( filter.name + '<!--sup>' + numNew + '</sup-->' ),
                $li = $( '<li />' ).html( $a );
            that.$el.find( '.pubRecTabs' ).append( $li );
        } );
        //this.origFilterText = this.$el.find('.dropdown-toggle span').text();
    },

    doTabClick: function( e ) {
        var $tar = $( e.currentTarget ),
            filterObj = {
                'name': $tar.text(),
                'id': $tar.data( 'id' ),
                'type': $tar.data( 'type' ),
                'typeName': $tar.data( 'typename' )
            };
        //this.addFilterToken(filterObj.name);
        // call to open the search
        this.openSearch( filterObj );
        e.preventDefault();
    },

    addFilterToken: function( name ) {
        this.$el.find( '.pubRecFilterWrap .dropdown-toggle span' ).text( name );
    },

    openSearch: function( filterObj ) {
        if ( filterObj && !this.paxSearchView ) {
                this.paxSearchView =  new PaxSearchView( {
                    filterObj: filterObj,

                    follow: this.follow,
                    presetFilters: this.presetFilters,
                    disableSelect: this.disableSelect,
                    multiSelect: this.multiSelect,
                    addSelectedPaxView: this.addSelectedPaxView,
                    recognition: this.recognition,
                    searchParams: this.searchParams,
                    selectedPaxCollection: this.selectedPaxCollection,
                    preSelectedParticipants: this.preSelectedParticipants,
                    selectUrl: this.selectUrl,
                    searchUrl: this.searchUrl,
                    deselectUrl: this.deselectUrl,
                    clearOnClose: this.clearOnClose,
                    addOnSelect: this.addOnSelect,
                    addOnFollow: this.addOnFollow,
                    filterSearch: this.filterSearch,
                    hideCheckAll: this.hideCheckAll,
                    saveGroup: this.saveGroup

                } );
                this.paxSearchView.on( 'closed', this.paxSearchViewClosed, this );
            } else if ( filterObj ) {
                // if keys are typed super fast update paxSearch key vals
                 this.paxSearchView.updateFilterObj( filterObj );
            }
    },

    // get and set params
    setAjaxParam: function( name, value ) {
        console.log( '--------------- settting SearchParams', name, value );
        if ( name && value ) { //set
            this.searchParams[ name ] = value;
            return value;
        } else if ( name ) {
            return this.searchParams[ name ];
        }
        //get all
        return this.searchParams;
    },
    clearAjaxParam: function() {
        this.searchParams = {};
    },
    setParticipantSelectModeSingle: function( val ) {
        this.multiSelect = val;
    },
    setSearchUrl: function( val ) {
        this.searchUrl = val;
    },


    show: function() {
        this.$el.show();
    },
    hide: function() {
        this.$el.hide();
    },
    hideShowTeamFilter: function( show ) {

        var filterTab = $( '.filter-search' ),
            nameTab = $( '.name-search' );
        if ( show ) {
            filterTab.show();
            nameTab.show();
            $( '.filter-search a' ).tab( 'show' );
        } else {
            filterTab.hide();
            nameTab.hide();
            $( '.name-search a' ).tab( 'show' );
        }
        //console.log(filterTab, filterTab.length);
    },
    forceIOSredraw: function() {
        $( '<style></style>' ).appendTo( $( document.body ) ).remove();
    },
    paxSearchViewClosed: function() {
        // reset the text on the select box
        //this.$el.find('.dropdown-toggle').text(this.origFilterText);


        this.paxSearchView.destroy();
        this.paxSearchView.undelegateEvents();
        this.paxSearchView = null;
        this.trigger( 'closedsearch' );
        this.$input.val( '' );
        this.forceIOSredraw();

    }
} );
