/*exported PurlCelebratePageView*/
/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*global
$,
_,
G5,
PageView,
TemplateManager,
PaginationView,
PurlCelebrateModel,
PurlCelebratePageView:true
*/
PurlCelebratePageView = PageView.extend( {

    //override super-class initialize function
    initialize: function () {
        'use strict';


        //set the appname (getTpl() method uses this)
        this.appName = 'purlCelebrate';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        //templates
        this.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'purlContribute/tpl/';
        this.tplName = 'purlCelebrateSet';

        //global variables and params
        this.$tabs = this.$el.find( '.purlCelebrateTabs' );
        this.autocompDelay = null;
        this.autocompMinChars = null;
        this.autocompUrl = this.$el.find( '#purlCelebrateNameInput' ).data( 'autocompUrl' ) ||
            G5.props.URL_JSON_PURL_CELEBRATE_SEARCH_AUTOCOMPLETE;
        this.searchUrl = this.$el.find( '#purlCelebrateNameInput' ).data( 'searchUrl' ) ||
            G5.props.URL_JSON_PURL_CELEBRATE_SEARCH_RESULTS;

        //for autocomplete timing
        this.autocompLastKeyupTime = 0;

        //our model
        this.model = new PurlCelebrateModel( {
            autocompUrl: this.autocompUrl,
            searchUrl: this.searchUrl
        } );

        this.model.loadData();

        this.model.on( 'dataLoaded', function( setId ) {
            this.render( setId );
        }, this );

        this.model.on( 'autocompleted', this.addCompletionsToDom, this );

        this.model.on( 'participantsChanged', function( msg, data, selectedName ) {
            this.renderParticipants( msg, data, selectedName );
        }, this );

        //put the view into a loading state
        this.setStateLoading();
        $( '.purlCelebrate' ).on( 'click', '.dropdown-menu', function ( e ) {

          if( $( e.target ).closest( 'a' ).data( 'name-id' ) === 'department' || $( e.target ).closest( 'a' ).data( 'name-id' ) === 'country' ) {
              e.stopPropagation();

          }
        } );
        $( '.purlCelebrate' ).on( 'click', '.dropdown-toggle', function( e ) {

            if( $( this ).parents( '.purlCelebrate' ).height() < 650 ) {
                $( '.purlCelebrateTabs' ).addClass( 'fix-height' );
            } else {
                $( '.purlCelebrateTabs' ).removeClass( 'fix-height' );
            }
        } );

    },

    events: {
        'click .purl-celebrate-tab': 'doTabClick',
        'click .tokenName': 'doResetDrawer',
        'click .removeSearchToken': 'doRemoveSearchToken',
        'click .purlCelebrateTabs li ul .subMenu': 'doCountryDeptFilterSelect',

        'click .sortable a': 'tableClickHandler',
        'change #purlPastPresentSelect': 'changeView',
        'click .profile-popover': 'attachParticipantPopover',

        //search input autocomp
        'focus #purlCelebrateNameInput': 'doSearchInputFocus',
        'blur #purlCelebrateNameInput': 'doSearchInputBlur',
        'keyup #purlCelebrateNameInput': 'doSearchInputKeyup',
        'keypress #purlCelebrateNameInput': 'doSearchInputKeypress',
        'keydown #purlCelebrateNameInput': 'doSearchInputKeydown',
        'mouseenter .purlSearchDropdownMenu li': 'doAutocompMenuMouseenter',
        'click .purlSearchDropdownMenu a': 'doAutocompSelect',
        'click .searchBtn': 'submitSearch',
        'click .sa-action': 'invokesaContribute'
    },
    invokesaContribute: function( event ) {
        G5.util.saContribute( event );
    },

    render: function( setId ) {
        var $ph = this.$el.find( '#purlCelebrateNameInput' ),
            that = this,
            defSet;

        if( !setId ) {
            this.renderTabs();

            defSet = this.model.getDefaultSet();

            if( defSet ) {
            	// simulate click on def tab
            	/*
                if( defSet.nameId === 'global' ) {
                    this.renderTabContent( 'global' );
                } else {
                    this.$el.find( '.purl-celebrate-tab[data-name-id=' + defSet.nameId + ']' ).click();
                }*/
                this.$el.find( '.purl-celebrate-tab[data-name-id=' + defSet.nameId + ']' ).click();
            } else {
                this.renderTabContent( 'global' );
            }

        } else if( setId ) {
            this.renderTabContent( setId );
        }

        TemplateManager.get( 'purlCelebratePage', function( tpl, vars, subTpls ) {
            that.paginationTpl = subTpls.paginationTpl;
        }, this.tplUrl );

        $ph.placeholder();

        this.autocompDelay = this.$el.find( '#purlCelebrateNameInput' ).data( 'autocompDelay' );
        this.autocompMinChars = this.$el.find( '#purlCelebrateNameInput' ).data( 'autocompMinChars' );

        // take the view out of the loading state
        this.setStateLoaded();

    },

    renderTabs: function() {
        var that = this;
        //TABS - each 'celebrateSet' gets a tab
        this.$tabs.empty();
        this.$el.find( '.filterTokens' ).empty();

        _.each( this.model.get( 'celebrationSets' ), function( celSet ) {
            var numNew = 0;

            //build the li>a element
            var $a = $( '<a />' )
                        .attr( {
                            'title': celSet.name,
                            'href': '#',
                            'class': 'purl-celebrate-tab purl-celebrate-tab-' + celSet.nameId,
                            'data-name-id': celSet.nameId,
                            'data-total-count': celSet.totalCount
                        } )
                        .html( '<span>' + celSet.name + '</span><!--sup>' + numNew + '</sup-->' ),
                $li = $( '<li />' ).html( $a );
                if( $li.find( 'a' ).data( 'name-id' ) === 'country' ) {
                    $li.prepend( '<hr/>' );
                }
                if( celSet.nameId === 'department' ) {

                    var $listHtml = '<ul style="display:none;">';
                    // console.log( that.model.get( 'list' ) );
                    _.each( celSet.list, function( item ) {

                        var $li = '<li class="subMenu" data-list-val="' + item.code + '" >' + item.name + '</li>';
                        $listHtml += $li;
                    } );
                    $listHtml += '</ul>';
                        $li.find( 'a' ).after( $listHtml );
                        $li.find( 'a' ).prepend( '<i class="icon-plus-circle"></i>' );
                } else if( celSet.nameId === 'country' ) {

                    var $listHtml = '<ul style="display:none;">';
                    // console.log( that.model.get( 'list' ) );
                    _.each( celSet.list, function( item ) {

                        var $li = '<li class="subMenu" data-list-val="' + item.countryCode + '" >' + item.countryName + '</li>';
                        $listHtml += $li;
                    } );
                    $listHtml += '</ul>';
                        $li.find( 'a' ).after( $listHtml );
                        $li.find( 'a' ).prepend( '<i class="icon-plus-circle"></i>' );
                }
            //append to tabs container
            that.$tabs.append( $li );
        } );
    },

    renderTabContent: function( id ) {
        var that = this,
            $celCont = this.$el.find( '.purlCelebrateItemsCont' ),
            //find the recognitions content DOM element
            $celSets = $( this.$el.find( '.purlCelebrateItems' ) ),
            //get the tab element
            $tab = this.$tabs.find( '[data-name-id=' + id + ']' ),
            $desc = this.$el.find( '.purlCelebrateDesc' ),
            $setTitle = this.$el.find( '.celebrationTitle' ),
            activeView = this.$el.find( '.purlSelectRadio:checked' ).val();

        //deactivate, and activate new tab (style class)
        this.$tabs.find( 'li' ).removeClass( 'active' );
        $tab.closest( 'li' ).addClass( 'active' );

        //remove the empty set classname if it exists from the container
        $celSets.removeClass( 'emptySet' );

        $setTitle.hide();

        if( activeView === 'past' ) {
            $setTitle.filter( '.pastTitle' ).show();
        } else {
            $setTitle.filter( '.upcomingTitle' ).show();
        }

        TemplateManager.get( 'purlCelebrateSet', function( tpl, vars, subTpls ) {
            //empty container
            $celSets.empty();

            _.each( that.model.get( 'celebrationSets' ), function( celSet ) {
                if( celSet.nameId === id ) {
                    $desc.text( celSet.description );

                    if( celSet.celebrations.length === 0 ) {
                        $celSets
                            .addClass( 'emptySet' )
                            .append( that.make( 'h2', {}, $celCont.data( 'msgEmpty' ) ) )
                            .find( 'h2' ).prepend( '<i class="icon-team-1" />' );

                        that.renderPagination( celSet );
                    } else {

                        that.processTabularData( celSet );
                        $celSets.append( tpl( celSet ) );
                        $celSets.find( 'table' ).responsiveTable();
                        that.renderPagination( celSet );
                    }

                }
            } );
        }, this.tplUrl );
    },

    renderPagination: function( celSet ) {
        var that = this;

        // if our data is paginated, add a special pagination view
        if( celSet.total > celSet.itemsPerPage ) {
            // if no pagination view exists, create a new one
            if( !that.purlCelebratePagination ) {
                that.purlCelebratePagination = new PaginationView( {
                    el: that.$el.find( '.pagination' ),
                    pages: Math.ceil( celSet.total / celSet.itemsPerPage ),
                    current: celSet.currentPage,
                    ajax: true,
                    tpl: that.paginationTpl || false
                } );

                this.purlCelebratePagination.on( 'goToPage', function( page ) {
                    var nameId = that.$tabs.find( 'li.active a' ).data( 'nameId' );
                    that.paginationClickHandler( page, nameId );
                } );

                this.model.on( 'dataLoaded', function( setId ) {
                    if( !that.purlCelebratePagination ) {
                        return false;
                    }
                    //have to re-get the set data since dataLoaded happens before renderPagination
                    _.each( that.model.get( 'celebrationSets' ), function( set ) {
                        if( setId === set.nameId ) {
                            that.purlCelebratePagination.setProperties( {
                                rendered: false,
                                pages: Math.ceil( set.total / set.itemsPerPage ),
                                current: set.currentPage
                            } );
                        }
                    } );
                } );
            }
            // otherwise, just make sure the $el is attached correctly
            else {
                this.purlCelebratePagination.setElement( that.$el.find( '.pagination' ) );
            }
        } else {
            this.$el.find( '.pagination' ).empty();
        }
    },

    // renderSearchPagination: function( celSet ) {
    //     var that = this;

    //     // if our data is paginated, add a special pagination view
    //     if( celSet.total > celSet.itemsPerPage ) {
    //         // if no pagination view exists, create a new one
    //         if( !that.purlCelebrateSearchPagination ) {
    //             that.purlCelebrateSearchPagination = new PaginationView( {
    //                 el: that.$el.find( '.pagination' ),
    //                 pages: Math.ceil( celSet.total / celSet.itemsPerPage ),
    //                 current: celSet.currentPage,
    //                 ajax: true,
    //                 tpl: that.paginationTpl || false
    //             } );

    //             this.purlCelebrateSearchPagination.on( 'goToPage', function( page ) {
    //                 that.paginationClickHandler( page, celSet.nameId );
    //             } );

    //             this.model.on( 'participantsChanged', function( msg, celSet ) {
    //                 if( !that.purlCelebrateSearchPagination ) {
    //                     return false;
    //                 }
    //                 that.purlCelebrateSearchPagination.setProperties( {
    //                     rendered: false,
    //                     pages: Math.ceil( celSet.total / celSet.itemsPerPage ),
    //                     current: celSet.currentPage
    //                 } );
    //             } );
    //         }
    //         // otherwise, just make sure the $el is attached correctly
    //         else {
    //             this.purlCelebrateSearchPagination.setElement( that.$el.find( '.pagination' ) );
    //         }
    //     } else {
    //         this.$el.find( '.pagination' ).empty();
    //     }
    // },

    renderParticipants: function( selectedName, setId ) {
        var that = this,
            $celContainer = this.$el.find( '.purlCelebrateItems' );

        // $searchContainer.slideDown(G5.props.ANIMATION_DURATION);
        //
        this.setSearchToken( selectedName );

        $celContainer.attr( 'data-selected-name', selectedName );
        G5.util.hideSpin( that.$el );

        if( !setId ) {
            that.renderTabContent( 'global' );

        } else if( setId ) {
            that.renderTabContent( setId );
        }

        // TemplateManager.get( tplName, function( tpl ) {
        //     var isSearch = true;
        //     that.$el.find( '.searchList' ).empty();

        //     $celContainer.empty();

        //     that.$el.find( '.pagination' ).empty();

        //     // if( data.celebrations.length === 0 ) {
        //     //     $celContainer
        //     //         .addClass( 'emptySet' )
        //     //         .find( 'h2' ).remove()
        //     //         .end().append( that.make( 'h2', {}, $searchContainer.data( 'msgNoResults' ) ) )
        //     //         .find( 'h2' ).prepend( '<i class="icon-team-1" />' );

        //     //     return false;
        //     // }

        //     that.$el.find( '.emptySet h2' ).remove();

        //     $celContainer.append( tpl( data ) );
        //     that.renderPagination( data, isSearch );
        //     //that.renderSearchPagination( data, isSearch );
        //     //that.processTabularData( data );

        //     $celContainer.attr( 'data-selected-name', selectedName );
        //     G5.util.hideSpin( that.$el );

        // }, this.tplUrl );
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

    changeView: function( e ) {
        var //$tar = $( e.target ).closest( '.purlSelectRadio' ),
            id = this.$el.find( '.purlCelebrateTabs li.active a' ).data( 'nameId' ) || 'global',
            $searchName = this.$el.find( '.selected-filter .filter-bold' ).text(),
            val = { 'listValue': this.$el.find( '.purlCelebrateTabs li.active' ).find( '.selected' ).data( 'list-val' ) };
            //$viewSelected = $tar.val();

        // reset the page num if we are changing filters
        this.model.set( 'currentPage', null );
        this.model.loadData( id, null, this.$el.find( '.purlSelectRadio:checked' ).val(), $searchName, val.listValue );
    },

    doTabClick: function( e ) {
        var $tar = $( e.currentTarget ),
            id = $tar.data( 'nameId' ),
            $viewSelected = this.$el.find( '.purlSelectRadio:checked' ).val(),
            $searchName = this.$el.find( '.selected-filter .filter-bold' ).text(),
            tabClicked = true,
            that = this;

            if( id === 'country' || id === 'department' ) {
                // $tar.parents( '.dropdown' ).addClass( 'open' );
                this.displayFilterDropdown( id, $tar );
            } else {

                this.setStateLoading( 'tabChange' );
                this.addFilterToken( $tar );
                this.model.loadData( id, tabClicked, $viewSelected, $searchName, null );
                if( this.purlCelebratePagination ) {
                    that.purlCelebratePagination = null;
                }
            }



        e.preventDefault();
    },
    doCountryDeptFilterSelect: function( e ) {
        var $tar = $( e.currentTarget ),
            id = $tar.parent( 'ul' ).siblings( 'a' ).data( 'nameId' ),
            $viewSelected = this.$el.find( '.purlSelectRadio:checked' ).val(),
            val = { 'listValue': $tar.data( 'list-val' ) };

            $tar.addClass( 'selected' );
            this.setStateLoading( 'tabChange' );

            this.addFilterToken( $tar );

            this.model.loadData( id, true, $viewSelected, null, val.listValue );

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
        this.$el.find( '.purlCelebrateFilterWrap .dropdown' ).prepend( $span );

        this.$el.find( '.addNewFilter' ).hide();

    },

    doResetDrawer: function( e ) {
        var $dropdownFilters = this.$el.find( '.purlCelebrateTabs li' );

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
            filterId = this.$el.find( '.purlCelebrateTabs li.active a' ).data( 'nameId' ),
            $viewSelected = this.$el.find( '.purlSelectRadio:checked' ).val(),
            listValue = this.$el.find( '.purlCelebrateTabs li.active' ).find( '.selected' ).data( 'list-val' );

        $searchTokenWrap.hide();
        $token.text( '' );

        this.model.loadData( filterId, null, $viewSelected, null, listValue );
    },

    processTabularData: function( set ) {
            _.each( set.tableColumns, function( col, place ) {

                if( set.sortedOn == set.tableColumns[ place ].id ) {
                    col.sortedOn = true;
                    col.sortedBy = set.sortedBy || 'desc';
                }

                if( col.sortable ) {
                    // mark if this column is the one on which the table is sorted
                    col.sortedOn = set.sortedOn == col.id ? true : false;
                    // default to ascending sort, but mark with the actual sort state
                    col.sortedBy = set.sortedOn == col.id ? set.sortedBy : 'desc';
                    // Handlebars helper because #if can't compare values
                     col.sortedByDesc = col.sortedBy == 'desc' ? true : false;
                }
            } );
    },

    tableClickHandler: function( e ) {
        e.preventDefault();

        var $tar = $( e.target ).closest( 'a' ),
            id = this.$el.find( '.purlCelebrateTabs li.active a' ).data( 'nameId' ),
            $viewSelected = this.$el.find( '.purlSelectRadio:checked' ).val(),
            $searchName = this.$el.find( '.selected-filter .filter-bold' ).text();

        // for table headers
        if( $tar.closest( '.sortable' ).length ) {
            var $newTar = $tar.closest( '.sortable' ),
                sortData = $newTar.data(),
                addlData = $.query.load( $newTar.find( 'a' ).attr( 'href' ) ).keys;

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
                selectedName: $searchName,
                selectedView: $viewSelected
            } );
        }
    },

    paginationClickHandler: function( page, setId ) {
        var $searchName = this.$el.find( '.selected-filter .filter-bold' ).text(),
            $viewSelected = this.$el.find( '.purlSelectRadio:checked' ).val();

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
            nameId: setId,
            selectedName: $searchName,
            selectedView: $viewSelected
        } );
    },

    //Auto complete
    doSearchInputFocus: function() {
        var $searchInputDd = this.$el.find( '.searchWrap' );

        //open the autocomplete (dropdown menu)
        $searchInputDd.addClass( 'open' );
        this.doAutocomplete();//attempt an autocomplete
    },

    doSearchInputBlur: function() {
        var that = this,
            $searchInputMenu = this.$el.find( '.purlSearchDropdownMenu' ),
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
        var $searchInputMenu = this.$el.find( '.purlSearchDropdownMenu' ),
            $searchInputDd = this.$el.find( '.searchWrap' ),
            $searchInput = this.$el.find( '#purlCelebrateNameInput' );

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
        var $searchInputMenu = this.$el.find( '.purlSearchDropdownMenu' ),
            active = $searchInputMenu.find( '.active' ).removeClass( 'active' ),
            prev = active.prev();

        if( !prev.length ) {
            prev = $searchInputMenu.find( 'li' ).last();
        }

        prev.addClass( 'active' );
    },

    doSearchInputNext: function() {
        var $searchInputMenu = this.$el.find( '.purlSearchDropdownMenu' ),
            active = $searchInputMenu.find( '.active' ).removeClass( 'active' ),
            next = active.next();

        if( !next.length ) {
            next = $( $searchInputMenu.find( 'li' )[ 0 ] );
        }

        next.addClass( 'active' );
    },

    //gateway to autocomplete, check num chars is enough
    doAutocomplete: function() {
        var $searchInput = this.$el.find( '#purlCelebrateNameInput' ),
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
        var $searchInput = this.$el.find( '#purlCelebrateNameInput' ),
            $searchInputMenu = this.$el.find( '.purlSearchDropdownMenu' ),
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
            $searchInputMenu = this.$el.find( '.purlSearchDropdownMenu' ),
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
            $searchInput = this.$el.find( '#purlCelebrateNameInput' ),
            $searchContainer = this.$el.find( '.searchList' ),
            $searchResults = $searchContainer.find( 'tbody' ),
            //$viewSelected = this.$el.find( '.purlSelectRadio' ).val(),
            $selectedFilter = this.$el.find( '.purlCelebrateTabs li.active a' ).data( 'nameId' ) || 'global',
            selectedName = $tar.data( 'acName' ),
            listVal = this.$el.find( '.purlCelebrateTabs li.active' ).find( '.selected' ).data( 'list-val' );


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

        this.model.loadParticipants( $selectedFilter, selectedName, this.$el.find( '.purlSelectRadio:checked' ).val(), listVal );
    },

    doAutocompMenuMouseenter: function( e ) {
        var $searchInputMenu = this.$el.find( '.purlSearchDropdownMenu' );

        $searchInputMenu.find( '.active' ).removeClass( 'active' );
        $( e.currentTarget ).not( '.dropdown-menu-info' ).addClass( 'active' );
    },

    clearAutocompMenu: function() {
        var $searchInputMenu = this.$el.find( '.purlSearchDropdownMenu' ),
            msg = $searchInputMenu.data( 'msgInstruction' );

        this.setAutocompMenuMsg( msg );
    },

    setAutocompMenuMsg: function( msg ) {
        var $i = $( this.make( 'li', { 'class': 'dropdown-menu-info' }, msg ) ),
            $searchInputMenu = this.$el.find( '.purlSearchDropdownMenu' );
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
