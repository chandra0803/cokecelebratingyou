/*jslint browser: true, nomen: true, unparam: true*/
/*exported PaxSearchReactModelView */
/*global
console,
$,
_,
PageView,
PaxSearchReactModel,
PaxSearchReactModelView:true
*/
PaxSearchReactModelView = PageView.extend( {
    events: {
        'keydown #paxNameInput': 'recipientSearchKeyController',
        'click   .moduleRecipWrapper': 'clickRecipient',
        'submit  #nameFilter': 'submitSearch',
        'click   .searchBtn': 'submitSearch'
    },

    initialize: function ( opts ) {
        console.log( 'I AM HERE!!!!' );
        this.appName = 'paxSearchReact';
        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        this.PaxSearchReactModel = new PaxSearchReactModel();

    },


    recipientSearchKeyController: function( event ) {
        /*
         *
         * General purpose function used to route advanced functions
         * when interacting with the search form via the keyboard.
         *
         * [1] Wait for the template to be loaded before we do anything.
         *
         * [2] Object of booleans to simplify checking on the key being used.
         *
         * [3] Holds the operations to be run when the enter key is pressed.
         *
         *     [3a] If there is currently a recipient selected, we're
         *          going to pass that information on from here.
         *
         *     [3b] Otherwise, we're going to stop the timer waiting for
         *          a search value and call the complete function.
         *
         * [4] Holds the operations for every key besides the enter key.
         *
         * [5] Operations for special keys
         *
         *     [5a] Stop trying to complete this section if there isn't
         *          a list of recipients to interact with yet.
         *
         *     [5b] Direction to move on the list of recipients
         *
         * [6] Default behavior for all other keys
         *
         */


        var self       = this,
            pressedKey = event.keyCode,
            checkKey   = function( key ) {
                            return key === pressedKey;
                         },
            keyIs      = { // [2]
                            backspace: checkKey(  8 ),
                            shiftKey: checkKey( 16 ),
                            enterKey: checkKey( 13 ),
                            downKey: checkKey( 40 ),
                            tabKey: checkKey(  9 ),
                            upKey: checkKey( 38 )
                         };

        if ( keyIs.enterKey ) { // [3]

            event.preventDefault();

            var $input = self.$el.find( '.paxNameInput' );
            this.fetchRecipientList( $input.val() );
        } else { // [4]
            if ( keyIs.downKey || keyIs.upKey ||
                keyIs.tabKey  || keyIs.shiftKey ) { // [5]

                event.preventDefault();

                if( !this.recipientList ) { // [5a]
                    return;
                }

                var direction = 0; // [5b]

                if ( keyIs.downKey ) {
                    direction = 1;
                }

                if ( keyIs.upKey ) {
                    direction = -1;
                }

                if ( keyIs.tabKey ) {
                    direction = ( event.shiftKey ) ? -1 :  1;
                }

                if ( !keyIs.shiftKey ) {
                    this.shiftHighlightedRecipient( direction );
                }
            } else if ( keyIs.backspace ) {
                this.$el.find( '.qtip' ).qtip( 'hide' );
            } else {
                this.recipientSearchAutocomplete();
            }
        }
    },
    submitSearch: function( event ) {
        //debugger;
        if ( event ) {

            event.preventDefault();

            if ( event.type === 'click' ) {
                this.$el.find( '.PaxNameInput' ).focus();
            }
        }

        var $input = this.$el.find( '.paxNameInput' );
        this.PaxSearchReactModel.fetchRecipientList( $input.val() );
        this.recipientList = this.PaxSearchReactModel.recipientList;
    },

    renderRecipientList: function() {
        console.log( 'The Plotting of the Recipient List is to be Implemented from this.recipientList' );
        console.log( this.recipientList );
    },
    recipientSearchSpinner: function( opt ) {
        //debugger;
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
    recipientSearchAutocomplete: function() {
        //debugger;
        /*
         *
         * Controls search timer delay functionality.
         *
         * [1] Grab data attributes set in the HTML
         *
         * [2] Exit this function if there aren't enough
         *     characters provided to make a meaningful ajax call.
         *
         * [3] Set the timer delay from the data attributes
         *
         */

        var $input     = this.$el.find( '.paxNameInput' ),
            delay      = $input.data( 'autocomp-delay' ),     // [1]
            minChars   = $input.data( 'autocomp-min-chars' ); // [1]

        if ( $input.val().length < minChars ) { // [2]
            return;
        }

        this.recipientSearchSpinner();

        if ( this.ajaxCallDelay ) {
            this.ajaxCallDelay.interval = delay || 1000; // [3]
        }

        if( this.ajaxCallDelay.isActive ) {
            this.ajaxCallDelay.restart();
        } else {
            this.ajaxCallDelay.start();
        }
    },
    clickRecipient: function( event ) {
        /*
         *
         * Clicking on an item in the autocompleted list.
         *
         */

        //var index = $(event.currentTarget).data("tabindex");
        //this.submitRecipient(index);
        console.log( 'Came Here' );
    }

} );
