/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported ThrowdownCombinedModuleView */
/*global
console,
_,
$,
G5,
ThrowdownPromoSelectCollection,
LaunchModuleView,
ThrowdownMatchCollection,
ThrowdownAllMatchesModel
ThrowdownAllMatchesTeamModel,
ThrowdownCombinedModuleView:true
*/
ThrowdownCombinedModuleView = LaunchModuleView.extend( {

    initialize: function( opts ) {
        var that = this;
        console.log( opts );
        $( 'body' ).on( 'click', this.checkPromoSelect );
        this.tplLoaded = false;
        this.tplMatchesLoaded = false;
        this.dataMatchesLoaded = false;
        LaunchModuleView.prototype.initialize.apply( this, arguments ); // this is how we call the super-class initialize function ( inherit its magic )
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events ); // inherit events from the superclass ModuleView


        G5.throwdown.dispatcher.on( 'promotionsLoaded promoChanged', function() {
            this.renderPromoInfo();
            that.throwdownAllMatchesModel = new ThrowdownAllMatchesModel( { 'json': opts.allMatchesJson, 'jsonUrl': opts.allMatchesJsonUrl } );
            that.throwdownAllMatchesTeamModel = new ThrowdownAllMatchesTeamModel( { 'json': opts.allMatchesTeamJson, 'jsonUrl': opts.allMatchesTeamJsonUrl } );

            if ( this.throwdownAllMatchesModel.get( 'visible' ) ) {
                this.initAllMatches();
            }
            if ( this.throwdownAllMatchesTeamModel.get( 'visible' ) ) {
                this.initAllTeamMatches();
            }
        }, this );

        this.collection = new ThrowdownPromoSelectCollection( );
        console.log( this );
        this.collection.loadData( );

        this.on( 'templateLoaded', function( ) {
            _.delay( G5.util.textShrink, 100, this.$el.find( '.title-icon-view h3, .td-promo-countdown h4' ) );
            G5.util.textShrink( this.$el.find( '.title-icon-view h3, .td-promo-countdown h4' ) );
        } );

        // resize the text to fit
        this.moduleCollection.on( 'filterChanged', function( ) {
            G5.util.textShrink( this.$el.find( '.title-icon-view h3, .td-promo-countdown h4' ) );
        }, this );

        //matchmodule init
        this.matchCollection = new ThrowdownMatchCollection( );
        //
        this.on( 'templateLoaded', function( ) {
            this.spinModule( true );
            this.tplLoaded = true;
            this.renderMatch( );

        }, this );

        this.matchCollection.on( 'matchDataLoaded', function( ) {
            this.dataLoaded = true;
            console.log( this.matchCollection.models[ 0 ].get( 'visible' ) );
            if ( this.matchCollection.models[ 0 ].get( 'visible' ) ) {
                this.renderMatch( );
            } else {
                $( '#matchData' ).hide();
                $( '.td-view-matches' ).hide();
            }

        }, this );
        that.throwdownAllMatchesModel = new ThrowdownAllMatchesModel( { 'json': opts.allMatchesJson, 'jsonUrl': opts.allMatchesJsonUrl } );
        that.throwdownAllMatchesTeamModel = new ThrowdownAllMatchesTeamModel( { 'json': opts.allMatchesTeamJson, 'jsonUrl': opts.allMatchesTeamJsonUrl } );

        this.on( 'templateLoaded', function( tpl, vars, subTpls ) {
            this.throwdownAllMatchesTpl = subTpls.throwdownAllMatchesTpl;
            this.throwdownAllMatchesTeamTpl = subTpls.throwdownAllMatchesTeamTpl;
            this.roundPaginationTpl = subTpls.roundPaginationTpl;

            this.spinModule( true );
            this.tplMatchesLoaded = true;
            console.log( this );
            if ( this.throwdownAllMatchesModel.get( 'visible' ) ) {
                $( '.title-icon-view' ).hide();
                $( '#allMatchesContainer' ).show();
                this.initAllMatches();
            }
            if ( this.throwdownAllMatchesModel.get( 'visible' ) ) {
                $( '#allMatchesContainer' ).show();
                this.initAllTeamMatches();
            }
            this.renderTabPopover();

            that.throwdownAllMatchesModel.on( 'loadAllMatchesDataFinished', function() {
                that.dataMatchesLoaded = true;
                console.log( that.throwdownAllMatchesModel.get );
                if ( that.throwdownAllMatchesModel.get( 'visible' ) ) {

                    that.initAllMatches();
                }
            }, this );

            that.throwdownAllMatchesTeamModel.on( 'loadAllMatchesTeamDataFinished', function() {
                that.dataMatchesLoaded = true;
                if ( that.throwdownAllMatchesTeamModel.get( 'visible' ) ) {

                    that.initAllTeamMatches();
                }
            }, this );

        }, this );

    },

    /**
     * G5.throwdown.dispatcher events triggered in this view:
     *        event: promoChanged
     *     location: changePromo( )
     *      purpose: announce that a user has selected a different promotion
     *
     * Event listeners for G5.throwdown.dispatcher:
     *        event: promotionsLoaded, promoChanged
     *       action: calls renderPromoInfo( )
     *      purpose: renders the promo select view when the inital set of
     *               promotions is loaded, or when the promotion is changed
     */

    events: {
        'click .td-promo-select-container': 'showHidePromoSelect',
        'click .td-promo-select-item': 'changePromo'
    },

    /**
     * Override the parent class's render method. The view's template should
     * wait to render until after the Promotion data has loaded.
     */
    render: function( ) {
        console.log( 'render' );
        return this;
    },
    /**
     * Renders this view and removes the spinner if present
     */
    renderMatch: function( ) {
        var that = this;
        if ( !this.dataLoaded || !this.tplLoaded ) {
            return; // if the template has not loaded yet don't try to render
        }
        console.log( this );
        console.log( 'renderMatch' );
        // if ( !this.processData( ) ){
        //     return false;
        // }
            console.log( 'renderMatch' );
            console.log( this );

            if ( this.matchCollection.models[ 0 ].get( 'visible' ) === false ) {
                $( '.td-view-matches' ).hide();
                return;
            }
        // empty the element in cases where the view needs to be rerendered with new data
        // that.$el.find( '.module-liner' ).empty( );
        //when template manager has the template, render it to this element
        that.$el.find( '#matchData' ).empty();
        that.$el.find( '#matchData' ).append( that.subTpls.throwdownMatchDetailsTpl( _.extend( {}, that.matchCollection.first( ).toJSON( ), { cid: that.cid } ) ) );

        // in initial load, templates may miss out on filter event
        // so we do filter change work here just in case
        // that.doFilterChangeWork( );

        // this.shrinkText( );

        that.spinModule( false );

        return this;
    },
    /**
     * Renders the view and starts the countdown clock
     */
    renderPromoInfo: function( ) {
        var that = this;
        console.log( 'promo' );

        this.getTemplateAnd( function( tpl ) {
            // empty the element in cases where the view needs to be rerendered with new data
            that.$el.empty( );

            // when template manager has the template, render it to this element
            // pass that.collection.toJSON( ) as a 'promotion' attribute of an object to make Handlebars play nicer
            that.$el.append( tpl( _.extend( {}, { promotion: that.collection.toJSON( ) }, { cid: that.cid } ) ) );

            // start the promotion countdown
            that.initCountdown( that.$el.find( '.td-promo-countdown ul' ), that.collection.first( ).toJSON( ).endDate );

            //in initial load, templates may miss out on filter event
            //so we do filter change work here just in case
            // that.doFilterChangeWork( );

            that.$el.find( '.td-promo-select-item' ).first( ).css( 'display', 'none' );
        } );

        this.updateHref( );

        },
    initAllMatches: function( ) {
        var that = this,
            $allMatchesCont = this.$el.find( '.allMatchesTable' ).html( '' ),
            $roundCont = this.$el.find( '.roundPagination' ).html( '' ),
            opts;

        // empty the element in cases where the view needs to be rerendered with new data
        $allMatchesCont.empty();
        $roundCont.empty();

        $allMatchesCont.removeClass( 'emptySet' );

        //when template manager has the template, render it to this element
        $allMatchesCont.append( that.subTpls.throwdownAllMatchesTpl( _.extend( {}, that.throwdownAllMatchesModel.toJSON() ) ) );

        if ( that.throwdownAllMatchesModel.get( 'totalRounds' ) >= that.throwdownAllMatchesModel.get( 'currentRound' ) ) {
            opts = {
                currentRound: that.throwdownAllMatchesModel.get( 'currentRound' ),
                totalRounds: that.throwdownAllMatchesModel.get( 'totalRounds' )
            };

            $roundCont.append( that.subTpls.roundPaginationTpl( _.extend( {}, that.throwdownAllMatchesModel.toJSON() ), opts ) );
        }

        if ( that.throwdownAllMatchesModel.length === 0 ) {
            that.$el.find( '.allMatchesTable' )
                .addClass( 'emptySet' )
                .append( this.make( 'h2', {}, this.$recs.data( 'msgEmpty' ) ) )
                .find( 'h2' ).prepend( '<i class="icon-g5-norecognitions" />' );
        }

        that.spinModule( false );

        // remove the spinner after changing rounds(pagination)
        G5.util.hideSpin( this.$el.find( '.wide-view' ) );

        return this;
    },
    initAllTeamMatches: function() {
        var that = this,
            $allMatchesTeamCont = this.$el.find( '.allMatchesTeamTable' ).html( '' ),
            $roundCont = this.$el.find( '.roundPagination' ).html( '' ),
            opts;

        // empty the element in cases where the view needs to be rerendered with new data
        $allMatchesTeamCont.empty();
        $roundCont.empty();

        $allMatchesTeamCont.removeClass( 'emptySet' );

        //when template manager has the template, render it to this element
        $allMatchesTeamCont.append( that.subTpls.throwdownAllMatchesTeamTpl( _.extend( {}, that.throwdownAllMatchesTeamModel.toJSON() ) ) );

        if ( that.throwdownAllMatchesTeamModel.get( 'totalRounds' ) >= that.throwdownAllMatchesTeamModel.get( 'currentRound' ) ) {
            opts = {
                currentRound: that.throwdownAllMatchesTeamModel.get( 'currentRound' ),
                totalRounds: that.throwdownAllMatchesTeamModel.get( 'totalRounds' )
            };
            $roundCont.append( that.subTpls.roundPaginationTpl( _.extend( {}, that.throwdownAllMatchesTeamModel.toJSON() ), opts ) );
        }

        if ( that.throwdownAllMatchesTeamModel.length === 0 ) {
           that.$el.find( '.allMatchesTeamTable' )
                .addClass( 'emptySet' )
                .append( this.make( 'h2', {}, this.$recs.data( 'msgEmpty' ) ) )
                .find( 'h2' ).prepend( '<i class="icon-g5-norecognitions" />' );
        }

    },
    renderTabPopover: function() {
        var $tooltipParent = $( '.allMatchesTabs li a' );

         _.each( $tooltipParent, function() {
            //give the <a> a tooltip
            $tooltipParent.tooltip( {
                template: '<div class="tooltip smack-talk-tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>',
                container: 'body',
                delay: 200
            } );

        } );
    },
    /**
     * Shows or hides the promotion select dropdown
     */
    showHidePromoSelect: function( e ) {
        var $psl = this.$el.find( '.td-promo-select-list' ),
            $dac = this.$el.find( '.down-arrow-container' );

        if ( this.$el.find( '.td-promo-select-list:hidden' ).length > 0 ) {
            e.stopPropagation( );
            $psl.slideDown( 250 );
            $dac.addClass( 'active' );
        } else {
            $psl.slideUp( 250 );
            $dac.removeClass( 'active' );
        }
    },

    /**
     * This will hide the promo select dropdown if a user clicks anywhere else on the page
     */
    checkPromoSelect: function( ) {
        if ( $( '.td-promo-select-list:visible' ).length > 0 ) {
            $( '.td-promo-select-list:visible' ).slideUp( 250 );
            $( '.down-arrow-container' ).removeClass( 'active' );
        }
    },

    /**
     * Get the promotion ID from the selected item and moves that promotion to
     * the beginning of the collection. Next it sets the promoId in the
     * G5.throwdown object and announces that the promotion has changed.
     *
     * @param e  The event object that triggered this method call
     */
    changePromo: function( e ) {
        var promoId = $( e.currentTarget ).data( 'promoid' );
        var selectedPromo = this.collection.get( promoId );
        this.showHidePromoSelect( );

        if ( G5.throwdown.promoId === promoId ) { // do nothing if same promotion is selected
            return;
        } else {
            this.collection.remove( selectedPromo );
            this.collection.unshift( selectedPromo );
            G5.throwdown.promoId = promoId;
            G5.throwdown.dispatcher.trigger( 'promoChanged' );
        }
    },

    /**
     * Stops the countdown clock and makes the call to rerender this view
     */
    collectionChanged: function( ) {
        clearInterval( G5.throwdown.roundCountdown );
        this.renderPromoInfo( );
    },

    /**
     * Handles the promotion round countdown clock in this view
     *
     * @param clock       jQuery object of the parent element of the countdown timer
     * @param targetdate  A date string of any type that is accepted by the JS Date object
     *                    https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date
     */
    initCountdown: function( $clock, targetdate ) {
        var t = new Date( targetdate ),
        temp,
        calcDate = function( ) {
            var n = new Date( );   // current date ( now )
            var i = ( t - n ) / 1000; // difference between target and now
            var c = {};           // object representing the countdown
            var x;

            if ( n >= t ) {
                clearInterval( G5.throwdown.roundCountdown );
            } else {
                c.d = Math.floor( i / 86400 );
                c.h = Math.floor( i / 3600 % 24 );
                c.m = Math.floor( i / 60 % 60 );
                c.s = Math.floor( i % 60 );
            }

            for ( x in c ) {
                if ( c.hasOwnProperty( x ) ) {
                    temp = c[ x ].toString( );
                    if ( temp.length === 1 ) {
                        c[ x ] = '0' + temp;
                    } else {
                        c[ x ] = temp;
                    }

                    $clock.find( '.' + x + ' span.cd-digit' ).html( c[ x ] );
                }
            }

            return false; // return false to prevent mobile safari from jumping to top of page
        };

        // run it the first time, then every second
        calcDate( );
        G5.throwdown.roundCountdown = setInterval( calcDate, 1000 );
    },
    spinModule: function( start ) {
        if ( start ) {
            this.$el.closest( '.module' )
                .append( '<span class="spin" />' )
                .find( '.spin' ).spin( );
        } else {
            this.$el.closest( '.module' )
                .find( '.spin' ).remove( ); //.spin( false );
        }
    },
    /**
     * Update the href attribute of the View Matches .visitAppBtn
     */
    updateHref: function( ) {
        var $anchor = this.$el.find( '.visitAppBtn' );

        $anchor.attr( 'href', this.collection.first( ).get( 'matchesUrl' ) );
    }
    // ,
    // processData: function( ) {
    //     var
    //     filters = this.model.get( 'filters' ),
    //     visible = this.collection.first( ).get( 'visible' );
    //     console.log( filters );
    //     if ( visible ) {
    //         // set the order in case the module is currently hidden
    //         // filters.throwdown.order = G5.throwdown.moduleOrder.allMatches;
    //
    //     } else {
    //         // filters.throwdown.order = 'hidden';
    //     }
    //
    //     // this.model.trigger( 'change' );
    //
    //     return visible;
    // }
} );
