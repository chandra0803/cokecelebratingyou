/*eslint no-underscore-dangle: ["error", { "allow": ["_globalEvents"] }]*/
/*exported PaxSearchView */
/*global
TemplateManager,
PaxSelectedPaxView,
PaxSearchCollectionView,
PaxSearchModel,
PaxSearchView:true
*/


PaxSearchView = Backbone.View.extend( {
    el: '#overlay',

    events: {
        'keyup .paxOverlaySearchInput': 'recipientSearchKeyController',
        'click .icon-close': 'checkExitSearch',
        'click [type="checkbox"]': 'checkSelectAll',
        'click .tokenselector': 'showHints',
        'click .tokenselector ul>li': 'selectFilterFromHints',
        'click .remove': 'removeFilter',
        'click .searchBG': 'checkhideSearchBox',
        'blur .paxOverlaySearchInput': 'searchBlur',
        'focus .paxOverlaySearchInput': 'searchFocus',
        'click .add-all-btn': 'callToFetchAll',
        'click .exit-confirm': 'removeAllExit',
        'click .pubRecTabs li': 'doTabClick'
    },


    //override super-class initialize function
    initialize: function( opts ) {

        var that = this;
        _.bindAll( that );

        this.appName = 'paxSearch';
        this.overlayTpl = 'paxSearchView';
        this.keyEnabled = false;
        this.selectAllWarnNumber = 20;
        this.isLoading = false;
        this.autocompLastKeyupTime = 0;
        this.autocompDelay = 0;
        this.startPlaceholder = opts.startPlaceholder;
        this.keyTyped = opts.searchKey;
        this.follow = opts.follow;
        this.multiSelect = opts.multiSelect;
        this.recognition = opts.recognition;
        this.searchParams = opts.searchParams;
        this.preSelectedParticipants = opts.preSelectedParticipants;
        this.disableSelect = opts.disableSelect;
        this.selectedPaxCollection = opts.selectedPaxCollection;
        this.searchUrl = opts.searchUrl || G5.props.URL_JSON_PARTICIPANT_TOKENIZED_SEARCH_AUTOCOMPLETE;
        this.selectUrl = opts.selectUrl;
        this.deselectUrl = opts.deselectUrl;
        this.addOnSelect = opts.addOnSelect;
        this.addOnFollow = opts.addOnFollow;
        this.presetFilters = opts.presetFilters;
        this.filterObj = opts.filterObj;
        this.clearOnClose = opts.clearOnClose;
        this.hideCheckAll = opts.hideCheckAll;
        this.saveGroup = opts.saveGroup;

        // set our searchCollection this displays all search results
        this.searchCollectionView = new PaxSearchCollectionView( {
            el: this.$el,
            follow: this.follow,
            disableSelect: this.disableSelect,
            multiSelect: this.multiSelect,
            recognition: this.recognition,
            searchUrl: this.searchUrl,
            selectUrl: this.selectUrl,
            deselectUrl: this.deselectUrl,
            addOnSelect: this.addOnSelect,
            addOnFollow: this.addOnFollow
        } );

        // add events
        this.searchCollectionView.on( 'callToFetchRecipientList', this.callToFetchRecipientList );
        this.searchCollectionView.on( 'exitSearch', this.exitSearch );
        this.searchCollectionView.on( 'setCheckAll', this.setCheckAll );
        this.searchCollectionView.on( 'pageLoadFull', function( ) { this.autocompWait( false, true ); }, this );
        G5._globalEvents.on( 'paxSearch:exitSearch', this.exitSearch, this );
        // check selected Pax Settings - add items to a selected view

        if ( opts.addSelectedPaxView ) {
            //console.log( 'opts.selectedPaxCollection',opts.selectedPaxCollection );
            this.selectedPaxView = new PaxSelectedPaxView( {
                selectedPaxCollection: this.selectedPaxCollection,
                preSelectedParticipants: this.preSelectedParticipants,
                recognition: this.recognition,
                follow: this.follow,
                multiSelect: this.multiSelect,
                selectUrl: this.selectUrl,
                deselectUrl: this.deselectUrl,
                saveGroup: this.saveGroup,
                dontRenderthat: true
            } );
            this.selectedPaxView.on( 'postRender', this.postRender );
            this.selectedPaxView.on( 'selectedOpen', this.selectedOpen );
            this.selectedPaxView.on( 'selectedClosed', this.selectedClosed );
            this.selectedPaxView.on( 'pageLoadFull', function( ) { this.autocompWait( false, true ); }, this );
            // pass a reference of the views collections so we can talk to eachother
            this.selectedPaxView.setCollectionReference( this.searchCollectionView.collection );
            this.searchCollectionView.setCollectionReference( this.selectedPaxView.collection );
        } else if ( this.selectedPaxCollection ) {
            this.searchCollectionView.setCollectionReference( this.selectedPaxCollection );
        }

        //set our model
        this.model = new PaxSearchModel( {
            searchUrl: this.searchUrl,
            // filterPresets: this.filters||null,
            // allow the select all button in the search results table
            searchParams: this.searchParams,
            allowSelectAll: this.allowSelectAll,
            participantsCollection: opts.participantsSearchCollection || null
        } );


        this.model.on( 'filtersDone', this.render );
        this.model.on( 'setNoQuery', this.setNoQuery );
        this.model.on( 'setNoResultsFound', this.setNoResultsFound );
        this.model.on( 'renderRecipientList', this.renderRecipientList );
        this.model.on( 'updateCounts', this.updateCounts );
        this.model.on( 'renderAutoCompleteList', this.renderAutoCompleteList );

        //spinner ( busy visualization ) during server calls
        this.model.on( 'startBusy:queryAutocomplete', function( ) { this.autocompWait( true, false ); }, this );
        this.model.on( 'endBusy:queryAutocomplete', function( ) { this.autocompWait( false, false ); }, this );
        this.model.on( 'startBusy:loadParticipants', function( ) { this.autocompWait( true, false ); }, this );
        this.model.on( 'endBusy:loadParticipants', function( ) { this.autocompWait( false, false ); }, this );
        this.model.on( 'allowSelectAllChanged', this.updateSelectAllVisibility, this );



        // check things ???? add more
        if ( this.presetFilters ) {
            this.render( );
        } else {
            this.model.fetchFilters( );
        }
        console.log( 'SEARCH this.multiSelect ', this.multiSelect );
        //this.$el.fadeTo( 0 );
    },

    render: function( ) {
        var that = this;

        // TODO: seperate template files
        TemplateManager.get( that.overlayTpl, function( tpl, vars, subTpls ) {
            that.$el.append( tpl );
            that.paxFilterTpl = subTpls.paxFilterTpl;
            that.paxSelectedFilterTpl = subTpls.paxSelectedFilterTpl;
            that.paxAutoCompleteSuggetionsTpl = subTpls.paxAutoCompleteSuggetionsTpl;
            if ( !that.selectedPaxView ) {
                that.postRender( );
            } else {
                that.selectedPaxView.setElement( that.$el.find( '.PaxSelectedPaxView' ) );
                that.selectedPaxView.render( );
            } // else postrender will be called when selected has rendered to avoid race condition
            that.searchCollectionView.postRender( );

        }, G5.props.URL_TPL_ROOT || G5.props.URL_BASE_ROOT + 'tpl/' );

        this.$el.find( '.paxOverlaySearchInput' ).placeholder( );


        // set the main page to not scroll
        $( 'body' ).addClass( 'no-scroll' );
        $( 'body' ).addClass( 'search-overlay-open' );
        $( 'html' ).addClass( 'no-scroll' );
    },

    postRender: function( ) {
        var searchFilterObj;
        if( this.hideCheckAll ) {
            this.$el.find( '.check-all-wrap' ).hide();
        }
        this.$el.find( '.searchWrapper' ).fadeTo( 300, 1 );
        if ( this.presetFilters ) {
            this.$el.find( '.pubRecFilterWrap' ).show( );
            this.$el.find( '.search-input-wrap' ).hide( );
            this.renderTabs( this.presetFilters );
            if ( this.filterObj ) {
                //console.log( 'this.filterObj', this.filterObj );
                this.addFilterToken( this.filterObj.name );
                //this.$el.find( '.dropdown-toggle span' ).text( this.filterObj.name );
                searchFilterObj = {
                    'orgUnit': this.filterObj.id,
                    'location': this.filterObj.id
                };
                this.queryData = searchFilterObj;
                this.model.fetchRecipientList( searchFilterObj );
            }
        } else {

            this.$el.find( '.pubRecFilterWrap' ).hide( );
            if ( this.startPlaceholder ) { this.$el.find( '.paxOverlaySearchInput' ).attr( 'placeholder', this.startPlaceholder ); }
            // add the key event after all is loaded
            this.$el.find( '.paxOverlaySearchInput' ).focus( );
        }
        if ( this.searchParams && ( this.searchParams.nodeId || this.searchParams.promotionId || this.searchParams.contestId ) ) {
            this.$el.find( '.only-eligible-prompt' ).show( );
        }
        /*this.hideSearchBox( );
        console.log( 'postRenter' );*/
        this.disableSelectAll( );
        this.initModals( );
        if ( this.selectedPaxView ) { this.selectedPaxView.off( 'postRender', this.postRender ); }

    },


/***
*     .d888 d8b 888 888                                                                    888
*    d88P"  Y8P 888 888                                                                    888
*    888        888 888                                                                    888
*    888888 888 888 888888 .d88b.  888d888      .d8888b   .d88b.   8888b.  888d888 .d8888b 88888b.
*    888    888 888 888   d8P  Y8b 888P"        88K      d8P  Y8b     "88b 888P"  d88P"    888 "88b
*    888    888 888 888   88888888 888          "Y8888b. 88888888 .d888888 888    888      888  888
*    888    888 888 Y88b. Y8b.     888               X88 Y8b.     888  888 888    Y88b.    888  888
*    888    888 888  "Y888 "Y8888  888           88888P'  "Y8888  "Y888888 888     "Y8888P 888  888
*
*
*
 */


    renderTabs: function( filters ) {
        var that = this;
        _.each( filters.filters, function( filter ) {
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
                            //'data-total-count':filter.get( 'totalCount' )
                        } )
                        .html( filter.name + '<!--sup>' + numNew + '</sup-->' ),
                $li = $( '<li />' ).html( $a );
            that.$el.find( '.pubRecTabs' ).append( $li );
        } );
    },

    doTabClick: function( e ) {
        var $tar = $( e.currentTarget ).find( 'a' ),
            searchFilterObj = {
                //'name':$tar.find( 'span' ).text( ),
                //'id':$tar.data( 'id' ),
                'orgUnit': $tar.data( 'id' ),
                'location': $tar.data( 'id' )
                //'type':$tar.data( 'type' ),
                //'typeName':$tar.data( 'typename' ),
            };
        this.addFilterToken( $( e.currentTarget ).text( ) );
        this.queryData = searchFilterObj;
        // call to the search
        this.model.fetchRecipientList( searchFilterObj );
        e.preventDefault( );
    },

    addFilterToken: function( name ) {
        this.$el.find( '.dropdown-toggle span' ).text( name );
    },


    /***
     *                                             888             888
     *                                             888             888
     *                                             888             888
     *     8888b.  88888b.  88888b.       .d8888b  888888  8888b.  888888 .d88b.  .d8888b
     *        "88b 888 "88b 888 "88b      88K      888        "88b 888   d8P  Y8b 88K
     *    .d888888 888  888 888  888      "Y8888b. 888    .d888888 888   88888888 "Y8888b.
     *    888  888 888 d88P 888 d88P           X88 Y88b.  888  888 Y88b. Y8b.          X88
     *    "Y888888 88888P"  88888P"        88888P'  "Y888 "Y888888  "Y888 "Y8888   88888P'
     *             888      888
     *             888      888
     *             888      888
     */


     // show and hide the search box
    showSearchBox: function( ) {
        if ( $( '.hints-wrapper' ).css( 'opacity' ) === 0 || !$( '.hints-wrapper' ).is( ':visible' ) ) {
            $( '.hints-wrapper' ).fadeIn( G5.props.ANIMATION_DURATION );
            $( '.searchBG' ).fadeIn( G5.props.ANIMATION_DURATION );

        } else {
           //
           //console.log( 'attempt to show searchbox',$( '.hints-wrapper' ).css( 'opacity' ),$( '.hints-wrapper' ).is( ':visible' ) );
        }
    },
    hideSearchBox: function( ) {
        $( '.hints-wrapper' ).fadeOut( G5.props.ANIMATION_DURATION );
        $( '.searchBG' ).fadeOut( G5.props.ANIMATION_DURATION );
        this.searchCollectionView.removeAnimateClass( );
    },

    // search bar focus and blur
    searchBlur: function( ) {
        //console.log( 'blur',document.activeElement );
    },
    searchFocus: function( ) {
        console.log( 'focus' );
        // check if we have inited first if not need to get value of
        if ( !this.keyEnabled ) {
            console.log( 'getInputValue' );
            this.trigger( 'getInputValue' );
        }
    },

    // see if we should hid the search box
    checkhideSearchBox: function( event ) {
        if ( event.target === event.currentTarget ) { this.hideSearchBox( ); }
    },
    setNoQuery: function( ) {
        //clear all
        this.setFiltersAndCounts( );

        this.updateCounts( );
        this.disableSelectAll( );
    },

    setNoResultsFound: function( errorText ) {
        if ( this.queryData ) { delete this.queryData.all; }
        this.searchCollectionView.setNoResultsFound( errorText );
    },

/***
*                      888             .d8888b.                                  888          888
*                      888            d88P  Y88b                                 888          888
*                      888            888    888                                 888          888
*     8888b.  888  888 888888 .d88b.  888         .d88b.  88888b.d88b.  88888b.  888  .d88b.  888888 .d88b.
*        "88b 888  888 888   d88""88b 888        d88""88b 888 "888 "88b 888 "88b 888 d8P  Y8b 888   d8P  Y8b
*    .d888888 888  888 888   888  888 888    888 888  888 888  888  888 888  888 888 88888888 888   88888888
*    888  888 Y88b 888 Y88b. Y88..88P Y88b  d88P Y88..88P 888  888  888 888 d88P 888 Y8b.     Y88b. Y8b.
*    "Y888888  "Y88888  "Y888 "Y88P"   "Y8888P"   "Y88P"  888  888  888 88888P"  888  "Y8888   "Y888 "Y8888
*                                                                       888
*                                                                       888
*                                                                       888
*/
    //gateway to autocomplete, check num chars is enough
    doAutocomplete: function( ) {
        //console.log( 'doAutocomplete' );
        var that       = this,
            $input     = this.$el.find( '.paxOverlaySearchInput' ),
            delay      = $input.data( 'autocomp-delay' ),
            minChars   = $input.data( 'autocomp-min-chars' ),
            stringLength = $.trim( $input.val( ) ).length,
            valid =  this.isValidString( $input.val( ), minChars ),
            filterInUse = this.haveFilterInUse( );

        this.autocompDelay = delay;

        if ( valid ) {
            this.autocompLastKeyupTime = ( new Date( ) ).getTime( );
            //set a timeout on delay, and see if keyboard has been idle for long enough
            setTimeout( function( ) { that.tryAutocomplete( ); }, this.autocompDelay );
        } else if ( !filterInUse ) {
            this.searchCollectionView.showEmtpyResults( );

            this.disableSelectAll( );
            // clear out tiles?
            this.refreshSearchResults( );
        } else if ( stringLength === 0 ) {
            // we have a filter in use and no search - so send previous search
            this.autocompLastKeyupTime = ( new Date( ) ).getTime( );
            //set a timeout on delay, and see if keyboard has been idle for long enough
            setTimeout( function( ) { that.tryAutocomplete( true ); }, this.autocompDelay );
        } else {
            // we have a filter in use but not a valid string, do we need to do anything
            console.log( 'hold search not valid no filter : stringLength = ', stringLength );
        }


    },

    //check the time since last keypress, and forward query if apropo
    tryAutocomplete: function( send ) {
        var $input     = this.$el.find( '.paxOverlaySearchInput' ),
            now = ( new Date( ) ).getTime( ),
            delay = now - this.autocompLastKeyupTime,
            minChars   = $input.data( 'autocomp-min-chars' );//,
            //searchType = this.$searchType.val( ),
            //searchTypeName = this.$searchType.find( 'option:selected' ).text( );
        //check to make sure no keys have been pressed during the delay ( give 10ms margin )
        if ( delay >= this.autocompDelay - 10 ) {
            // check to make sure the query string isn't too long
            if ( this.isValidString( $input.val( ), minChars ) || send ) {
                this.recipientSearchAutocomplete( );
            } else {
                this.searchCollectionView.showEmtpyResults( );
                this.disableSelectAll( );
                this.hideSearchBox( );
                // clear out tiles?
                this.refreshSearchResults( );
            }
        }
    },

    /***
     *    888                        888 d8b
     *    888                        888 Y8P
     *    888                        888
     *    888  .d88b.   8888b.   .d88888 888 88888b.   .d88b.
     *    888 d88""88b     "88b d88" 888 888 888 "88b d88P"88b
     *    888 888  888 .d888888 888  888 888 888  888 888  888
     *    888 Y88..88P 888  888 Y88b 888 888 888  888 Y88b 888
     *    888  "Y88P"  "Y888888  "Y88888 888 888  888  "Y88888
     *                                                     888
     *                                                Y8b d88P
     *                                                 "Y88P"
     */
    // lazy load on scroll
    setStateLoading: function( mode ) {
        //console.log( 'setStateLoading mode:',mode );
        var spinProps = {};
        spinProps.classes = mode;

        switch ( mode ) {
          case 'more':
            G5.util.showSpin( this.$el.find( '.search-results' ), spinProps );
            break;
          case 'cover':
            G5.util.showSpin( this.$el.find( '.searchWrapper' ), { cover: true } );
            break;
          default:
            G5.util.showSpin( this.$el.find( '.search-results' ), spinProps );
            break;
        }
        this.isLoading = true;
    },

    setStateLoaded: function( ) {
        G5.util.hideSpin( this.$el.find( '.search-results' ) );
        G5.util.hideSpin( this.$el.find( '.searchWrapper' ) );
        this.searchCollectionView.setLazyLoadSpinner();
        this.isLoading = false;
    },
    //TODO: put in spinners etc.
    autocompWait: function( isWait, isDisable ) {
        if ( isWait ) {
            this.setStateLoading( 'more' );

        } else if ( isDisable ) {
            this.setStateLoading( 'cover' );
        } else {
            this.setStateLoaded( );

        }
    },

    /***
    *     .d888                            888    d8b
    *    d88P"                             888    Y8P
    *    888                               888
    *    888888 888  888 88888b.   .d8888b 888888 888  .d88b.  88888b.  .d8888b
    *    888    888  888 888 "88b d88P"    888    888 d88""88b 888 "88b 88K
    *    888    888  888 888  888 888      888    888 888  888 888  888 "Y8888b.
    *    888    Y88b 888 888  888 Y88b.    Y88b.  888 Y88..88P 888  888      X88
    *    888     "Y88888 888  888  "Y8888P  "Y888 888  "Y88P"  888  888  88888P'
    *
    *
    *
    */
    setFiltersAndCounts: function( ) {
        var searchFilterTypeCounts = this.model.searchFilterTypeCounts,
            that = this;
         if ( this.queryData ) { delete this.queryData.all; }
        _.each( searchFilterTypeCounts, function( filterTypeCount ) {
            //console.log( 'filterTypeCount',filterTypeCount );
            var el = that.$el.find( '.tokenselector[data-id=' + filterTypeCount.paxSearchFilterType + ']' );
            if ( el.length ) {
                if ( filterTypeCount.count > 0 ) {
                    el.removeClass( 'disabled' );
                } else {
                    el.addClass( 'disabled' );

                }
                el.find( '.num-count' ).html( filterTypeCount.count );
            } else {
                //?
            }

        } );
        // update total records
            this.$el.find( '.tokenselector[data-id="name"] .num-count' ).html( this.model.totalRecordsFound );
            this.$el.find( '.totalRecordsFoundText' ).text( this.model.totalRecordsFound );
            this.$el.find( '.hints' ).fadeIn( G5.props.ANIMATION_DURATION );
            this.searchCollectionView.totalRecordsFound = this.model.totalRecordsFound;
            this.searchCollectionView.checkLoadNum( );
    },
    updateCounts: function( ) {
        this.setFiltersAndCounts( );
    },
    // render
    // Fired when we have new search results
    renderRecipientList: function( event ) {

        // update the collection
        this.searchCollectionView.renderRecipientList( event.participants, event.lazyLoad, event.preselected );
        //  check our total records per search
        this.setFiltersAndCounts( );

        this.recipientListActiveId = undefined; // [3]
        if ( !event.preselected ) { this.setCheckAll( false ); }
        this.enableSelectAll( );

    },
    renderAutoCompleteList: function( event ) {
        //console.log( 'renderAutoCompleteList:' );
        var templateData = this.paxAutoCompleteSuggetionsTpl( this.model.autoCompletionsData );
        this.$el.find( '.tokenselector>ul' ).hide( );
        this.$el.find( '.tokenselector[data-id=' + event.type + '] .hints-container' ).html( templateData );
    },

    haveFilterInUse: function( ) {
        this.queryData = this.queryData || {};
        if ( Object.keys( this.queryData ).length ) {
            return true;
        } else { return false; }
    },

    isValidString: function( startString, minChars ) {

        var string = $.trim( startString );
        if ( string.length >= minChars && ( /\S/.test( string ) ) ) {
            this.showSearchBox( );
            return true;
        } else {
            this.hideSearchBox( );
            return false;
        }
    },

    recipientSearchAutocomplete: function( ) {
        //console.log( 'recipientSearchAutocomplete' );
        var that       = this,
            $input     = this.$el.find( '.paxOverlaySearchInput' ),
            queryData,
            activeFilter,
            queryInput,
            type,
            query = {};

        this.searchCollectionView.hideEmtpyResults( );
        this.enableSelectAll( );
        this.showFilters( );

        this.queryData = this.queryData || {};

        // do we have any selected filters
        if ( !this.haveFilterInUse( ) ) {
            query.name = $input.val( ).trim( );
            if ( query.name ) {
                that.callToFetchRecipientList( query );
            }
        } else {
                queryData;
                activeFilter = this.$el.find( '.tokenselector.active' );
                queryInput = this.$el.find( '.paxOverlaySearchInput' ).val( ).trim( );
            if ( activeFilter.length ) {
                type = activeFilter.parent( ).data( 'id' );
                if ( type === 'name' ) {
                    //tk do something here to reset the active
                    this.$el.find( '.tokenselector>li:first-child' ).addClass( 'active' );
                    return false;
                }
                // check if name is being used
                if ( !this.queryData.name ) {
                    queryData = _.extend( { 'name': queryInput, 'type': type }, this.queryData );
                } else {
                    queryData = _.extend( { 'filter': queryInput, 'type': type }, this.queryData );
                }
                 //console.log( 'queryData ',queryData );
                this.callToFetchRecipientList( queryData );
            } else {
                queryData = _.extend( { 'filter': queryInput }, this.queryData );
                //console.log( 'this.queryData ',queryData );
                that.callToFetchRecipientList( queryData );
            }
        }
    },

    /***
    *    888 d8b          888
    *    888 Y8P          888
    *    888              888
    *    888 888 .d8888b  888888 .d88b.  88888b.   .d88b.  888d888 .d8888b
    *    888 888 88K      888   d8P  Y8b 888 "88b d8P  Y8b 888P"   88K
    *    888 888 "Y8888b. 888   88888888 888  888 88888888 888     "Y8888b.
    *    888 888      X88 Y88b. Y8b.     888  888 Y8b.     888          X88
    *    888 888  88888P'  "Y888 "Y8888  888  888  "Y8888  888      88888P'
    *
    *
    *
    */

    // search key listener
    recipientSearchKeyController: function( event ) {
        var pressedKey,
            checkKey,
            //direction,
            keyIs;
        if ( this.keyEnabled ) {
            //console.log( 'recipientSearchKeyController ',event.keyCode );
            pressedKey = event.keyCode;
            checkKey   = function( key ) {
                            return key === pressedKey;
                         };
            keyIs      = { // [2]
                            escape: checkKey( 27 ),
                            backspace: checkKey( 8 ),
                            shiftKey: checkKey( 16 ),
                            enterKey: checkKey( 13 ),
                            downKey: checkKey( 40 ),
                            tabKey: checkKey( 9 ),
                            upKey: checkKey( 38 )
                         };

            if ( keyIs.enterKey ) { // [3]
                event.preventDefault( );
                if ( !this.recipientListActiveIdLocked && this.recipientListActiveId !== undefined ) { // [3a]
                    this.submitParticipant( this.recipientListActiveId );
                } else { // [3b]
                    this.submitSearch( );
                }
            } else { // [4]
                if ( keyIs.downKey || keyIs.upKey ||
                    keyIs.tabKey  || keyIs.shiftKey ) { // [5]

                    event.preventDefault( );

                    if ( !this.recipientList ) { // [5a]
                        return;
                    }

                    /*direction = 0; // [5b]

                    if ( keyIs.downKey ) {
                        direction = 1;
                    }

                    if ( keyIs.upKey ) {
                        direction = -1;
                    }

                    if ( keyIs.tabKey ) {
                        direction = ( event.shiftKey )? -1 :  1;
                    }

                    if ( !keyIs.shiftKey ) {
                        //this.shiftHighlightedRecipient( direction );
                    }*/
                } else if ( keyIs.backspace ) {
                    this.refreshSearchResults( );
                    this.doAutocomplete( );
                } else if ( keyIs.escape ) {
                    this.$el.closest( '.qtip' ).qtip( 'hide' );
                } else {
                    this.doAutocomplete( );

                }
            }
        }
    },


    /***
     *                      888                   888                 888
     *                      888                   888                 888
     *                      888                   888                 888
     *    .d8888b   .d88b.  888  .d88b.   .d8888b 888888 .d88b.   .d88888
     *    88K      d8P  Y8b 888 d8P  Y8b d88P"    888   d8P  Y8b d88" 888
     *    "Y8888b. 88888888 888 88888888 888      888   88888888 888  888
     *         X88 Y8b.     888 Y8b.     Y88b.    Y88b. Y8b.     Y88b 888
     *     88888P'  "Y8888  888  "Y8888   "Y8888P  "Y888 "Y8888   "Y88888
     *
     *
     *
     */

    selectedOpen: function( ) {
        this.$el.find( '.PaxSearchView' ).addClass( 'selectedOpen' );
    },
    selectedClosed: function( ) {
        this.$el.find( '.PaxSearchView' ).removeClass( 'selectedOpen' );
    },
    // enable disale select all input
    enableSelectAll: function( ) {
        $( '.totalRecordsFound' ).css( 'visibility', 'visible' );
        this.$el.find( '.checkAll' ).prop( 'disabled', false );
        if ( this.selectedPaxView && this.multiSelect ) { $( '.check-all-wrap' ).css( 'visibility', 'visible' ); }
    },
    disableSelectAll: function( ) {
        $( '.totalRecordsFound' ).css( 'visibility', 'hidden' );
        this.$el.find( '.checkALL' ).prop( 'disabled', true );
        $( '.check-all-wrap' ).css( 'visibility', 'hidden' );
    },
    // set check all checked or unchecked
    setCheckAll: function( bool ) {
        this.$el.find( '.checkAll' ).prop( 'checked', bool );
        //this.$el.find( '.checkAll' ).prop( 'checked',false ).parent( ).removeClass( 'selected-checkbox' );
    },


    //TODO: make sure this is added everywhwere and better popup
    // check max number of cards reached
    checkMaxNumberCardsAdded: function( numToAdd ) {
        var totalAdded = numToAdd + this.selectedPaxView.totalAdded( );
        if ( this.model.maxAllowedToRecognize && totalAdded >= this.model.maxAllowedToRecognize ) {
            this.showModalMaxReached( );
            this.setCheckAll( false );
            return false;
        } else {
            return true;
        }
    },

    // TODO: this needs to change - temp to alert user of errors etc.
    checkSelectAll: function( e ) {
        var checked = e.target.checked;
        if ( this.checkMaxNumberCardsAdded( this.model.totalRecordsFound ) ) {
            if ( this.model.totalRecordsFound > this.selectAllWarnNumber && checked ) { //this.$el.find( '.checkAll' ).prop( 'checked' )
                this.showModalWarn( );
            } else {
                this.toggleSelectall( );
            }
        } else {
           // error
        }
    },


    // MODAL POP UPS
    initModals: function( ) {
        var that = this;
        this.$el.find( '.pax-modal-confirm' ).on( 'hide', function ( ) {
            //e.preventDefault( );
            if ( !$( '.pax-modal-confirm' ).data( 'confirm' ) ) {
                that.setCheckAll( false );
            }
        } );
    },
    showModalWarn: function( ) {
        this.$el.find( '.pax-modal-confirm' ).data( 'confirm', false );
        this.$el.find( '.pax-modal-confirm' ).find( '.total-to-add' ).text( this.model.totalRecordsFound );
        this.$el.find( '.pax-modal-confirm' ).modal( );
    },

    showModalMaxReached: function( ) {
        this.$el.find( '.pax-modal-too-many' ).find( '.max-allowed' ).text( this.model.maxAllowedToRecognize );
        this.$el.find( '.pax-modal-too-many' ).modal( );
    },

    //
    toggleSelectall: function( ) {
        var isChecked = this.$el.find( '.checkAll' ).prop( 'checked' );
        // set all cards to the correct check status
        if ( isChecked ) {
            // call to get all the cards
            this.callToFetchAll( );
            //this.searchCollectionView.selectAllCards( );
        } else {
            // deselect all cards
            this.searchCollectionView.deSelectAllCards( );
        }
    },

    // set all search cards not selected
    deselectAll: function( ) {
        this.searchCollectionView.deSelectAllCards( );
    },
    /***
    *                                               888
    *                                               888
    *                                               888
    *    .d8888b   .d88b.   8888b.  888d888 .d8888b 88888b.
    *    88K      d8P  Y8b     "88b 888P"  d88P"    888 "88b
    *    "Y8888b. 88888888 .d888888 888    888      888  888
    *         X88 Y8b.     888  888 888    Y88b.    888  888
    *     88888P'  "Y8888  "Y888888 888     "Y8888P 888  888
    *
    *
    *
    */

    // show searh hints in search input
    showHints: function( event ) {
        var type = $( event.currentTarget ).data( 'id' ),
            tokenselector,
            query,
            queryData;

        event.preventDefault( );

        if ( type === 'name' ) {
            $( event.currentTarget ).addClass( 'disabled' );
            this.selectFilter( event );
            return false;
        }

        tokenselector = $( event.currentTarget );
        if ( tokenselector[ 0 ].tagName !== 'LI' ) {
            tokenselector = $( event.currentTarget ).parent( );
        }
        if ( tokenselector.hasClass( 'active' ) ) {
            tokenselector.removeClass( 'active' );
            return false;
        }

        this.$el.find( '.tokenselector.active' ).removeClass( 'active' );
        tokenselector.addClass( 'active' );

        if ( this.$el.find( '.tokens>li:first-child' ).hasClass( 'active' ) ) {
             this.$el.find( '.triangle' ).addClass( 'triangledark' );
        } else {
            this.$el.find( '.triangle' ).removeClass( 'triangledark' );
        }

        query = this.$el.find( '.paxOverlaySearchInput' ).val( ).trim( );
        if ( query ) {
            queryData = _.extend( { 'query': query, 'type': type }, this.queryData );
            this.callToFetchAutoCompleteResult( queryData );
        }
    },

    // show filters
    showFilters: function( searchValue ) {
        //console.log( 'this.filters', this.model.filters );
        var that = this,
            filters = this.model.filters,
            filtersTemplate;

        searchValue = searchValue || that.$el.find( '.paxOverlaySearchInput' ).val( ).trim( );
        that.unselectedFilters = [];
        that.queryData = that.queryData || {};

        _.each( filters, function( filter ) {
            if ( !filter.inuse ) {
                 that.unselectedFilters.push( { id: filter.code, name: filter.name, value: searchValue } );
            }
        } );

        filtersTemplate = that.paxFilterTpl( this.unselectedFilters );

        this.$el.find( '.tokens' ).html( filtersTemplate );

        this.$el.find( '.tokens>li:first-child' ).hover( function( ) {
             if ( !$( this ).hasClass( 'active' ) ) { that.$el.find( '.triangle' ).addClass( 'triangledarkHover' ); }
          }, function( ) {
            that.$el.find( '.triangle' ).removeClass( 'triangledarkHover' );
          } );

        if ( this.$el.find( '.tokens .enter-btn' ).length ) {
            this.$el.find( '.triangle' ).addClass( 'triangledark' );
        } else {
            this.$el.find( '.triangle' ).removeClass( 'triangledark' );
        }
    },


    showSelectedFilters: function( filterID ) {
        var that = this;
        //console.log( 'filterID',filterID );
        // find the correct filter - .
        var filterMatch = _.find( this.model.filters, function ( filter ) { return filter.code === filterID; } ),
            fObj = {
                code: filterMatch.code,
                id: filterMatch.code,
                name: filterMatch.name,
                value: filterMatch.code === 'name' ? that.queryData[ filterMatch.code ] : that.filterData[ filterMatch.code ]
            },
            filtersTemplate = that.paxSelectedFilterTpl( fObj );

        filterMatch.inuse = true;
        this.$el.find( '.selected-filters .paxOverlaySearchInput' ).before( filtersTemplate );
        //console.log( 'showSelectedFilters' );
        this.hideSearchBox( );
        this.checkTotalFilters( );
    },

    // check to see if we have used all the filters
    checkTotalFilters: function( ) {
        // check if we have used them all up
        if ( this.model.filters.length === this.$el.find( '.selected-filter' ).length ) {
            $( '.paxOverlaySearchInput' ).prop( 'disabled', true );
            $( '.paxOverlaySearchInput' ).hide( );
            $( '.paxOverlaySearchInput' ).blur( );
        } else {
            $( '.paxOverlaySearchInput' ).prop( 'disabled', false );
            $( '.paxOverlaySearchInput' ).show( );
            $( '.paxOverlaySearchInput' ).focus( );
        }
    },

    // select filter no sub value ( ie name hint );
    selectFilter: function( event ) {
        var queryInput;
        if ( this.$el.find( '.paxOverlaySearchInput' ) ) {
            queryInput = this.$el.find( '.paxOverlaySearchInput' ).val( ).trim( );
            this.queryData[ $( event.currentTarget ).data( 'id' ) ] = queryInput;
            this.$el.find( '.paxOverlaySearchInput' ).val( '' );
            this.callToFetchRecipientList( this.queryData );
            this.showSelectedFilters( $( event.currentTarget ).closest( '.tokenselector' ).data( 'id' ) );
            this.showFilters( );
        }
       // return false;
    },

    // hint value clicked
    selectFilterFromHints: function( event ) {
        var pID = $( event.currentTarget ).closest( '.tokenselector' ).data( 'id' );
        this.filterData = this.filterData || {};

        this.queryData[ pID ] = $( event.currentTarget ).data( 'field-id' );
        this.filterData[ pID ] = $( event.currentTarget ).text( );
        this.$el.find( '.paxOverlaySearchInput' ).val( '' );
        this.callToFetchRecipientList( this.queryData );
        this.showFilters( );

        this.showSelectedFilters( pID );
        //return false;
    },

    // remove filter from search bar
    removeFilter: function( event ) {
        var that = this,
            filterMatch,
            el = $( event.currentTarget ).closest( '.selected-filter' ),
            fid =  el.data( 'id' );

        //console.log( 'remove Filter', $( event.target ).closest( '.selected-filter' ).data( 'id' ) );
        if ( this.queryData ) { delete this.queryData[ $( event.currentTarget ).closest( '.selected-filter' ).data( 'id' ) ]; }
        this.callToFetchRecipientList( this.queryData );
        this.showFilters( );

        // remove the filter from the list
        el.remove( );

        filterMatch = _.find( that.model.filters, function ( filter ) { return filter.code === fid; } );
        filterMatch.inuse = null;

        this.refreshSearchResults( );
        this.showFilters( );
        this.checkTotalFilters( );
    },

    refreshSearchResults: function( ) {
        var queryInput;
        if ( this.$el.find( '.paxOverlaySearchInput' ).length ) {
            queryInput = this.$el.find( '.paxOverlaySearchInput' ).val( ).trim( );
            // remove entries one just one letter is there
            if ( !this.queryData || !( queryInput.length > 1 || Object.keys( this.queryData ).length ) ) {
                this.searchCollectionView.setEmptyResults( );
                this.setCheckAll( false );

            }
        }

    },

    callToFetchAll: function( e ) {
        this.queryData.all = true;
        if ( e ) {
            // close the modal if open
            $( '.pax-modal-confirm' ).data( 'confirm', true );
            $( '.pax-modal-confirm' ).modal( 'hide' );
        }
        this.hideSearchBox( );
        this.callToFetchRecipientList( null, null, true );
    },
    callToFetchAutoCompleteResult: function( queryData ) {
        this.model.fetchAutoCompleteResult( queryData );
    },
    callToFetchRecipientList: function( queryData, lazyLoad, preselected ) {
        var queryData;
        //console.log( 'callToFetchRecipientList',queryData );
        if ( this.model.totalRecordsFound <= this.searchCollectionView.totalAdded( ) && lazyLoad ) {
            this.searchCollectionView.setLazyLoadSpinner();
            return false;
        }
        if ( !queryData ) {

            queryData = _.extend( {}, this.queryData );
            if ( !this.queryData.name && !this.presetFilters ) {
                queryData.name = this.$el.find( '.paxOverlaySearchInput' ).val( ).trim( );
            }
            if ( lazyLoad ) { queryData.fromIndex = this.searchCollectionView.totalAdded( ); }
        }
        this.model.fetchRecipientList( queryData, lazyLoad, preselected );
    },


    /***
    *                      888                    d8b 888
    *                      888                    Y8P 888
    *                      888                        888
    *    .d8888b  888  888 88888b.  88888b.d88b.  888 888888 .d8888b
    *    88K      888  888 888 "88b 888 "888 "88b 888 888    88K
    *    "Y8888b. 888  888 888  888 888  888  888 888 888    "Y8888b.
    *         X88 Y88b 888 888 d88P 888  888  888 888 Y88b.       X88
    *     88888P'  "Y88888 88888P"  888  888  888 888  "Y888  88888P'
    *
    *
    *
    */

     // force submit search ( return key )
     submitSearch: function( ) {
        // new tk
        // find current active filter
        // check if we have min chars
        var $input     = this.$el.find( '.paxOverlaySearchInput' ),
            minChars   = $input.data( 'autocomp-min-chars' ); // [1]
        //console.log( 'submitSearch' );
        this.hideSearchBox( );
        if ( this.isValidString( $input.val( ), minChars ) ) {
            //console.log( $input.val( ).length +' '+ minChars );
            this.$el.find( '.tokenselector.active' ).trigger( 'click' );
        }
    },

     // TODO: what is this?  look at all recipient lists
     submitParticipant: function( index ) {
        //console.log( 'submitParticipant??????????' );
        var participantUrl = _.where( this.recipientList, { 'index': index } )[ 0 ].participantUrl;
        window.location.assign( participantUrl );
    },


    /***
    *                      888 888               888                        888                                      888
    *                      888 888               888                        888                                      888
    *                      888 888               888                        888                                      888
    *     .d8888b  8888b.  888 888  .d88b.   .d88888       .d88b.  888  888 888888 .d88b.  888d888 88888b.   8888b.  888
    *    d88P"        "88b 888 888 d8P  Y8b d88" 888      d8P  Y8b `Y8bd8P' 888   d8P  Y8b 888P"   888 "88b     "88b 888
    *    888      .d888888 888 888 88888888 888  888      88888888   X88K   888   88888888 888     888  888 .d888888 888
    *    Y88b.    888  888 888 888 Y8b.     Y88b 888      Y8b.     .d8""8b. Y88b. Y8b.     888     888  888 888  888 888
    *     "Y8888P "Y888888 888 888  "Y8888   "Y88888       "Y8888  888  888  "Y888 "Y8888  888     888  888 "Y888888 888
    *
    *
    *
    */

     updateKeyTypes: function( keysTyped ) {
        var that = this,
            $input = this.$el.find( '.paxOverlaySearchInput' ),
            strLength;

        $input.val( keysTyped );
        strLength = $input.val( ).length;
        this.keyTyped = keysTyped;
        this.keyEnabled = true;

        $input[ 0 ].setSelectionRange( strLength, strLength );
        console.log( 'updateKeyTypes ', keysTyped );

        // adding small delay to try and update keys
        setTimeout( function( ) {
            that.doAutocomplete( );
        }, that.autocompDelay + 100 );
        //this.doAutocomplete( );
    },
    checkExitSearch: function( ) {
        if ( this.clearOnClose && this.selectedPaxView.totalAdded( ) ) {
            // prompt you will lose your drawer
            this.$el.find( '.pax-modal-exit-confirm' ).modal( );

        } else {
            this.exitSearch( );
        }
    },
    removeAllExit: function( ) {
        this.searchCollectionView.deSelectAllCards( );
        this.$el.find( '.pax-modal-exit-confirm' ).modal( 'hide' );
        this.exitSearch( true );
    },
    //
    exitSearch: function( ) {
        var that = this;

        this.setStateLoading( 'cover' );

        this.$el.find( '.searchWrapper' ).fadeOut( G5.props.ANIMATION_DURATION, function( ) {
                that.trigger( 'closed', this );
            // Animation complete
          } );
        // close out of search
        G5._globalEvents.off( 'paxSearch:exitSearch', this.exitSearch, this );
        //$( '.pageWrapContainer' ).show( );
        //this.trigger( 'closed', this );
    },
    // destroy
    destroy: function( removeAll ) {
        // tk
        console.log( 'destroy' );
        if ( removeAll ) {
            this.selectedPaxCollection.reset( undefined, { silent: true } );
        }
        //
        if ( this.selectedPaxView ) { this.selectedPaxView.destroy( this.selectedPaxCollection ); }
        this.searchCollectionView.destroy( );
        // set the body to scroll
        $( 'body' ).removeClass( 'no-scroll' );
        $( 'body' ).removeClass( 'search-overlay-open' );
        $( 'html' ).removeClass( 'no-scroll' );

        $( '#paxNameInput' ).val( '' );
        this.$el.html( '' );
        //this.$el.find( '.selectedPaxView' ).remove( );
        this.selectedPaxView = null;
    }

} );
