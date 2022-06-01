/*exported PurlCelebrateModuleView */
/*global
LaunchModuleView,
PurlCelebrateModel,
PurlCelebrateModuleView:true
*/
PurlCelebrateModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function () {
        var that = this;

        LaunchModuleView.prototype.initialize.apply( this, arguments ); // this is how we call the super-class initialize function (inherit its magic)
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events ); // inherit events from the superclass LaunchModuleView

        this.celebrateModel = new PurlCelebrateModel( {
            isModule: true
        } );

        this.on( 'templateLoaded', function( tpl, vars, subTpls ) {
            this.purlCelebrateListTpl = subTpls.purlCelebrateListTpl;
            that.dataLoad( true );

            that.celebrateModel.loadData();

            that.celebrateModel.on( 'dataLoaded', function() {
                that.renderList();
            } );

        }, this );
    },
    events: {
        'click .profile-popover': 'attachParticipantPopover'
    },
    renderList: function() {
        var $celebrateListCont = this.$el.find( '.purlCelebrateList' ),
            celebrationSets = this.celebrateModel.get( 'celebrationSets' ),
            that = this;

        _.each( celebrationSets, function( celSet ) {
            _.each( celSet.celebrations, function( cel ) {
                cel.anniversaryInt = parseInt( cel.anniversary );
            } );

            if ( celSet.isDefault === true ) {
                $celebrateListCont.append( that.purlCelebrateListTpl( celSet ) );
                    that.renderCarousel();
            }
        } );

        that.dataLoad( false );

    },
    attachParticipantPopover: function( e ) {
        var $tar = $( e.target );

        //attach participant popovers
        if ( !$tar.data( 'participantPopover' ) ) {
            $tar.participantPopover( { containerEl: this.$el } ).qtip( 'show' );
        }
        e.preventDefault();
    },
    renderCarousel: function() {
        //first get some listeners for balloon handling

        $( '#purlCarousel' ).on( 'init', function() {
            $( '#purlCarousel' ).find( '.item' ).filter( ':nth-child(1), :nth-child(2), :nth-child(3)' ).removeClass( 'slick-active' );
            _.delay( function( ) { $( '#purlCarousel' ).find( '.item' ).filter( ':nth-child(1), :nth-child(2), :nth-child(3)' ).addClass( 'slick-active' ); }, 500 );
        } );
        this.startSlick( { } );
    },
    startSlick: function() {
        // slick length =
        var slickNumItems = $( '#purlCarousel' ).find( '.item' ).length,
            desktopNum = 3,
            tabletNum = 2;
        var $slickEl = this.$el.find( '#purlCarousel' ); //find all modules with a carousel

        if ( slickNumItems === 1 ) {
            desktopNum = 1;
            tabletNum = 1;
        }else if( slickNumItems === 2 ) {
            desktopNum = 2;
        }
        $slickEl.slick( {
            dots: false,
            infinite: false,
            speed: 300,
            slidesToShow: desktopNum,
            slidesToScroll: desktopNum,
            centerMode: false,
            variableWidth: false,
            prevArrow: '<i class="icon-arrow-1-left slick-arrow slick-prev"></i>',
            nextArrow: '<i class="icon-arrow-1-right slick-arrow slick-next"></i>',
            responsive: [
            {
              breakpoint: 1199,
              settings: {
                slidesToShow: tabletNum,
                slidesToScroll: tabletNum,
                dots: false
              }
            },
            {
              breakpoint: 640,
              settings: {
                slidesToShow: 1,
                slidesToScroll: 1,
                dots: false
              }
            }
            ]
        } );
    }

} );
