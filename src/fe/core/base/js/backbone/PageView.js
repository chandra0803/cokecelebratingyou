/*exported PageView */
/*global
TemplateManager,
GlobalHeaderView,
GlobalSidebarView,
GlobalNavRouter,
GlobalNavView,
PageNavView,
GlobalFooterView,
PageView:true
*/

//PAGE VIEW SUPER CLASS
// anything needed for all PageViews, goes here
PageView = Backbone.View.extend( {

    className: 'page',
    //init function
    initialize: function( opts ) {
        'use strict';
        var that = this;
        //logic for all pages

        this.appName = opts.appName || this.appName;

        //add a style class that identifies this by app and module name
        this.$el.addClass( this.className + ' ' + this.appName );

        // add the ID of the el for later reference
        this.elid = this.$el.attr( 'id' ) || 'contents';

        G5.views.page = this; // create a reference to this page

        // create handy references for later
        this.$body = $( 'body' );
        this.$sidebar = this.$body.find( '.sidebar' );
        this.$header = this.$body.find( '#header' );
        this.$gnav = this.$body.find( '#globalNav' );
        this.$contents = this.$body.find( '#contents' );


        // sets value based on css sizes in _global.scss
        G5.breakpoint = {};
        G5.breakpoint.refreshValue = function () {
          var prevValue = this.value;
          this.value = window.getComputedStyle( document.querySelector( 'body' ), ':before' ).getPropertyValue( 'content' ).replace( /\"/g, '' );
          if( this.value !== prevValue ) {
            G5._globalEvents.trigger( 'breakpointChanged', {}, this.value, prevValue );
          }
        };
        G5.breakpoint.refreshValue();

        // Initialize Modal resize on window resize
        $( window ).on( 'resize', _.throttle( function( event ) {
                G5.breakpoint.refreshValue();
                G5._globalEvents.trigger( 'windowResized', event, G5.breakpoint.value );
            }, 16.67 ) ); // 16.67 ~= 60fps

        $( window ).on( 'scroll', _.throttle( function( event ) {
                G5._globalEvents.trigger( 'windowScrolled', event );
            }, 16.67 ) ); // 16.67 ~= 60fps


        this.prepareGlobalAttributes();

        this.bodyIsFrozen = false;
        this.freezeBodyCheck();

        this.showAutoModal();

        G5._globalEvents.on( 'windowScrolled', function() {
            that.freezeBodyCheck();
        }, this );

        // Initialize Modal resize on window resize
        G5._globalEvents.on( 'windowResized', function() {
            // that.resizeModal();
            that.freezeBodyCheck();
        }, this );

        // doing a setInterval to check for an accidentally frozen body
        // first tried a few ways to listen to changes in #contents but didn't come up with anything robust enough
        this.freezeCheckInterval = setInterval( function() {
            that.freezeBodyCheck();
        }, 1000 );

        // Listen for modals to be opened and resize
        $( 'body' ).on( 'modal-shown modal-hidden', function() {
            // pausing for a fraction of a second to make sure the modal is fully open and positioned before trying to resize
            setTimeout( function() {
                // that.resizeModal();
            }, 1 );
        } );


        // show layout boxes for styling help
        if( G5.props.SHOWLAYOUTBOXES ) {
            $( 'body' ).addClass( 'showLayoutBoxes' );
        }

        // remove the pageloadingspinner
        // (check to make sure we're not inside a modal)
        if( !this.$el.closest( '.modal-body' ).length ) {
            $( '#pageLoadingSpinner' ).show().fadeOut( G5.props.ANIMATION_DURATION );
        }

        //resize textarea plugin for IE
        this.showResizeTextArea();

    },


    events: {
        // generic print button/link listener
        'click .pageView_print': 'doPrint',
        // generic help tooltip
        'click .pageView_help': 'doHelpTip'
    },

    doPrint: function( e ) {
        e.preventDefault();
        window.print();
    },
    doHelpTip: function( e ) {
        var $target = $( e.target ),
            popContent = $target.data( 'helpContent' );

        e.preventDefault();

        // TODO - add ability to set qtip options from target data attrs

        $target.qtip( {
                content: {
                   text: popContent
                },
                position: {
                    my: 'left center',
                    at: 'right center',
                    viewport: $( 'body' ),
                    adjust: {
                        method: 'shift none'
                    }
                },
                show: {
                    ready: true,
                    delay: false
                },
                hide: {
                    event: 'unfocus',
                    fixed: true
                },
                //only show the qtip once
                events: {
                    show: function() {
                        $target.css( 'position', 'relative' );
                    },
                    hide: function() {
                        $target.qtip( 'destroy' );
                        $target.css( 'position', '' );
                    },
                    render: function() {
                    }
                },
                style: { classes: 'ui-tooltip-shadow ui-tooltip-light' }
            } );

        $target.qtip( 'show' );
    },

    freezeBodyCheck: function() {
        this.$sidebar = this.$sidebar || this.$body.find( '.sidebar' );

        // if there's no sidebar, we bail
        if( this.$sidebar.length <= 0 && this.$body.hasClass( 'no-sidebar' )  && !this.$body.hasClass( 'sidebar-expanded' ) ) {
            if( this.bodyIsFrozen ) {
                this.unfreeze( 'body' );
            }
            return false;
        }

        this.scrollPrev = this.scrollPrev || 0;

        var scroll = window.pageYOffset,
            hH = this.$header.outerHeight(),
            gH = this.$gnav.outerHeight(),
            cH = this.$contents.outerHeight(),
            sH = this.$sidebar.outerHeight();

        var measurements = {
            windowHeight: window.innerHeight,
            scroll: window.pageYOffset,
            scrollPrev: this.measurements && this.measurements.scrollPrev || 0,

            // note, top and bottom measurements are calculated the same way for both body and sidebar
            // logic tree for body.top is expanded for usefulness.
            // negative values are above (top) or below (bottom) the edge of the window
            // these measurements help us keep the body and sidebar contained within the window at all times

            body: {
                height: hH + gH + cH,
                // keep the final top value <= 0
                // e.g. so we never see past the top of the body
                top: Math.min(
                    0,
                    // subtract the body height from the window height, keeping it >= the calculation below
                    // e.g. if this difference is greater than the scroll, we can't pull the top up so far that the bottom ends up showing
                    Math.max(
                        window.innerHeight - ( hH + gH + cH ),
                        // find the negative offset of the top of the body in relationship to the window scroll, keeping it <= 0
                        // e.g. the body is positioned 300px below the top of the window, but the window is only scrolled 150px, we want 0
                        Math.min(
                            0,
                            this.$header.offset().top - window.pageYOffset
                        )
                    )
                ),
                bottom: Math.min( 0, Math.max( window.innerHeight - ( hH + gH + cH ), Math.min( 0, window.innerHeight - ( this.$header.offset().top + hH + gH + cH ) + window.pageYOffset ) ) )
            },

            sidebar: {
                height: sH,
                top: Math.min( 0, Math.max( window.innerHeight - sH, Math.min( 0, this.$sidebar.offset().top - window.pageYOffset ) ) ),
                bottom: Math.min( 0, Math.max( window.innerHeight - sH, Math.min( 0, window.innerHeight - ( this.$sidebar.offset().top + sH ) + window.pageYOffset ) ) )
            }
        };

        // if we have previously measured and our new body height is different than the old body height, we need to adjust things
        if( this.measurements && this.measurements.body.height !== measurements.body.height ) {
            // calculate how much the body height changed (new minus old to get negative numbers if it shrunk)
            var difference = measurements.body.height - this.measurements.body.height;

            // if the body is shorter than the window, we set top and bottom to 0
            if( measurements.body.height < measurements.windowHeight ) {
                measurements.body.top = 0;
                measurements.body.bottom = 0;
            }
            // otherwise, adjust the bottom and only the bottom because we don't want to move the top of the screen from where the user thinks it is
            else {
                measurements.body.bottom = Math.min( 0, measurements.body.bottom - difference );
            }

            // set a flag that we need to reposition a fixed body
            measurements.body.refix = true;
        }
        // if not, we're fine
        else {
            measurements.body.refix = false;
        }

        this.measurements = $.extend( {}, this.measurements, measurements, true );

        // not using delta or direction at the moment, but they seem like they could be useful someday
        this.measurements.scrollDelta = this.measurements.scroll - this.measurements.scrollPrev;
        this.measurements.scrollDirection = this.measurements.scrollDelta < 0 ? 'up' : 'down';

        // console.log( 'freezeVars: ', this.measurements );

        this.measurements.scrollPrev = this.measurements.scroll;

        // shortcut
        this.m = this.measurements;

        /**
         * this short piece of code is for the global nav pinning
         * skip past it to get back to the sidebar/body meat-and-potatoes
         * LOGIC:
         *     if the amount of body hidden at the top is greater than gnav top position (represented by header height)
         *     && ( ( breakpoint is any one of the mobile-ish ones ) ? only when sidebar is hidden : true )
         */
        if( Math.abs( this.m.body.top ) > hH && ( ( G5.breakpoint.value == 'mini' || G5.breakpoint.value == 'mobile' || G5.breakpoint.value == 'tablet' ) ? this.globalSidebar.isExpanded === false : true ) ) {
            this.$body.addClass( 'fix-gnav' );
            this.$contents.css( 'margin-top', this.$gnav.outerHeight() );
        }
        else {
            this.$body.removeClass( 'fix-gnav' );
            this.$contents.css( 'margin-top', '' );
        }
        // ok, back to the real stuff

        /**
         * Possible scenarios
         * ------------------------------
         * A: window  > body    > sidebar
         * B: window  > body    < sidebar
         *
         * C: sidebar > window  > body
         * D: body    > window  > sidebar
         *
         * E: body    > sidebar > window
         * F: sidebar > body    > window
         *
         * Y: sidebar collapsed
         * Z: sidebar expanded
         *
         * Behavior
         * ------------------------------
         * For A and B: Do nothing; both unfrozen.
         *
         * For C: Freeze body, scroll sidebar with window
         * For D: Freeze sidebar, scroll body with window
         *
         * For E and F: Scroll both with window until shorter is exhausted
         *
         * For Y: Do nothing; both unfrozen
         * For Z: Freeze body
         */

        // check for Y
        if( this.$body.hasClass( 'sidebar-collapsed' ) ) {
            // console.log( 'doFreezeWork: false (Y)' );

            this.unfreeze( 'body' );
            this.unfreeze( 'sidebar' );
        }

        // check for A or B
        else if( this.m.windowHeight >= Math.max( this.m.windowHeight, this.m.body.height, this.m.sidebar.height ) ) {
            // console.log( 'doFreezeWork: false (A) or (B)' );

            this.unfreeze( 'body' );
            this.unfreeze( 'sidebar' );
        }

        else {
            // check for C
            if( this.m.windowHeight > this.m.body.height ) {
                // console.log( 'freezeBody (C)' );

                this.unfreeze( 'sidebar' );
                this.freeze( 'body' );
            }

            // check for D
            // note: "or equal to" used because sidebar sizes itself to the body
            else if( this.m.windowHeight >= this.m.sidebar.height ) {
                // console.log( 'freezeSidebar (D)' );

                this.unfreeze( 'body' );
                this.freeze( 'sidebar' );
            }

            // check for Z
            else if( this.$body.hasClass( 'sidebar-expanded' ) ) {
                // console.log( 'freezeSidebar (Z)' );

                this.unfreeze( 'sidebar' );
                this.freeze( 'body' );
            }

            // assume E or F
            else {
                // console.log( 'doFreezeWork: true (E) or (F)' );

                var taller, shorter;

                // check for E
                if( this.m.body.height > this.m.sidebar.height ) {

                    taller = 'body';
                    shorter = 'sidebar';
                }
                else {

                    taller = 'sidebar';
                    shorter = 'body';
                }

                // console.log( 'doFreezeWork: true (' + scenario + ')' );

                this.unfreeze( taller );

                // if we have scrolled the entire short element into view, fix it
                if( this.m[ shorter ].bottom >= 0 ) {
                    // console.log( 'doFreezeWork: true (' + scenario + ') freeze' );

                    this.freeze( shorter );
                }

                // TODO: add more comments - and clarify - tk
                // pin on scroll up, if the body is longer then the sidebar - this is to help prevent lazy loading jumping, while scrolling down
                // if we've hit the bottom of the entire window, pin it
                //if( this.m[ taller ].bottom >=  0 ) { // previous  this.m[ taller ].bottom  >=  0
                if( this.m[ taller ].bottom >=  -1 && this.measurements.scrollDirection == 'up' || ( this.m[ taller ].bottom  >=  0  && shorter == 'body' ) ) { // previous  this.m[ taller ].bottom  >=  0
                    //console.log( 'doFreezeWork: true  pin' );
                    this.freeze( shorter, { mode: 'pin' } );
                }

                // if the short element is already pindned and we scroll the entire top into view, fix it
                if( this.isfrozen( shorter ) == 'pin' && this.m[ shorter ].top >= 0 ) {
                    // console.log( 'doFreezeWork: true (' + scenario + ') freeze top' );

                    this.freeze( shorter );
                }

                // TODO: add more comments - and clarify - tk
                // pin on scroll up, if the body is longer then the sidebar - this is to help prevent lazy loading jumping, while scrolling down
                // if the short element is already fixed and we scroll the bottom of the page to meet the bottom of the short element, pin it
                //if( this.isfrozen( shorter ) == 'fix' && this.m[ shorter ].bottom <= this.m[ taller ].bottom ) {
                if( this.isfrozen( shorter ) == 'fix' && ( this.m[ shorter ].bottom  <=  this.m[ taller ].bottom  ) && ( this.measurements.scrollDirection == 'up' || shorter == 'body' ) )  { // previous     if( this.isfrozen( shorter ) == 'fix' && this.m[ shorter ].bottom <= this.m[ taller ].bottom ) {

                    //console.log( 'doFreezeWork: true  pin match' );

                    this.freeze( shorter, { mode: 'pin' } );
                }

                // if we've scrolled all the way back to the top OR we're close enough that a negative fixed short element needs to scroll into view, reset to normal
                if( this.m[ taller ].top >= 0 || Math.abs( this.m[ shorter ].top ) >= this.m.scroll ) {
                    // console.log( 'doFreezeWork: true (' + scenario + ') unfreeze' );

                    this.unfreeze( shorter );
                }
            }
        }

        // store the last scroll position
        this.scrollPrev = scroll;
    },

    // freeze, unfreeze, and isfrozen are little util/proxy methods to enable code reuse
    freeze: function( which, opts ) {
        if( which == 'body' ) {
            this.freezeBody( opts );
        }
        else {
            this.freezeSidebar( opts );
        }
    },
    unfreeze: function( which, mode, opts ) {
        if( which == 'body' ) {
            this.unfreezeBody( mode, opts );
        }
        else {
            this.unfreezeSidebar( mode, opts );
        }
    },
    isfrozen: function( which ) {
        return this[ which + 'IsFrozen' ];
    },

    freezeBody: function( opts ) {
        var defaults = {
            mode: 'fix',
            sidebarHeight: this.m.sidebar.height,
            top: this.m.body.top
        };

        var settings = _.extend( defaults, opts );

        if( this.bodyIsFrozen === settings.mode && this.measurements.body.refix !== true  ) {
            //console.log( 'freezeBody NO WORK' );
            return false;
        }
        //console.log( 'freezeBody' );
        if( settings.mode === 'fix' ) {
            this.$body.css( 'height', settings.sidebarHeight ).addClass( 'fix-body' ).removeClass( 'pin-body' );
            this.$header.css( 'top', settings.top );
            this.$gnav.css( 'top', settings.top + this.$header.outerHeight() );
            this.$contents.css( 'top', settings.top + this.$header.outerHeight() + this.$gnav.outerHeight() );
        }

        if( settings.mode === 'pin' ) {
            this.$body.addClass( 'pin-body' );
            this.$gnav.css( 'bottom', this.$contents.outerHeight() );
            this.$header.css( 'bottom', this.$contents.outerHeight() + this.$gnav.outerHeight() );
        }

        this.bodyIsFrozen = settings.mode;
    },

    unfreezeBody: function() {
        if( this.bodyIsFrozen === false ) {
            // console.log( 'unfreezeBody NO WORK' );
            return false;
        }
        //console.log( 'unrfreeezeeeeee ' );
        this.$body.css( 'height', '' ).removeClass( 'fix-body pin-body' );
        this.$header.css( { 'top': '', 'bottom': '' } );
        this.$gnav.css( { 'top': '', 'bottom': '' } );
        this.$contents.css( { 'top': '', 'bottom': '' } );

        this.bodyIsFrozen = false;
    },

    freezeSidebar: function( opts ) {
        var defaults = {
            mode: 'fix',
            bodyHeight: this.m.body.height,
            top: this.m.sidebar.top
        };

        var settings = _.extend( defaults, opts );

        if( this.sidebarIsFrozen === settings.mode ) {
            // console.log( 'freezeSidebar NO WORK' );
            return false;
        }

        if( settings.mode === 'fix' ) {
            this.$body.css( 'height', settings.bodyHeight ).addClass( 'fix-sidebar' ).removeClass( 'pin-sidebar' );
            this.$sidebar.css( 'top', settings.top );
        }

        if( settings.mode === 'pin' ) {
            this.$body.addClass( 'pin-sidebar' );
        }

        this.sidebarIsFrozen = settings.mode;
    },

    unfreezeSidebar: function() {
        if( this.sidebarIsFrozen === false ) {
            // console.log( 'unfreezeSidebar NO WORK' );
            return false;
        }

        this.$body.css( 'height', '' ).removeClass( 'fix-sidebar pin-sidebar' );
        this.$sidebar.css( 'top', '' );

        this.sidebarIsFrozen = false;
    },

    /*
    resizeModal: function() {
        if( $('body').hasClass('modal-open') ) {
            var windowHeight = $(window).height(),
                $modal = $('.modal:visible'),
                newTop;//,
                //newBodyHeight;

            //If normal modal, then run the following code.
            if( $modal.length && !$modal.hasClass('modal-stack') ){
                var $modalBody = $modal.find('.modal-body'),
                    modalHeight = $modal.outerHeight(true),
                    modalPosition = $modal.position(),
                    windowScrolltop = $(window).scrollTop(),
                    modalWindowPosition = modalPosition.top - windowScrolltop,
                    modalBodyHeight = $modalBody.height();

                // IF the window height is less than the modal height + 2x the gap between the top of the window and the top of the modal
                // OR if the modal body is currently height adjusted
                if( windowHeight <= modalHeight + 2 * modalWindowPosition || $modalBody.data('resized') === true ) {
                    // IF the window height is less than the modal height
                    // OR the modal body height is less than the modal body wrapper height
                    if( windowHeight < modalHeight || $modalBody.height() < $modalBody.find('.modal-body-wrapper').height() ) {
                        // we adjust the height of the modal body
                        if( !$modalBody.find('.modal-body-wrapper').length ) {
                            $modalBody.wrapInner('<div class="modal-body-wrapper" />');
                        }
                        $modalBody
                            .css('height', windowHeight - (modalHeight - modalBodyHeight))
                            .data('resized', true);
                        $modal.css('top', 0);
                    }
                    // otherwise, we assume the modal height is fine as is and adjust the position of the modal in the window
                    else {
                        // calculate the new top measurement
                        newTop = (windowHeight - modalHeight) / 2;
                        // reset the modal body height adjustments and unwrap the contents
                        $modalBody
                            .css('height', '')
                            .data('resized', false)
                            .find('.modal-body-wrapper').children().unwrap();
                        $modal.css('top', newTop);
                    }
                }
                // otherwise, reset the top of the modal to default
                else {
                    $modal.css('top', '');
                }
            }
        }
    },
    */

    prepareGlobalAttributes: function() { 
    		//Global Privacy policy popup close action
    		$( '.compliant-close' ).unbind().click( function ( e ) {
                   e.preventDefault();
    			   $.ajax( {
    			      dataType: 'g5json', //must set this so SeverResponse can parse and return to success()
    			      type: 'POST',
    			      url: G5.props.URL_COOKIES_UPDATE,
    			      success: function () {
    			          $( '#compliantModal' ).hide();
    			      },
    			      error: function ( jqXHR, textStatus, errorThrown ) {
                         //Handle Error
    			          $( '#compliantModal' ).show();
    			      }
    			   } );
    			} );
		 // GLOBAL HEADER
        // only do this once -- if there are two page view active
        if( !G5.views.globalHeader && G5.views.globalHeader != 'hidden' ) {
            this.globalHeader = new GlobalHeaderView( {
                page: this,
                globalHeader: this.options.globalHeader || null,
                loggedIn: this.options.loggedIn === false ? false : true
            } );
            G5.views.globalHeader = this.globalHeader;
        } else {
            this.globalHeader = G5.views.globalHeader;
        }

        // global sidebar and nav don't render when the user isn't logged in
        if( this.options.loggedIn === false || this.options.noSidebar === true ) {        	
        	//Invoking the privacy policy popup except first page login screen            	
			$( '#compliantModal' ).hide();
			
            $( '.sidebar' ).remove();
            this.$sidebar = null;
            $( 'body' ).addClass( 'no-sidebar' );
            G5.views.globalSidebar = 'hidden';
            
        } // if loggedIn
        else {
        	$( '#compliantModal' ).show();
            // GLOBAL SIDEBAR - same treatment as global header
            if( !G5.views.globalSidebar && G5.views.globalSidebar != 'hidden' ) {
                this.globalSidebar = new GlobalSidebarView( {
                    page: this,
                    globalSidebar: this.options.globalSidebar || null,
                    loggedIn: this.options.loggedIn === false ? false : true
                } );
                G5.views.globalSidebar = this.globalSidebar;
            } else {
                this.globalSidebar = G5.views.globalSidebar;
            }

            if( G5.views.globalSidebar && G5.views.globalSidebar != 'hidden' ) {
                G5.views.globalSidebar.on( 'rendered', this.freezeBodyCheck, this );
                G5.views.globalSidebar.on( 'sidebarCollapsed', this.freezeBodyCheck, this );
                G5.views.globalSidebar.on( 'sidebarExpanded', this.freezeBodyCheck, this );
            }

        }

        if( this.options.loggedIn === false || this.options.noGlobalNav === true ) {
            // it's possible to open a stacked page when not logged in, which would run through this logic tree a second time
            // let's bail out after finding G5.views.globalNav already existing
            if( G5.views.globalNav ) {
                return false;
            }
            this.router = new GlobalNavRouter();
            $( '#globalNav' ).remove();
            $( 'body' ).addClass( 'no-globalNav' );
            G5.views.globalNav = 'hidden';
        } // if loggedIn
        else {

            // GLOBAL Nav - same treatment as global header
            if( !G5.views.globalNav && G5.views.globalNav != 'hidden' ) {
                this.globalNav = new GlobalNavView( {
                    page: this,
                    globalNav: this.options.globalNav || null,
                    loggedIn: this.options.loggedIn === false ? false : true
                } );
                G5.views.globalNav = this.globalNav;
            } else {
                this.globalNav = G5.views.globalNav;
            }

        }

        // PAGE NAVIGATION - same treatment as global header
        if( !G5.views.pageNav && G5.views.pageNav != 'hidden' ) {
            this.pageNav = new PageNavView( {
                page: this,
                pageNav: this.options.pageNav || null
            } );
            G5.views.pageNav = this.pageNav;
        } else {
            this.pageNav = G5.views.pageNav;
        }

        // Global Footer ONLY on Login Page
        // GLOBAL FOOTER - same treatment as global header
        if( !G5.views.globalFooter && G5.views.globalFooter != 'hidden' ) {
            this.globalFooter = new GlobalFooterView( {
                page: this,
                loggedIn: this.options.loggedIn === false ? false : true,
                noSidebar: this.options.noSidebar === true ? true : false,
                // display links as stacked modals?
                isSheets: this.options.isFooterSheets === false ? false : true,
                // if loaded already, get from header prof, else listen for data load
                profileModel: this.getParticipantProfile()
            } );
            G5.views.globalFooter = this.globalFooter;
        } else {
            this.globalFooter = G5.views.globalFooter;
        }
    },

    getPageTitle: function() {
        return this.pageNav.getPageTitle();
    },
    setPageTitle: function( title ) {
        this.pageNav.setPageTitle( title );
    },

    // use appName to get a template from this apps template dir
    getTpl: function( tplName, callback ) {
        'use strict';
        var tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + this.appName + '/tpl/';

        // grab template and do the bidding of callback
        TemplateManager.get( tplName, function( tpl ) {
            callback( tpl );
        }, tplUrl );
    },

    getParticipantProfile: function( name ) {
        var m;
        // 'use strict';
        if( this.globalSidebar &&
            this.globalSidebar.partProf &&
            this.globalSidebar.partProf.model ) {

            m = this.globalSidebar.partProf.model;
            return name ? m.get( name ) : m;

        } else {
            return null; // not there yet
        }
    },

    // if there is an .autoModal in html (bootstrap style modal), then show it
    showAutoModal: function() {
        var $am = $( 'body' ).find( '.autoModal' );

        if ( this.$el.closest( '.modal-stack' ).length > 0 ) {
            return false;
        }

        // log an error if more than one autoModal EL exists
        // * if the case for two or more arises, we'll have to build in the functionality to show
        //     consecutive modals gracefully
        if( $am.length > 1 ) {
            console.error( '[ERROR] PageView: more than two autoModals found on this page, will show first found' );
            // only do first one
            $am = $( $am.get( 0 ) );
        }

        // attach and show the modal
        // * closing of the modal is handled via 'data-dismiss="modal"' bootstrap data-api method
        // * more sophisticated cases will have to be evaluated on a case-by-case basis to see what
        //     additional features will be necessary
        // * as this is a generic piece, all logic should be generic -- any super-specific logic
        //     should be contained in a view
        $am.modal( {
            backdrop: true,
            keyboard: true,
            show: true
        } );

    },
        
    showResizeTextArea: function() {
        var resizeMe = $( 'body' ).find( '.resize-me' );
        if( this.detectIE() ) {
            if( resizeMe.length > 0 ) {
                var el = $( 'body' ).find( '.resize-me' );
                window.resizeHandlerPolyfill( el, true );
            }
        }
    },
    
    detectIE: function() {
        var ua = window.navigator.userAgent;
      
        // Test values; Uncomment to check result …
      
        // IE 10
        // ua = 'Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)';
        
        // IE 11
        // ua = 'Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko';
        
        // Edge 12 (Spartan)
        // ua = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0';
        
        // Edge 13
        // ua = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586';
      
        var msie = ua.indexOf( 'MSIE ' );
        if ( msie > 0 ) {
          // IE 10 or older => return version number
          return parseInt( ua.substring( msie + 5, ua.indexOf( '.', msie ) ), 10 );
        }
      
        var trident = ua.indexOf( 'Trident/' );
        if ( trident > 0 ) {
          // IE 11 => return version number
          var rv = ua.indexOf( 'rv:' );
          return parseInt( ua.substring( rv + 3, ua.indexOf( '.', rv ) ), 10 );
        }
      
        var edge = ua.indexOf( 'Edge/' );
        if ( edge > 0 ) {
          // Edge (IE 12+) => return version number
          return parseInt( ua.substring( edge + 5, ua.indexOf( '.', edge ) ), 10 );
        }
      
        // other browser
        return false;
      }


} );
