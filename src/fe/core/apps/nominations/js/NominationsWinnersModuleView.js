/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported NominationsWinnersModuleView */
/*global
_,
$,
G5,
LaunchModuleView,
NominationsWinnersModuleView:true
*/
NominationsWinnersModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function( opts ) {

        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        this.$el.addClass( 'NominationsWinners' );

		this.on( 'templateLoaded', function( tpl, vars, subTpls ) {
            this.display();
            this.nominationsWinnersTpl = subTpls.nominationsWinnersTpl;

        } );
        console.log( opts );
        this.nominations = null;

        G5._globalEvents.on( 'windowResized', this.resizeListener, this );

    },

    events: {
        'click .profile-popover': 'doProfilePopoverView'
    },

    // override super-class render function
    display: function() {

        var that = this,
            $selectWrap = this.$el.find( '#winnersContainer' );

        // start the loading state and spinner
        this.dataLoad( true );

        $.ajax( {
            url: G5.props.URL_JSON_NOMINATIONS_WINNERS_LIST,
            dataType: 'g5json',
            type: 'POST',
            success: function( serverResp ) {
                var nominations = serverResp.data;

                // stop the loading state and spinner
                that.dataLoad( false );

                $selectWrap.html( that.subTpls.nominationWinnersTpl( nominations ) );
                // set same height

                that.resizeListener();
                that.nominations = nominations;
                //console.log(that.nominations);
            }
        } );
    },

    doProfilePopoverView: function( e ) {
        var $tar = $( e.target ).closest( '.profile-popover' ),
            isSheet = ( $tar.closest( '.modal-stack' ).length > 0 ) ? { isSheet: true } : { isSheet: false };

        isSheet.containerEl = this.$el;
        //attach participant popovers
        if ( !$tar.data( 'participantPopover' ) ) {
            $tar.participantPopover( isSheet ).qtip( 'show' );
        }
        e.preventDefault();
    },

    resizeListener: function() {
        var breakpoint = G5.breakpoint.value,
            $selectWrap = this.$el.find( '#winnersContainer' ),
            titleClass = '.title',
            cardClass = '.card';

        if( breakpoint === 'mobile' || breakpoint == 'mini' ) {
            $selectWrap.find( titleClass ).height( '' ).css( 'padding-top', '' );
            $selectWrap.find( cardClass ).height( '' );
        }else{
            // set the titles to equal heights
            G5.util.equalheight( titleClass, $selectWrap );

            // then, iterate through the titles,
                // storing their equalized heights,
                // removing the fixed height,
                // then adding the difference back as top padding to bottom-align the text
            $selectWrap.find( titleClass ).each( function() {
                var beforeh = $( this ).height(),
                    afterh;

                $( this ).height( '' );
                afterh = $( this ).height();
                $( this ).css( 'padding-top', beforeh - afterh );
            } );

            // when all that futzing is done, set the cards to equal heights
            G5.util.equalheight( cardClass, $selectWrap );
        }

    }


} );
