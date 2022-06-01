/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported NominationWinnersListPageView, PaginationView */
/*global
$,
_,
Backbone,
G5,
TemplateManager,
PaginationView,
NominationWinnersListPageModel,
NominationWinnersListPageView:true
*/
NominationWinnersListPageView = PageView.extend( {
    //init function
    initialize: function( opts ) {
        var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'nominations';

		//this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        var today = new Date();
        var aYear = new Date();

        aYear.setFullYear( today.getFullYear() - 1 );

		this.$el.find( '.datepickerTrigger' ).datepicker( { 'startDate': '-1y', 'endDate': 'today' } );
        this.$el.find( '#nominationsDateStart' ).parent().datepicker( 'update', aYear );
        this.$el.find( '#nominationsDateEnd' ).parent().datepicker( 'update', today );

		this.$nomination = this.$el.find( '#nominationId' );
		this.autocompDelay = 200;
        this.autocompMinChars = 2;
	    this.autocompUrl = this.$el.find( '#nominationWinnersSearchInput' ).data( 'autocompUrl' ) || G5.props.URL_JSON_NOMINATIONS_SEARCH_AUTOCOMPLETE;
        this.searchUrl = this.$el.find( '#nominationWinnersSearchInput' ).data( 'searchUrl' ) || G5.props.URL_JSON_NOMINATIONS_WINNERS_LIST;

		//our model
        this.model = new NominationWinnersListPageModel( {
            autocompUrl: this.autocompUrl,
            searchUrl: this.searchUrl
        } );

		//for autocomplete timing
        this.autocompLastKeyupTime = 0;

        this.opts = this.options.data;
        this.model.loadData( this.opts );

		this.model.on( 'autocompleted', this.addCompletionsToDom, this );

		this.renderSearch();

		G5.util.showSpin( this.$el, { cover: true, classes: 'pageView' } );

        this.model.on( 'loadDataFinished', function( data ) {
            that.render( data );
        } );

		this.model.on( 'loadCommentsFinished', function( comments ) {
			that.$el.find( '.spincover' ).hide();
            that.renderWinners( comments );
        } );

        this.model.on( 'loadCommentsNoResults', function( comments ) {
            that.$el.find( '.spincover' ).hide();
            that.showErrorModal( comments );
        } );
    },

    events: {
		'change #nominationId': 'nominationChanged',
		'click  #nominationsWinnersSubmit': 'submitFormData',
		'change .date': 'validateDates',
		'click  .profile-popover': 'attachParticipantPopover',
		'click  #buttonChangeNomination': 'changeNominationButtonClick',
		'click  #buttonChangeNominationConfirm': 'changeNominationConfirmButtonClick',
		'click  #buttonChangeNominationCancel': 'changePromoCancelButtonClick',

		//search input autocomp
        'focus #nominationWinnersSearchInput': 'doSearchInputFocus',
        'blur #nominationWinnersSearchInput': 'doSearchInputBlur',
        'keyup #nominationWinnersSearchInput': 'doSearchInputKeyup',
        'keypress #nominationWinnersSearchInput': 'doSearchInputKeypress',
        'keydown #nominationWinnersSearchInput': 'doSearchInputKeydown',
        'mouseenter .winnersSearchDropdownMenu li': 'doAutocompMenuMouseenter',
        'click .winnersSearchDropdownMenu a': 'doAutocompSelect',
		'click .searchBtn': 'submitSearch',
        'change .winnerSearchSelect': 'doSearchSelectType',
        'click .filterDelBtn': 'doFilterDelClick'
    },

    render: function( data ) {
		 var that = this,
    		 opt,
    		 $nomination = this.$el.find( '#nominationId' ),
             preSelectPromo = this.$el.find( '#promotionId' ).val();

		 this.$nomination.find( 'option' ).not( '.defaultOption' ).remove();

         if( data.totalPromotionCount >= 2 ) {
             _.each( data.nominations, function( promo ) {

     				 opt = that.make( 'option', { value: promo.promoId }, promo.name );
                     $nomination.append( opt );

     			} );
                this.allPromotionDisplay( data );
         } else {
            _.each( data.nominations, function( promo ) {
    			if( promo.promoId == preSelectPromo ) {
    				  opt = that.make( 'option', { value: promo.promoId, selected: true }, promo.name );
    			} else {
    				 opt = that.make( 'option', { value: promo.promoId }, promo.name );
    			}

                $nomination.append( opt );
            } );

            this.nominationChanged();
        }

    },

    allPromotionDisplay: function( data ) {
        console.log( data );

        this.$nomination.closest( '.nominationWrapper' ).hide();

        this.$nomination
            .closest( '.nominationSection' )
            .find( '.nominationChangeWrapper' )
            .fadeIn( G5.props.ANIMATION_DURATION )
            .find( '.nominationName' )
            .text( 'All Promotions' );

        this.$( '.nominationPromo' ).find( '.validate-tooltip' ).qtip( 'hide' );
        this.$( '.nominationPromo' ).removeClass( 'validateme' );

        this.model.displayComments( data );

        G5.util.hideSpin( this.$el, { cover: true, classes: 'pageView' } );

    },

	nominationChanged: function( e ) {
            var nVal = this.$nomination.val(),
    			$form = $( '#nominationWinnersForm' ),
    			dataToSend  = $form.serializeArray(),
                nName = this.$nomination.find( ':selected' ).text();

            var formdata = {};

            $( dataToSend ).each( function( index, value ) {
                var thisName = value.name;

                formdata[ thisName ] = value.value;
            } );

            dataToSend = formdata;

        if( nVal ) {
            this.$nomination.closest( '.nominationWrapper' ).hide();

            this.$nomination.closest( '.nominationSection' )
                .find( '.nominationChangeWrapper' ).fadeIn( G5.props.ANIMATION_DURATION )
                .find( '.nominationName' ).text( nName );

			// clear validation qtip (they will be added again if errors exist)
			this.$( '.nominationPromo' ).find( '.validate-tooltip' ).qtip( 'hide' );

			this.model.displayComments( dataToSend );
        }

		G5.util.hideSpin( this.$el, { cover: true, classes: 'pageView' } );
    },

    doSearchSelectType: function( e ) {
        var selectedText = $( e.target.selectedOptions ).text();

        $( '#nominationWinnersSearchInput' ).val( '' );
        $( '#nominationWinnersSearchInput' ).attr( 'placeholder', selectedText );
    },

    doFilterDelClick: function( e ) {
        var $tar = $( e.currentTarget );

            $tar.parent( '.filter' ).remove();

        e.preventDefault();
    },

	submitFormData: function( e ) {
		var $form   = $( '#nominationWinnersForm' ),
            dateData = $form.find( '.datepickerInp' ).serializeArray(),
    		$dataToSend  = $( '.filter' ),
            promoId = $( '#nominationId option:selected' ).val();

        e.preventDefault();

        var formdata = {};

        $dataToSend.each( function( index, value ) {
            var thisName = $( value ).data( 'ac-type' );

            formdata[ thisName ] = $( value ).data( 'ac-name' );
        } );

        formdata[ 'nominationId' ] = promoId;

        $( dateData ).each( function( index, value ) {
            formdata[ value.name ] = value.value;
        } );

        // $form.find('input').each(function(){
        //     $(this).val('');
        // });

	    this.model.displayComments( formdata );
    },

	// when an autocomplete selection comes back from server
    addCompletionsToDom: function( comps, serverMsg ) {
        var that = this,
            $searchInputMenu = this.$el.find( '.dropdown-menu' );
            console.log( $searchInputMenu );

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
            // $searchBtn.removeAttr('disabled');

            //highlight first item
            $( $searchInputMenu.find( 'li' )[ 0 ] ).addClass( 'active' );
        } else if( !serverMsg && comps.length === 0 ) {
            //add display no results message
            this.setAutocompMenuMsg( $searchInputMenu.data( 'msgNoResults' ) );
            // $searchBtn.attr('disabled', 'disabled');
        } else {//server message
            this.setAutocompMenuMsg( serverMsg );
        }
    },

	// NOMINATION visual state and change dialogs funcs
    changeNominationButtonClick: function( e ) {
        var $tar = $( e.target );

        e.preventDefault();

        // show a qtip
        if( !$tar.data( 'qtip' ) ) {
            this.addConfirmTip( $tar,
                this.$nomination.closest( '.nominationSection' ).find( '.nominationChangeConfirmDialog' )
            );
        }
    },

	changeNominationConfirmButtonClick: function( e ) {
      this.$nomination.closest( '.nominationSection' ).find( '.nominationChangeWrapper' ).hide();
      this.$nomination.closest( '.nominationWrapper' ).fadeIn( G5.props.ANIMATION_DURATION );
      this.$nomination.closest( '.nominationSection' ).find( '#buttonChangeNomination' ).qtip( 'hide' );

      e.preventDefault();
    },

	changePromoCancelButtonClick: function( e ) {
        this.$nomination.closest( '.nominationSection' ).find( '#buttonChangeNomination' ).qtip( 'hide' );

        e.preventDefault();
    },

	recipientSearchSpinner: function( opt ) {
        var self  = this,
            $spinnerWrap   = this.$el.find( '.searchWrap' ),
            $searchBtn     = $spinnerWrap.find( '.searchBtn' ),
            $searchSpinner = this.$searchSpinner || ( function() {
    			var opts = {
                           color: $searchBtn.find( 'i' ).css( 'color' )
                };
    			self.$searchSpinner = $spinnerWrap.find( '.spincover' ).show().spin( opts );
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

	renderWinners: function( comments ) {
		var that = this,
            tplName = 'commentItem',
            $container = that.$el.find( '.commentsListWrapper' ),
            commentsData = comments;

            $container.empty();
            // console.log($container.html());

		TemplateManager.get( tplName, function( tpl ) {
			$container.empty().append( tpl( commentsData ) );
		}, that.tplUrl );
	},

    showErrorModal: function( comments ) {
        var that = this,
            tplName = 'commentError',
            tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/',
            commentsData = comments;
            console.log( tplUrl );

        TemplateManager.get( tplName, function( tpl ) {
            $( '.commentsListWrapper' ).empty().append( tpl( commentsData ) );
            var $m = $( '.commentsListWrapper' ).find( '.winListErrorsModal' );
            $m.modal();
        }, that.tplUrl );
        //
        // if($m.length === 0){
        //     setTimeout(function(){ // IE8 + JQ1.8.3 -- push target.empty() to end of stack
        //         $m = $('.commentsListWrapper').find('.winListErrorsModal');
        //     }, 300);
        // }
        // console.log($m);
        // console.log(comments);

    },

	validateDates: function( event ) {
        'use strict';

        var $startDate = this.$el.find( '#nominationsDateStart' ),
            $endDate = this.$el.find( '#nominationsDateEnd' ),
            $target = $( event.currentTarget ),
            startDate = $startDate.closest( '.datepickerTrigger' ).data( 'datepicker' ).date,
            endDate = $endDate.closest( '.datepickerTrigger' ).data( 'datepicker' ).date;

        switch( $target.attr( 'id' ) ) {
            case $startDate.attr( 'id' ):
            case undefined:

                // once start date has been set, other dates cannot occur before it
                $endDate.closest( '.datepickerTrigger' ).datepicker( 'setStartDate', $startDate.val() );

                // if the new startDate is later than the current endDate AND a value already exists in endDate, clear the value & indicate change
                if ( startDate.valueOf() > endDate.valueOf() && $endDate.val() !== '' ) {
                    G5.util.animBg( $endDate, 'background-flash' );
                    $endDate.val( '' ).closest( '.datepickerTrigger' ).datepicker( 'update' );
                }
                break;
            case $endDate.attr( 'id' ):
                G5.util.formValidate( $( '#nominationWinnersForm' ).find( '.validateme' ) );
                // if endDate is updated, displayEndDate updates automatically to endDate + 10 days
                break;
        } // switch
    }, // validateDates

	attachParticipantPopover: function( e ) {
        var $tar = $( e.target ),
            isSheet = ( $tar.closest( '.modal-stack' ).length > 0 ) ? { isSheet: true }  :  { isSheet: false };

        if ( $tar.is( 'img' ) ) {
            $tar = $tar.closest( 'a' );
        }
        isSheet.containerEl = this.$el;
        //attach participant popovers
        if ( !$tar.data( 'participantPopover' ) ) {
            $tar.participantPopover( isSheet ).qtip( 'show' );
        }

        e.preventDefault();
    },

	renderSearch: function() {
        var $ph = this.$el.find( '#nominationWinnersSearchInput' );

        $ph.placeholder();
        console.log( $ph.placeholder() );

        this.autocompDelay = this.$el.find( '#nominationWinnersSearchInput' ).data( 'autocompDelay' );
        this.autocompMinChars = this.$el.find( '#nominationWinnersSearchInput' ).data( 'autocompMinChars' );
    },

	 //Auto complete
    doSearchInputFocus: function( e ) {
        var $searchInputDd = this.$el.find( '.searchWrap' );

		this.$el.find( '.dateRange .control-group' ).removeClass( 'validateme' );

        //open the autocomplete (dropdown menu)
        $searchInputDd.addClass( 'open' );
        this.doAutocomplete();//attempt an autocomplete
    },

	doSearchInputBlur: function( e ) {
        var that = this,
            $searchInputMenu = this.$el.find( '.dropdown-menu' ),
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

	doSearchInputKeyup: function( e ) {
        var $searchInputMenu = this.$el.find( '.dropdown-menu' ),
            $searchInputDd = this.$el.find( '.searchWrap' ),
            $searchInput = this.$el.find( '#nominationWinnersSearchInput' );

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

	doSearchInputKeypress: function( e ) {
        this.doSearchInputMove( e );
    },

	doSearchInputKeydown: function( e ) {
        if( $.browser.webkit || $.browser.msie ) {
            this.supressKeyPressRepeat = !~$.inArray( e.keyCode, [ 40, 38, 9, 13, 27 ] );
            this.doSearchInputMove( e );
        }
    },

	doAutocompMenuMouseenter: function( e ) {
        var $searchInputMenu = this.$el.find( '.dropdown-menu' );

        $searchInputMenu.find( '.active' ).removeClass( 'active' );
        $( e.currentTarget ).not( '.dropdown-menu-info' ).addClass( 'active' );
    },

	doAutocompSelect: function( e ) {
        var $tar = $( e.target ),
            $searchInputDd = this.$el.find( '.searchWrap' ),
            $searchInput = this.$el.find( '#nominationWinnersSearchInput' ),
            $searchContainer = this.$el.find( '.commentsListWrapper' ),
            selectedName = $tar.data( 'acName' ),
            typeSelect = this.$el.find( '#winnerSearchSelect option:selected' ).val(),
            typeDisplay = this.$el.find( '#winnerSearchSelect option:selected' ).text(),
            labels = this.$el.find( '.xlabel' );

        e.preventDefault();


        //hide menu
        $searchInputDd.removeClass( 'open' );


        labels.each( function( index, value ) {
            if( $( value ).data( 'ac-type' ) == typeSelect ) {
                $( value ).remove();
            }
        } );
        //clear entered text
        $searchInput.val( selectedName );
        this.clearAutocompMenu();
        G5.util.showSpin( $searchContainer );

        var filterHtml = '<span class="filter xlabel xlabel-info" data-ac-type="' + typeSelect + '" data-ac-name="' + selectedName + '"><a class="filterDelBtn btn btn-mini" href="#" data-ac-type="' + typeSelect + '"><i class="icon-close"></i></a><span class="muted">' + typeDisplay + ':</span> <strong>' + selectedName + '</strong> </span>';

        $( '.filterContainer' ).append( filterHtml );

        if( $( '.filterContainer' ).not( ':visible' ) ) {
            $( '.filterContainer' ).show();
        }
		// this.model.loadWinners(selectedName);
    },

	//check the time since last keypress, and forward query if apropo
    tryAutocomplete: function() {
        var $searchInput = this.$el.find( '#nominationWinnersSearchInput' ),
            $searchInputMenu = this.$el.find( '.dropdown-menu' ),
            $searchSelect = this.$el.find( '#winnerSearchSelect option:selected' ).val(),
            now = ( new Date() ).getTime(),
            delay = now - this.autocompLastKeyupTime,
            query = {
                'query': $searchInput.val(),
                'type': $searchSelect
            };

        //check to make sure no keys have been pressed during the delay (give 10ms margin)
        if( delay >= this.autocompDelay - 10 ) {
            // check to make sure the query string isn't too long
            if( query.query.length >= this.autocompMinChars ) {
                $searchInputMenu.empty();
                this.model.queryAutocomplete( query );
            }
            // not enough chars, set default text
            else {
                this.clearAutocompMenu();
            }
        }
    },

    //gateway to autocomplete, check num chars is enough
    doAutocomplete: function() {
        var $searchInput = this.$el.find( '#nominationWinnersSearchInput' ),
            query = $searchInput.val(),
            that = this;

        if( query.length >= that.autocompMinChars ) {
            //set the time
            that.autocompLastKeyupTime = ( new Date() ).getTime();
            //set a timeout on delay, and see if keyboard has been idle for long enough
            setTimeout( function() {that.tryAutocomplete();}, that.autocompDelay );

        //    if( !G5.util.formValidate(this.$el.find('.validateme')) ) {
        //         return false;
        //     }
            this.recipientSearchSpinner();
        }else{
            //clear the menu results
            this.clearAutocompMenu();
        }
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

	clearAutocompMenu: function() {
        var $searchInputMenu = this.$el.find( '.dropdown-menu' ),
            msg = $searchInputMenu.data( 'msgInstruction' );

        this.setAutocompMenuMsg( msg );
    },

    setAutocompMenuMsg: function( msg ) {
        var $i = $( this.make( 'li', { 'class': 'dropdown-menu-info' }, msg ) ),
            $searchInputMenu = this.$el.find( '.dropdown-menu' );

        //empty menu, and add a msg
        setTimeout( function() { // IE8 + JQ1.8.3 -- push target.empty() to end of stack
            $searchInputMenu.empty();
            $searchInputMenu.append( $i );
        }, 0 );
    },

	doSearchInputPrev: function() {
        var $searchInputMenu = this.$el.find( '.dropdown-menu' ),
            active = $searchInputMenu.find( '.active' ).removeClass( 'active' ),
            prev = active.prev();

        if( !prev.length ) {
            prev = $searchInputMenu.find( 'li' ).last();
        }

        prev.addClass( 'active' );
    },

    doSearchInputNext: function() {
        var $searchInputMenu = this.$el.find( '.dropdown-menu' ),
            $active = $searchInputMenu.find( '.active' ).removeClass( 'active' ),
            next = $active.next();

        if( !next.length ) {
            next = $( $searchInputMenu.find( 'li' )[ 0 ] );
        }

        next.addClass( 'active' );
    },

	submitSearch: function( e ) {
        var $input = this.$el.find( '#nominationWinnersSearchInput' ),
            $validate = this.$el.find( '.validateme' );

        e.preventDefault();
        console.log( $input );
		this.$el.find( '.dateRange .control-group' ).removeClass( 'validateme' );

        if( !G5.util.formValidate( $validate ) ) {
            return false;
        }

        this.doAutocompSelect( e );
    },

	//add confirm tooltip
    addConfirmTip: function( $trig, cont ) {
        //attach qtip and show
        $trig.qtip( {
            content: { text: cont },
            position: {
                my: 'left center',
                at: 'right center',
                container: this.$el,
                viewport: $( 'body' ),
                adjust: { method: 'shift none' }
            },
            show: {
                event: 'click',
                ready: true
            },
            hide: {
                event: 'unfocus',
                fixed: true,
                delay: 200
            },
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-light participantPopoverQtip',
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            }
        } );
    }
} );
