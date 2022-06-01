/*exported CareerMomentsPageView*/
/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*global
$,
_,
G5,
PageView,
TemplateManager,
PaginationView,
CareerMomentsModel,
CareerMomentsPageView:true
*/
CareerMomentsPageView = PageView.extend( {

    //override super-class initialize function
    initialize: function ( opts ) {
        'use strict';

        //set the appname (getTpl() method uses this)
        this.appName = 'careerMoments';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        //global variables and params
        this.defaultFilter = null;
        this.$tabItems = opts.cmdd;
        this.$tabs = this.$el.find( '.careerMomentsTabs' );
        this.currentCmType = G5.props.urlParams.cmType ? G5.props.urlParams.cmType : 'newHire';

        // Auto complete related variables
        this.autocompDelay = null;
        this.autocompMinChars = null;
        this.autocompUrl = this.$el.find( '#careerMomentsNameInput' ).data( 'autocompUrl' ) ||
            G5.props.URL_JSON_CAREERMOMENTS_SEARCH_AUTOCOMPLETE;
        this.searchUrl = this.$el.find( '#careerMomentsNameInput' ).data( 'searchUrl' ) ||
            G5.props.URL_JSON_CAREERMOMENTS_SEARCH_RESULTS;
        //for autocomplete timing
        this.autocompLastKeyupTime = 0;

        //our model
        this.model = new CareerMomentsModel( {
            autocompUrl: this.autocompUrl,
            searchUrl: this.searchUrl
        } );

        this.model.on( 'renderTableContent', function( cont ) {
            this.renderTableContent( cont );        
            this.initTable();
         }, this );

        this.on( 'tabsRendered', function( tar ) {            
            this.addFilterToken( tar );
            this.model.loadData(this.defaultFilter, null, null, null, null, this.currentCmType);
        }, this);

        this.attachAutoCompleteWrap();

        if ( this.currentCmType ) {
            this.updateCmDropdown();
            this.renderTabs();
        }

        this.model.on( 'autocompleted', this.addCompletionsToDom, this );

        this.model.on( 'participantsChanged', function( selectedName, setId, data ) {
            this.renderParticipants(  selectedName, setId, data );
        }, this );

        //put the view into a loading state
        this.setStateLoading();

        $( '.careerMoments' ).on( 'click', '.dropdown-menu', function ( e ) {
          if( $( e.target ).closest( 'a' ).data( 'name-id' ) === 'department' || $( e.target ).closest( 'a' ).data( 'name-id' ) === 'country' ) {
              e.stopPropagation();
          }
        } );

        $( '.careerMoments' ).on( 'click', '.dropdown-toggle', function( e ) {
            if( $( this ).parents( '.careerMoments' ).height() < 650 ) {
                $( '.careerMomentsTabs' ).addClass( 'fix-height' );
            } else {
                $( '.careerMomentsTabs' ).removeClass( 'fix-height' );
            }
        } );
    },

    events: {
        'click .career-moments-tab': 'doTabClick',
        'click .tokenName': 'doResetDrawer',
        'click .removeSearchToken': 'doRemoveSearchToken',
        'click .careerMomentsTabs li ul .subMenu': 'doCountryDeptFilterSelect',
        'click .cmItem a': 'doCMItemSelect',
        //'click .sortable a': 'tableClickHandler',
        //'click .paginationControls a': 'paginationClickHandler',
        'change #cmPastPresentSelect': 'changeView',
        'click .profile-popover': 'attachParticipantPopover',

        //search input autocomp
        'focus #careerMomentsNameInput': 'doSearchInputFocus',
        'blur #careerMomentsNameInput': 'doSearchInputBlur',
        'keyup #careerMomentsNameInput': 'doSearchInputKeyup',
        'keypress #careerMomentsNameInput': 'doSearchInputKeypress',
        'keydown #careerMomentsNameInput': 'doSearchInputKeydown',
        'mouseenter .cmSearchDropdownMenu li': 'doAutocompMenuMouseenter',
        'click .cmSearchDropdownMenu a': 'doAutocompSelect',
        'click .searchBtn': 'submitSearch'
    },
    
    attachAutoCompleteWrap: function() {
        var $ph = this.$el.find( '#careerMomentsNameInput' ),
            that = this;

        $ph.placeholder();

        this.autocompDelay = this.$el.find( '#careerMomentsNameInput' ).data( 'autocompDelay' );
        this.autocompMinChars = this.$el.find( '#careerMomentsNameInput' ).data( 'autocompMinChars' );

        // take the view out of the loading state
        this.setStateLoaded();

    },

    initTable: function() {
        if( this.dispTableView ) { return; }

        this.dispTableView = new DisplayTableAjaxView( {
            el: $( this.$el.find( '.careerMomentsItems' ) ),
            delayLoad: true // don't load data immediately
        } );

        // attach pax popovers on load of table html
        this.dispTableView.on( 'htmlLoaded', function() {
            this.dispTableView.$el.find( '.paxPopover' ).participantPopover( { containerEl: this.$el } );
        }, this );

    },

    renderTableContent: function( cont ) {
        var that = this,
            $celCont = this.$el.find( '.careerMomentsItemsCont' ),
            //find the recognitions content DOM element
            $container = $( this.$el.find( '.careerMomentsItems' ) );            

        var $newContents = cont;        
        G5.util.formatDisplayTable( $newContents, {
            doPagination: true
        } );
        $container.empty();
        $container.html( $newContents );
        this.setStateLoaded();
    },

    renderTabs: function() {
        var that = this;
        //TABS - each 'celebrateSet' gets a tab
        this.$tabs.empty();
        this.$el.find( '.filterTokens' ).empty();

        _.each( that.$tabItems, function( cmSet ) {
            var numNew = 0;
            if ( cmSet.isDefault ) {
                that.defaultFilter = cmSet.nameId;
            }
            //build the li>a element
            var $a = $( '<a />' )
                        .attr( {
                            'title': cmSet.name,
                            'href': '#',
                            'class': 'career-moments-tab career-moments-tab-' + cmSet.nameId,
                            'data-name-id': cmSet.nameId,
                            'data-total-count': cmSet.totalCount
                        } )
                        .html( '<span>' + cmSet.name + '</span><!--sup>' + numNew + '</sup-->' ),
                $li = $( '<li />' ).html( $a );
                if( $li.find( 'a' ).data( 'name-id' ) === 'country' ) {
                    $li.prepend( '<hr/>' );
                }
                if( cmSet.nameId === 'department' ) {

                    var $listHtml = '<ul style="display:none;">';
                    // console.log( that.model.get( 'list' ) );
                    _.each( cmSet.list, function( item ) {

                        var $li = '<li class="subMenu" data-list-val="' + item.code + '" >' + item.name + '</li>';
                        $listHtml += $li;
                    } );
                    $listHtml += '</ul>';
                        $li.find( 'a' ).after( $listHtml );
                        $li.find( 'a' ).prepend( '<i class="icon-plus-circle"></i>' );
                } else if( cmSet.nameId === 'country' ) {

                    var $listHtml = '<ul style="display:none;">';
                    // console.log( that.model.get( 'list' ) );
                    _.each( cmSet.list, function( item ) {

                        var $li = '<li class="subMenu" data-list-val="' + item.countryCode + '" >' + item.countryName + '</li>';
                        $listHtml += $li;
                    } );
                    $listHtml += '</ul>';
                        $li.find( 'a' ).after( $listHtml );
                        $li.find( 'a' ).prepend( '<i class="icon-plus-circle"></i>' );
                }
            //append to tabs container
            that.$tabs.append( $li );
        });        
        console.log('careerMomentsTabs', this.$el.find('.careerMomentsTabs') );
        that.trigger ( 'tabsRendered', this.$el.find( '.career-moments-tab[data-name-id=' + this.defaultFilter + ']' ) );
    },

    renderParticipants: function(  selectedName, setId, data ) {
        var that = this,
        $celContainer = this.$el.find( '.purlCelebrateItems' );

        this.setSearchToken( selectedName );

        $celContainer.attr( 'data-selected-name', selectedName );
        G5.util.hideSpin( that.$el );
        that.renderTableContent( data );
    },

    setStateLoading: function( mode ) {
        var spinProps = {};

        if( this.$el.closest( '.module' ).length ) {
            spinProps.spinopts = {
                color: '#fff'
            };
        }
        if( mode ) {
            spinProps.classes = mode;
        }

        G5.util.showSpin( this.$el, spinProps );
    },

    setStateLoaded: function() {
        G5.util.hideSpin( this.$el );
    },

    doCMItemSelect: function(e) {
        var that = this,
        $tar = $( e.currentTarget ),
            cmId = $tar.data( 'cmId' ),
            cmValue = $tar.text();

        e.preventDefault();
        that.updateCmDropdown(cmId, cmValue);        
        that.changeView();
    },

    updateCmDropdown: function(id, val) {        
        var that = this;
        if ( !id ) {
            that.$el.find('.cmItem a').each(function( index ) {
              if ( $( this ).data('cmId') == that.currentCmType ) {
                that.$el.find('.cmBtnContent .cmName').text( $( this ).text() );
                that.$el.find('.selectedCMType').val( that.currentCmType );
              } 
            });
            
        }
        else {
            that.$el.find('.cmBtnContent .cmName').text( val );
            that.$el.find('.selectedCMType').val( id );
            this.currentCmType = id;  
        }              
    },

    changeView: function( e ) {
        var that = this,
            id = that.defaultFilter || 'global',
            $searchName = this.$el.find( '.selected-filter .filter-bold' ).text(),
            val = { 'listValue': this.$el.find( '.careerMomentsTabs li.active' ).find( '.selected' ).data( 'list-val' ) },
            cmType = this.currentCmType;
            //$viewSelected = $tar.val();

        // reset the page num if we are changing filters
        this.model.set( 'currentPage', null );
        this.model.loadData( id, null, this.$el.find( '.cmSelectRadio:checked' ).val(), $searchName, val.listValue, cmType );
    },

    doTabClick: function( e ) {
        var $tar = $( e.currentTarget ),
            id = $tar.data( 'nameId' ),
            $viewSelected = this.$el.find( '.cmSelectRadio:checked' ).val(),
            $searchName = this.$el.find( '.selected-filter .filter-bold' ).text(),
            tabClicked = true,
            that = this;

            that.defaultFilter = id;

            if( id === 'country' || id === 'department' ) {
                // $tar.parents( '.dropdown' ).addClass( 'open' );
                this.displayFilterDropdown( id, $tar );
            } else {

                this.setStateLoading( 'tabChange' );
                this.addFilterToken( $tar );
                this.model.loadData( id, tabClicked, $viewSelected, $searchName, null, this.currentCmType );
                if( this.purlCelebratePagination ) {
                    that.purlCelebratePagination = null;
                }
            }
        e.preventDefault();
    },

    doCountryDeptFilterSelect: function( e ) {
        var $tar = $( e.currentTarget ),
            $searchName = this.$el.find( '.selected-filter .filter-bold' ).text(),
            id = $tar.parent( 'ul' ).siblings( 'a' ).data( 'nameId' ),
            $viewSelected = this.$el.find( '.cmSelectRadio:checked' ).val(),
            val = { 'listValue': $tar.data( 'list-val' ) };
            $tar.parent().closest( 'li' ).addClass('active');
            $tar.addClass( 'selected' );
            this.setStateLoading( 'tabChange' );

            this.addFilterToken( $tar );

            this.model.loadData( id, true, $viewSelected, $searchName, val.listValue, this.currentCmType );

            e.preventDefault();
    },

    displayFilterDropdown: function( id, $tar ) {
        $tar.siblings( 'ul' ).slideToggle(  );
        if( $tar.find( 'i' ).hasClass( 'icon-plus-circle' ) ) {
            $tar.find( 'i' ).removeClass( 'icon-plus-circle' );
            $tar.find( 'i' ).addClass( 'icon-minus-circle' );
        } else {
            $tar.find( 'i' ).removeClass( 'icon-minus-circle' );
            $tar.find( 'i' ).addClass( 'icon-plus-circle' );
        }

    },

    addFilterToken: function( target ) {
        var $filterName, $span, $i;
        if( target.parent( 'ul' ).siblings( 'a' ).data( 'name-id' ) === 'country' || target.parent( 'ul' ).siblings( 'a' ).data( 'name-id' ) === 'department' ) {
            $filterName = target.parent( 'ul' ).siblings( 'a' ).text() + ': ' + target.text();
        } else {
            $filterName = target.text();
        }

        $span = $( '<span />' )
                    .attr( {
                        'class': 'tokenName btn btn-primary btn-inverse dropdown-toggle',
                        'data-toggle': 'dropdown'
                    } )
                    .html( $filterName );
        $i = $( '<i />' )
                .attr( {
                    'class': 'icon-arrow-1-down arrowToken'
                } );

        $span.append( $i );

        this.$el.find( '.tokenName' ).remove();
        this.$el.find( '.careerMomentsFilterWrap .dropdown' ).prepend( $span );

        this.$el.find( '.addNewFilter' ).hide();
    },

    doResetDrawer: function( e ) {
        var $dropdownFilters = this.$el.find( '.careerMomentsTabs li' );

        $dropdownFilters.find( 'ul' ).hide();

    },

    setSearchToken: function( name ) {
        var $searchFilterWrap = this.$el.find( '.selected-filter' ),
            $filter = $searchFilterWrap.find( '.filter-bold' );

        $searchFilterWrap.show();
        $filter.text( name );
    },

    doRemoveSearchToken: function() {
        var $searchTokenWrap = this.$el.find( '.selected-filter' ),
            $token = $searchTokenWrap.find( '.filter-bold' ),
            filterId = this.$el.find( '.careerMomentsTabs li.active a' ).data( 'nameId' ) || this.defaultFilter,
            $viewSelected = this.$el.find( '.cmSelectRadio:checked' ).val(),
            listValue = this.$el.find( '.careerMomentsTabs li.active' ).find( '.selected' ).data( 'list-val' );

        $searchTokenWrap.hide();
        $token.text( '' );

        this.model.loadData( filterId, null, $viewSelected, null, listValue, this.currentCmType );
    },

    /*tableClickHandler: function( e ) {
        e.preventDefault();

        var $tar = $( e.target ).closest( 'a' ),
            id = this.$el.find( '.careerMomentsTabs li.active a' ).data( 'nameId' ),
            $viewSelected = this.$el.find( '.cmSelectRadio:checked' ).val(),
            $searchName = this.$el.find( '.selected-filter .filter-bold' ).text();

        // for table headers
        if( $tar.closest( '.sortable' ).length ) {
            var $newTar = $tar.closest( '.sortable' ),
                sortData = $newTar.data(),
                addlData = $.query.load( $newTar.find( 'a' ).attr( 'href' ) ).keys,
                cmType = this.currentCmType;
            G5.util.showSpin( this.$el, {
                cover: true
            } );

            //if selected name exists it came from search, otherwise set to false
            if( !$searchName ) {
                $searchName = false;
            }

            this.model.update( {
                force: true,
                data: $.extend(
                    true,       // deep merge = true
                    {},         // start with an empty object
                    addlData,   // merge in addlData
                    {           // then overwrite with these values
                        pageNumber: 1,
                        sortedOn: sortData.sortById,
                        sortedBy: sortData.sortedOn === true && sortData.sortedBy == 'asc' ? 'desc' : 'asc'
                    }
                ),
                type: 'tabular',
                nameId: id,
                cmType: cmType,
                selectedName: $searchName,
                selectedView: $viewSelected
            } );
        }
    },

    paginationClickHandler: function( e ) {
        e.preventDefault();
        var that = this,
        $tar = $(e.currentTarget), 
        page = $tar.text(),       
        $searchName = this.$el.find( '.selected-filter .filter-bold' ).text(),
        $viewSelected = this.$el.find( '.cmSelectRadio:checked' ).val();

        G5.util.showSpin( this.$el, {
            cover: true
        } );

        //if selected name exists it came from search, otherwise set to false
        if( !$searchName ) {
            $searchName = false;
        }

        this.model.update( {
            force: true,
            data: {
                pageNumber: page
            },
            type: 'getPage',
            nameId: that.defaultFilter,
            
            selectedName: $searchName,
            selectedView: $viewSelected
        } );
    },*/

    //Auto complete
    doSearchInputFocus: function() {
        var $searchInputDd = this.$el.find( '.searchWrap' );

        //open the autocomplete (dropdown menu)
        $searchInputDd.addClass( 'open' );
        this.doAutocomplete();//attempt an autocomplete
    },

    doSearchInputBlur: function() {
        var that = this,
            $searchInputMenu = this.$el.find( '.cmSearchDropdownMenu' ),
            $searchInputDd = this.$el.find( '.searchWrap' );

        //hide the autocomplete (dropdown menu)
        if( $searchInputMenu.find( '.active' ).length === 0 ) {
            $searchInputDd.removeClass( 'open' );
            this.clearAutocompMenu();//clear results
        }else{
            setTimeout( function() {
                $searchInputDd.removeClass( 'open' );
                that.clearAutocompMenu();//clear results
            }, 300 );
        }
    },

    doSearchInputKeypress: function( e ) {
        this.doSearchInputMove( e );
    },

    doSearchInputKeydown: function( e ) {
        if( $.browser.webkit || $.browser.msie ) {
            this.supressKeyPressRepeat = !~$.inArray( e.keyCode, [ 40, 38, 9, 13, 27 ] );
            this.doSearchInputMove( e );
        }
    },

    doSearchInputKeyup: function( e ) {
        var $searchInputMenu = this.$el.find( '.cmSearchDropdownMenu' ),
            $searchInputDd = this.$el.find( '.searchWrap' ),
            $searchInput = this.$el.find( '#careerMomentsNameInput' );

        switch( e.keyCode ) {
            case 40: //down arrow
            case 38: //up arrow
                break;

            case 9: //tab
            case 13: //enter
                // check to see if there are results in the search input drop-down
                // if so, the enter key selects the result
                if( $searchInputDd.find( '.active' ).length ) {
                    $searchInputMenu.find( '.active a' ).click();
                    $searchInput.blur();
                }
                // if not, the enter key doesn't do anything special (and won't stop a search from kicking off)
                break;

            case 27: //escape
                $searchInputDd.removeClass( 'open' );
                this.clearAutocompMenu();//clear results
                $searchInput.blur();
                break;

            default:
                this.doAutocomplete();
        }

        e.stopPropagation();
        e.preventDefault();
    },

    doSearchInputMove: function( e ) {
        switch( e.keyCode ) {
            case 9: // tab
            case 13: // enter
            case 27: // escape
                e.preventDefault();
                break;

            case 38: // up arrow
                e.preventDefault();
                this.doSearchInputPrev();
                break;

            case 40: // down arrow
                e.preventDefault();
                this.doSearchInputNext();
                break;
        }

        e.stopPropagation();
    },

    doSearchInputPrev: function() {
        var $searchInputMenu = this.$el.find( '.cmSearchDropdownMenu' ),
            active = $searchInputMenu.find( '.active' ).removeClass( 'active' ),
            prev = active.prev();

        if( !prev.length ) {
            prev = $searchInputMenu.find( 'li' ).last();
        }

        prev.addClass( 'active' );
    },

    doSearchInputNext: function() {
        var $searchInputMenu = this.$el.find( '.cmSearchDropdownMenu' ),
            active = $searchInputMenu.find( '.active' ).removeClass( 'active' ),
            next = active.next();

        if( !next.length ) {
            next = $( $searchInputMenu.find( 'li' )[ 0 ] );
        }

        next.addClass( 'active' );
    },

    //gateway to autocomplete, check num chars is enough
    doAutocomplete: function() {
        var $searchInput = this.$el.find( '#careerMomentsNameInput' ),
            query = $searchInput.val(),
            that = this;


        if( query.length >= that.autocompMinChars ) {
            //set the time
            that.autocompLastKeyupTime = ( new Date() ).getTime();
            //set a timeout on delay, and see if keyboard has been idle for long enough
            setTimeout( function() {that.tryAutocomplete();}, that.autocompDelay );
            if( !G5.util.formValidate( this.$el.find( '.validateme' ) ) ) {
                return false;
            }
            this.recipientSearchSpinner();
        }else{
            //clear the menu results
            this.clearAutocompMenu();
        }
    },

    //check the time since last keypress, and forward query if apropo
    tryAutocomplete: function() {
        var $searchInput = this.$el.find( '#careerMomentsNameInput' ),
            $searchInputMenu = this.$el.find( '.cmSearchDropdownMenu' ),
            now = ( new Date() ).getTime(),
            delay = now - this.autocompLastKeyupTime,
            query = $searchInput.val();

        //check to make sure no keys have been pressed during the delay (give 10ms margin)
        if( delay >= this.autocompDelay - 10 ) {
            // check to make sure the query string isn't too long
            if( query.length >= this.autocompMinChars ) {
                $searchInputMenu.empty();
                this.model.queryAutocomplete( query );
            }
            // not enough chars, set default text
            else {
                this.clearAutocompMenu();
            }
        }
    },

    // when an autocomplete selection comes back from server
    addCompletionsToDom: function( comps, serverMsg ) {
        var that = this,
            $searchInputMenu = this.$el.find( '.cmSearchDropdownMenu' ),
            $searchBtn = this.$el.find( '.searchBtn' );

        $searchInputMenu.empty();
        this.recipientSearchSpinner( 'stop' );

        if( !serverMsg && comps.length > 0 ) {
            //populate menu with results
            _.each( comps, function( comp ) {
                $searchInputMenu.append(
                    that.make( 'li', {},
                        that.make( 'a', {
                            href: '#',
                            'data-ac-id': comp.id,
                            'data-ac-name': comp.name
                        }, comp.name )
                    )
                );
            } );
            $searchBtn.removeAttr( 'disabled' );

            //highlight first item
            $( $searchInputMenu.find( 'li' )[ 0 ] ).addClass( 'active' );
        } else if( !serverMsg && comps.length === 0 ) {
            //add display no results message
            this.setAutocompMenuMsg( $searchInputMenu.data( 'msgNoResults' ) );
            $searchBtn.attr( 'disabled', 'disabled' );
        } else {//server message
            this.setAutocompMenuMsg( serverMsg );
        }
    },

    doAutocompSelect: function( e ) {
        var $tar = $( e.target ),
            $searchInputDd = this.$el.find( '.searchWrap' ),
            $searchInput = this.$el.find( '#careerMomentsNameInput' ),
            $searchContainer = this.$el.find( '.searchList' ),
            $searchResults = $searchContainer.find( 'tbody' ),
            //$viewSelected = this.$el.find( '.cmSelectRadio' ).val(),
            $selectedFilter = this.defaultFilter || 'global',
            selectedName = $tar.data( 'acName' ),
            listVal = this.$el.find( '.careerMomentsTabs li.active' ).find( '.selected' ).data( 'list-val' ),
            cmType = this.$el.find('.selectedCMType').val() || this.currentCmType;


        e.preventDefault();

        //hide menu
        $searchInputDd.removeClass( 'open' );

        //clear entered text
        $searchInput.val( '' );

        this.clearAutocompMenu();

        //empty results and slide container up
        $searchResults.empty();
        $searchContainer.slideUp( G5.props.ANIMATION_DURATION );

        G5.util.showSpin( $searchContainer );
        //clear currentPage for call as we have a new search
        this.model.currentPage = null;

        this.model.loadParticipants( $selectedFilter, selectedName, this.$el.find( '.cmSelectRadio:checked' ).val(), listVal, cmType );
    },

    doAutocompMenuMouseenter: function( e ) {
        var $searchInputMenu = this.$el.find( '.cmSearchDropdownMenu' );

        $searchInputMenu.find( '.active' ).removeClass( 'active' );
        $( e.currentTarget ).not( '.dropdown-menu-info' ).addClass( 'active' );
    },

    clearAutocompMenu: function() {
        var $searchInputMenu = this.$el.find( '.cmSearchDropdownMenu' ),
            msg = $searchInputMenu.data( 'msgInstruction' );

        this.setAutocompMenuMsg( msg );
    },

    setAutocompMenuMsg: function( msg ) {
        var $i = $( this.make( 'li', { 'class': 'dropdown-menu-info' }, msg ) ),
            $searchInputMenu = this.$el.find( '.cmSearchDropdownMenu' );
        //empty menu, and add a msg
        setTimeout( function() { // IE8 + JQ1.8.3 -- push target.empty() to end of stack
            $searchInputMenu.empty();
            $searchInputMenu.append( $i );
        }, 0 );
    },

    recipientSearchSpinner: function( opt ) {

        var self           = this,
            $spinnerWrap   = this.$el.find( '.searchWrap' ),
            $searchBtn     = $spinnerWrap.find( '.searchBtn' ),
            $searchSpinner = this.$searchSpinner || ( function() {

                                var opts = {
                                                color: $searchBtn.find( 'i' ).css( 'color' )
                                           };

                                self.$searchSpinner = $spinnerWrap.find( '.spinnerWrap' )
                                                        .show()
                                                        .spin( opts );

                                return self.$searchSpinner;
                             } )();

        if ( opt === 'stop' ) {
            $searchSpinner.hide();
            $searchBtn.show();
        } else {
            this.$el.find( '.qtip' ).qtip( 'hide' );
            $searchSpinner.show();
            $searchBtn.hide();
        }
    },

    submitSearch: function( e ) {
        var $validate = this.$el.find( '.validateme' );

        e.preventDefault();

        if( !G5.util.formValidate( $validate ) ) {
            return false;
        }

        this.doAutocompSelect( e );

    },

    attachParticipantPopover: function( e ) {
        var $tar = $( e.target );

        //attach participant popovers
        if( !$tar.data( 'participantPopover' ) ) {
            $tar.participantPopover( { containerEl: this.$el } ).qtip( 'show' );
        }
        e.preventDefault();
    }
} );
